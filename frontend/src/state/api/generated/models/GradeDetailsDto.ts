/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { GradeDto } from './GradeDto';
import type { UserDto } from './UserDto';
export type GradeDetailsDto = {
  grade: GradeDto;
  students: Array<UserDto>;
  teachers: Array<UserDto>;
};
