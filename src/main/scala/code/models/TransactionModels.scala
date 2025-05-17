package code.models

import java.time.{Instant, LocalDate}


/**
 * Case class representing a raw transaction record from the input file
 *
 * @param timestamp     The exact time when the transaction occurred (in UTC)
 * @param productName   Name/description of the purchased product
 * @param expiryDate    Date when the product expires (for perishable goods)
 * @param quantity      Number of units purchased in this transaction
 * @param unitPrice     Price per single unit of the product
 * @param channel       Sales channel where transaction occurred (e.g., "app", "web", "store")
 * @param paymentMethod Payment method used (e.g., "visa", "mastercard", "cash")
 */
case class TransactionRecord(
                              timestamp: Instant,
                              productName: String,
                              expiryDate: LocalDate,
                              quantity: Int,
                              unitPrice: Double,
                              channel: String,
                              paymentMethod: String
                            )

/**
 * Case class representing a transaction with all discounts applied
 * Extends TransactionRecord with additional discount information
 *
 * @param discount    Total discount percentage applied (0-100)
 * @param finalPrice  Calculated final price after all discounts
 */
case class DiscountedTransaction(
                                  timestamp: Instant,
                                  productName: String,
                                  expiryDate: LocalDate,
                                  quantity: Int,
                                  unitPrice: Double,
                                  channel: String,
                                  paymentMethod: String,
                                  discount: Double,
                                  finalPrice: Double
                                )