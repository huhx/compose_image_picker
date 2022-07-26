package com.huhx.picker.data

import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.provider.MediaStore
import com.huhx.picker.constant.RequestType

private val projection = arrayOf(
    MediaStore.Video.Media._ID,
    MediaStore.Video.Media.DISPLAY_NAME,
    MediaStore.Video.Media.DATE_TAKEN,
    MediaStore.Files.FileColumns.MEDIA_TYPE,
    MediaStore.Video.Media.MIME_TYPE,
    MediaStore.Video.Media.DURATION,
    MediaStore.Video.Media.BUCKET_DISPLAY_NAME
)

object AssetLoader {

    fun load(context: Context, requestType: RequestType): List<AssetInfo> {
        val assets = ArrayList<AssetInfo>()
        val cursor = createCursor(context, requestType)
        if (cursor != null) {
            val indexId = cursor.getColumnIndex(projection[0])
            val indexFilename = cursor.getColumnIndex(projection[1])
            val indexDate = cursor.getColumnIndex(projection[2])
            val indexMediaType = cursor.getColumnIndex(projection[3])
            val indexMimeType = cursor.getColumnIndex(projection[4])
            val indexDuration = cursor.getColumnIndex(projection[5])
            val indexDirectory = cursor.getColumnIndex(projection[6])

            while (cursor.moveToNext()) {
                val id = cursor.getLong(indexId)
                val mediaType = cursor.getInt(indexMediaType)
                val contentUri = if (mediaType == MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE) {
                    ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
                } else {
                    ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, id)
                }

                assets.add(
                    AssetInfo(
                        id = id,
                        uriString = contentUri.toString(),
                        filename = cursor.getString(indexFilename),
                        date = cursor.getLong(indexDate),
                        mediaType = mediaType,
                        mimeType = cursor.getString(indexMimeType),
                        duration = cursor.getLong(indexDuration),
                        directory = cursor.getString(indexDirectory),
                    )
                )
            }
        }
        cursor?.close()
        return assets
    }

    private fun createCursor(context: Context, requestType: RequestType): Cursor? {
        val mediaType = MediaStore.Files.FileColumns.MEDIA_TYPE
        val image = MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE
        val video = MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO

        val selection = when (requestType) {
            RequestType.COMMON -> Selection(
                selection = "$mediaType=? OR $mediaType=?",
                arguments = listOf(image.toString(), video.toString())
            )
            RequestType.IMAGE -> Selection(
                selection = "$mediaType=?",
                arguments = listOf(image.toString())
            )
            RequestType.VIDEO -> Selection(
                selection = "$mediaType=?",
                arguments = listOf(video.toString())
            )
        }
        return createMediaCursor(context, selection)
    }

    private fun createMediaCursor(context: Context, selection: Selection): Cursor? {
        return context.contentResolver.query(
            MediaStore.Files.getContentUri("external"),
            projection,
            selection.selection,
            selection.arguments.toTypedArray(),
            "${MediaStore.Files.FileColumns.DATE_ADDED} DESC",
            null
        )
    }

    private data class Selection(
        val selection: String,
        val arguments: List<String>
    )
}

