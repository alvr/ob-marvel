package dev.alvr.marvel.ui.characters.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.alvr.marvel.domain.characters.usecases.GetCharacterDetailUseCase
import dev.alvr.marvel.ui.base.state.ViewState
import dev.alvr.marvel.ui.characters.CHARACTER_ARG
import dev.alvr.marvel.ui.characters.details.item.CharacterInfo
import dev.alvr.marvel.ui.characters.details.item.toInfo
import javax.inject.Inject
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
internal class CharacterDetailsViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getCharacterDetailUseCase: GetCharacterDetailUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow<ViewState<CharacterInfo>>(ViewState.Uninitialized)
    val state = _state.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(SUBSCRIPTION_MILLIS),
        initialValue = ViewState.Uninitialized
    )

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        _state.value = ViewState.Failure(throwable)
    }

    init {
        fetchCharacterDetails()
    }

    fun fetchCharacterDetails() {
        viewModelScope.launch(coroutineExceptionHandler) {
            _state.value = ViewState.Loading

            savedStateHandle.get<Int>(CHARACTER_ARG)?.let { id ->
                val details = getCharacterDetailUseCase(id).toInfo()
                _state.value = ViewState.Success(details)
            } ?: run {
                _state.value = ViewState.Failure(Exception("no character id specified"))
            }
        }
    }

    companion object {
        private const val SUBSCRIPTION_MILLIS = 5000L
    }
}
