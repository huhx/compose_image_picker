package com.huhx.picker.data

import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import com.huhx.picker.constant.RequestType

private val projection = arrayOf(
    MediaStore.Video.Media._ID,
    MediaStore.Video.Media.DISPLAY_NAME,
    MediaStore.Video.Media.DATE_TAKEN,
    MediaStore.Files.FileColumns.MEDIA_TYPE,
    MediaStore.Video.Media.MIME_TYPE,
    MediaStore.Video.Media.SIZE,
    MediaStore.Video.Media.DURATION,
    MediaStore.Video.Media.BUCKET_DISPLAY_NAME
)

internal object AssetLoader {

    fun insertImage(context: Context): Uri? {
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "compose-camera-${System.currentTimeMillis()}.jpg")
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        }
        return context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
    }

    fun deleteByUri(context: Context, uri: Uri) {
        context.contentResolver.delete(uri, null, null)
    }

    fun findByUri(context: Context, uri: Uri): AssetInfo? {
        val cursor = context.contentResolver.query(uri, projection, null, null, null, null)
        cursor?.use { it ->
            val indexId = it.getColumnIndex(projection[0])
            val indexFilename = it.getColumnIndex(projection[1])
            val indexDate = it.getColumnIndex(projection[2])
            val indexMediaType = it.getColumnIndex(projection[3])
            val indexMimeType = it.getColumnIndex(projection[4])
            val indexSize = it.getColumnIndex(projection[5])
            val indexDuration = it.getColumnIndex(projection[6])
            val indexDirectory = it.getColumnIndex(projection[7])

            if (it.moveToNext()) {
                val id = it.getLong(indexId)
                val mediaType = it.getInt(indexMediaType)
                val contentUri = if (mediaType == MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE) {
                    ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
                } else {
                    ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, id)
                }

                return AssetInfo(
                    id = id,
                    uriString = contentUri.toString(),
                    filename = it.getString(indexFilename),
                    date = it.getLong(indexDate),
                    mediaType = mediaType,
                    mimeType = it.getString(indexMimeType),
                    size = it.getLong(indexSize),
                    duration = it.getLong(indexDuration),
                    directory = it.getString(indexDirectory),
                )
            }
        }
        return null
    }

    fun load(context: Context, requestType: RequestType): List<AssetInfo> {
        val assets = ArrayList<AssetInfo>()
        val cursor = createCursor(context, requestType)
        cursor?.use { it ->
            val indexId = it.getColumnIndex(projection[0])
            val indexFilename = it.getColumnIndex(projection[1])
            val indexDate = it.getColumnIndex(projection[2])
            val indexMediaType = it.getColumnIndex(projection[3])
            val indexMimeType = it.getColumnIndex(projection[4])
            val indexSize = it.getColumnIndex(projection[5])
            val indexDuration = it.getColumnIndex(projection[6])
            val indexDirectory = it.getColumnIndex(projection[7])

            while (it.moveToNext()) {
                val id = it.getLong(indexId)
                val mediaType = it.getInt(indexMediaType)
                val contentUri = if (mediaType == MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE) {
                    ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
                } else {
                    ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, id)
                }

                assets.add(
                    AssetInfo(
                        id = id,
                        uriString = contentUri.toString(),
                        filename = it.getString(indexFilename),
                        date = it.getLong(indexDate),
                        mediaType = mediaType,
                        mimeType = it.getString(indexMimeType),
                        size = it.getLong(indexSize),
                        duration = it.getLong(indexDuration),
                        directory = it.getString(indexDirectory),
                    )
                )
            }
        }
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

    private data class Selection(val selection: String, val arguments: List<String>)
}

