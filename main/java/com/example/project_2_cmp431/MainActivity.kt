package com.example.project_2_cmp431

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.project_2_cmp431.model.JobPost
import com.example.project_2_cmp431.ui.screens.DetailScreen
import com.example.project_2_cmp431.ui.screens.FavoriteScreen
import com.example.project_2_cmp431.ui.screens.HomeScreen
import com.example.project_2_cmp431.ui.screens.JobPostingsViewModel
import com.example.project_2_cmp431.ui.theme.Project_2_CMP431Theme
import com.example.project_2_cmp431.util.TAG

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i( TAG , "MainActivity onCreate")
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Project_2_CMP431Theme {
                Surface (
                    modifier = Modifier.fillMaxSize()
                ) {
                    AppNavigation()
                }



            }
        }
    }

    @Composable
    fun AppNavigation() {
        val navController = rememberNavController()
        val viewModel: JobPostingsViewModel by viewModels { JobPostingsViewModel.Factory }
        NavHost(navController = navController, startDestination = "home") {

            composable("home") {
                HomeScreen(
                    viewModel = viewModel,
                    onCardClick = { jobPost ->
                        navController.navigate("detail/${jobPost.jobId}")
                    },
                    onFavClick = { navController.navigate("fav") }
                )
            }

            composable(
                route = "detail/{jobId}",
                arguments = listOf(navArgument("jobId") { type = NavType.IntType })
            ) { backStackEntry ->
                val jobId = backStackEntry.arguments?.getInt("jobId") ?: return@composable

                var jobPost = viewModel.getJobById(jobId)
                if (jobPost != null) {
                    DetailScreen(
                        viewModel,
                        jobPost,
                        onBack = { navController.popBackStack() }
                    )
                } else {
                    Text(
                        text = "Job not found",
                        modifier = Modifier
                            .padding(16.dp)
                    )
                }
            }


            composable("fav") {
                FavoriteScreen(
                    viewModel = viewModel,
                    onHomeClick = { navController.navigate("home") },
                    onFavClick = { navController.navigate("fav") },
                    onCardClick = { jobPost: JobPost ->
                        navController.navigate("detail/${jobPost.jobId}")
                    }
                )
            }
        }
    }


}


