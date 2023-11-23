package arasaac.api.auth

import org.koin.core.qualifier.named
import org.koin.dsl.module

val authModule = module {
    single<ArasaacAuth>(named(ArasaacAuth.Type.BEARER)) { ArasaacAuthBearerImpl(get()) }
    single<ArasaacAuth>(named(ArasaacAuth.Type.USER_PASS)) { ArasaacAuthUserPassImpl(get(), get()) }
}
