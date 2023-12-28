package com.huhx.picker.model

enum class AssetResourceType {
    IMAGE, GIF, VIDEO;

    companion object {
        private val videos = listOf("mp4", "avi", "mov")

        fun fromFileName(filename: String): AssetResourceType {
            return if (filename.endsWith("gif", true)) {
                GIF
            } else if (videos.any { filename.endsWith(it, true) }) {
                VIDEO
            } else {
                IMAGE
            }
        }
    }
}