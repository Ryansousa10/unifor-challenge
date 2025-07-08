import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-professor',
  template: `
    <div class="container mt-4">
      <div class="card">
        <div class="card-header">
          <h2>Área do Professor</h2>
        </div>
        <div class="card-body">
          <p>Bem-vindo à área do professor. Aqui você pode gerenciar suas disciplinas e acompanhar seus alunos.</p>
          
          <div class="row mt-4">
            <div class="col-md-6">
              <div class="card mb-3">
                <div class="card-header">Minhas Disciplinas</div>
                <div class="card-body">
                  <p>Você ainda não possui disciplinas atribuídas.</p>
                  <button class="btn btn-primary">Ver Disciplinas</button>
                </div>
              </div>
            </div>
            
            <div class="col-md-6">
              <div class="card mb-3">
                <div class="card-header">Meus Alunos</div>
                <div class="card-body">
                  <p>Você ainda não possui alunos atribuídos.</p>
                  <button class="btn btn-primary">Ver Alunos</button>
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
export class ProfessorComponent implements OnInit {
  constructor() { }

  ngOnInit(): void {
    console.log('ProfessorComponent inicializado');
  }
}
