package com.example.triviaapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [HighscoreEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase(){
    abstract fun highscoreDao(): HighscoreDao
}