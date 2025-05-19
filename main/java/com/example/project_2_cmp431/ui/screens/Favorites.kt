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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.project_2_cmp431.model.JobPost
import com.example.project_2_cmp431.util.capitalizeWords


@Composable
fun FavoriteScreen(
    viewModel: JobPostingsViewModel,
    onHomeClick: () -> Unit,
    onFavClick: () -> Unit,
    onCardClick: (JobPost) -> Unit,

){
    Scaffold (
        topBar = {
            FavoriteTitleTopBar()
        },
        bottomBar = {
            FavoriteBottomAppBar(
                onHomeClick,
                onFavClick)
        }
    ){ innerPadding ->

        val favorites by viewModel.favorites.collectAsState(emptyList())

        Column (Modifier.padding(innerPadding)){
            DisplayFavorites(favorites, onCardClick)
        }
    }
}


@Composable
fun DisplayFavorites(
    favPosting: List<JobPost>,
    onCardClick: (JobPost) -> Unit
){

    LazyColumn(modifier = Modifier.padding(16.dp)) {
        items(favPosting) { jobPost: JobPost ->


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
                    textAlign = TextAlign.Center,
                )
                Text(
                    text = formattedTitle,
                    modifier = Modifier,
                    textAlign = TextAlign.Center,
                )
                Text(
                    text = careerLevel,
                    modifier = Modifier,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}








@Composable
 fun FavoriteTitleTopBar() {
    TopAppBar(
        title = {
            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Favorites",
                    style = MaterialTheme.typography.titleLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        },
        actions = {
            IconButton(onClick = { /* Handle search click */ }) {
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

@Composable
fun FavoriteBottomAppBar(
    onHomeClick: () -> Unit,
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
            IconButton(onClick = onHomeClick ) {
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
