package com.example.quoteapp

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

data class Quote(val text: String, val author: String)

class MainActivity : AppCompatActivity() {

    // MutableList для возможности добавления/удаления
    private val quotes = mutableListOf(
        Quote("Мы должны быть самим собой и только тогда мы счастливы.", "Фёдор Достоевский"),
        Quote("Надо любить жизнь больше, чем смысл жизни.", "Фёдор Достоевский"),
        Quote("Великое счастье — работать над тем, что ты любишь.", "Конфуций")
    )

    private var currentIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Находим все View элементы
        val quoteTextView = findViewById<TextView>(R.id.quoteTextView)
        val authorTextView = findViewById<TextView>(R.id.authorTextView)
        val prevButton = findViewById<Button>(R.id.prevButton)
        val nextButton = findViewById<Button>(R.id.nextButton)
        val addQuoteButton = findViewById<Button>(R.id.addQuoteButton)
        val deleteQuoteButton = findViewById<Button>(R.id.deleteQuoteButton)

        // Показываем первую цитату
        showQuote(quoteTextView, authorTextView)

        // Кнопка предыдущей цитаты
        prevButton.setOnClickListener {
            if (quotes.isNotEmpty()) {
                currentIndex = if (currentIndex - 1 < 0) quotes.size - 1 else currentIndex - 1
                showQuote(quoteTextView, authorTextView)
            }
        }

        // Кнопка следующей цитаты
        nextButton.setOnClickListener {
            if (quotes.isNotEmpty()) {
                currentIndex = (currentIndex + 1) % quotes.size
                showQuote(quoteTextView, authorTextView)
            }
        }

        // Кнопка добавления цитаты
        addQuoteButton.setOnClickListener {
            showAddQuoteDialog(quoteTextView, authorTextView)
        }

        // Кнопка удаления цитаты
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
                    currentIndex = quotes.size - 1 // Перейти к новой цитате
                    showQuote(quoteTextView, authorTextView)
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

                // Если удалили последнюю цитату
                if (quotes.isEmpty()) {
                    currentIndex = 0
                    showQuote(quoteTextView, authorTextView)
                    Toast.makeText(this, "Цитата удалена. Список пуст.", Toast.LENGTH_SHORT).show()
                } else {
                    // Если удалили не последнюю, перейти к предыдущей или первой
                    if (currentIndex >= quotes.size) {
                        currentIndex = quotes.size - 1
                    }
                    showQuote(quoteTextView, authorTextView)
                    Toast.makeText(this, "Цитата удалена", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Отмена", null)
            .show()
    }

    // Функция для добавления тестовых цитат (можно удалить позже)
    private fun addSampleQuotes() {
        quotes.addAll(listOf(
            Quote("Свобода ничего не стоит, если она не включает в себя свободу ошибаться.", "Махатма Ганди"),
            Quote("Любовь — это единственная сила, способная превратить врага в друга.", "Мартин Лютер Кинг"),
            Quote("Единственный способ сделать что-то очень хорошо — любить то, что ты делаешь.", "Стив Джобс")
        ))
    }
}