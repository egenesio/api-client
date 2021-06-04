package com.egenesio.api_client.domain

/**
 * TODO add
 */
interface APIVersion {
  val name: String
  fun encoded() = "$name/"
}