import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { AcademicService } from '../../../../services/academic.service';

@Component({
  selector: 'app-semester-form',
  templateUrl: './semester-form.component.html',
  styleUrls: ['./semester-form.component.css']
})
export class SemesterFormComponent implements OnInit {
  semesterForm!: FormGroup;
  isEditMode = false;
  semesterId: string | null = null;
  loading = false;
  submitted = false;
  error = '';
  maxEndDate: string = '';
  successMessage = '';

  constructor(
    private formBuilder: FormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    private academicService: AcademicService
  ) { }

  ngOnInit(): void {
    this.semesterForm = this.formBuilder.group({
      year: ['', [
        Validators.required,
        Validators.pattern('^\\d{4}$'),
        Validators.min(2000),
        Validators.max(2100),
        Validators.maxLength(4),
        Validators.minLength(4)
      ]],
      semesterNumber: ['', [Validators.required, Validators.pattern('^[12]$')]],
      name: [{ value: '', disabled: true }, Validators.required],
      startDate: ['', Validators.required],
      endDate: ['', Validators.required]
    });

    this.semesterForm.get('year')!.valueChanges.subscribe(() => {
      this.updateName();
      this.updateEndDateLimit();
    });
    this.semesterForm.get('semesterNumber')!.valueChanges.subscribe(() => {
      this.updateName();
      this.updateEndDateLimit();
    });

    this.semesterId = this.route.snapshot.paramMap.get('id');
    this.isEditMode = !!this.semesterId;

    if (this.isEditMode && this.semesterId) {
      this.loadSemester(this.semesterId);
    }
  }

  updateName(): void {
    const year = this.semesterForm.get('year')!.value;
    const semesterNumber = this.semesterForm.get('semesterNumber')!.value;
    if (year && semesterNumber) {
      this.semesterForm.get('name')!.setValue(`${year}.${semesterNumber}`, { emitEvent: false });
    } else {
      this.semesterForm.get('name')!.setValue('', { emitEvent: false });
    }
  }

  updateEndDateLimit(): void {
    const year = this.semesterForm.get('year')!.value;
    const semesterNumber = this.semesterForm.get('semesterNumber')!.value;
    let maxDate = '';
    if (year && semesterNumber) {
      if (semesterNumber === '1') {
        maxDate = `${year}-06-30`;
      } else if (semesterNumber === '2') {
        maxDate = `${year}-12-31`;
      }
    }
    this.maxEndDate = maxDate;
    // Se a data de fim já for maior que o limite, limpa o campo
    const endDate = this.semesterForm.get('endDate')!.value;
    if (maxDate && endDate && endDate > maxDate) {
      this.semesterForm.get('endDate')!.setValue('');
    }
  }

  loadSemester(id: string): void {
    this.loading = true;
    this.academicService.getSemester(id).subscribe({
      next: (data) => {
        const startDate = data.startDate ? new Date(data.startDate).toISOString().split('T')[0] : '';
        const endDate = data.endDate ? new Date(data.endDate).toISOString().split('T')[0] : '';
        let year = '', semesterNumber = '';
        if (data.name && data.name.includes('.')) {
          [year, semesterNumber] = data.name.split('.');
        }
        this.semesterForm.patchValue({
          year: year,
          semesterNumber: semesterNumber,
          name: data.name,
          startDate: startDate,
          endDate: endDate
        });
        this.loading = false;
      },
      error: (err) => {
        this.error = 'Erro ao carregar semestre. Por favor, tente novamente.';
        this.loading = false;
        console.error('Erro ao carregar semestre:', err);
      }
    });
  }

  get f() { return this.semesterForm.controls; }

  onSubmit(): void {
    this.submitted = true;
    // Marca todos os campos como touched para exibir erros imediatamente
    Object.values(this.semesterForm.controls).forEach(control => control.markAsTouched());

    // Validação customizada: datas devem estar no mesmo ano do campo 'year' e dentro do intervalo permitido
    const year = this.semesterForm.get('year')!.value;
    const semesterNumber = this.semesterForm.get('semesterNumber')!.value;
    const startDate = this.semesterForm.get('startDate')!.value;
    const endDate = this.semesterForm.get('endDate')!.value;
    let customDateError = '';
    let startDateValid = true;
    let endDateValid = true;
    if (year && semesterNumber && startDate && endDate) {
      if (!startDate.startsWith(year + '-')) {
        customDateError = `A data de início deve ser do ano ${year}.`;
        startDateValid = false;
        this.semesterForm.get('startDate')!.setErrors({ ...this.semesterForm.get('startDate')!.errors, yearMismatch: true });
      }
      if (!endDate.startsWith(year + '-')) {
        customDateError = `A data de término deve ser do ano ${year}.`;
        endDateValid = false;
        this.semesterForm.get('endDate')!.setErrors({ ...this.semesterForm.get('endDate')!.errors, yearMismatch: true });
      }
      if (startDateValid && endDateValid) {
        if (semesterNumber === '1') {
          // 1º semestre: início entre 01/01 e 30/06, fim entre início e 30/06
          const minStart = `${year}-01-01`;
          const maxStart = `${year}-06-30`;
          const maxEnd = `${year}-06-30`;
          if (startDate < minStart || startDate > maxStart) {
            customDateError = `A data de início do 1º semestre deve ser entre 01/01 e 30/06 de ${year}.`;
            startDateValid = false;
            this.semesterForm.get('startDate')!.setErrors({ ...this.semesterForm.get('startDate')!.errors, startRange: true });
          }
          if (endDate < startDate || endDate > maxEnd) {
            customDateError = `A data de término do 1º semestre deve ser entre a data de início e 30/06 de ${year}.`;
            endDateValid = false;
            this.semesterForm.get('endDate')!.setErrors({ ...this.semesterForm.get('endDate')!.errors, endRange: true });
          }
        } else if (semesterNumber === '2') {
          // 2º semestre: início entre 01/07 e 31/12, fim entre início e 31/12
          const minStart = `${year}-07-01`;
          const maxStart = `${year}-12-31`;
          const maxEnd = `${year}-12-31`;
          if (startDate < minStart || startDate > maxStart) {
            customDateError = `A data de início do 2º semestre deve ser entre 01/07 e 31/12 de ${year}.`;
            startDateValid = false;
            this.semesterForm.get('startDate')!.setErrors({ ...this.semesterForm.get('startDate')!.errors, startRange: true });
          }
          if (endDate < startDate || endDate > maxEnd) {
            customDateError = `A data de término do 2º semestre deve ser entre a data de início e 31/12 de ${year}.`;
            endDateValid = false;
            this.semesterForm.get('endDate')!.setErrors({ ...this.semesterForm.get('endDate')!.errors, endRange: true });
          }
        }
        // Validação: diferença máxima de 6 meses entre início e fim
        if (startDateValid && endDateValid) {
          const start = new Date(startDate);
          const end = new Date(endDate);
          const diffMonths = (end.getFullYear() - start.getFullYear()) * 12 + (end.getMonth() - start.getMonth());
          if (diffMonths > 6 || (diffMonths === 6 && end.getDate() > start.getDate())) {
            customDateError = 'A diferença entre a data de início e de término não pode ser superior a 6 meses.';
            startDateValid = false;
            endDateValid = false;
          }
        }
        // Validação: início não pode ser depois do término
        if (startDate && endDate && startDate > endDate) {
          customDateError = 'A data de início não pode ser posterior à data de término.';
          startDateValid = false;
          endDateValid = false;
          this.semesterForm.get('startDate')!.setErrors({ ...this.semesterForm.get('startDate')!.errors, afterEnd: true });
          this.semesterForm.get('endDate')!.setErrors({ ...this.semesterForm.get('endDate')!.errors, beforeStart: true });
        }
      }
    }
    // Seta erros customizados nos campos
    if (!startDateValid) {
      this.semesterForm.get('startDate')!.setErrors({ ...this.semesterForm.get('startDate')!.errors, range: true });
    } else if (this.semesterForm.get('startDate')!.hasError('range')) {
      this.semesterForm.get('startDate')!.setErrors(null);
    }
    if (!endDateValid) {
      this.semesterForm.get('endDate')!.setErrors({ ...this.semesterForm.get('endDate')!.errors, range: true });
    } else if (this.semesterForm.get('endDate')!.hasError('range')) {
      this.semesterForm.get('endDate')!.setErrors(null);
    }
    if (!startDateValid || !endDateValid) {
      this.error = '';
      return;
    } else {
      // Limpa erros customizados se não houver
      if (this.semesterForm.get('startDate')!.hasError('yearMismatch')) {
        this.semesterForm.get('startDate')!.setErrors(null);
      }
      if (this.semesterForm.get('endDate')!.hasError('yearMismatch')) {
        this.semesterForm.get('endDate')!.setErrors(null);
      }
    }

    if (this.semesterForm.invalid) {
      return;
    }

    this.loading = true;
    this.error = '';

    if (this.isEditMode && this.semesterId) {
      this.updateSemester();
    } else {
      this.createSemester();
    }
  }

  async createSemester(): Promise<void> {
    const name = this.semesterForm.get('name')!.value;
    const startDate = this.semesterForm.get('startDate')!.value;
    const endDate = this.semesterForm.get('endDate')!.value;
    // Verifica se já existe semestre com o mesmo nome
    try {
      const existing = await this.academicService.getSemesters().toPromise();
      if (existing && existing.some((s: any) => s.name === name)) {
        this.error = 'Já existe um semestre cadastrado com esse nome.';
        this.loading = false;
        return;
      }
    } catch (e) {
      this.error = 'Erro ao verificar semestres existentes.';
      this.loading = false;
      return;
    }
    const payload = { name, startDate, endDate };
    this.academicService.createSemester(payload).subscribe({
      next: () => {
        this.loading = false;
        this.successMessage = 'Semestre cadastrado com sucesso!';
        setTimeout(() => this.router.navigate(['/coordinator/semesters']), 1200);
      },
      error: (err) => {
        this.error = 'Erro ao criar semestre. Por favor, tente novamente.';
        this.loading = false;
        console.error('Erro ao criar semestre:', err);
      }
    });
  }

  updateSemester(): void {
    if (!this.semesterId) return;
    if (this.semesterForm.invalid) {
      this.error = 'Por favor, corrija os erros do formulário antes de salvar.';
      return;
    }
    const payload = {
      name: this.semesterForm.get('name')!.value,
      startDate: this.semesterForm.get('startDate')!.value,
      endDate: this.semesterForm.get('endDate')!.value
    };
    this.academicService.updateSemester(this.semesterId, payload).subscribe({
      next: () => {
        this.loading = false;
        this.successMessage = 'Semestre atualizado com sucesso!';
        setTimeout(() => this.router.navigate(['/coordinator/semesters']), 1200);
      },
      error: (err) => {
        this.error = 'Erro ao atualizar semestre. Por favor, tente novamente.';
        this.loading = false;
        console.error('Erro ao atualizar semestre:', err);
      }
    });
  }

  cancel(): void {
    this.router.navigate(['/coordinator/semesters']);
  }

  goBack(): void {
    this.router.navigate(['/coordinator/semesters']);
  }
}
