package com.example.quoteapp

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private val quotes = listOf(
        "Мы должны быть самим собой и только тогда мы счастливы." to "Фёдор Достоевский",
        "Надо любить жизнь больше, чем смысл жизни." to "Фёдор Достоевский",
        "Великое счастье — работать над тем, что ты любишь." to "Конфуций"
    )

    private var currentIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val quoteTextView = findViewById<TextView>(R.id.quoteTextView)
        val authorTextView = findViewById<TextView>(R.id.authorTextView)
        val nextButton = findViewById<Button>(R.id.nextButton)

        showQuote(quoteTextView, authorTextView)

        nextButton.setOnClickListener {
            currentIndex = (currentIndex + 1) % quotes.size
            showQuote(quoteTextView, authorTextView)
        }
    }

    private fun showQuote(quoteTextView: TextView, authorTextView: TextView) {
        val (quote, author) = quotes[currentIndex]
        quoteTextView.text = "\"$quote\""
        authorTextView.text = "— $author"
    }
}