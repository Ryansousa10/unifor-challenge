import {Component, OnInit, OnDestroy} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {ActivatedRoute, Router} from '@angular/router';
import {Subscription} from 'rxjs';
import {UserService} from '../../../services/user.service';
import {RoleService} from '../../../services/role.service';

@Component({
    selector: 'app-user-form',
    templateUrl: './user-form.component.html',
    styleUrls: ['./user-form.component.css']
})
export class UserFormComponent implements OnInit, OnDestroy {
    userForm!: FormGroup;
    isEditMode = false;
    userId?: string;
    availableRoles: any[] = [];
    selectedRoles: string[] = [];
    errorMessage: string | null = null;
    success: string = '';

    private subs: Subscription[] = [];

    constructor(
        private fb: FormBuilder,
        private userService: UserService,
        private roleService: RoleService,
        private route: ActivatedRoute,
        private router: Router
    ) {
    }

    ngOnInit(): void {
        const id = this.route.snapshot.paramMap.get('id');
        if (id) {
            this.isEditMode = true;
            this.userId = id;
        }

        this.initForm();
        this.loadRoles();
        this.handleFieldErrors();

        if (this.isEditMode) {
            this.loadUserData(this.userId!);
        }
    }

    ngOnDestroy(): void {
        this.subs.forEach(s => s.unsubscribe());
    }

    initForm(): void {
        this.userForm = this.fb.group({
            username: ['', [Validators.required, Validators.minLength(6)]],
            password: ['', [Validators.required, Validators.minLength(6)]],
            firstName: ['', [Validators.required, Validators.minLength(2), Validators.pattern('^[A-Za-zÀ-ÿ\s]+$')]],
            lastName: ['', [Validators.required, Validators.minLength(2), Validators.pattern('^[A-Za-zÀ-ÿ\s]+$')]],
            email: ['', [Validators.required, Validators.email, Validators.pattern('^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$')]],
        });
    }

    loadRoles(): void {
        this.roleService.getRoles().subscribe({
            next: roles => this.availableRoles = roles,
            error: err => console.error('Erro ao carregar perfis:', err)
        });
    }

    loadUserData(id: string): void {
        this.userService.getUser(id).subscribe({
            next: user => {
                this.userForm.patchValue({
                    username: user.username,
                    email: user.email,
                    firstName: user.firstName,
                    lastName: user.lastName
                });
                this.selectedRoles = user.roles?.map(r => r.id) || [];
            },
            error: err => console.error('Erro ao carregar dados do usuário:', err)
        });
    }

    onRoleChange(roleId: string, event: any) {
        if (event.target.checked) {
            this.selectedRoles.push(roleId);
        } else {
            this.selectedRoles = this.selectedRoles.filter(id => id !== roleId);
        }
    }

    hasRoleError(): boolean {
        return this.selectedRoles.length === 0;
    }

    private handleFieldErrors(): void {
        const userSub = this.userForm.get('username')!.valueChanges.subscribe(() => {
            const control = this.userForm.get('username');
            if (control?.hasError('serverError')) {
                const errs = {...control.errors};
                delete errs.serverError;
                control.setErrors(Object.keys(errs).length ? errs : null);
            }
        });
        const emailSub = this.userForm.get('email')!.valueChanges.subscribe(() => {
            const control = this.userForm.get('email');
            if (control?.hasError('serverError')) {
                const errs = {...control.errors};
                delete errs.serverError;
                control.setErrors(Object.keys(errs).length ? errs : null);
            }
        });
        this.subs.push(userSub, emailSub);
    }

    onSubmit(): void {
        this.errorMessage = null;
        this.success = '';
        this.userForm.get('email')?.setErrors(null);
        this.userForm.get('username')?.setErrors(null);

        if (this.hasRoleError()) {
            this.setErrorMessage('role');
            return;
        }
        if (this.userForm.invalid) {
            this.userForm.markAllAsTouched();
            this.setErrorMessage();
            return;
        }
        const userData: any = {
            ...this.userForm.value,
            roles: this.selectedRoles.filter(r => !!r)
        };
        if (!userData.roles.length) {
            this.setErrorMessage('role');
            return;
        }
        let request$ = this.isEditMode
            ? this.userService.updateUser(this.userId!, userData)
            : this.userService.createUser(userData);
        const sub = request$.subscribe({
            next: () => {
                this.success = this.isEditMode ? 'Usuário atualizado com sucesso!' : 'Usuário criado com sucesso!';
                setTimeout(() => this.router.navigate(['/admin/users']), 1200);
            },
            error: (err) => this.handleSubmitError(err)
        });
        this.subs.push(sub);
    }

    private setErrorMessage(type?: string) {
        if (type === 'role' || this.hasRoleError()) {
            this.errorMessage = 'Selecione pelo menos um perfil.';
        } else if (this.userForm.get('username')?.hasError('required')) {
            this.errorMessage = 'O nome de usuário é obrigatório.';
        } else if (this.userForm.get('password')?.hasError('required')) {
            this.errorMessage = 'A senha é obrigatória.';
        } else if (this.userForm.get('firstName')?.hasError('required')) {
            this.errorMessage = 'O nome é obrigatório.';
        } else if (this.userForm.get('firstName')?.hasError('pattern')) {
            this.errorMessage = 'O nome deve conter apenas letras.';
        } else if (this.userForm.get('lastName')?.hasError('required')) {
            this.errorMessage = 'O sobrenome é obrigatório.';
        } else if (this.userForm.get('lastName')?.hasError('pattern')) {
            this.errorMessage = 'O sobrenome deve conter apenas letras.';
        } else if (this.userForm.get('email')?.hasError('required')) {
            this.errorMessage = 'O e-mail é obrigatório.';
        } else if (this.userForm.get('email')?.hasError('email') || this.userForm.get('email')?.hasError('pattern')) {
            this.errorMessage = 'Informe um e-mail válido.';
        }
    }

    private handleSubmitError(err: any) {
        let errorMsg = (err.error?.message || err.message || JSON.stringify(err)).toLowerCase();
        if (this.hasRoleError() || (err.status === 400 && errorMsg.includes('role'))) {
            this.errorMessage = 'Selecione pelo menos um perfil.';
        } else if (err.status === 409) {
            if (errorMsg.includes('email')) {
                this.errorMessage = 'E-mail já cadastrado.';
            } else if (errorMsg.includes('username') || errorMsg.includes('usuário')) {
                this.errorMessage = 'Usuário já existe.';
            } else {
                this.errorMessage = 'Usuário ou e-mail já cadastrado.';
            }
        } else if ((typeof err === 'string' && err.includes('409')) || (err.error && typeof err.error === 'string' && (err.error.includes('409') || err.error.includes('Conflict')))) {
            this.errorMessage = 'Usuário ou e-mail já cadastrado.';
        } else {
            this.errorMessage = 'Erro ao salvar usuário: ' + (err.error?.message || err.message || JSON.stringify(err));
        }
    }

    /** Cancela e volta para a lista */
    cancel(): void {
        this.router.navigate(['/admin/users']);
    }
}