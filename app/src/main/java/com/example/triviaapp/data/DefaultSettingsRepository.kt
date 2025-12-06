package com.example.triviaapp.data

import android.content.Context
import android.media.audiofx.Equalizer
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.triviaapp.domain.Difficulty
import com.example.triviaapp.domain.QuestionType
import com.example.triviaapp.domain.Settings
import com.example.triviaapp.domain.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class DefaultSettingsRepository(private val context: Context) : SettingsRepository {
    private companion object {
        private val DIFFICULTY_KEY = stringPreferencesKey("difficulty")
        private val QUESTION_TYPE_KEY = stringPreferencesKey("question_type")
    }

    private inline fun <reified T : Enum<T>> getEnumSetting(
        value: String?,
        defaultValue: T
    ): T {
        return try {
            value?.let { enumValueOf<T>(it) } ?: defaultValue
        } catch (e: IllegalArgumentException) {
            defaultValue
        }
    }

    override fun getSettings(): Flow<Settings> =
        context.dataStore.data.map { preferences ->
            Settings(
                difficulty = getEnumSetting(
                    preferences[DIFFICULTY_KEY], Difficulty.ANY
                ),
                questionType = getEnumSetting(
                    preferences[QUESTION_TYPE_KEY], QuestionType.ANY
                )
            )
        }

    override suspend fun setDifficulty(difficulty: Difficulty) {
        context.dataStore.edit { preferences ->
            preferences[DIFFICULTY_KEY] = difficulty.name
        }
    }

    override suspend fun setType(type: QuestionType) {
        context.dataStore.edit { preferences ->
            preferences[QUESTION_TYPE_KEY] = type.name
        }
    }
}