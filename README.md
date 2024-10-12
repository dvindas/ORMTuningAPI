# ORMTuningAPI
Demo project to tune ORM query problem against n+1

## Overview
This is a Spring Boot application designed to demonstrate efficient data handling using ORM strategies. The application addresses common ORM performance issues like the N+1 query problem and provides optimized solutions using techniques like `JOIN FETCH` and **Entity Graphs**.

For more details please check my [post on medium.com](https://medium.com/@dperez_/n-1-problem-the-silent-performance-killer-in-orms-4980830cba93)

## Features
- Invoice and InvoiceDetail entities
- Efficient querying using ORM strategies
- Excel report generation for invoices
- Async processing of large data sets
- Handling of large datasets using `Slice` for pagination

## Requirements
- Java 21 or later
- Spring Boot 3.x
- Maven 3.x
- H2 or any relational database

## Running the Application
1. Clone the repository:
   ```bash
   git clone https://github.com/dvindas/ORMTuningAPI.git
   cd ormtunningapi

## Test Endpoints

To test the operation you can use:

You can send the following query modes: NORMAL_QUERY_MODE = 1, FETCH_QUERY_MODE = 2, ENTITY_GRAPH_QUERY_MODE = 3;

```bash
curl -X GET "http://localhost:8080/reports/invoices/excel?accountNumber=ZXY-234&queryMode=2"
