package com.example.triviaapp.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.triviaapp.domain.Difficulty
import com.example.triviaapp.domain.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class DefaultSettingsRepository(private val context: Context): SettingsRepository {
    private val DIFFICULTY_KEY = stringPreferencesKey("difficulty")

    override fun getDifficulty(): Flow<Difficulty> =
        context.dataStore.data.map { preferences ->
            val difficultyName = preferences[DIFFICULTY_KEY] ?: Difficulty.ANY.name
            try {
                Difficulty.valueOf(difficultyName)
            } catch (e: IllegalArgumentException) {
                Difficulty.ANY
            }
        }

    override suspend fun setDifficulty(difficulty: Difficulty) {
        context.dataStore.edit { preferences ->
            preferences[DIFFICULTY_KEY] = difficulty.name
        }
    }
}