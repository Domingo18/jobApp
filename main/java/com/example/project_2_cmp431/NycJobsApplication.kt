package com.example.project_2_cmp431

import android.app.Application
import android.util.Log
import com.example.project_2_cmp431.data.AppContainer
import com.example.project_2_cmp431.data.DefaultAppContainer
import com.example.project_2_cmp431.util.TAG

/**  NYC Open Jobs Application /
 *
 */
class NYCOpenJobsApplication : Application() {
    /**  App container   This property is used to get the app container. /
     *
     */
    lateinit var container: AppContainer

    /**  Application onCreate   Initializes the app container using the default app container. /
     *
     */
    override fun onCreate() {
        super.onCreate()
        Log.i(TAG, "Application: starting app")
        container = DefaultAppContainer(this)
    }
}


