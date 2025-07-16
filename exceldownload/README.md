# Excel Download Module

## Features

- Memory-efficient data streaming with configurable fetch size (1000 rows)
- SXSSFWorkbook implementation for memory overflow prevention
- Support for large datasets (tested with 3 million+ records)

## Technical Architecture

### Core Components

- ExcelColumn: Configurable column definition with type safety
- Stream Processing: JPA Stream implementation for data pagination
- Apache POI Integration: SXSSFWorkbook for efficient Excel generation

### Supported Data Types

- String, Integer, Long
- Double, Float (with percentage support)
- Date, DateTime, Time
- BigDecimal (double and long conversions)

## Configuration

```shell
cd ../excel-util
./gradlew publishToMavenLocal
cd ../exceldownload
./gradlew build --refresh-dependencies
```