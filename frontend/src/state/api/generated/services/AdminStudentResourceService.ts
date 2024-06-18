/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { CancelablePromise } from '../core/CancelablePromise';
import { OpenAPI } from '../core/OpenAPI';
import { request as __request } from '../core/request';
import type { StudentDetailsDto } from '../models/StudentDetailsDto';
export class AdminStudentResourceService {
  /**
   * @param schoolId
   * @returns StudentDetailsDto OK
   * @throws ApiError
   */
  public static getAllStudentsWithDetailsBySchoolId(schoolId: string): CancelablePromise<Array<StudentDetailsDto>> {
    return __request(OpenAPI, {
      method: 'GET',
      url: '/admin/student',
      query: {
        schoolId: schoolId,
      },
    });
  }
}
