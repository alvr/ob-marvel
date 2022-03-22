package dev.alvr.marvel.ui.characters.details.item

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import dev.alvr.marvel.domain.characters.models.CharacterDetails
import dev.alvr.marvel.ui.characters.R

@Immutable
internal data class CharacterInfo(
    val id: Int = 0,
    val name: String = "",
    val description: String = "",
    val image: String = "",
    val copyright: String = "",
    val extras: Extras = Extras(),
) {
    @Immutable
    @JvmInline
    internal value class Extras(val extras: Map<Type, List<String>> = emptyMap()) {
        enum class Type(@StringRes val title: Int) {
            Comic(R.string.extra_comic),
            Event(R.string.extra_event),
            Serie(R.string.extra_serie),
            Story(R.string.extra_story),
        }
    }
}

internal fun CharacterDetails.toInfo() = CharacterInfo(
    id = id,
    name = name,
    description = description,
    image = image,
    copyright = copyright,
    extras = extras.toInfo()
)

private fun CharacterDetails.Extras.toInfo(): CharacterInfo.Extras {
    val mappedExtras = extras.mapKeys { entry ->
        when (entry.key) {
            CharacterDetails.Extras.Type.COMIC -> CharacterInfo.Extras.Type.Comic
            CharacterDetails.Extras.Type.EVENT -> CharacterInfo.Extras.Type.Event
            CharacterDetails.Extras.Type.SERIE -> CharacterInfo.Extras.Type.Serie
            CharacterDetails.Extras.Type.STORY -> CharacterInfo.Extras.Type.Story
        }
    }

    return CharacterInfo.Extras(mappedExtras)
}
