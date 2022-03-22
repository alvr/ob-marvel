package dev.alvr.marvel.ui.characters.list.item

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SearchOff
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.items
import dev.alvr.marvel.ui.characters.CARD_HEIGHT
import dev.alvr.marvel.ui.characters.COVER_MAX_WIDTH
import dev.alvr.marvel.ui.characters.FILTER_NOT_FOUND_SCALE
import dev.alvr.marvel.ui.characters.R
import dev.alvr.marvel.ui.characters.components.CharacterImage
import dev.alvr.marvel.ui.characters.components.DataLoading

@Composable
@OptIn(ExperimentalFoundationApi::class)
internal fun CharactersList(
    items: LazyPagingItems<CharacterItem>,
    openDetails: (Int?) -> Unit,
    modifier: Modifier = Modifier,
) {
    val noItemsFound = with(items) {
        itemCount == 0 && loadState.append is LoadState.NotLoading && loadState.append.endOfPaginationReached
    }

    if (noItemsFound) {
        NoItemsFound()
    } else {
        List(
            modifier = modifier,
            items = items,
            openDetails = openDetails
        )
    }
}

@Composable
@ExperimentalFoundationApi
private fun List(
    items: LazyPagingItems<CharacterItem>,
    openDetails: (Int?) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        with(items) {
            items(this) { item ->
                CharactersListItem(
                    item = item,
                    modifier = Modifier
                        .animateItemPlacement()
                        .fillParentMaxWidth(),
                    openDetails = openDetails,
                )
            }

            when {
                loadState.refresh is LoadState.Loading -> item {
                    DataLoading(modifier = Modifier.fillParentMaxSize())
                }
                loadState.append is LoadState.Loading -> item { LoadingItem() }
                loadState.refresh is LoadState.Error -> item {
                    ErrorItem(
                        modifier = Modifier.fillParentMaxSize(),
                        onClickRetry = ::retry
                    )
                }
                loadState.append is LoadState.Error -> item {
                    ErrorItem(onClickRetry = ::retry)
                }
            }
        }
    }
}

@Composable
@ExperimentalFoundationApi
private fun CharactersListItem(
    item: CharacterItem?,
    modifier: Modifier = Modifier,
    openDetails: (Int?) -> Unit,
) {
    Card(
        modifier = modifier
            .height(CARD_HEIGHT)
            .clickable { openDetails(item?.id) }
    ) {
        ConstraintLayout {
            val (image, title) = createRefs()

            CharacterImage(
                image = item?.image,
                title = item?.name,
                modifier = Modifier
                    .widthIn(max = COVER_MAX_WIDTH)
                    .fillMaxHeight()
                    .constrainAs(image) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        bottom.linkTo(parent.bottom)
                    }
            )

            Title(
                title = item?.name,
                modifier = Modifier.constrainAs(title) {
                    width = Dimension.fillToConstraints
                    top.linkTo(parent.top, margin = 4.dp)
                    start.linkTo(image.end, margin = 8.dp)
                    end.linkTo(parent.end, margin = 8.dp)
                }
            )
        }
    }
}

@Composable
private fun Title(
    title: String?,
    modifier: Modifier = Modifier
) {
    Text(text = title.orEmpty(), modifier = modifier, style = MaterialTheme.typography.h6)
}

@Composable
private fun NoItemsFound() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            modifier = Modifier.scale(FILTER_NOT_FOUND_SCALE).padding(bottom = 4.dp),
            imageVector = Icons.Default.SearchOff,
            contentDescription = null
        )
        Text(text = stringResource(id = R.string.no_characters_found))
    }
}

@Composable
private fun LoadingItem() {
    CircularProgressIndicator(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .wrapContentWidth(Alignment.CenterHorizontally)
    )
}

@Composable
private fun ErrorItem(
    modifier: Modifier = Modifier,
    onClickRetry: () -> Unit
) {
    Row(
        modifier = modifier.padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(id = R.string.loading_next_page_error),
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp),
            color = MaterialTheme.colors.error
        )
        OutlinedButton(onClick = onClickRetry) {
            Text(text = stringResource(id = R.string.try_again))
        }
    }
}
