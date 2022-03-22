package dev.alvr.marvel.ui.characters.list.item

import androidx.compose.runtime.Immutable
import dev.alvr.marvel.domain.characters.models.Character

@Immutable
internal data class CharacterItem(
    val id: Int,
    val name: String,
    val image: String,
)

internal fun Character.toItem() = CharacterItem(
    id = id,
    name = name,
    image = image
)
