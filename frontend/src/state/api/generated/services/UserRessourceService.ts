/* generated using openapi-typescript-codegen -- do no edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { CancelablePromise } from '../core/CancelablePromise';
import { OpenAPI } from '../core/OpenAPI';
import { request as __request } from '../core/request';
import type { UserDto } from '../models/UserDto';
export class UserRessourceService {
  /**
   * @returns UserDto OK
   * @throws ApiError
   */
  public static test(): CancelablePromise<UserDto> {
    return __request(OpenAPI, {
      method: 'GET',
      url: '/user',
    });
  }
}
