package dev.alvr.marvel.domain.characters.usecases

import androidx.paging.PagingData
import dev.alvr.marvel.domain.base.FlowUseCase
import dev.alvr.marvel.domain.characters.models.Character
import dev.alvr.marvel.domain.characters.repositories.CharactersRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetCharactersUseCase @Inject constructor(
    private val repository: CharactersRepository,
) : FlowUseCase<String, PagingData<Character>>() {
    override fun createFlow(params: String): Flow<PagingData<Character>> =
        repository.characters(params)
}
