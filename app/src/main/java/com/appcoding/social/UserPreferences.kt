package com.appcoding.social

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserPreferences @Inject constructor(
    @ApplicationContext private val context: Context) {

    private val Context.dataStore by preferencesDataStore(name = "user")

    private val USER_ID = longPreferencesKey("userid")

    suspend fun saveUserId(context: Context, userId: Long) {
        context.dataStore.edit { prefs ->
            prefs[USER_ID] = userId
        }
    }

    fun getUserIdFlow(): Flow<Long?> {
        return context.dataStore.data.map { prefs ->
            prefs[USER_ID] ?: 0L
        }
    }



}
