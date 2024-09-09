package com.example.newsapp

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
@Composable
fun NewsScreen(viewModel: NewsViewModel = viewModel()) {
    val newsList by viewModel.newsList.collectAsState(emptyList())

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                newsList.getOrNull(0)?.let { news ->
                    NewsItemView(
                        news = news,
                        onLike = { id -> viewModel.likeNews(id) },
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                    )
                }
                newsList.getOrNull(1)?.let { news ->
                    NewsItemView(
                        news = news,
                        onLike = { id -> viewModel.likeNews(id) },
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                    )
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                newsList.getOrNull(2)?.let { news ->
                    NewsItemView(
                        news = news,
                        onLike = { id -> viewModel.likeNews(id) },
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                    )
                }
                newsList.getOrNull(3)?.let { news ->
                    NewsItemView(
                        news = news,
                        onLike = { id -> viewModel.likeNews(id) },
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                    )
                }
            }
        }
    }
}
