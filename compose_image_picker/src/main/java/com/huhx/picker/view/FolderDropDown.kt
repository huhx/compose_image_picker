package com.huhx.picker.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.huhx.picker.R

data class ImageInfo(
    val directory: String,
    val counts: Int
)

val mockList = listOf(
    ImageInfo("所有项目", 8705),
    ImageInfo("Picture", 5291),
    ImageInfo("Camera", 987),
    ImageInfo("知乎", 982),
    ImageInfo("皮皮虾", 907),
    ImageInfo("QQ", 123),
    ImageInfo("Wechat", 90),
    ImageInfo("qq_image", 786),
    ImageInfo("QQ_image", 12),
    ImageInfo("weixin", 23),
    ImageInfo("weibo", 63),
    ImageInfo("test1", 63),
    ImageInfo("test2", 63),
    ImageInfo("test3", 63),
    ImageInfo("test4", 63),
    ImageInfo("test5", 63),
    ImageInfo("test6", 63),
    ImageInfo("test7", 63),
    ImageInfo("test8", 63),
    ImageInfo("test9", 63),
)

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FolderDropDown(
    folderName: String,
    onClick: (String) -> Unit,
) {
    val directory = remember { mutableStateOf(folderName) }

    LazyColumn {
        items(mockList) {
            ListItem(
                modifier = Modifier.clickable { onClick(it.directory) },
                icon = {
                    Image(
                        modifier = Modifier.size(32.dp),
                        painter = painterResource(id = R.drawable.app_icon_background),
                        contentDescription = ""
                    )
                },
                text = {
                    Row {
                        Text(text = it.directory)
                        Text(text = "(${it.counts})")
                    }
                },
                trailing = if (directory.value == it.directory) {
                    {
                        Icon(
                            imageVector = Icons.Default.Done,
                            contentDescription = "",
                            tint = Color.Blue
                        )
                    }
                } else {
                    null
                }
            )
        }
    }
}