package com.example.project_2_cmp431.ui.screens

import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.project_2_cmp431.model.JobPost
import com.example.project_2_cmp431.util.capitalizeWords


@Composable
fun DetailScreen(
    viewModel: JobPostingsViewModel,
    jobPost: JobPost,
    onBack: () -> Unit,
//    jobId: String?,
//    viewModel: JobPostingsViewModel
){


    Scaffold (
        topBar = {
            DetailTitleTopBar(viewModel, jobPost,onBack)
        }
    ){ innerPadding ->

        Column (modifier = Modifier
            .padding(innerPadding)
            .verticalScroll(rememberScrollState())
        ){
            Text("Agency: ${jobPost.agency}")
            Spacer(modifier = Modifier.height(8.dp))

            Text("Title: ${jobPost.businessTitle}")
            Spacer(modifier = Modifier.height(8.dp))

            Text("Category: ${jobPost.jobCategory}")
            Spacer(modifier = Modifier.height(8.dp))

            Text("Career Level: ${jobPost.careerLevel}")
            Spacer(modifier = Modifier.height(8.dp))

            Text("Salary: ${jobPost.salaryRangeFrom} - ${jobPost.salaryRangeTo} ${jobPost.salaryFrequency}")
            Spacer(modifier = Modifier.height(8.dp))

            Text("Location: ${jobPost.workLocation}")
            Spacer(modifier = Modifier.height(8.dp))

            Text("Description: ${jobPost.jobDescription}")
            Spacer(modifier = Modifier.height(8.dp))

            Text("Minimum Requirements: ${jobPost.minRequirement}")
            Spacer(modifier = Modifier.height(8.dp))

            Text("Preferred Skills: ${jobPost.preferredSkills}")
            Spacer(modifier = Modifier.height(8.dp))
        }

    }
}




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailTitleTopBar(
    viewModel: JobPostingsViewModel,
    jobPost: JobPost,
    onBack: () -> Unit
) {
    TopAppBar(
        title = {
            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "NYCJobs",
                    style = MaterialTheme.typography.titleLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        },
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back Nav"

                )
            }
        },
        actions = {
            IconButton(onClick = {
                if (jobPost.isFavorite) {
                    viewModel.removeFavorite(jobPost)
                } else {
                    viewModel.addFavorite(jobPost)
                }
            }) {
                Icon(
                    imageVector = if (jobPost.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = if (jobPost.isFavorite) "Unfavorite" else "Favorite",
                    tint = if (jobPost.isFavorite) Color.Red else Color.Gray
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = Color.White,
            navigationIconContentColor = Color.White,
            actionIconContentColor = Color.White
        )
    )
}