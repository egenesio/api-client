package com.egenesio.api_client.util

import io.ktor.http.*

val URLBuilder.isDefaultHost: Boolean get() = this.host == "localhost"
val URLBuilder.isHostSet: Boolean get() = this.host != "localhost"