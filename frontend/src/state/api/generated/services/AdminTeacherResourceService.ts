/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { CancelablePromise } from '../core/CancelablePromise';
import { OpenAPI } from '../core/OpenAPI';
import { request as __request } from '../core/request';
import type { UserDto } from '../models/UserDto';
export class AdminTeacherResourceService {
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
}
