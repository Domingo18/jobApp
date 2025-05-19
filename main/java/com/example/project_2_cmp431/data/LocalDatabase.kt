package com.example.project_2_cmp431.data

import android.content.Context
import android.util.Log
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Upsert
import com.example.project_2_cmp431.model.JobPost
import com.example.project_2_cmp431.util.TAG
import kotlinx.coroutines.flow.Flow

/**  Job Post Data Access Object (DAO)   This interface defines the data access object for the JobPost entity. /
 *
 */
@Dao interface JobPostDao {
    @Query("SELECT * FROM JobPost ORDER BY postingLastUpdated DESC")
    fun getAll(): Flow<List<JobPost>> @Query("SELECT * FROM JobPost WHERE jobId = :id")
    fun get(id: Int): Flow<JobPost> @Upsert(entity = JobPost::class)
    suspend fun upsert(jobPostings: List<JobPost>) }

@Dao
interface FavoritesDao {
    @Query("SELECT * FROM JobPost WHERE isFavorite = 1")
    fun getAllFavorites(): Flow<List<JobPost>>

    @Query("SELECT * FROM JobPost WHERE jobId = :id")
    suspend fun getFavoriteById(id: Int): JobPost?

    @Query("UPDATE JobPost SET isFavorite = 1 WHERE jobId = :id")
    suspend fun addToFavorites(id: Int)

    @Query("UPDATE JobPost SET isFavorite = 0 WHERE jobId = :id")
    suspend fun removeFromFavorites(id: Int)
}




/**  Local Database   This class defines the local database for the app. /
 *
 */
@Database(entities = [JobPost::class], version = 2, exportSchema = false)
abstract class LocalDatabase : RoomDatabase() {

    abstract fun jobPostDao(): JobPostDao
    abstract fun favoritesDao(): FavoritesDao

    companion object {
        private const val DATABASE = "local_database"

        @Volatile
        private var Instance: LocalDatabase? = null

        /**  Get the database.
         * @param context the context of the app
         * @return the database
         * */

        fun getDatabase(context: Context): LocalDatabase { Log.i(TAG, "getting database")
            Log.i(TAG, "getting database")
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, LocalDatabase::class.java, DATABASE)
                    .fallbackToDestructiveMigration().build().also { Instance = it }
            }
        }
    }
}

