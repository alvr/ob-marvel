package dev.alvr.marvel.domain.characters.repositories

import androidx.paging.PagingData
import dev.alvr.marvel.domain.characters.models.Character
import dev.alvr.marvel.domain.characters.models.CharacterDetails
import kotlinx.coroutines.flow.Flow

interface CharactersRepository {
    suspend fun character(id: Int): CharacterDetails
    fun characters(filterQuery: String): Flow<PagingData<Character>>
}
