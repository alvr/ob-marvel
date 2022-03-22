package dev.alvr.marvel.data.remote.characters.sources

import androidx.paging.PagingSource
import androidx.paging.PagingState
import dev.alvr.marvel.data.remote.characters.api.CharactersApi
import dev.alvr.marvel.data.remote.characters.responses.toModel
import dev.alvr.marvel.domain.characters.models.Character

internal class CharactersPagingSource(
    private val api: CharactersApi,
    private val filterQuery: String,
) : PagingSource<Int, Character>() {
    override fun getRefreshKey(state: PagingState<Int, Character>): Int? =
        state.anchorPosition

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Character> {
        val position = params.key ?: STARTING_OFFSET

        return try {
            val characters = if (filterQuery != "") {
                api.getCharactersByName(
                    query = filterQuery,
                    offset = position,
                    limit = params.loadSize
                ).data.results.map { it.toModel() }
            } else {
                api.getCharacters(
                    offset = position,
                    limit = params.loadSize
                ).data.results.map { it.toModel() }
            }

            LoadResult.Page(
                data = characters,
                prevKey = if (position == STARTING_OFFSET) null else position - PAGE_SIZE,
                nextKey = if (characters.isEmpty()) null else position + PAGE_SIZE
            )
        } catch (@Suppress("TooGenericExceptionCaught") exception: Exception) {
            LoadResult.Error(exception)
        }
    }

    companion object {
        private const val STARTING_OFFSET = 0
        const val PAGE_SIZE = 20
        const val MAX_PAGE_SIZE = 100
    }
}
