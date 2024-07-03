/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { CancelablePromise } from '../core/CancelablePromise';
import { OpenAPI } from '../core/OpenAPI';
import { request as __request } from '../core/request';
import type { CreateStudentDto } from '../models/CreateStudentDto';
import type { StudentDetailsDto } from '../models/StudentDetailsDto';
export class AdminStudentResourceService {
  /**
   * @param schoolId
   * @param requestBody
   * @returns any OK
   * @throws ApiError
   */
  public static createStudent(schoolId: string, requestBody: CreateStudentDto): CancelablePromise<any> {
    return __request(OpenAPI, {
      method: 'POST',
      url: '/admin/student/createStudent',
      query: {
        schoolId: schoolId,
      },
      body: requestBody,
      mediaType: 'application/json',
    });
  }
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
