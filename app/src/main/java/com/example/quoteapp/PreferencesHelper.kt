package com.example.quoteapp

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class PreferencesHelper(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences("quotes_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    // Сохранить список цитат
    fun saveQuotes(quotes: List<Quote>) {
        val json = gson.toJson(quotes)
        prefs.edit().putString("quotes_list", json).apply()
    }

    // Загрузить список цитат
    fun loadQuotes(): List<Quote> {
        val json = prefs.getString("quotes_list", null)
        return if (json != null) {
            val type = object : TypeToken<List<Quote>>() {}.type
            gson.fromJson(json, type) ?: getDefaultQuotes()
        } else {
            getDefaultQuotes()
        }
    }

    // Стандартные цитаты при первом запуске
    private fun getDefaultQuotes(): List<Quote> {
        return listOf(
            Quote("Мы должны быть самим собой и только тогда мы счастливы.", "Фёдор Достоевский"),
            Quote("Надо любить жизнь больше, чем смысл жизни.", "Фёдор Достоевский"),
            Quote("Великое счастье — работать над тем, что ты любишь.", "Конфуций")
        )
    }
}