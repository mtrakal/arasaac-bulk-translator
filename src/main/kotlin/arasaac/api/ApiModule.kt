package arasaac.api

import org.koin.core.qualifier.named
import org.koin.core.scope.Scope
import org.koin.dsl.module
import retrofit2.Retrofit

val apiModule = module {
    single { createService("ApiAuth", LoginApi::class.java) }
    single { createService("ApiPrivate", PictogramsApi::class.java) }
}

private fun <T> Scope.createService(retrofitType: String, clazz: Class<T>): T = (get(named(retrofitType)) as Retrofit)
    .create(clazz)
