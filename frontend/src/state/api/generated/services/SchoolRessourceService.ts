/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { CancelablePromise } from '../core/CancelablePromise';
import { OpenAPI } from '../core/OpenAPI';
import { request as __request } from '../core/request';
import type { CreateSchoolDto } from '../models/CreateSchoolDto';
import type { SchoolDto } from '../models/SchoolDto';
export class SchoolRessourceService {
  /**
   * @param requestBody
   * @returns any OK
   * @throws ApiError
   */
  public static createSchool(requestBody: CreateSchoolDto): CancelablePromise<any> {
    return __request(OpenAPI, {
      method: 'POST',
      url: '/backoffice/school/create',
      body: requestBody,
      mediaType: 'application/json',
    });
  }
  /**
   * @returns SchoolDto OK
   * @throws ApiError
   */
  public static getAllSchools(): CancelablePromise<Array<SchoolDto>> {
    return __request(OpenAPI, {
      method: 'GET',
      url: '/backoffice/school',
    });
  }
}
