package code.utils
import java.time.format.DateTimeFormatter
import java.time.LocalDateTime
import java.util.logging.{FileHandler, Level, Logger, SimpleFormatter}

object LoggerUtils {
  private val LogFilePath = "D:\\iti\\Scala_rule_engine\\rules_engine.log"

  /**
   * Initializes and configures a logger with file output
   */
  def initializeLogger(name: String): Logger = {
    val logger = Logger.getLogger("test")

    if (logger.getHandlers.isEmpty) {
      val fileHandler = new FileHandler(LogFilePath, 1024*1024, 3, true)
      fileHandler.setFormatter(new SimpleFormatter {
        override def format(record: java.util.logging.LogRecord): String = {
          s"${LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)} " +
            s"${record.getLevel.getName} ${record.getMessage}\n"
        }
      })
      logger.addHandler(fileHandler)
      logger.setUseParentHandlers(false)
    }
    logger
  }
}