package code.services

import code.models.DiscountedTransaction
import java.sql.{Connection, DriverManager, PreparedStatement, Timestamp}
import code.utils.LoggerUtils
import java.util.logging.{FileHandler, Level, Logger, SimpleFormatter}


object DatabaseService {
  private val logger = LoggerUtils.initializeLogger("DatabaseService")
  private val InsertBatchSize = 1000

  /**
   * Inserts discounted transactions into the database in batches for efficiency
   *
   * @param conn          Active database connection
   * @param transactions  List of discounted transactions to insert
   */
   def insertDiscountedTransactions(conn: Connection, transactions: List[DiscountedTransaction]): Unit = {
    logger.log(Level.INFO, s"Inserting ${transactions.size} transactions into database in batches of $InsertBatchSize")

    val sql = """
      INSERT INTO discounted_transactions
      (timestamp, product_name, expiry_date, quantity, unit_price, channel, payment_method, discount, final_price)
      VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
    """

    val pstmt = conn.prepareStatement(sql)

    try {
      // Process transactions in batches to optimize performance
      transactions.grouped(InsertBatchSize).foreach { batch =>
        batch.foreach { tx =>
          // Set all parameters for the prepared statement
          pstmt.setTimestamp(1, Timestamp.from(tx.timestamp))
          pstmt.setString(2, tx.productName)
          pstmt.setDate(3, java.sql.Date.valueOf(tx.expiryDate))
          pstmt.setInt(4, tx.quantity)
          pstmt.setDouble(5, tx.unitPrice)
          pstmt.setString(6, tx.channel)
          pstmt.setString(7, tx.paymentMethod)
          pstmt.setDouble(8, tx.discount)
          pstmt.setDouble(9, tx.finalPrice)
          pstmt.addBatch()  // Add to current batch
        }

        // Execute the batch and log results
        val results = pstmt.executeBatch()
        logger.log(Level.FINE, s"Inserted batch of ${results.length} transactions")
      }
      logger.log(Level.INFO, "Finished inserting all transactions")
    } catch {
      case e: Exception =>
        logger.log(Level.SEVERE, s"Database insertion failed: ${e.getMessage}")
        throw e
    } finally {
      pstmt.close()  // Ensure statement is  closed
    }
  }
}