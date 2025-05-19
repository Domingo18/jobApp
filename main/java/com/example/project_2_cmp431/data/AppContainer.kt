package com.example.project_2_cmp431.data

import android.content.Context
import com.example.project_2_cmp431.util.TAG
import android.util.Log
import com.example.project_2_cmp431.api.AppRemoteApis

/**  App Container   This interface defines the app container.
 *
 */
interface AppContainer { val appRepository: AppRepository }

/**  Default App Container   This class defines the default app container.
 * The container is responsible for providing  the app repository.
 * @param context the context of the app /
 *
 */
class DefaultAppContainer(private val context: Context) : AppContainer {
    /*  App Repository   This property is the app repository.
    The app repository is responsible for providing  an interface to the data.
    @return the app repository
    */
    override val appRepository: AppRepository by lazy {
        Log.i(TAG, "initializing app repository")

        AppRepositoryImpl(
            AppRemoteApis().getNycOpenDataApi(),
            AppPreferences(context).getSharedPreferences(),
            LocalDatabase.getDatabase(context).jobPostDao(),
            LocalDatabase.getDatabase(context).favoritesDao()
        )
    }
}
