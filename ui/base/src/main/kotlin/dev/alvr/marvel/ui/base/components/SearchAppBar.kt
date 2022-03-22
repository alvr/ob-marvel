package dev.alvr.marvel.ui.base.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import dev.alvr.marvel.ui.base.R

@Composable
@ExperimentalComposeUiApi
fun SearchAppBar(
    placeHolder: String,
    actionIcon: ImageVector,
    onTextChange: (String) -> Unit,
) {
    var searchWidgetState by rememberSaveable { mutableStateOf(SearchWidget.Closed) }

    when (searchWidgetState) {
        SearchWidget.Closed -> DefaultAppBar(
            actionIcon = actionIcon,
            onSearchClicked = { searchWidgetState = SearchWidget.Opened }
        )
        SearchWidget.Opened -> SearchAppBar(
            placeHolder = placeHolder,
            onTextChange = onTextChange,
            onCloseClicked = { searchWidgetState = SearchWidget.Closed },
        )
    }
}

@Composable
private fun DefaultAppBar(
    actionIcon: ImageVector,
    onSearchClicked: () -> Unit
) {
    TopAppBar(
        title = { Text(text = stringResource(id = R.string.appbar_title)) },
        actions = {
            IconButton(onClick = onSearchClicked) {
                Icon(
                    imageVector = actionIcon,
                    contentDescription = stringResource(id = R.string.search_icon),
                )
            }
        }
    )
}

@Composable
@ExperimentalComposeUiApi
private fun SearchAppBar(
    placeHolder: String,
    onTextChange: (String) -> Unit,
    onCloseClicked: () -> Unit,
) {
    var searchText by rememberSaveable { mutableStateOf("") }

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }

    TextField(
        modifier = Modifier.fillMaxWidth().focusRequester(focusRequester),
        value = searchText,
        onValueChange = { text ->
            searchText = text
            onTextChange(text)
        },
        placeholder = {
            Text(
                modifier = Modifier.alpha(ContentAlpha.medium),
                text = placeHolder,
            )
        },
        singleLine = true,
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = stringResource(id = R.string.search_icon),
            )
        },
        trailingIcon = {
            IconButton(
                onClick = {
                    if (searchText.isNotEmpty()) {
                        searchText = ""
                        onTextChange("")
                    } else {
                        onCloseClicked()
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = stringResource(id = R.string.close_icon),
                )
            }
        },
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Search
        ),
        keyboardActions = KeyboardActions(
            onSearch = {
                keyboardController?.hide()
            }
        ),
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = MaterialTheme.colors.primary,
            cursorColor = MaterialTheme.colors.onPrimary,
            focusedIndicatorColor = Color.Transparent,
        )
    )

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
}

@Immutable
private enum class SearchWidget {
    Closed,
    Opened,
}
