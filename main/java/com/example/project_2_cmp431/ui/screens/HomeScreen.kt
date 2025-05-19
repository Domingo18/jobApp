@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.project_2_cmp431.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.project_2_cmp431.R
import com.example.project_2_cmp431.model.JobPost
import com.example.project_2_cmp431.util.capitalizeWords
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce


/**  Home Screen   This composable function is the home screen for the app.
 *   It's responsible for displaying the list of job postings.
 *   @param viewModel the view model for the app
 *   @param modifier the modifier for the composable
 */



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: JobPostingsViewModel,
    onCardClick: (JobPost) -> Unit,
    onFavClick: () -> Unit,
    modifier: Modifier = Modifier
) {


    Scaffold(
        topBar = {
            CenteredTitleTopBar(viewModel)
        },
        bottomBar = {
            CenteredBottomAppBar(onFavClick)
        }

    ) { innerPadding ->

            Column(
                modifier = Modifier
                    .padding(innerPadding),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                when (val uiState = viewModel.jobPostingsUIState) {
                    is JobPostingsUIState.Loading -> LoadingSpinner()

                    is JobPostingsUIState.Success  -> {
                        val jobsToDisplay = if (viewModel.searchQuery.isNotBlank()) {
                            viewModel.filteredJobs
                        } else {
                            uiState.data
                        }

                        JobPostList(
                            jobsToDisplay,
                            {
                                viewModel.getJobPostings() },
                            {
                                scrollPosition ->
                                viewModel.setScrollingPosition(scrollPosition)
                            },
                            viewModel.getScrollPosition(),
                            onCardClick = onCardClick,
                            modifier.fillMaxSize() )
                    }

                    is JobPostingsUIState.Error -> ToastMessage(stringResource(R.string.data_failed))
                    else -> ToastMessage(stringResource(R.string.loaded))
                }

            }
        }
    }

/**  Job Post List   This composable function is the list of job postings.
 *  @param jobPostings the list of job postings
 *  @param loadMoreData the function to load more data
 *  @param updateScrollPosition the function to update the scroll position
 *  @param scrollPosition the current scroll position
 *  @param modifier the modifier for the composable
 */

@OptIn(FlowPreview::class)
@Composable
fun JobPostList(
    jobPostings: List<JobPost>,
    loadMoreData: () -> Unit,
    updateScrollPosition: (Int) -> Unit,
    scrollPosition: Int,
    onCardClick: (jobPost: JobPost) -> Unit,
    modifier: Modifier
) {
    val firstVisibleIndex = if (scrollPosition > jobPostings.size) 0 else scrollPosition
    val listState: LazyListState = rememberLazyListState(firstVisibleIndex)

    Brush.verticalGradient(
        listOf(Color.Red, Color.Blue, Color.Green),
        0.0f,
        10000.0f,
        TileMode.Repeated
        )

    LazyColumn(modifier = modifier.padding(16.dp), listState) {
        items(jobPostings) { jobPost: JobPost ->


            val formattedTitle = jobPost.businessTitle.capitalizeWords()
            val agency = jobPost.agency.capitalizeWords()
            val careerLevel = jobPost.careerLevel.capitalizeWords()

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .clickable { onCardClick(jobPost) },
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(8.dp),
                border = BorderStroke(1.dp, Color.LightGray),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Text(
                    text = agency,
                    modifier = Modifier,
                    textAlign = TextAlign.Center,)
                Text(
                    text = formattedTitle,
                    modifier = Modifier,
                    textAlign = TextAlign.Center,)
                Text(
                    text = careerLevel,
                    modifier = Modifier,
                    textAlign = TextAlign.Center,)
            }

            LaunchedEffect(listState) {
                snapshotFlow {
                    listState
                        .layoutInfo
                        .visibleItemsInfo.lastOrNull()?.index
                }
                    .debounce(timeoutMillis = 500L)
                    .collect { lastVisibleItemIndex ->
                        updateScrollPosition(listState.firstVisibleItemIndex)
                        if (lastVisibleItemIndex != null && lastVisibleItemIndex >= jobPostings.size - 1) {
                            loadMoreData()
                        }
                    }
            }
        }
    }
}

@Composable
fun CenteredBottomAppBar(
    onFavClick: () -> Unit
) {
    BottomAppBar(
        containerColor = MaterialTheme.colorScheme.primary,
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        // Fill the width and center the icons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { /* Handle Home click */ }) {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = "Home",
                    tint = Color.White
                )
            }

            Spacer(modifier = Modifier.width(32.dp)) // Space between icons

            IconButton(onClick = onFavClick ) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "Favorites",
                    tint = Color.White
                )
            }
        }
    }
}

@Composable
fun CenteredTitleTopBar(
    viewModel: JobPostingsViewModel,
) {
    var isSearching by remember { mutableStateOf(false) }
    TopAppBar(
        title = {
            if (isSearching) {
                TextField(
                    value = viewModel.searchQuery,
                    onValueChange = viewModel::updateSearchQuery,
                    placeholder = { Text("Search jobs...") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            } else {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "NYCJobs",
                        style = MaterialTheme.typography.titleLarge,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        },
        actions = {
            IconButton(onClick = { isSearching = !isSearching }) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search"
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = Color.White,
            actionIconContentColor = Color.White
        )
    )
}









