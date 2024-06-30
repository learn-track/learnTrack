/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { CancelablePromise } from '../core/CancelablePromise';
import { OpenAPI } from '../core/OpenAPI';
import { request as __request } from '../core/request';
import type { CreateGradeDto } from '../models/CreateGradeDto';
import type { GradeDetailsDto } from '../models/GradeDetailsDto';
import type { GradeInfoDto } from '../models/GradeInfoDto';
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
   * @returns GradeInfoDto OK
   * @throws ApiError
   */
  public static getAllGradesWithInfosBySchoolId(schoolId: string): CancelablePromise<Array<GradeInfoDto>> {
    return __request(OpenAPI, {
      method: 'GET',
      url: '/admin/grade',
      query: {
        schoolId: schoolId,
      },
    });
  }
  /**
   * @param schoolId
   * @param gradeId
   * @returns GradeDetailsDto OK
   * @throws ApiError
   */
  public static getGradeDetails(schoolId: string, gradeId: string): CancelablePromise<GradeDetailsDto> {
    return __request(OpenAPI, {
      method: 'GET',
      url: '/admin/grade/getGradeDetails',
      query: {
        schoolId: schoolId,
        gradeId: gradeId,
      },
    });
  }
}
