package com.example.planfollower.api

import android.content.Context
import android.content.SharedPreferences

object TokenManager {
        private const val PREFS_NAME = "PlanFollowerPrefs"
        private const val KEY_TOKEN = "jwt_token"

        private fun getPrefs(context: Context): SharedPreferences {
            return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        }

        // save the Token
        fun saveToken(context: Context, token: String) {
            val editor = getPrefs(context).edit()
            editor.putString(KEY_TOKEN, token)
            editor.apply()
        }

        // get the Token
        fun getToken(context: Context): String? {
            return getPrefs(context).getString(KEY_TOKEN, null)
        }

        // Delete token when exit
        fun clearToken(context: Context) {
            val editor = getPrefs(context).edit()
            editor.remove(KEY_TOKEN)
            editor.apply()
        }

}