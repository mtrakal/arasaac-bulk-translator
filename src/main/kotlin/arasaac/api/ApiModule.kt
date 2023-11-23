package arasaac.api

import arasaac.api.usecases.DownloadPictogramsUseCase
import arasaac.api.usecases.DownloadPictogramsUseCaseImpl
import arasaac.api.usecases.UploadPictogramsUseCase
import arasaac.api.usecases.UploadPictogramsUseCaseImpl
import files.FileWriter
import org.koin.core.qualifier.named
import org.koin.core.scope.Scope
import org.koin.dsl.module
import retrofit2.Retrofit

val apiModule = module {
    single { createService("ApiAuth", LoginApi::class.java) }
    single { createService("ApiPrivate", PictogramsApi::class.java) }
    single<UploadPictogramsUseCase> { UploadPictogramsUseCaseImpl(get(), get()) }
    single<DownloadPictogramsUseCase> { DownloadPictogramsUseCaseImpl(get(), get(named(FileWriter.Type.JSON))) }
}

private fun <T> Scope.createService(retrofitType: String, clazz: Class<T>): T = (get(named(retrofitType)) as Retrofit)
    .create(clazz)
