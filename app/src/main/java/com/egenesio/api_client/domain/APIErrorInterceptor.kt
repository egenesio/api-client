package com.egenesio.api_client.domain

/**
 * TODO
 */
interface APIErrorInterceptor {
    suspend fun from(apiRequestError: APIRequestError): APIError
}