package com.example.project_2_cmp431.data

import android.content.SharedPreferences
import android.util.Log
import com.example.project_2_cmp431.api.NycOpenDataService
import com.example.project_2_cmp431.util.TAG
import com.example.project_2_cmp431.model.JobPost
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

/**  app repository interface   The app repository is responsible for providing data to the view model. /
 *
 */
interface AppRepository {
    fun getScrollPosition(): Int
    fun setScrollPosition(position: Int)
    suspend fun getJobPostings(): List<JobPost>
    suspend fun getJobPost(jobId: Int): JobPost

    fun getFavoriteJobs(): Flow<List<JobPost>>
    suspend fun addToFavorites(jobPost: JobPost)
    suspend fun removeFromFavorites(jobPost: JobPost)
}


/**  app repository implementation   The app repository implementation is responsible for providing data to the view model.
 * This class is responsible for providing data to the view model.
 * It implements the  AppRepository interface.
 * @param nycOpenDataAPI the API to get data from NYC Open Data
 * @param sharedPreferences the shared preferences to store preference data
 * @param dao the data access object to get data from the local database /
 *
 */
class AppRepositoryImpl(
    private val nycOpenDataAPI: NycOpenDataService,
    private val sharedPreferences: SharedPreferences,
    private val jobDao: JobPostDao,
    private val favDao: FavoritesDao,
    ) : AppRepository {

    private val scrollPositionKey = "scroll_position"
    private val offsetKey = "offset"
    private var offset = sharedPreferences.getInt(offsetKey, 0)

    private var totalJobs = 0

    /**  offset value is used to paginate the data from the API. /
     *
     */
    private fun updateOffset() {
        offset += (totalJobs - offset)
        Log.i(TAG, "offset: $offset")
        sharedPreferences.edit().putInt(offsetKey, offset).apply()
    }

    /**  update total jobs.   total jobs is used to determine if we need to go get more data from the API.
     *  @param newTotalJobs the new total jobs /
     *
     */
    private fun updateTotalJobs(newTotalJobs: Int) {
        totalJobs = newTotalJobs
        Log.i(TAG, "total jobs: $totalJobs")
    }

    /**  get job postings from the NYC Open Data API.
     *  This function will get job postings from the NYC Open Data API.
     *  It will then  add the job postings to the local database.
     *  It will then return the job postings.
     *  offset and totalJobs are use to determine if we need to go get more data by calling  the remote API.
     *  Note: The first time this function is called, offset is set to 0.
     *  If totalJobs is 0 than,  we'll call the remote API to get the job postings.
     *  If totalJobs is not 0, we'll return  the job postings from the local database.
     *  @return list of job postings /
     *
     */
    override suspend fun getJobPostings(): List<JobPost> {
        Log.i(TAG, "getting job postings")
        updateOffset()
        val localData = jobDao.getAll().first()
        updateTotalJobs(localData.size)
        if (offset == totalJobs)
    {
        Log.i(TAG, "getting job postings via API")
        val jobs = nycOpenDataAPI.getJobPostings(limit = 100, offset = offset)
        Log.i(TAG, "The API return ${jobs.size} jobs. Updating local database")
        jobDao.upsert(jobs)
        val updatedData = jobDao.getAll().first()
        updateTotalJobs(updatedData.size)
        return updatedData
    }
    return localData
}

    /**  get job post from local database.
     * @param jobId the id of the job post to get
     * @return the job post /
     *
     */
    override suspend fun getJobPost(jobId: Int): JobPost {
        Log.i(TAG, "getting job post id $jobId")
        return jobDao.get(jobId).first()
    }
    /**  get scroll position from shared preferences.
     * The scroll position is used to scroll to the correct position when the app user
     * closes and reopens the app.  @return the scroll position */
    override fun getScrollPosition(): Int {
        val position = sharedPreferences.getInt(scrollPositionKey, 0)
        Log.i(TAG, "getting scroll position $position")
        return position
    }
    /**  set scroll position in shared preferences.
     *  The scroll position is used to scroll to the correct position when the app user
     *  closes and reopens the app.  @param position the position to set */
    override fun setScrollPosition(position: Int) {
        Log.i(TAG, "setting scroll position to $position")
        sharedPreferences.edit().putInt(scrollPositionKey, position).apply()
    }
    override fun getFavoriteJobs(): Flow<List<JobPost>> = favDao.getAllFavorites()

    override suspend fun addToFavorites(jobPost: JobPost) = favDao.addToFavorites(jobPost.jobId)

    override suspend fun removeFromFavorites(jobPost: JobPost) = favDao.removeFromFavorites(jobPost.jobId)

}