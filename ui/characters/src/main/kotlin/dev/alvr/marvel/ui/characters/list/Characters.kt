package dev.alvr.marvel.ui.characters.list

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PersonSearch
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import dev.alvr.marvel.ui.base.components.SearchAppBar
import dev.alvr.marvel.ui.characters.CharactersNavigator
import dev.alvr.marvel.ui.characters.R
import dev.alvr.marvel.ui.characters.list.item.CharactersList

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun Characters(navigator: CharactersNavigator) {
    Characters(vm = hiltViewModel(), openDetails = navigator::goToDetails)
}

@Composable
@ExperimentalComposeUiApi
private fun Characters(
    vm: CharactersViewModel,
    openDetails: (Int?) -> Unit,
) {
    val items = vm.characters.collectAsLazyPagingItems()

    Scaffold(
        topBar = {
            SearchAppBar(
                placeHolder = stringResource(id = R.string.search_characters),
                actionIcon = Icons.Filled.PersonSearch,
                onTextChange = vm::filterUsers
            )
        },
        content = { paddingValues ->
            CharactersList(
                modifier = Modifier.padding(paddingValues),
                items = items,
                openDetails = openDetails,
            )
        }
    )
}
