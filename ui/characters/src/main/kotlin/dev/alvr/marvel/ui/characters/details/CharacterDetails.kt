package dev.alvr.marvel.ui.characters.details

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import dev.alvr.marvel.ui.base.state.ViewState
import dev.alvr.marvel.ui.characters.CHARACTER_IMAGE_SCALE
import dev.alvr.marvel.ui.characters.R
import dev.alvr.marvel.ui.characters.components.CharacterImage
import dev.alvr.marvel.ui.characters.components.DataLoading
import dev.alvr.marvel.ui.characters.details.item.CharacterInfo

@Composable
@OptIn(ExperimentalFoundationApi::class)
fun CharacterDetails() {
    CharacterDetails(hiltViewModel())
}

@Composable
@ExperimentalFoundationApi
private fun CharacterDetails(vm: CharacterDetailsViewModel) {
    val state by vm.state.collectAsState()

    Scaffold { paddingValues ->
        CharacterInfo(
            state = state,
            modifier = Modifier.padding(paddingValues),
            retryOnError = vm::fetchCharacterDetails,
        )
    }
}

@Composable
@ExperimentalFoundationApi
private fun CharacterInfo(
    state: ViewState<CharacterInfo>,
    retryOnError: () -> Unit,
    modifier: Modifier = Modifier
) {
    when (state) {
        ViewState.Uninitialized -> Unit
        ViewState.Loading -> DataLoading(modifier = modifier.fillMaxSize())
        is ViewState.Success -> CharacterInfo(info = state.value)
        is ViewState.Failure -> CharacterInfoError(
            modifier = modifier,
            retry = retryOnError
        )
    }
}

@Composable
@ExperimentalFoundationApi
private fun CharacterInfo(
    info: CharacterInfo
) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                top = 16.dp,
                start = 8.dp,
                end = 8.dp
            )
    ) {
        val (details, extras, copyright) = createRefs()

        HeaderDetails(
            image = info.image,
            name = info.name,
            description = info.description,
            modifier = Modifier.constrainAs(details) {
                width = Dimension.fillToConstraints
                height = Dimension.wrapContent
                start.linkTo(parent.start)
                top.linkTo(parent.top)
                end.linkTo(parent.end)
            }
        )

        Extras(
            extras = info.extras,
            modifier = Modifier.constrainAs(extras) {
                width = Dimension.fillToConstraints
                height = Dimension.fillToConstraints
                start.linkTo(parent.start)
                top.linkTo(details.bottom)
                end.linkTo(parent.end)
                bottom.linkTo(copyright.top)
            }
        )

        Footer(
            copyright = info.copyright,
            modifier = Modifier.constrainAs(copyright) {
                width = Dimension.fillToConstraints
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                bottom.linkTo(parent.bottom)
            }
        )
    }
}

@Composable
@ExperimentalFoundationApi
private fun HeaderDetails(
    image: String,
    name: String,
    description: String,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
    ) {
        var expanded by remember { mutableStateOf(false) }

        Column(modifier = Modifier.clickable(description.isNotBlank()) { expanded = !expanded }) {
            CharacterImage(
                image = image,
                title = name,
                modifier = Modifier
                    .scale(CHARACTER_IMAGE_SCALE)
                    .align(Alignment.CenterHorizontally)
                    .clip(MaterialTheme.shapes.large)
            )

            Text(
                modifier = Modifier.fillMaxWidth(),
                text = buildAnnotatedString {
                    append(name)
                    if (description.isNotBlank()) {
                        append(' ')
                        appendInlineContent(INLINED_ARROW)
                    }
                },
                inlineContent = mapOf(
                    INLINED_ARROW to InlineTextContent(
                        placeholder = Placeholder(
                            width = MaterialTheme.typography.h5.fontSize,
                            height = MaterialTheme.typography.h5.fontSize,
                            placeholderVerticalAlign = PlaceholderVerticalAlign.TextCenter
                        ),
                        children = {
                            Icon(
                                imageVector = if (expanded) {
                                    Icons.Filled.ArrowDropUp
                                } else {
                                    Icons.Filled.ArrowDropDown
                                },
                                contentDescription = null,
                            )
                        }
                    )
                ),
                style = MaterialTheme.typography.h5,
                textAlign = TextAlign.Center
            )

            AnimatedVisibility(visible = expanded) {
                Text(
                    modifier = Modifier.padding(8.dp),
                    text = description,
                    style = MaterialTheme.typography.subtitle2,
                    textAlign = TextAlign.Justify,
                )
            }
        }
    }
}

@Composable
@ExperimentalFoundationApi
private fun Extras(
    extras: CharacterInfo.Extras,
    modifier: Modifier = Modifier,
) {
    if (extras.extras.isNotEmpty()) {
        Column(modifier = modifier) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(id = R.string.extras),
                style = MaterialTheme.typography.h6,
            )

            LazyColumn {
                extras.extras.forEach { (type, chExtras) ->
                    stickyHeader {
                        if (chExtras.isNotEmpty()) {
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(MaterialTheme.colors.primary),
                                text = stringResource(id = type.title),
                                style = MaterialTheme.typography.body1
                            )
                        }
                    }

                    items(chExtras) { extra ->
                        Text(text = extra, style = MaterialTheme.typography.body2)
                    }
                }
            }
        }
    } else {
        Row(
            modifier = modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(id = R.string.no_extras_found),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.h6,
            )
        }
    }
}

@Composable
private fun Footer(
    copyright: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = copyright,
        modifier = modifier,
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.caption
    )
}

@Composable
private fun CharacterInfoError(
    retry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.fetch_details_error),
            modifier = Modifier.padding(bottom = 8.dp),
            textAlign = TextAlign.Center,
            color = MaterialTheme.colors.error
        )
        OutlinedButton(onClick = retry) {
            Text(text = stringResource(id = R.string.try_again))
        }
    }
}

private const val INLINED_ARROW = "inlineArrowContent"
