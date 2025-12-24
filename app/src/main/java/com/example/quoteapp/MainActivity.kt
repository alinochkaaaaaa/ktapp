package com.example.quoteapp

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var preferencesHelper: PreferencesHelper
    private val quotes = mutableListOf<Quote>()
    private var currentIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Инициализируем SharedPreferences
        preferencesHelper = PreferencesHelper(this)

        // Загружаем сохраненные цитаты
        quotes.addAll(preferencesHelper.loadQuotes())

        // Находим View элементы
        val quoteTextView = findViewById<TextView>(R.id.quoteTextView)
        val authorTextView = findViewById<TextView>(R.id.authorTextView)
        val prevButton = findViewById<Button>(R.id.prevButton)
        val nextButton = findViewById<Button>(R.id.nextButton)
        val addQuoteButton = findViewById<Button>(R.id.addQuoteButton)
        val deleteQuoteButton = findViewById<Button>(R.id.deleteQuoteButton)

        // Показываем первую цитату
        showQuote(quoteTextView, authorTextView)

        // Обработчики кнопок
        prevButton.setOnClickListener {
            if (quotes.isNotEmpty()) {
                currentIndex = if (currentIndex - 1 < 0) quotes.size - 1 else currentIndex - 1
                showQuote(quoteTextView, authorTextView)
            }
        }

        nextButton.setOnClickListener {
            if (quotes.isNotEmpty()) {
                currentIndex = (currentIndex + 1) % quotes.size
                showQuote(quoteTextView, authorTextView)
            }
        }

        addQuoteButton.setOnClickListener {
            showAddQuoteDialog(quoteTextView, authorTextView)
        }

        deleteQuoteButton.setOnClickListener {
            deleteCurrentQuote(quoteTextView, authorTextView)
        }
    }

    private fun showQuote(quoteTextView: TextView, authorTextView: TextView) {
        if (quotes.isNotEmpty()) {
            val currentQuote = quotes[currentIndex]
            quoteTextView.text = "\"${currentQuote.text}\""
            authorTextView.text = "— ${currentQuote.author}"
        } else {
            quoteTextView.text = "Нет цитат"
            authorTextView.text = "Добавьте первую цитату!"
        }
    }

    private fun showAddQuoteDialog(quoteTextView: TextView, authorTextView: TextView) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_quote, null)
        val quoteEditText = dialogView.findViewById<EditText>(R.id.quoteEditText)
        val authorEditText = dialogView.findViewById<EditText>(R.id.authorEditText)

        val dialog = AlertDialog.Builder(this)
            .setTitle("Добавить цитату")
            .setView(dialogView)
            .setPositiveButton("Добавить") { _, _ ->
                val quoteText = quoteEditText.text.toString().trim()
                val authorText = authorEditText.text.toString().trim()

                if (quoteText.isNotEmpty() && authorText.isNotEmpty()) {
                    quotes.add(Quote(quoteText, authorText))
                    currentIndex = quotes.size - 1
                    showQuote(quoteTextView, authorTextView)

                    // СОХРАНЯЕМ в SharedPreferences
                    preferencesHelper.saveQuotes(quotes)

                    Toast.makeText(this, "Цитата добавлена!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Отмена", null)
            .create()

        dialog.show()
    }

    private fun deleteCurrentQuote(quoteTextView: TextView, authorTextView: TextView) {
        if (quotes.isEmpty()) {
            Toast.makeText(this, "Нет цитат для удаления", Toast.LENGTH_SHORT).show()
            return
        }

        AlertDialog.Builder(this)
            .setTitle("Удалить цитату")
            .setMessage("Вы уверены, что хотите удалить текущую цитату?")
            .setPositiveButton("Удалить") { _, _ ->
                quotes.removeAt(currentIndex)

                if (quotes.isEmpty()) {
                    currentIndex = 0
                    showQuote(quoteTextView, authorTextView)
                    Toast.makeText(this, "Цитата удалена. Список пуст.", Toast.LENGTH_SHORT).show()
                } else {
                    if (currentIndex >= quotes.size) {
                        currentIndex = quotes.size - 1
                    }
                    showQuote(quoteTextView, authorTextView)
                    Toast.makeText(this, "Цитата удалена", Toast.LENGTH_SHORT).show()
                }

                // СОХРАНЯЕМ изменения в SharedPreferences
                preferencesHelper.saveQuotes(quotes)
            }
            .setNegativeButton("Отмена", null)
            .show()
    }
}