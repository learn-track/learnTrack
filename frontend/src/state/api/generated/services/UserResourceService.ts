/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { CancelablePromise } from '../core/CancelablePromise';
import { OpenAPI } from '../core/OpenAPI';
import { request as __request } from '../core/request';
import type { CreateUserDto } from '../models/CreateUserDto';
import type { LoginDto } from '../models/LoginDto';
import type { LoginResponseDto } from '../models/LoginResponseDto';
export class UserResourceService {
  /**
   * @param requestBody
   * @returns LoginResponseDto OK
   * @throws ApiError
   */
  public static register(requestBody: CreateUserDto): CancelablePromise<LoginResponseDto> {
    return __request(OpenAPI, {
      method: 'POST',
      url: '/user/register',
      body: requestBody,
      mediaType: 'application/json',
    });
  }
  /**
   * @param requestBody
   * @returns LoginResponseDto OK
   * @throws ApiError
   */
  public static login(requestBody: LoginDto): CancelablePromise<LoginResponseDto> {
    return __request(OpenAPI, {
      method: 'POST',
      url: '/user/login',
      body: requestBody,
      mediaType: 'application/json',
    });
  }
  /**
   * @returns string OK
   * @throws ApiError
   */
  public static test(): CancelablePromise<string> {
    return __request(OpenAPI, {
      method: 'GET',
      url: '/user/test',
    });
  }
  /**
   * @param email
   * @returns boolean OK
   * @throws ApiError
   */
  public static isEmailFree(email: string): CancelablePromise<boolean> {
    return __request(OpenAPI, {
      method: 'GET',
      url: '/user/register/check-email-free',
      query: {
        email: email,
      },
    });
  }
}
