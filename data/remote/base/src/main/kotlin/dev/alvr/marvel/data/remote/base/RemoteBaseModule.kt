package dev.alvr.marvel.data.remote.base

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import de.nycode.retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
internal object RemoteBaseModule {

    private const val MARVEL_BASE_URL = "https://gateway.marvel.com:443/v1/public/"
    private const val MARVEL_CLIENT_TIMEOUT = 30L

    private const val JSON_MEDIA_TYPE = "application/json"

    private const val QUERY_API_KEY = "apikey"
    private const val QUERY_HASH_KEY = "hash"
    private const val QUERY_TIMESTAMP = "ts"

    /**
     * Is a must to append some parameters to the url. Check the following:
     * https://developer.marvel.com/documentation/authorization
     *
     * @return the interceptor that appends the necessary parameters to the url
     */
    @Provides
    @Singleton
    @UrlInterceptor
    fun provideUrlTokenInterceptor(): Interceptor = Interceptor { chain ->
        val timestamp = "${System.currentTimeMillis()}"

        val original = chain.request()
        val newUrl = original.url.newBuilder()
            .addQueryParameter(QUERY_API_KEY, BuildConfig.MARVEL_PUBLIC_KEY)
            .addQueryParameter(QUERY_HASH_KEY, marvelApiHash(timestamp))
            .addQueryParameter(QUERY_TIMESTAMP, timestamp)
            .build()

        chain.proceed(original.newBuilder().url(newUrl).build())
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        @UrlInterceptor urlInterceptor: Interceptor
    ): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }

        return OkHttpClient.Builder()
            .addInterceptor(urlInterceptor)
            .addInterceptor(loggingInterceptor)
            .connectTimeout(MARVEL_CLIENT_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(MARVEL_CLIENT_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(MARVEL_CLIENT_TIMEOUT, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideJson(): Json = Json {
        prettyPrint = true
        ignoreUnknownKeys = true
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        client: OkHttpClient,
        json: Json
    ): Retrofit = Retrofit.Builder()
        .client(client)
        .addConverterFactory(json.asConverterFactory(JSON_MEDIA_TYPE.toMediaType()))
        .baseUrl(MARVEL_BASE_URL)
        .build()
}
