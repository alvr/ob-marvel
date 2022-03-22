package dev.alvr.marvel.data.remote.characters.responses

import dev.alvr.marvel.domain.characters.models.Character
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class CharactersResponse(
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
            @SerialName("id")
            val id: Int,
            @SerialName("name")
            val name: String,
            @SerialName("thumbnail")
            val thumbnail: Thumbnail,
        ) {

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

internal fun CharactersResponse.Data.Result.toModel() = Character(
    id = id,
    name = name,
    image = "${thumbnail.path}.${thumbnail.extension}".replace("http://", "https://"),
)
