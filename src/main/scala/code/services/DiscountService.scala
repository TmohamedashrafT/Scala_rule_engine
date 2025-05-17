package code.services
import code.utils.LoggerUtils

import java.time.temporal.ChronoUnit
import java.util.logging.Level
import code.models.{TransactionRecord}

object DiscountService {
  private val logger = LoggerUtils.initializeLogger("DiscountService")


  /**
   * Applies increasing discount as expiry date approaches (up to 29% when 1 day remains)
   *
   * @param tx  Transaction record to evaluate
   * @return    Discount percentage (0-29)
   */
   def calculateExpiryDiscount(tx: TransactionRecord): Double = {
    // Calculate days remaining until expiry
    val daysRemaining = ChronoUnit.DAYS.between(
      tx.timestamp.atZone(java.time.ZoneOffset.UTC).toLocalDate,
      tx.expiryDate
    ).toInt

    // Apply progressive discount for perishable goods
    if (daysRemaining < 30 && daysRemaining >= 1) {
      val discount = 30 - daysRemaining
      discount
    } else {
      0.0
    }
  }

  /**
   * Applies category-specific discounts based on product name
   *
   * @param tx  Transaction record to evaluate
   * @return    Discount percentage (0, 5, or 10)
   */
   def categoryDiscount(tx: TransactionRecord): Double = {
    val discount = tx.productName.toLowerCase match {
      case name if name.contains("cheese") => 10.0  // Cheese gets 10% discount
      case name if name.contains("wine") => 5.0     // Wine gets 5% discount
      case _ => 0.0
    }
    discount
  }

  /**
   * Special anniversary discount applied only on March 23rd
   *
   * @param tx  Transaction record to evaluate
   * @return    Discount percentage (0 or 50)
   */
  def march23rdDiscount(tx: TransactionRecord): Double = {
    val date = tx.timestamp.atZone(java.time.ZoneOffset.UTC).toLocalDate
    if (date.getMonthValue == 3 && date.getDayOfMonth == 23) {
      50.0
    } else {
      0.0
    }
  }

  /**
   * Quantity-based bulk purchase discounts
   *
   * @param tx  Transaction record to evaluate
   * @return    Discount percentage based on quantity (0, 5, 7, or 10)
   */
   def quantityDiscount(tx: TransactionRecord): Double = {
    val discount = tx.quantity match {
      case q if q >= 15 => 10.0  // 15+ units: 10% discount
      case q if q >= 10 => 7.0   // 10-14 units: 7% discount
      case q if q >= 6 => 5.0    // 6-9 units: 5% discount
      case _ => 0.0
    }
    discount
  }

  /**
   * Channel-specific discount for app purchases
   * App purchases get a discount equal to the next multiple of 5 of their quantity
   * (e.g., 3 items → 5%, 7 items → 10%, etc.)
   *
   * @param tx  Transaction record to evaluate
   * @return    Discount percentage (0 or multiple of 5)
   */
  def channelDiscount(tx: TransactionRecord): Double = {
    if (tx.channel.equalsIgnoreCase("app")) {
      val discount = ((tx.quantity / 5).ceil * 5).toInt
      discount
    }
    else {
      0.0
    }
  }

  /**
   * Payment method discount for Visa cards
   *
   * @param tx  Transaction record to evaluate
   * @return    Discount percentage (0 or 5)
   */
  def cardDiscount(tx: TransactionRecord): Double = {
    if (tx.paymentMethod.equalsIgnoreCase("visa")) {
      logger.log(Level.INFO, s"Visa card discount for ${tx.productName}: 5%")
      5.0
    } else {
      0.0
    }
  }

  /**
   * Determines the final discount to apply by combining applicable discounts
   * Business rule: Take the top two highest discounts and average them
   * If only one non-zero discount exists, use that one
   *
   * @param tx  Transaction record to evaluate
   * @return    Final discount percentage to apply
   */
  def calculateFinalDiscount(tx: TransactionRecord): Double = {
    val discountFunctions: List[TransactionRecord => Double] = List(
      calculateExpiryDiscount,
      categoryDiscount,
      march23rdDiscount,
      quantityDiscount,
      channelDiscount,
      cardDiscount
    )
    // Calculate all possible discounts
    val discounts = discountFunctions.map(f => f(tx)).sorted(Ordering[Double].reverse)  // Sort descending

    // Take top two discounts
    val top2 = discounts.take(2)

    // Apply business rule for combining discounts
    val finalDiscount = if (top2.size > 1 && top2(1) > 0) {
      (top2(0) + top2(1)) / 2  // Average of top two
    } else {
      top2.head  // Use single highest discount
    }

    logger.log(Level.FINE, s"Final discount for ${tx.productName}: $finalDiscount% (from discounts: ${discounts.mkString(", ")})")
    finalDiscount
  }

  /**
   * Calculates the final price after applying discount percentage
   *
   * @param tx              Transaction record
   * @param discountPercent Total discount percentage to apply (0-100)
   * @return                Final price after discount
   */
  def calculateFinalPrice(tx: TransactionRecord, discountPercent: Double): Double = {
    val originalPrice = tx.unitPrice * tx.quantity
    val finalPrice = originalPrice * (100 - discountPercent) / 100
    logger.log(Level.FINE, s"${tx.productName} - Original: $originalPrice, Discount: $discountPercent%, Final: $finalPrice")
    finalPrice
  }


}
