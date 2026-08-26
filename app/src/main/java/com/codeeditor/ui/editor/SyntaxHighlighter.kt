package com.codeeditor.ui.editor

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import java.util.regex.Pattern

object SyntaxHighlighter {
    private val KEYWORD_COLOR = Color(0xFF569CD6) // VS Code Blue
    private val STRING_COLOR = Color(0xFFCE9178)  // VS Code Orange/Brown
    private val COMMENT_COLOR = Color(0xFF6A9955) // VS Code Green
    private val NUMBER_COLOR = Color(0xFFB5CEA8)  // Light Green
    private val TYPE_COLOR = Color(0xFF4EC9B0)    // Cyan
    private val DEFAULT_TEXT_COLOR = Color(0xFFD4D4D4)

    private val KEYWORDS = setOf(
        "abstract", "actual", "annotation", "as", "break", "by", "catch", "class",
        "companion", "const", "constructor", "continue", "crossinline", "data",
        "delegate", "do", "else", "enum", "expect", "false", "field", "file",
        "final", "finally", "for", "fun", "get", "if", "import", "in", "infix",
        "init", "inline", "inner", "interface", "internal", "is", "it", "lateinit",
        "noinline", "null", "object", "open", "operator", "out", "override",
        "package", "private", "protected", "public", "reified", "return", "sealed",
        "set", "super", "suspend", "tailrec", "this", "throw", "true", "try", "typealias",
        "val", "var", "vararg", "when", "while", "function", "const", "let", "def", "import",
        "from", "export", "default", "struct", "fn", "pub", "use", "impl", "trait"
    )

    private val KEYWORD_PATTERN = Pattern.compile("\\b(${KEYWORDS.joinToString("|")})\\b")
    private val STRING_PATTERN = Pattern.compile("\".*?\"|'.*?'")
    private val COMMENT_PATTERN = Pattern.compile("//.*|/\\*.*?\\*/|#.*")
    private val NUMBER_PATTERN = Pattern.compile("\\b\\d+\\b")

    fun highlight(text: String): AnnotatedString {
        return buildAnnotatedString {
            append(text)
            addStyle(SpanStyle(color = DEFAULT_TEXT_COLOR, fontFamily = FontFamily.Monospace), 0, text.length)

            // Keywords
            val keywordMatcher = KEYWORD_PATTERN.matcher(text)
            while (keywordMatcher.find()) {
                addStyle(SpanStyle(color = KEYWORD_COLOR), keywordMatcher.start(), keywordMatcher.end())
            }

            // Numbers
            val numberMatcher = NUMBER_PATTERN.matcher(text)
            while (numberMatcher.find()) {
                addStyle(SpanStyle(color = NUMBER_COLOR), numberMatcher.start(), numberMatcher.end())
            }

            // Strings
            val stringMatcher = STRING_PATTERN.matcher(text)
            while (stringMatcher.find()) {
                addStyle(SpanStyle(color = STRING_COLOR), stringMatcher.start(), stringMatcher.end())
            }

            // Comments
            val commentMatcher = COMMENT_PATTERN.matcher(text)
            while (commentMatcher.find()) {
                addStyle(SpanStyle(color = COMMENT_COLOR), commentMatcher.start(), commentMatcher.end())
            }
        }
    }
}
