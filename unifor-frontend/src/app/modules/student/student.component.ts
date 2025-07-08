import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-student',
  template: `
    <div class="container mt-4">
      <div class="card">
        <div class="card-header">
          <h2>Portal do Aluno</h2>
        </div>
        <div class="card-body">
          <p>Bem-vindo ao portal do aluno. Aqui você pode visualizar suas disciplinas e informações acadêmicas.</p>
          
          <div class="row mt-4">
            <div class="col-md-6">
              <div class="card mb-3">
                <div class="card-header">Minhas Disciplinas</div>
                <div class="card-body">
                  <p>Você ainda não está matriculado em nenhuma disciplina.</p>
                  <button class="btn btn-primary">Ver Disciplinas</button>
                </div>
              </div>
            </div>
            
            <div class="col-md-6">
              <div class="card mb-3">
                <div class="card-header">Meu Histórico</div>
                <div class="card-body">
                  <p>Você ainda não possui histórico acadêmico.</p>
                  <button class="btn btn-primary">Ver Histórico</button>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  `,
  styles: [`
    .card-header {
      background-color: #f8f9fa;
      font-weight: bold;
    }
  `]
})
export class StudentComponent implements OnInit {
  constructor() { }

  ngOnInit(): void {
    console.log('StudentComponent inicializado');
  }
}
