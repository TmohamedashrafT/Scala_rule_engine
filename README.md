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

| Rule Type               | Description                                  | Example Application          |
|-------------------------|----------------------------------------------|------------------------------|
| `ExpiryBasedRule`       | Progressive discount nearing expiry date     | 29% discount when 1 day left |
| `CategoryRule`          | Product category-specific discounts         | 10% off all cheeses          |
| `AnniversaryRule`       | Special date promotions                     | 50% off on March 23rd        |
| `VolumeRule`            | Bulk purchase discounts                     | 10% off 15+ units            |
| `ChannelRule`           | Sales channel-specific offers               | Extra 5% for app purchases   |
| `PaymentMethodRule`     | Payment type incentives                     | 5% discount for Visa cards   |

**Rule Composition Strategy**: 
Applies the average of the top two eligible discounts per transaction.

## Tech Stack 🛠️

- **Language**: Scala (Functional Programming)
- **Build Tool**: sbt
- **Database**: Oracle (JDBC)
- **Logging**: java.util.logging with file rotation

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


## Project Structure: 
```
Project Directory Structure:
src/main/scala/code/
├── Main.scala               - Application entry point
├── models/
│   ├── TransactionModels.scala  
├── services/
│   ├── TransactionService.scala 
│   ├── DatabaseService.scala    
│   ├── DiscountService.scala    
│   └── FileService.scala        
├── transformers/              
│   ├── TransformTransaction.scala  
└── utils/
    └── LoggerUtils.scala    
```
