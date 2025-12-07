package com.example.triviaapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface HighscoreDao {
    @Query("""
      SELECT * FROM highscores 
      WHERE categoryId = :categoryId AND difficulty = :difficulty AND questionType = :questionType
      LIMIT 1
    """)
    fun getHighscoreEntityFlow(
        categoryId: Int, difficulty: String, questionType: String
    ): Flow<HighscoreEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(highscore: HighscoreEntity)

    @Query("DELETE FROM highscores")
    suspend fun deleteAll()
}