package com.example.project_2_cmp431.api

import android.util.Log

import com.example.project_2_cmp431.util.TAG
import com.example.project_2_cmp431.model.JobPost
import kotlinx.serialization.json.Json
import okhttp3.MediaType import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import retrofit2.http.GET import retrofit2.http.Query

/**  Retrofit interface for the NYC Open Data API.   This interface defines the API endpoints for the NYC Open Data API. /
 *
 */
interface NycOpenDataService {
    @GET("kpav-sd4t.json?posting_type=External&\$order=posting_updated%20DESC&")
    suspend fun getJobPostings(
        @Query("\$limit") limit: Int = 50,
        @Query("\$offset") offset: Int ): List<JobPost>
}

/**  App Remote APIs   This class defines the remote APIs for the app. /
 *
 */
class AppRemoteApis {
    private val baseUrl = "https://data.cityofnewyork.us/resource/"
    private val contentType = MediaType.get("application/json; charset=utf-8")
    private val json = Json { ignoreUnknownKeys = true
    }

    /**  Get the API calls.
     * @return the interface that defines the API endpoints
     */
    fun getNycOpenDataApi(): NycOpenDataService { Log.i(TAG, "retrofit service creating API calls")
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
            .create(NycOpenDataService::class.java)
    }
}



