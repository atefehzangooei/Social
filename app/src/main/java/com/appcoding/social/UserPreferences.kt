package com.appcoding.social

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

val Context.dataStore : DataStore<Preferences> by preferencesDataStore(name = "user")

class UserPreferences @Inject constructor(
    @ApplicationContext private val context: Context) {

    private val USER_ID = longPreferencesKey("userid")

    suspend fun saveUserId(userId: Long) {
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
