package com.egenesio.api_client.networking

import com.egenesio.api_client.domain.APIRequest
import io.ktor.client.features.auth.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.http.auth.*

/**
 * Add [TokenAuthProvider] to client [Auth] providers.
 */
/**
 * TODO add
 */
public fun Auth.authToken(block: TokenAuthConfig.() -> Unit) {
    with(TokenAuthConfig().apply(block)) {
        providers.add(TokenAuthProvider(header, staticToken, token))
    }
}

/**
 * [TokenAuthProvider] configuration.
 */
/**
 * TODO add
 */
public class TokenAuthConfig {

    public var header: String = HttpHeaders.Authorization

    public var staticToken: String? = null

    public var token: (suspend () -> String)? = null
}

/**
 * Client token authentication provider.
 */
/**
 * TODO add
 */
public class TokenAuthProvider(
    private val header: String,
    private val staticToken: String?,
    private val token: (suspend () -> String)?
) : AuthProvider {

    override val sendWithoutRequest: Boolean = true

    override fun isApplicable(auth: HttpAuthHeader): Boolean = true

    override suspend fun addRequestHeaders(request: HttpRequestBuilder) {
        val isPrivate = request.attributes[APIRequest.httpRequestAttributeIsPrivate]

        if (isPrivate) {
            request.headers[header] = token?.invoke() ?: staticToken ?: ""
        }
    }
}
