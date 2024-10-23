package pretty_print

import java.io.File

class PrettyPrinter(private val logger: org.slf4j.Logger) {

    private val stringBuilder = StringBuilder()

    fun header(level: Int, content: String) {
        val prefix = "#".repeat(level)
        stringBuilder.append("$prefix $content\n\n")
        logger.debug("Добавлен заголовок уровня $level: $content")
    }

    fun text(block: TextBuilder.() -> Unit) {
        val textBuilder = TextBuilder(logger)
        textBuilder.apply(block)
        stringBuilder.append(textBuilder.build())
        stringBuilder.append("\n")
    }

    fun saveToFile(filePath: String) {
        try {
            File(filePath).bufferedWriter().use { writer ->
                writer.write(stringBuilder.toString())
            }
            logger.info("Файл успешно сохранен по пути: $filePath")
        } catch (e: Exception) {
            logger.error("Ошибка при сохранении файла: ${e.localizedMessage}", e)
        }
    }

    fun printToConsole() {
        println(stringBuilder.toString())
        logger.info("Вывод выполнен в консоль")
    }
}

class TextBuilder(private val logger: org.slf4j.Logger) {

    private val textBuilder = StringBuilder()

    fun bold(content: String) {
        textBuilder.append("**$content**")
        logger.debug("Добавлен жирный текст: $content")
    }

    fun underlined(content: String) {
        textBuilder.append("__${content}__")
        logger.debug("Добавлено подчеркнутое слово: $content")
    }

    fun link(link: String, text: String) {
        textBuilder.append("[$text]($link)")
        logger.debug("Добавлена ссылка: $text -> $link")
    }

    fun code(language: String, content: String) {
        textBuilder.append("```$language\n$content\n```\n")
        logger.debug("Добавлен блок кода на языке $language")
    }

    operator fun String.unaryPlus() {
        textBuilder.append(this)
    }

    fun build(): String = textBuilder.toString()
}
