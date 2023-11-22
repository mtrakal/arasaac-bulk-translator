package network

import arasaac.api.Api
import arasaac.model.AccessToken
import arasaac.model.AccessTokenImpl
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.skydoves.sandwich.retrofit.adapters.ApiResponseCallAdapterFactory
import core.Logger
import kotlinx.serialization.json.Json
import okhttp3.HttpUrl
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

val networkModule = module {
    single { provideHttpLoggingInterceptor(get()) }
    single { HeaderInterceptor() }
    single(named("BaseOkHttpClient")) { provideBaseOkHttpClient(get(), get()) }
    single<AccessToken> { AccessTokenImpl("void") }
    single { (TokenInterceptor(get())) }
    single { TokenAuthenticator(get(), get(named("BaseOkHttpClient")), get()) }
    single(named("ArasaacPrivateOkHttpClient")) {
        provideSecureOkHttpClient(
            get(named("BaseOkHttpClient")),
            get(),
        )
    }
    single(named("ApiAuth")) { provideRetrofit(get(named("BaseOkHttpClient")), Api.arasaacApiAuth) }
    single(named("ApiPublic")) { provideRetrofit(get(named("BaseOkHttpClient")), Api.arasaacApiPublic) }
    single(named("ApiPrivate")) { provideRetrofit(get(named("ArasaacPrivateOkHttpClient")), Api.arasaacApiPrivate) }
}

fun provideHttpLoggingInterceptor(logger: Logger): HttpLoggingInterceptor =
    HttpLoggingInterceptor { message -> logger.d(message) }
        .apply { level = HttpLoggingInterceptor.Level.HEADERS }

fun provideBaseOkHttpClient(
    headerInterceptor: HeaderInterceptor,
    logging: HttpLoggingInterceptor,
): OkHttpClient = OkHttpClient
    .Builder()
    .addInterceptor(headerInterceptor)
    .readTimeout(30, TimeUnit.SECONDS)
    .writeTimeout(30, TimeUnit.SECONDS)
    .connectTimeout(15, TimeUnit.SECONDS)
    .addInterceptor(logging)
    .build()

fun provideSecureOkHttpClient(
    base: OkHttpClient,
    interceptor: TokenInterceptor,
//    authenticator: TokenAuthenticator,
): OkHttpClient = base
    .newBuilder()
    .addInterceptor(interceptor)
//    .authenticator(authenticator)
    .build()

fun provideRetrofit(
    client: OkHttpClient,
    api: HttpUrl,
    converterFactory: retrofit2.Converter.Factory = Json.asConverterFactory("application/json".toMediaType()),
): Retrofit = Retrofit.Builder()
    .baseUrl(api)
    .client(client)
    .addCallAdapterFactory(ApiResponseCallAdapterFactory.create())
    .addConverterFactory(converterFactory)
    .build()
