package com.example.levelgame

import android.content.Context

/**
 * Simple persistent storage for level-unlock progress.
 * unlockedLevel = the highest level the player is currently allowed to play.
 * Starts at 1 (only Level 1 open). Beating level N raises this to N+1 (max 3).
 */
object Prefs {
    private const val PREFS_NAME = "level_game_prefs"
    private const val KEY_UNLOCKED_LEVEL = "unlocked_level"
    const val MAX_LEVEL = 3

    fun getUnlockedLevel(context: Context): Int {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getInt(KEY_UNLOCKED_LEVEL, 1)
    }

    fun unlockNextLevel(context: Context, completedLevel: Int) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val current = getUnlockedLevel(context)
        val next = (completedLevel + 1).coerceAtMost(MAX_LEVEL)
        if (next > current) {
            prefs.edit().putInt(KEY_UNLOCKED_LEVEL, next).apply()
        }
    }

    fun resetProgress(context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putInt(KEY_UNLOCKED_LEVEL, 1).apply()
    }
}
