package code
import code.utils.LoggerUtils
import code.services.TransactionProcessing.processTransactions

object main extends App{
  private val logger = LoggerUtils.initializeLogger("Main")
  logger.log(java.util.logging.Level.INFO, "Starting transaction processing application")
  processTransactions()
  logger.log(java.util.logging.Level.INFO, "Application completed successfully")
}
