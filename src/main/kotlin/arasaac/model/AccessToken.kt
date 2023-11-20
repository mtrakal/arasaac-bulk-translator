package arasaac.model

interface AccessToken {
    var token: String
}

data class AccessTokenImpl(override var token: String) : AccessToken
