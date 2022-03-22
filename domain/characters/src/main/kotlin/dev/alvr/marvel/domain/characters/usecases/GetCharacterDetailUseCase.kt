package dev.alvr.marvel.domain.characters.usecases

import dev.alvr.marvel.domain.base.IoDispatcher
import dev.alvr.marvel.domain.base.UseCase
import dev.alvr.marvel.domain.characters.models.CharacterDetails
import dev.alvr.marvel.domain.characters.repositories.CharactersRepository
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher

class GetCharacterDetailUseCase @Inject constructor(
    private val repository: CharactersRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher,
) : UseCase<Int, CharacterDetails>(dispatcher) {
    override suspend fun doWork(params: Int): CharacterDetails =
        repository.character(params)
}
