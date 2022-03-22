package dev.alvr.marvel.data.remote.characters.repositories

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import dev.alvr.marvel.data.remote.characters.api.CharactersApi
import dev.alvr.marvel.data.remote.characters.responses.toModel
import dev.alvr.marvel.data.remote.characters.sources.CharactersPagingSource
import dev.alvr.marvel.domain.characters.models.Character
import dev.alvr.marvel.domain.characters.models.CharacterDetails
import dev.alvr.marvel.domain.characters.repositories.CharactersRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

internal class CharactersRemoteRepositoryImpl @Inject constructor(
    private val api: CharactersApi,
) : CharactersRepository {
    override suspend fun character(id: Int): CharacterDetails =
        api.getCharacterDetails(id).toModel()

    override fun characters(filterQuery: String): Flow<PagingData<Character>> = Pager(
        config = PagingConfig(
            pageSize = CharactersPagingSource.PAGE_SIZE,
            maxSize = CharactersPagingSource.MAX_PAGE_SIZE,
        ),
        pagingSourceFactory = { CharactersPagingSource(api, filterQuery) }
    ).flow
}
