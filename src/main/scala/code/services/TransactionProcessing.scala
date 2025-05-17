package code.services
import code.utils.LoggerUtils
import code.services.FileService.{readFromFile, toTransactionRecord}
import code.services.DatabaseService.insertDiscountedTransactions
import code.transformers.transformTransaction.transformTransaction

import java.sql.{Connection, DriverManager}
import java.util.logging.Level

object TransactionProcessing {
  private val logger = LoggerUtils.initializeLogger("TransactionProcessing")
  private val DataFilePath = "D:\\iti\\Scala_rule_engine\\src\\main\\scala\\data\\TRX1000.csv"
  private val DbUrl = "jdbc:oracle:thin:@//localhost:1521/XE"
  private val DbUser = "HR"
  private val DbPassword = "hr"
  /**
   * Main transaction processing pipeline:
   * 1. Reads transactions from file
   * 2. Parses and validates records
   * 3. Calculates discounts and final prices
   * 4. Stores results in database
   */
  def processTransactions(): Unit = {
    logger.log(Level.INFO, "Starting transaction processing")

    var conn: Connection = null
    try {
      // Establish database connection
      conn = DriverManager.getConnection(DbUrl, DbUser, DbPassword)
      logger.log(Level.INFO, "Database connection established")

      // Read and parse transaction file
      val lines = readFromFile(DataFilePath)
      val transactions = lines.flatMap(toTransactionRecord)

      // Log parsing failures
      if (transactions.size < lines.size) {
        logger.log(Level.WARNING, s"Failed to parse ${lines.size - transactions.size} of ${lines.size} records")
      }

      // Apply discounts and calculate final prices
      val transformedTransactions = transactions.map(transformTransaction)

      // Store results in database
      insertDiscountedTransactions(conn, transformedTransactions)

      logger.log(Level.INFO, s"Successfully processed ${transformedTransactions.size} transactions")
    } catch {
      case e: Exception =>
        logger.log(Level.SEVERE, s"Processing failed: ${e.getMessage}")
        throw e
    } finally {
      // Ensure database connection is always closed
      if (conn != null) {
        conn.close()
        logger.log(Level.INFO, "Database connection closed")
      }
    }
  }
}
