package dev.alvr.marvel.data.remote.base

import java.security.MessageDigest

internal fun marvelApiHash(timestamp: String): String {
    val input = "$timestamp${BuildConfig.MARVEL_PRIVATE_KEY}${BuildConfig.MARVEL_PUBLIC_KEY}"
    return MessageDigest.getInstance("MD5")
        .digest(input.toByteArray())
        .joinToString(separator = "") { byte -> "%02x".format(byte) }
}
