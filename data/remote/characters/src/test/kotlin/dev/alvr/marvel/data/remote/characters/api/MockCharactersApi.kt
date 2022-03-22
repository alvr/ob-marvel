package dev.alvr.marvel.data.remote.characters.api

import dev.alvr.marvel.data.remote.characters.responses.CharacterDetailsResponse
import dev.alvr.marvel.data.remote.characters.responses.CharactersResponse

internal class MockCharactersApi : CharactersApi {
    private val model = mutableListOf<CharactersResponse.Data.Result>()

    private val characters get() = CharactersResponse(
        data = CharactersResponse.Data(
            results = model,
        )
    )

    fun addCharacter(character: CharactersResponse.Data.Result) {
        model.add(character)
    }

    override suspend fun getCharacters(offset: Int, limit: Int): CharactersResponse = characters
    override suspend fun getCharactersByName(query: String, offset: Int, limit: Int): CharactersResponse {
        if (query == "error") error("simulate error")

        return characters.copy(
            data = characters.data.copy(
                results = characters.data.results.filter { it.name.startsWith(query, ignoreCase = true) }
            )
        )
    }
    override suspend fun getCharacterDetails(id: Int): CharacterDetailsResponse = TODO()
}
