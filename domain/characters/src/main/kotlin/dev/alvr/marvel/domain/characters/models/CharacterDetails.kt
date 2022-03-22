package dev.alvr.marvel.domain.characters.models

data class CharacterDetails(
    val id: Int,
    val name: String,
    val description: String,
    val image: String,
    val copyright: String,
    val extras: Extras
) {
    @JvmInline
    value class Extras(val extras: Map<Type, List<String>> = emptyMap()) {
        enum class Type {
            COMIC,
            EVENT,
            SERIE,
            STORY,
        }
    }
}
