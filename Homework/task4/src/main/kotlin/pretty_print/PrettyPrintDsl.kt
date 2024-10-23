package pretty_print

import org.slf4j.LoggerFactory

fun prettyPrint(block: PrettyPrinter.() -> Unit): PrettyPrinter {
    val logger = LoggerFactory.getLogger("PrettyPrintDsl")
    val printer = PrettyPrinter(logger)
    try {
        printer.apply(block)
        logger.info("DSL PrettyPrint выполнен успешно")
    } catch (e: Exception) {
        logger.error("Ошибка при выполнении DSL PrettyPrint: ${e.localizedMessage}", e)
    }
    return printer
}
