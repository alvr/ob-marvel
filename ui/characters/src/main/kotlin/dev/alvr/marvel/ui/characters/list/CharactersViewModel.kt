package dev.alvr.marvel.ui.characters.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.alvr.marvel.domain.characters.models.Character
import dev.alvr.marvel.domain.characters.usecases.GetCharactersUseCase
import dev.alvr.marvel.ui.characters.list.item.toItem
import javax.inject.Inject
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@HiltViewModel
internal class CharactersViewModel @Inject constructor(
    getCharactersUseCase: GetCharactersUseCase,
) : ViewModel() {
    private val filterQuery = MutableStateFlow("")

    val characters = getCharactersUseCase.flow.map { paging ->
        paging.map(Character::toItem)
    }.cachedIn(viewModelScope)

    init {
        @OptIn(FlowPreview::class)
        viewModelScope.launch {
            filterQuery
                .debounce(SEARCH_DEBOUNCE_MILLIS)
                .onEach { filter ->
                    getCharactersUseCase(filter)
                }.collect()
        }
    }

    fun filterUsers(query: String) {
        filterQuery.value = query
    }

    companion object {
        private const val SEARCH_DEBOUNCE_MILLIS = 350L
    }
}
