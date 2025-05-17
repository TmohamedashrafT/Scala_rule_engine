# Scala Rule Engine 🚀

![Scala](https://img.shields.io/badge/Scala-2.13.x-DC322F?logo=scala&logoColor=white)
![Build](https://img.shields.io/badge/Build-sbt-green.svg)

A high-performance rule engine for applying complex business rules to transaction data, written in pure functional Scala.

## Key Features ✨

- **Rule Composition**: Combine multiple discount rules with custom logic
- **Dynamic Evaluation**: Rules evaluated at runtime based on transaction attributes
- **Batch Processing**: Efficiently process 1,000+ transactions
- **Oracle Integration**: JDBC-based data persistence
- **Audit Logging**: Detailed execution logs for every transaction
- **Pure Functions**: Immutable data structures and side-effect-free logic

## Rule Catalog

| Function Name            | Description                                                                 | Example Application                                                             |
|--------------------------|-----------------------------------------------------------------------------|----------------------------------------------------------------------------------|
| `calculateExpiryDiscount`| Calculates a discount based on how close the product is to expiration.     | A product expiring in 29 days gets 1% off, expiring in 1 day gets 29% off.      |
| `categoryDiscount`       | Applies a discount for specific product types (cheese or wine).             | "Cheddar Cheese" → 10% discount, "Red Wine" → 5% discount.                      |
| `march23rdDiscount`      | Applies a special 50% discount for purchases on March 23rd.                 | Any item bought on 23rd March gets 50% off.                                     |
| `quantityDiscount`       | Applies a bulk discount based on how many units are bought.                 | Buying 12 units → 7% discount, buying 6 units → 5% discount.                    |
| `channelDiscount`        | Applies a discount for app purchases based on quantity (rounded to 5).     | Buying 7 units through app → 10% discount, 12 units → 15% discount.             |
| `cardDiscount`           | Applies a 5% discount for purchases made using Visa card.                   | Any product purchased with Visa → 5% discount.                                  |

**Rule Composition Strategy**: 
Applies the average of the top two eligible discounts per transaction.

## Tech Stack 🛠️

- **Language**: Scala (Functional Programming)
- **Build Tool**: sbt
- **Database**: Oracle (JDBC)

## Getting Started

### Prerequisites

- Java 8+
- sbt 1.10.3
- scala 2.13.13
- Oracle Database (or Docker container)

### Installation

```bash
git clone https://github.com/yourusername/scala_rule_engine.git
cd scala_rule_engine
```

### Run the Project
1. Open a terminal in your project directory.  
2. Execute:  
   ```
   sbt run
   ```




## Project Structure: 
```
Project Directory Structure:
src/main/scala/code/
├── Main.scala               - Application entry point
├── models/
│   ├── TransactionModels.scala  
├── services/
│   ├── TransactionProcessing.scala 
│   ├── DatabaseService.scala    
│   ├── DiscountService.scala    
│   └── FileService.scala        
├── transformers/              
│   ├── TransformTransaction.scala  
└── utils/
    └── LoggerUtils.scala    
```

## Database Schema
The application expects the following table:

```
CREATE TABLE discounted_transactions (
  timestamp TIMESTAMP,
  product_name VARCHAR2(100),
  expiry_date DATE,
  quantity NUMBER,
  unit_price NUMBER(10,2),
  channel VARCHAR2(20),
  payment_method VARCHAR2(20),
  discount NUMBER(5,2),
  final_price NUMBER(10,2)
);
```

Logging
The application generates detailed logs in `rules_engine.log`:
```
2025-05-17T19:58:58.7708005 INFO Starting transaction processing application
2025-05-17T19:58:58.7746673 INFO Starting transaction processing
2025-05-17T19:58:59.6603174 INFO Database connection established
2025-05-17T19:58:59.6623348 INFO Reading transactions from D:\iti\Scala_rule_engine\src\main\scala\data\TRX1000.csv
2025-05-17T19:58:59.6925342 INFO Read 1000 transactions
``
