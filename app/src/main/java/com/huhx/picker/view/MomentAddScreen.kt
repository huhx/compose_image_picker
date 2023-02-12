package com.huhx.picker.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.huhx.picker.data.IdHelper
import com.huhx.picker.data.Moment
import com.huhx.picker.data.MomentViewModel
import com.huhx.picker.data.imageUrl
import com.huhx.picker.data.username
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MomentAddScreen(
    viewModel: MomentViewModel,
    navController: NavHostController,
    navigateUp: () -> Unit
) {

    Scaffold(
        topBar = {
            MomentAddAppBar(
                navigateUp = navigateUp,
                onSend = {
                    val moment = Moment(
                        id = IdHelper.nextID(),
                        username = username,
                        imageUrl = imageUrl,
                        content = viewModel.content,
                        images = viewModel.selectedList.joinToString(",") { it.uriString },
                        createTime = SimpleDateFormat(
                            "yyyy-MM-dd HH:mm:ss",
                            Locale.getDefault()
                        ).format(System.currentTimeMillis())
                    )
                    viewModel.addMoment(moment)
                    viewModel.content = ""
                    viewModel.selectedList.clear()
                    navigateUp()
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color.Gray.copy(alpha = 0.1F))
        ) {
            MomentAddContent(viewModel, navController)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MomentAddAppBar(
    navigateUp: () -> Unit,
    onSend: () -> Unit,
) {
    CenterAlignedTopAppBar(
        title = { Text(text = "Moment Add") },
        navigationIcon = {
            IconButton(onClick = navigateUp) {
                Icon(Icons.Filled.ArrowBack, contentDescription = "")
            }

        },
        actions = {
            Button(
                onClick = onSend,
                modifier = Modifier.defaultMinSize(minHeight = 1.dp, minWidth = 1.dp),
                shape = RoundedCornerShape(16.dp),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 6.dp),
            ) {
                Text(text = "Send")
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MomentAddContent(viewModel: MomentViewModel, navController: NavHostController) {

    Surface(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
        color = Color.Transparent
    ) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                CircleNetworkImage(
                    imageName = imageUrl,
                    size = 40.dp
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(text = username)
            }

            Spacer(modifier = Modifier.height(12.dp))

            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                value = viewModel.content,
                onValueChange = { viewModel.content = it },
                placeholder = { Text("Say something....") },
                maxLines = 6,
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.White,
                    textColor = Color.Black,
                    focusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                )
            )
            ImageAdd(modifier = Modifier, viewModel = viewModel, navController = navController)
        }
    }
}

@Composable
fun ImageAdd(
    modifier: Modifier,
    viewModel: MomentViewModel,
    navController: NavHostController
) {
    LazyVerticalGrid(
        modifier = Modifier.heightIn(0.dp, 600.dp),
        columns = GridCells.Fixed(3),
        contentPadding = PaddingValues(vertical = 10.dp),
        verticalArrangement = Arrangement.spacedBy(3.dp),
        horizontalArrangement = Arrangement.spacedBy(3.dp),
        userScrollEnabled = false
    ) {
        item {
            CameraIcon(modifier = modifier, navController = navController)
        }

        items(viewModel.selectedList) {
            AsyncImage(
                model = it.uriString,
                modifier = Modifier
                    .fillMaxSize()
                    .aspectRatio(1.0F),
                filterQuality = FilterQuality.Low,
                contentScale = ContentScale.Crop,
                contentDescription = ""
            )
        }
    }
}


@Composable
internal fun CameraIcon(
    modifier: Modifier,
    navController: NavHostController
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .clickable { navController.navigate("asset_picker") }
            .then(Modifier.background(Color.Gray.copy(alpha = 0.1F)))
    ) {
        AsyncImage(
            model = "https://media.istockphoto.com/vectors/image-place-holder-with-a-gray-camera-icon-vector-id1226328537?k=20&m=1226328537&s=612x612&w=0&h=2klft8QdMSyDj3oAmFyRyD24Mogj2OygLWrX9Lk6oGQ=",
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .aspectRatio(1f)
                .alpha(0.2F)
        )
    }
}