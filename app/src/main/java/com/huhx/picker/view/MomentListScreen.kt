package com.huhx.picker.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.huhx.picker.data.Moment
import com.huhx.picker.data.MomentViewModel

@Composable
fun MomentListScreen(
    viewModel: MomentViewModel,
    navigateToAdd: () -> Unit,
) {

    Scaffold(
        topBar = {
            AppBar(navigateToAdd = navigateToAdd)
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color.Gray.copy(alpha = 0.1F))
        ) {
            Content(viewModel)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AppBar(navigateToAdd: () -> Unit) {
    CenterAlignedTopAppBar(
        title = { Text(text = "Moment") },
        actions = {
            Button(
                onClick = navigateToAdd,
                modifier = Modifier.defaultMinSize(minHeight = 1.dp, minWidth = 1.dp),
                shape = RoundedCornerShape(16.dp),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 6.dp),
            ) {
                Text(text = "Add")
            }
        }
    )
}

@Composable
fun Content(viewModel: MomentViewModel) {
    Column(modifier = Modifier.padding(16.dp)) {
        LazyColumn(modifier = Modifier.fillMaxHeight()) {
            items(viewModel.moments, key = { it.id }) { moment ->
                MomentItem(moment)
            }
        }
    }
}

@Composable
fun MomentItem(moment: Moment) {
    Column(
        modifier = Modifier
            .padding(top = 16.dp)
            .wrapContentSize()
    ) {
        ItemHeader(moment)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = moment.content)
        Spacer(modifier = Modifier.height(8.dp))
        if (moment.images.isNotBlank()) {
            GridViewImages(moment.images.split(","))
            Spacer(modifier = Modifier.height(8.dp))
        }
        Divider(thickness = 16.dp, color = Color.White)
    }
}

@Composable
fun ItemHeader(moment: Moment) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row {
            CircleNetworkImage(imageName = moment.imageUrl, size = 40.dp)
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(text = moment.username)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = moment.createTime)
                    Spacer(modifier = Modifier.width(10.dp))
                }
            }
        }
    }
}