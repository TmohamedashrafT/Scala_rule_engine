package code.services

import java.util.logging.Level
import scala.io.Source
import code.utils.LoggerUtils
import java.time.{Instant, LocalDate}
import scala.util.Try
import code.models.{TransactionRecord, DiscountedTransaction}


object FileService {

  private val logger = LoggerUtils.initializeLogger("FileService")

  /**
   * Reads transaction data from a CSV file
   *
   * @param path       Path to the input CSV file
   * @param hasHeader  Whether the CSV file has a header row to skip
   * @return           List of transaction lines as Strings
   */
  def readFromFile(path: String, hasHeader: Boolean = true): List[String] = {
    logger.log(Level.INFO, s"Reading transactions from $path")
    val source = Source.fromFile(path)
    try {
      // Read all lines, skipping header if present
      val lines = if (hasHeader) source.getLines().drop(1).toList else source.getLines().toList
      logger.log(Level.INFO, s"Read ${lines.size} transactions")
      lines
    } finally {
      // Ensure file handle is closed even if an exception occurs
      source.close()
    }
  }
  /**
   * Parses a line from the input CSV file into a TransactionRecord object
   *
   * @param line  A single line from the input CSV file
   * @return      Option[TransactionRecord] - Some if parsing succeeded, None if failed
   */
  def toTransactionRecord(line: String): Option[TransactionRecord] = {
    // Split the line into fields and trim whitespace
    val fields = line.split(",").map(_.trim)

    // Validate we have exactly 7 fields as expected
    if (fields.length != 7) {
      logger.log(Level.WARNING, s"Invalid line format - expected 7 fields, got ${fields.length}: $line")
      return None
    }

    // Try to parse all fields, converting to appropriate types
    Try {
      TransactionRecord(
        timestamp = Instant.parse(fields(0)),      // Parse ISO-8601 timestamp
        productName = fields(1),                  // Product name as string
        expiryDate = LocalDate.parse(fields(2)),  // Parse expiry date
        quantity = fields(3).toInt,               // Convert quantity to integer
        unitPrice = fields(4).toDouble,           // Convert price to double
        channel = fields(5),                      // Sales channel
        paymentMethod = fields(6)                 // Payment method
      )
    }.toOption match {
      case Some(tx) =>
        // Log successful parsing at FINE level to avoid log spam
        logger.log(Level.FINE, s"Parsed transaction: ${tx.productName}")
        Some(tx)
      case None =>
        // Log parsing failures with WARNING level
        logger.log(Level.WARNING, s"Failed to parse transaction record from line: $line")
        None
    }
  }

  /**
   * Converts a TransactionRecord to a DiscountedTransaction by applying calculated discounts
   *
   * @param tx           The original transaction record
   * @param discount     Calculated discount percentage (0-100)
   * @param finalPrice   Final price after applying discount
   * @return             DiscountedTransaction with all original fields plus discount info
   */
  def toDiscountedTransaction(tx: TransactionRecord, discount: Double, finalPrice: Double): DiscountedTransaction = {
    logger.log(Level.FINE, s"Applying $discount% discount to ${tx.productName}")
    DiscountedTransaction(
      timestamp = tx.timestamp,
      productName = tx.productName,
      expiryDate = tx.expiryDate,
      quantity = tx.quantity,
      unitPrice = tx.unitPrice,
      channel = tx.channel,
      paymentMethod = tx.paymentMethod,
      discount = discount,
      finalPrice = finalPrice
    )
  }

}
