/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { CancelablePromise } from '../core/CancelablePromise';
import { OpenAPI } from '../core/OpenAPI';
import { request as __request } from '../core/request';
import type { CreateGradeDto } from '../models/CreateGradeDto';
import type { Grade } from '../models/Grade';
export class GradeResourceService {
  /**
   * @param schoolId
   * @param requestBody
   * @returns Grade OK
   * @throws ApiError
   */
  public static createGrade(schoolId: string, requestBody: CreateGradeDto): CancelablePromise<Grade> {
    return __request(OpenAPI, {
      method: 'POST',
      url: '/admin/grade/createGrade',
      query: {
        schoolId: schoolId,
      },
      body: requestBody,
      mediaType: 'application/json',
    });
  }
}
