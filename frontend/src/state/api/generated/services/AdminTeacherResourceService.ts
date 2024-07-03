/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { CancelablePromise } from '../core/CancelablePromise';
import { OpenAPI } from '../core/OpenAPI';
import { request as __request } from '../core/request';
import type { TeacherDto } from '../models/TeacherDto';
import type { UserDto } from '../models/UserDto';
export class AdminTeacherResourceService {
  /**
   * @param schoolId
   * @param teacherId
   * @returns any OK
   * @throws ApiError
   */
  public static assignTeacherToSchool(schoolId: string, teacherId: string): CancelablePromise<any> {
    return __request(OpenAPI, {
      method: 'PUT',
      url: '/admin/teacher/assignTeacherToSchool',
      query: {
        schoolId: schoolId,
        teacherId: teacherId,
      },
    });
  }
  /**
   * @param schoolId
   * @returns UserDto OK
   * @throws ApiError
   */
  public static getAllTeachersForSchool(schoolId: string): CancelablePromise<Array<UserDto>> {
    return __request(OpenAPI, {
      method: 'GET',
      url: '/admin/teacher',
      query: {
        schoolId: schoolId,
      },
    });
  }
  /**
   * @param schoolId
   * @param email
   * @returns TeacherDto OK
   * @throws ApiError
   */
  public static searchTeacherByEmail(schoolId: string, email: string): CancelablePromise<Array<TeacherDto>> {
    return __request(OpenAPI, {
      method: 'GET',
      url: '/admin/teacher/search',
      query: {
        schoolId: schoolId,
        email: email,
      },
    });
  }
}
