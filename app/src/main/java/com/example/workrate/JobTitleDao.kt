package com.example.workrate.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface JobTitleDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(jobTitles: List<JobTitle>)

    @Query("SELECT * FROM job_titles WHERE title LIKE '%' || :query || '%' ORDER BY title LIMIT :limit OFFSET :offset")
    suspend fun searchJobTitles(query: String, limit: Int, offset: Int): List<JobTitle>

    @Query("SELECT COUNT(*) FROM job_titles WHERE title LIKE '%' || :query || '%'")
    suspend fun countJobTitles(query: String): Int
}
