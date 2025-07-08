export interface CurricDisc {
  id?: {
    curriculumId: string;
    disciplineId: string;
  };
  curriculumId?: string;
  disciplineId?: string;
  disciplineName?: string;
  disciplineCode?: string;
  workload?: number;
  semester: number;
  optional?: boolean;
  credits?: number;
}

export interface CurriculumWithDisciplines {
  curriculum: {
    id: string;
    courseId: string;
    courseName: string;
    name: string;
    startDate: string;
    endDate?: string;
    active: boolean;
    description?: string;
  };
  disciplines: {
    id: string;
    code: string;
    name: string;
    semester: number;
    credits: number;
    workload: number;
    optional: boolean;
  }[];
}
