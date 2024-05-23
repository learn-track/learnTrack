/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { CancelablePromise } from '../core/CancelablePromise';
import { OpenAPI } from '../core/OpenAPI';
import { request as __request } from '../core/request';
import type { WhoamiDto } from '../models/WhoamiDto';
export class WhoamiResourceService {
  /**
   * @returns WhoamiDto OK
   * @throws ApiError
   */
  public static getSelf(): CancelablePromise<WhoamiDto> {
    return __request(OpenAPI, {
      method: 'GET',
      url: '/whoami',
    });
  }
}
