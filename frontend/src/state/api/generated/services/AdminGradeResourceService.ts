/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { CancelablePromise } from '../core/CancelablePromise';
import { OpenAPI } from '../core/OpenAPI';
import { request as __request } from '../core/request';
import type { CreateGradeDto } from '../models/CreateGradeDto';
import type { GradeDetailsDto } from '../models/GradeDetailsDto';
export class AdminGradeResourceService {
  /**
   * @param schoolId
   * @param requestBody
   * @returns any OK
   * @throws ApiError
   */
  public static createGrade(schoolId: string, requestBody: CreateGradeDto): CancelablePromise<any> {
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
  /**
   * @param schoolId
   * @returns GradeDetailsDto OK
   * @throws ApiError
   */
  public static getAllGradesWithDetailsBySchoolId(schoolId: string): CancelablePromise<Array<GradeDetailsDto>> {
    return __request(OpenAPI, {
      method: 'GET',
      url: '/admin/grade',
      query: {
        schoolId: schoolId,
      },
    });
  }
}
