package ru.ermolnik.news

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterStart
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun NewsScreen(viewModel: NewsViewModel) {
    val state = viewModel.state.collectAsState()
    Box(modifier = Modifier.padding(20.dp)) {
        when(state.value) {
            is NewsState.Loading -> {
                Loading()
            }
            is NewsState.Error -> {
                Error(state, viewModel)
            }
            is NewsState.Content -> {
                Content(state, viewModel)
            }
        }
    }
}

@Composable
private fun Content(state: State<NewsState>, viewModel: NewsViewModel) {
    Column(modifier = Modifier.fillMaxSize()) {
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            (state.value as? NewsState.Content)?.newsList?.forEach {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 2.dp)
                            .border(1.dp, color = Color.Gray)
                            .padding(vertical = 2.dp, horizontal = 8.dp)
                    )
                    {
                        Text(
                            modifier = Modifier
                                .wrapContentSize()
                                .align(CenterStart),
                            text = it,
                            textAlign = TextAlign.Start
                        )
                    }
                }
            }
        }
        UpdateButton(viewModel)
    }
}

@Composable
private fun Error(state: State<NewsState>, viewModel: NewsViewModel) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.align(Alignment.Center)) {
            Text(
                modifier = Modifier
                    .align(CenterHorizontally)
                    .padding(vertical = 16.dp),
                text = "Error:",
                fontSize = 32.sp,
                color = Color.Red
            )
            Text(
                modifier = Modifier
                    .align(CenterHorizontally)
                    .wrapContentSize(),
                text = (state.value as NewsState.Error).throwable.localizedMessage
                    ?: "<Nothing to say>",
                textAlign = TextAlign.Center
            )
            UpdateButton(viewModel = viewModel)
        }
    }
}

@Preview
@Composable
private fun Loading() {
    Box(modifier = Modifier.fillMaxSize()) {
        CircularProgressIndicator(
            modifier = Modifier
                .size(50.dp)
                .align(Alignment.Center)
        )
    }
}

@Composable
private fun ColumnScope.UpdateButton(viewModel: NewsViewModel) {
    Spacer(modifier = Modifier.height(20.dp))
    Button(modifier = Modifier.Companion.align(CenterHorizontally),
        onClick = { viewModel.refreshState(true) }) {
        Text("Обновить")
    }
}
