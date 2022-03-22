package dev.alvr.marvel.data.remote.characters.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.alvr.marvel.data.remote.characters.api.CharactersApi
import dev.alvr.marvel.data.remote.characters.repositories.CharactersRemoteRepositoryImpl
import dev.alvr.marvel.domain.characters.repositories.CharactersRepository
import javax.inject.Singleton
import retrofit2.Retrofit
import retrofit2.create

@Module
@InstallIn(SingletonComponent::class)
internal object CharactersRemoteModule {
    @Provides
    @Singleton
    fun provideCharactersApi(retrofit: Retrofit): CharactersApi = retrofit.create()

    @Provides
    fun provideListsRemoteRepository(impl: CharactersRemoteRepositoryImpl): CharactersRepository = impl
}
