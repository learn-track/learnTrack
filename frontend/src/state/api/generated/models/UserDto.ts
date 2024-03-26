/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
export type UserDto = {
  id: string;
  email: string;
  firstname: string;
  middlename?: string | null;
  lastname: string;
  userRole: 'TEACHER' | 'STUDENT' | 'ADMIN';
  birthDate?: string | null;
};
