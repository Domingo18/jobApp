package com.example.project_2_cmp431.ui.screens

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.project_2_cmp431.NYCOpenJobsApplication
import com.example.project_2_cmp431.data.AppRepository
import com.example.project_2_cmp431.model.JobPost
import com.example.project_2_cmp431.util.TAG
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

/**  UI state   This sealed interface defines the UI state used by the app.
 *  @property JobPostingsUIState.Success the data is successfully loaded
 *  @property JobPostingsUIState.Error the data failed to load
 *  @property JobPostingsUIState.Loading the data is loading
 *  @property JobPostingsUIState.Ready the data is ready to be loaded /
 *
 */
sealed interface JobPostingsUIState {
//     Success state @property data the data that was successfully loaded
    data class Success(val data: List<JobPost>) : JobPostingsUIState
    data object Error : JobPostingsUIState
    data object Loading : JobPostingsUIState
    data object Ready : JobPostingsUIState
    }

/**  view model   This view model is responsible for providing data to the UI.
 * @param repository the repository to get data from
 */
class JobPostingsViewModel(private val repository: AppRepository) : ViewModel() {

    var jobPostingsUIState: JobPostingsUIState by mutableStateOf(JobPostingsUIState.Ready)
        private set
    private val _favorites = MutableStateFlow<List<JobPost>>(emptyList())
    val favorites: StateFlow<List<JobPost>> = _favorites

    init {
            getJobPostings()
            observeFavorites()
    }
    /**  get job postings
     * This function will get job postings from the repository.
     */

    fun getJobPostings() {
        viewModelScope.launch {
            jobPostingsUIState = JobPostingsUIState.Loading
            jobPostingsUIState = try {
                JobPostingsUIState.Success(repository.getJobPostings())
            } catch (e: IOException) {
                e.message?.let { Log.e(TAG, it) }
                JobPostingsUIState.Error
            } catch (e: HttpException) {
                e.message?.let { Log.e(TAG, it) }
                JobPostingsUIState.Error
            }

        }
    }
    fun getJobById(id: Int): JobPost? {
        return (jobPostingsUIState as? JobPostingsUIState.Success)?.data?.find { it.jobId == id }
    }

    /**  get scroll position   This function will get the scroll position from the repository. /
     *
     */
    fun getScrollPosition(): Int {
        return repository.getScrollPosition() }

    /**  set scroll position   This function will set the scroll position in the repository.
     * @param position the position to set */

    fun setScrollingPosition(position: Int) {
        return repository.setScrollPosition(position) }

    private fun observeFavorites() {
        viewModelScope.launch {
            repository.getFavoriteJobs().collect { list ->
                // Update the StateFlow whenever the repository emits a new list
                _favorites.value = list
            }
        }
    }

    fun addFavorite(jobPost: JobPost) {
        viewModelScope.launch {
            repository.addToFavorites(jobPost)
            refreshJobPostingsFavoriteState(jobPost.jobId, true)
        }
    }

    fun removeFavorite(jobPost: JobPost) {
        viewModelScope.launch {
            repository.removeFromFavorites(jobPost)
            refreshJobPostingsFavoriteState(jobPost.jobId, false)
        }
    }

    private fun refreshJobPostingsFavoriteState(jobId: Int, isFav: Boolean) {
        val currentState = jobPostingsUIState
        if (currentState is JobPostingsUIState.Success) {
            val updatedList = currentState.data.map {
                if (it.jobId == jobId) it.copy(isFavorite = isFav) else it
            }
            jobPostingsUIState = JobPostingsUIState.Success(updatedList)
        }
    }

    var searchQuery by mutableStateOf("")
        private set

    val filteredJobs: List<JobPost>
        get() = when (val state = jobPostingsUIState) {
            is JobPostingsUIState.Success -> {
                if (searchQuery.isBlank()) state.data
                else state.data.filter { job ->
                    job.businessTitle.contains(searchQuery, ignoreCase = true) ||
                            job.jobDescription.contains(searchQuery, ignoreCase = true)
                }
            }
            else -> emptyList()
        }

    fun updateSearchQuery(query: String) {
        searchQuery = query
    }


    /**  This singleton object is used to create a view model factory.
     *  It implements the ViewModelProvider.
     *  Factory interface by providing a create method  /
     */

    companion object { val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory
        {
        /**  This method will create a view model.
        @param model Class the class of the view model
        @param extras the extras to pass to the view model
         */
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create( modelClass: Class<T>, extras: CreationExtras ): T {
            Log.i(TAG, "view model factory: getting application repository")
            val application = checkNotNull(extras[APPLICATION_KEY]) as NYCOpenJobsApplication
            val nycJobsRepository = application.container.appRepository
            return JobPostingsViewModel(repository = nycJobsRepository) as T
            }

        }
    }
}







