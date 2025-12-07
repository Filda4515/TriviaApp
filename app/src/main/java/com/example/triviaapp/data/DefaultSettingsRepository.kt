package com.example.triviaapp.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.triviaapp.domain.Category
import com.example.triviaapp.domain.Difficulty
import com.example.triviaapp.domain.QuestionType
import com.example.triviaapp.domain.Settings
import com.example.triviaapp.domain.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class DefaultSettingsRepository(private val context: Context) : SettingsRepository {
    private val json = Json { ignoreUnknownKeys = true }

    private companion object {
        private val DIFFICULTY_KEY = stringPreferencesKey("difficulty")
        private val QUESTION_TYPE_KEY = stringPreferencesKey("question_type")
        private val CATEGORY_KEY = stringPreferencesKey("category")
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
                ),
                category = try {
                    preferences[CATEGORY_KEY]?.let { jsonString ->
                        json.decodeFromString<Category>(jsonString)
                    } ?: Category.DEFAULT
                } catch (e: Exception) {
                    Category.DEFAULT
                }
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

    override suspend fun setCategory(category: Category) {
        context.dataStore.edit { preferences ->
            val jsonString = json.encodeToString(Category.serializer(), category)
            preferences[CATEGORY_KEY] = jsonString
        }
    }
}