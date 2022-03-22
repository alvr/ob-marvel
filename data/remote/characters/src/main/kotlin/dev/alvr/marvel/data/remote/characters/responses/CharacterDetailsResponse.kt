package dev.alvr.marvel.data.remote.characters.responses

import dev.alvr.marvel.domain.characters.models.CharacterDetails
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class CharacterDetailsResponse(
    @SerialName("copyright")
    val copyright: String,
    @SerialName("data")
    val data: Data,
) {
    @Serializable
    data class Data(
        @SerialName("results")
        val results: List<Result>,
    ) {
        @Serializable
        data class Result(
            @SerialName("comics")
            val comics: Comics,
            @SerialName("description")
            val description: String,
            @SerialName("events")
            val events: Events,
            @SerialName("id")
            val id: Int,
            @SerialName("name")
            val name: String,
            @SerialName("series")
            val series: Series,
            @SerialName("stories")
            val stories: Stories,
            @SerialName("thumbnail")
            val thumbnail: Thumbnail,
        ) {
            @Serializable
            data class Comics(
                @SerialName("items")
                val items: List<Item>,
            ) {
                @Serializable
                data class Item(
                    @SerialName("name")
                    val name: String,
                )
            }

            @Serializable
            data class Events(
                @SerialName("items")
                val items: List<Item>,
            ) {
                @Serializable
                data class Item(
                    @SerialName("name")
                    val name: String,
                )
            }

            @Serializable
            data class Series(
                val items: List<Item>,
            ) {
                @Serializable
                data class Item(
                    @SerialName("name")
                    val name: String,
                )
            }

            @Serializable
            data class Stories(
                @SerialName("items")
                val items: List<Item>,
            ) {
                @Serializable
                data class Item(
                    @SerialName("name")
                    val name: String,
                )
            }

            @Serializable
            data class Thumbnail(
                @SerialName("extension")
                val extension: String,
                @SerialName("path")
                val path: String
            )
        }
    }
}

internal fun CharacterDetailsResponse.toModel(): CharacterDetails {
    val item = data.results.first()

    return CharacterDetails(
        id = item.id,
        name = item.name,
        description = item.description,
        copyright = copyright,
        image = "${item.thumbnail.path}.${item.thumbnail.extension}".replace("http://", "https://"),
        extras = item.parseExtras()
    )
}

private fun CharacterDetailsResponse.Data.Result.parseExtras(): CharacterDetails.Extras {
    val extras = mutableMapOf<CharacterDetails.Extras.Type, List<String>>().apply {
        putIfNotEmpty(CharacterDetails.Extras.Type.COMIC, comics.items.map { it.name })
        putIfNotEmpty(CharacterDetails.Extras.Type.EVENT, events.items.map { it.name })
        putIfNotEmpty(CharacterDetails.Extras.Type.SERIE, series.items.map { it.name })
        putIfNotEmpty(CharacterDetails.Extras.Type.STORY, stories.items.map { it.name })
    }

    return CharacterDetails.Extras(extras)
}

private fun MutableMap<CharacterDetails.Extras.Type, List<String>>.putIfNotEmpty(
    type: CharacterDetails.Extras.Type,
    items: List<String>
) {
    if (items.isNotEmpty()) {
        put(type, items)
    }
}
