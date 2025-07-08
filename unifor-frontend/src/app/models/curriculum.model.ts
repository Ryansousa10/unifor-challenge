export interface CurricDisc {
  id?: {
    curriculumId: number;
    disciplineId: number;
  };
  curriculumId?: number;
  disciplineId?: number;
  disciplineName?: string;
  disciplineCode?: string;
  workload?: number;
  semester: number;
}

export interface CurriculumWithDisciplines {
  curriculum: {
    id: number;
    courseId: number;
    courseName: string;
    startDate: string;
    endDate?: string;
    active: boolean;
  };
  disciplines: CurricDisc[];
}
