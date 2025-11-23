package com.appcoding.social.screen.components

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

val Context.dataStore : DataStore<Preferences> by preferencesDataStore(name = "user")

class UserPreferences @Inject constructor(
    @ApplicationContext private val context: Context) {

    private val USER_ID = longPreferencesKey("userid")
    private val PROFILE_IMAGE = stringPreferencesKey("profileImage")


    suspend fun saveUser(userId: Long, profileImage : String) {
        context.dataStore.edit { prefs ->
            prefs[USER_ID] = userId
            prefs[PROFILE_IMAGE] = profileImage

        }
    }

    suspend fun updateProfileImage(newProfileImage : String){
        context.dataStore.edit { prefs ->
            prefs[PROFILE_IMAGE] = newProfileImage
        }
    }

    fun getUserIdFlow(): Flow<Long?> {
        return context.dataStore.data.map { prefs ->
            prefs[USER_ID] ?: 0L
        }
    }

  fun getUserProfileFlow(): Flow<String?> {
        return context.dataStore.data.map { prefs ->
            prefs[PROFILE_IMAGE] ?: ""
        }
    }



}
