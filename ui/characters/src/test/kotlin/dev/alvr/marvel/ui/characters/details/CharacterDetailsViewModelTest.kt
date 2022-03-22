package dev.alvr.marvel.ui.characters.details

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import dev.alvr.marvel.domain.characters.usecases.GetCharacterDetailUseCase
import dev.alvr.marvel.ui.base.state.ViewState
import dev.alvr.marvel.ui.characters.details.item.CharacterInfo
import dev.alvr.marvel.ui.characters.details.item.toInfo
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.equality.shouldBeEqualToComparingFields
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeTypeOf
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import dev.alvr.marvel.domain.characters.models.CharacterDetails as CharacterDomainDetails

@OptIn(ExperimentalCoroutinesApi::class)
internal class CharacterDetailsViewModelTest : StringSpec() {
    private val characterDomain = CharacterDomainDetails(
        id = 0,
        name = "IronMan",
        image = "",
        description = "Test character",
        copyright = "MARVEL",
        extras = CharacterDomainDetails.Extras(emptyMap())
    )

    private val testDispatcher = StandardTestDispatcher()

    private val savedStateHandle = mockk<SavedStateHandle>(relaxed = true)
    private val useCase = mockk<GetCharacterDetailUseCase>()

    override fun isolationMode() = IsolationMode.InstancePerTest

    init {
        beforeEach {
            Dispatchers.setMain(testDispatcher)
        }

        afterEach {
            Dispatchers.resetMain()
        }

        "the id is not found" {
            runTest {
                every { savedStateHandle.get<Int>(any()) } returns null
                coEvery { useCase(any()) } returns characterDomain

                CharacterDetailsViewModel(savedStateHandle, useCase).state.test {
                    awaitItem().shouldBeTypeOf<ViewState.Uninitialized>()
                    awaitItem().shouldBeTypeOf<ViewState.Failure>().also { failure ->
                        failure.error.message shouldBe "no character id specified"
                    }
                    cancelAndIgnoreRemainingEvents()
                }

                coVerify(exactly = 0) { useCase(any()) }
            }
        }

        "the use case fails to execute" {
            runTest {
                every { savedStateHandle.get<Int>(any()) } returns 0
                coEvery { useCase(any()) } throws Exception("something happened in the request")

                CharacterDetailsViewModel(savedStateHandle, useCase).state.test {
                    awaitItem().shouldBeTypeOf<ViewState.Uninitialized>()
                    awaitItem().shouldBeTypeOf<ViewState.Failure>().also { failure ->
                        failure.error.message shouldBe "something happened in the request"
                    }
                    cancelAndIgnoreRemainingEvents()
                }

                coVerify(exactly = 1) { useCase(any()) }
            }
        }

        "the use case is successful" {
            runTest {
                every { savedStateHandle.get<Int>(any()) } returns 0
                coEvery { useCase(any()) } returns characterDomain

                CharacterDetailsViewModel(savedStateHandle, useCase).state.test {
                    awaitItem().shouldBeTypeOf<ViewState.Uninitialized>()
                    awaitItem().shouldBeTypeOf<ViewState.Success<CharacterInfo>>().also { success ->
                        success.value shouldBeEqualToComparingFields characterDomain.toInfo()
                    }
                    cancelAndIgnoreRemainingEvents()
                }

                coVerify(exactly = 1) { useCase(any()) }
            }
        }
    }
}
