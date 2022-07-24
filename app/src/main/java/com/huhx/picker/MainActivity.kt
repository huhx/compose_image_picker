package com.huhx.picker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.huhx.picker.constant.AssetPickerConfig
import com.huhx.picker.constant.showShortToast
import com.huhx.picker.ui.theme.Compose_image_pickerTheme
import com.huhx.picker.view.QQAssetPicker

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val context = LocalContext.current

            Compose_image_pickerTheme {
                QQAssetPicker(assetPickerConfig = AssetPickerConfig(gridCount = 3)) {
                    context.showShortToast("picked size = ${it.size}")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    val context = LocalContext.current
    Compose_image_pickerTheme {
        QQAssetPicker(assetPickerConfig = AssetPickerConfig(gridCount = 3)) {
            context.showShortToast("picked size = ${it.size}")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Indicator(
    modifier: Modifier = Modifier,
    selected: Boolean,
    num: Int,
    onClick: () -> Unit,
    selectedColor: Color,
    unSelectedColor: Color,
    size: Dp,
    stroke: Dp,
) {
    Surface(
        onClick = onClick,
        modifier = modifier.size(size),
        shape = CircleShape,
        border = if (!selected) BorderStroke(stroke, Color.White) else null,
        color = if (selected) selectedColor else unSelectedColor
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(text = "$num", color = Color.White)
        }
    }
}

@Preview
@Composable
fun IndicatorPreview() {
    var selected by remember { mutableStateOf(false) }
    Indicator(
        selected = selected,
        num = 2,
        onClick = { selected = !selected },
        selectedColor = Color(64, 151, 246),
        unSelectedColor = Color(0f, 0f, 0f, 0.6F),
        size = 32.dp,
        stroke = 1.dp
    )
}