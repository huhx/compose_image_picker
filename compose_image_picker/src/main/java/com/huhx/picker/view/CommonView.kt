package com.huhx.picker.view

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.huhx.picker.R
import com.huhx.picker.model.AssetInfo
import com.huhx.picker.model.AssetPickerConfig

internal val LocalAssetConfig = compositionLocalOf { AssetPickerConfig() }

@Composable
internal fun AppBarButton(size: Int, onPicked: () -> Unit) {
    val maxAssets = LocalAssetConfig.current.maxAssets
    val buttonText = stringResource(R.string.text_select_button, size, maxAssets)
    Button(
        modifier = Modifier.defaultMinSize(minHeight = 1.dp, minWidth = 1.dp),
        shape = RoundedCornerShape(5.dp),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 6.dp),
        onClick = onPicked,
    ) {
        Text(buttonText, fontSize = 14.sp, color = Color.White)
    }
}

@Composable
internal fun AssetImageIndicator(
    assetInfo: AssetInfo,
    selected: Boolean,
    size: Dp = 24.dp,
    fontSize: TextUnit = 16.sp,
    assetSelected: SnapshotStateList<AssetInfo>,
    onClicks: ((Boolean) -> Unit)? = null
) {
    val context = LocalContext.current
    val maxAssets = LocalAssetConfig.current.maxAssets
    val errorMessage = stringResource(R.string.message_selected_exceed, maxAssets)

    val (border, color) = if (selected) {
        Pair(null, Color(64, 151, 246))
    } else {
        Pair(BorderStroke(width = 1.dp, color = Color.White), Color.Black.copy(alpha = 0.3F))
    }
    Surface(
        onClick = {
            val isSelected = !selected
            if (onClicks != null) {
                onClicks(isSelected)
                return@Surface
            }
            if (assetSelected.size == maxAssets && isSelected) {
                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                return@Surface
            }
            if (isSelected) assetSelected.add(assetInfo) else assetSelected.remove(assetInfo)
        },
        modifier = Modifier
            .padding(6.dp)
            .size(size = size),
        shape = CircleShape,
        border = border,
        color = color
    ) {
        Box(contentAlignment = Alignment.Center) {
            if (selected) {
                Text(
                    text = "${assetSelected.indexOf(assetInfo) + 1}",
                    color = Color.White,
                    fontSize = fontSize,
                )
            }
        }
    }
}