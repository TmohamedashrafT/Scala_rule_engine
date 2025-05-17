package code.transformers

import code.services.DiscountService.{calculateFinalDiscount, calculateFinalPrice}
import code.services.FileService.{toDiscountedTransaction}
import code.models.{TransactionRecord, DiscountedTransaction}

import java.util.logging.Level
import code.utils.LoggerUtils

object transformTransaction {
  private val logger = LoggerUtils.initializeLogger("DiscountService")

  /**
   * Transforms a TransactionRecord into a DiscountedTransaction by:
   * 1. Calculating applicable discounts
   * 2. Computing final price
   * 3. Creating new record with discount info
   *
   * @param tx  Original transaction record
   * @return    Discounted transaction with final price
   */
   def transformTransaction(tx: TransactionRecord): DiscountedTransaction = {
    logger.log(Level.FINE, s"Transforming transaction for ${tx.productName}")
    val discount = calculateFinalDiscount(tx)
    val finalPrice = calculateFinalPrice(tx, discount)
    toDiscountedTransaction(tx, discount, finalPrice)
  }
}
