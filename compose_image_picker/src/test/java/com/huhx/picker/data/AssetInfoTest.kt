package com.huhx.picker.data

import android.provider.MediaStore.Files.FileColumns
import com.huhx.picker.model.AssetInfo
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class AssetInfoTest {
    private val assetInfo = AssetInfo(
        id = 8L,
        filepath = "/data/emulator/test.jpeg",
        uriString = "http://lcoalhost/5",
        filename = "test.jpeg",
        directory = "Picture",
        mediaType = 1,
        size = 1150260,
        mimeType = "img/jpeg",
        duration = 16000,
        date = 23423434433
    )

    @Test
    fun `should return true when call isVideo given media type 3`() {
        val info = assetInfo.copy(mediaType = FileColumns.MEDIA_TYPE_VIDEO)

        val result = info.isVideo()

        assertTrue(result)
    }

    @Test
    fun `should return true when call isImage given media type 1`() {
        val info = assetInfo.copy(mediaType = FileColumns.MEDIA_TYPE_IMAGE)

        val result = info.isImage()

        assertTrue(result)
    }

    @Test
    fun `should return empty string when format duration given null value`() {
        val info = assetInfo.copy(duration = null)

        val result = info.formatDuration()

        assertEquals("", result)
    }

    @Test
    fun `should return minute-second string when format duration given not null value`() {
        val info = assetInfo.copy(duration = 169098)

        val result = info.formatDuration()

        assertEquals("02:49", result)
    }

    @Test
    fun `should return same random string when multiple invoked`() {
        val info = assetInfo.copy()
        val info2 = assetInfo.copy()

        assertEquals(info.randomName, info.randomName)
        assertNotEquals(info.randomName, info2.randomName)
    }

    @Test
    fun `should return same random string`() {
        val info = assetInfo.copy()
        val info2 = assetInfo.copy(filename = "aa.maimai.aa.jpg")

        println(info.randomName)
        println(info2.randomName)
    }
}