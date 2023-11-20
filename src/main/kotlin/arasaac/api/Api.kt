package arasaac.api

import okhttp3.HttpUrl.Companion.toHttpUrl

object Api {
    const val ARASAAC_API_AUTH = "https://auth.arasaac.org"
    const val ARASAAC_API_PUBLIC = "https://api.arasaac.org"
    const val ARASAAC_API_PRIVATE = "https://privateapi.arasaac.org"

    val arasaacApiAuth = ARASAAC_API_AUTH.toHttpUrl()
    val arasaacApiPublic = ARASAAC_API_PUBLIC.toHttpUrl()
    val arasaacApiPrivate = ARASAAC_API_PRIVATE.toHttpUrl()
}
