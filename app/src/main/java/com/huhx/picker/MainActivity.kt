package com.huhx.picker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
                QQAssetPicker(
                    assetPickerConfig = AssetPickerConfig(gridCount = 3),
                    onPicked = {
                        context.showShortToast("picked size = ${it.size}")
                    },
                    onClose = {
                        context.showShortToast("close size = ${it.size}")
                    }
                )
            }
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

@Preview
@Composable
fun GifTextPreview() {
    Box {
        Box(
            modifier = Modifier
                .padding(bottom = 4.dp, end = 6.dp)
                .background(
                    color = Color(0F, 0F, 0F, 0.4F),
                    shape = RoundedCornerShape(6.dp)
                )
        ) {
            Text(
                modifier = Modifier.padding(horizontal = 5.dp, vertical = 1.dp),
                text = "GIF",
                color = Color.White,
                fontSize = 8.sp
            )
        }
    }
}

@Preview
@Composable
fun VideoPreview() {
    Box(
        modifier = Modifier.background(
            brush = Brush.verticalGradient(
                colors = listOf(Color(0F, 0F, 0F, 0.4F), Color(0F, 0F, 0F, 0.6F))
            )
        )
    ) {
        Text(
            modifier = Modifier.padding(bottom = 10.dp, end = 8.dp),
            text = "00:34",
            color = Color.White,
            fontSize = 14.sp
        )
    }
}

@Preview
@Composable
fun ImageItemPreview() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Black),
        contentAlignment = Alignment.BottomStart
    ) {
        Image(
            painter = painterResource(id = R.drawable.app_icon_foreground),
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Fit,
            contentDescription = ""
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.Red)
                .padding(horizontal = 10.dp, vertical = 16.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(selected = false, onClick = { /*TODO*/ })
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "选择", color = Color.White)
                }
                Button(
                    modifier = Modifier.defaultMinSize(minHeight = 1.dp, minWidth = 1.dp),
                    enabled = true,
                    shape = RoundedCornerShape(5.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    onClick = { }
                ) {
                    Text("确定", color = Color.White)
                }
            }
        }
    }
}