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
import type { UserDto } from '../models/UserDto';
export class UserRessourceService {
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
   * @param requestBody
   * @returns any OK
   * @throws ApiError
   */
  public static createAdminUser(requestBody: CreateUserDto): CancelablePromise<any> {
    return __request(OpenAPI, {
      method: 'POST',
      url: '/backoffice/user/create',
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
   * @returns UserDto OK
   * @throws ApiError
   */
  public static getAllAdminUsers(): CancelablePromise<Array<UserDto>> {
    return __request(OpenAPI, {
      method: 'GET',
      url: '/backoffice/user',
    });
  }
}
