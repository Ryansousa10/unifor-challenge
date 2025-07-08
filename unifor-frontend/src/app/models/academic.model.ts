export interface Course {
  id?: string;
  name: string;
  code: string;
  description?: string;
}

export interface Discipline {
  id?: string;
  name: string;
  code: string;
  credits: number;
  workload: number;
  description?: string;
}

export interface Semester {
  id?: string;
  name: string;
  number: number;
  year: number;
  active: boolean;
  startDate: string;
  endDate: string;
}

export interface Curriculum {
  id?: string;
  courseId: string;
  courseName?: string;
  startDate: string;
  endDate?: string;
  active: boolean;
}
