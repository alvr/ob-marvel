package dev.alvr.marvel.data.remote.characters.sources

import androidx.paging.PagingSource
import com.appmattus.kotlinfixture.decorator.nullability.NeverNullStrategy
import com.appmattus.kotlinfixture.decorator.nullability.nullabilityStrategy
import com.appmattus.kotlinfixture.kotlinFixture
import dev.alvr.marvel.data.remote.characters.api.MockCharactersApi
import dev.alvr.marvel.data.remote.characters.responses.CharactersResponse
import dev.alvr.marvel.data.remote.characters.responses.toModel
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeTypeOf
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest

@OptIn(ExperimentalCoroutinesApi::class)
internal class CharactersPagingSourceTest : StringSpec() {
    private val fixture = kotlinFixture {
        nullabilityStrategy(NeverNullStrategy)
    }

    private val characters = listOf(
        CharactersResponse.Data.Result(
            id = 0,
            name = "Hulk",
            thumbnail = fixture()
        ),
        CharactersResponse.Data.Result(
            id = 1,
            name = "Thor",
            thumbnail = fixture()
        ),
        CharactersResponse.Data.Result(
            id = 2,
            name = "SpiderMan",
            thumbnail = fixture()
        ),
        CharactersResponse.Data.Result(
            id = 3,
            name = "IronMan",
            thumbnail = fixture()
        ),
    )

    private val fakeApi = MockCharactersApi().apply {
        characters.forEach { addCharacter(it) }
    }

    init {
        "return all characters if filter is empty" {
            runTest {
                val pagingSource = CharactersPagingSource(fakeApi, "")

                pagingSource.load(
                    PagingSource.LoadParams.Refresh(
                        key = null,
                        loadSize = 2,
                        placeholdersEnabled = false
                    )
                ).shouldBeTypeOf<PagingSource.LoadResult.Page<*, *>>() shouldBe PagingSource.LoadResult.Page(
                    data = characters.map(CharactersResponse.Data.Result::toModel),
                    prevKey = null,
                    nextKey = 20
                )
            }
        }

        "return characters that start with filter" {
            runTest {
                val pagingSource = CharactersPagingSource(fakeApi, "iron")

                pagingSource.load(
                    PagingSource.LoadParams.Refresh(
                        key = null,
                        loadSize = 2,
                        placeholdersEnabled = false
                    )
                ).shouldBeTypeOf<PagingSource.LoadResult.Page<*, *>>() shouldBe PagingSource.LoadResult.Page(
                    data = listOf(characters[3].toModel()),
                    prevKey = null,
                    nextKey = 20
                )
            }
        }

        "error happens" {
            runTest {
                val pagingSource = CharactersPagingSource(fakeApi, "error")

                pagingSource.load(
                    PagingSource.LoadParams.Refresh(
                        key = null,
                        loadSize = 2,
                        placeholdersEnabled = false
                    )
                ).shouldBeTypeOf<PagingSource.LoadResult.Error<*, *>>()
            }
        }
    }
}
