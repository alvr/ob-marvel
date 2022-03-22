package dev.alvr.marvel.data.remote.characters.api

import dev.alvr.marvel.data.remote.characters.responses.CharacterDetailsResponse
import dev.alvr.marvel.data.remote.characters.responses.CharactersResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

internal interface CharactersApi {
    @GET("characters")
    suspend fun getCharacters(
        @Query("offset") offset: Int,
        @Query("limit") limit: Int,
    ): CharactersResponse

    @GET("characters")
    suspend fun getCharactersByName(
        @Query("nameStartsWith") query: String,
        @Query("offset") offset: Int,
        @Query("limit") limit: Int,
    ): CharactersResponse

    @GET("characters/{id}")
    suspend fun getCharacterDetails(
        @Path("id") id: Int,
    ): CharacterDetailsResponse
}
