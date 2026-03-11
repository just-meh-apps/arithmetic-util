[한국어](./README-ko.md)

# ArithmeticUtil - High-Precision Arithmetic Utility

`ArithmeticUtil` is a utility class designed for safe and precise numerical operations in Java, avoiding floating-point errors. All operations internally use `java.math.BigDecimal` to ensure reliable results in scenarios requiring high precision, such as financial and engineering calculations.

## Core Concepts

- **Precision Guarantee**: All operations are performed via `BigDecimal`, fundamentally preventing calculation errors that can occur with `double` or `float` types.
- **Ease of Use**: Conveniently accepts various number types such as `Integer`, `Double`, and `String` as arguments, automatically converting them to `BigDecimal`.
- **Null Safety**: All methods treat `null` inputs as `BigDecimal.ZERO`, ensuring safe execution without `NullPointerExceptions`.

---

## Requirements

- **Java 8** or higher.

---

## `Scale` Enum

Provides the `Scale` enum to manage the precision of operations clearly and consistently.

- `INTEGER(0)`: Used when an integer result is desired.
- `CURRENCY(2)`: Used for financial calculations requiring two decimal places of precision.
- `PERCENTAGE(4)`: Provides four decimal places of precision for percentage calculations.
- `DEFAULT(32)`: The default precision used for general division operations.

---

## API Documentation

### 1. Core Conversion & Stringification

Converts various number types to `BigDecimal` or formats a `BigDecimal` into a string.

#### `toBigDecimal(Number)`
Converts any object of a number type to a `BigDecimal`. `null` is handled as `BigDecimal.ZERO`.

```java
BigDecimal bd1 = ArithmeticUtil.toBigDecimal(123.45); // 123.45
BigDecimal bd2 = ArithmeticUtil.toBigDecimal("123.45"); // 123.45
BigDecimal bd3 = ArithmeticUtil.toBigDecimal(null);   // 0
```

#### `toString(Number, boolean)`
Converts a number to a plain numeric string, not scientific notation. Passing `true` as the second argument removes unnecessary trailing zeros from the decimal part.

```java
String s1 = ArithmeticUtil.toString(new BigDecimal("123.4500"), true);  // "123.45"
String s2 = ArithmeticUtil.toString(new BigDecimal("123.00"), true);   // "123"
String s3 = ArithmeticUtil.toString(new BigDecimal("123.4500"), false); // "123.4500"
```

### 2. Basic Arithmetic Operations

Performs precise `+`, `-`, `*`, `/` operations.

```java
BigDecimal sum = ArithmeticUtil.add(1.1, 2.2); // 3.3
BigDecimal diff = ArithmeticUtil.subtract(10, 3.5); // 6.5
BigDecimal product = ArithmeticUtil.multiply(1.23, 100); // 123.00
BigDecimal quotient = ArithmeticUtil.divide(10, 3, Scale.CURRENCY, RoundingMode.HALF_UP); // 3.33
```
- The result of `divide(0, 10)` is `BigDecimal.ZERO`.
- Dividing by zero throws an `ArithmeticException`.

### 3. Rounding Operations

Performs `ceil` (round up), `round` (half up), and `floor` (round down) operations.

```java
BigDecimal ceiled = ArithmeticUtil.ceil(1.234, 2);   // 1.24
BigDecimal rounded = ArithmeticUtil.round(1.235, 2); // 1.24
BigDecimal floored = ArithmeticUtil.floor(1.239, 2); // 1.23
```
- The second argument can be an `int` or a `Scale` type.

### 4. Unary Operations

Performs absolute value (`abs`) or sign negation (`negate`) operations.

```java
BigDecimal absolute = ArithmeticUtil.abs(-123.45); // 123.45
BigDecimal negated = ArithmeticUtil.negate(123.45);  // -123.45
```

### 5. Comparison Operations

Precisely compares the values of two numbers.

```java
boolean isEqual = ArithmeticUtil.isEqual(10, 10.0); // true
boolean isGreater = ArithmeticUtil.isGreaterThan(10.1, 10.0); // true
boolean isLess = ArithmeticUtil.isLessThan(9.9, 10.0); // true
```
- `isGreaterThanOrEqual` and `isLessThanOrEqual` are also supported.

### 6. Value Checks

Checks if a number is zero, positive, or negative.

```java
boolean isZero = ArithmeticUtil.isZero(new BigDecimal("0.00")); // true
boolean isPositive = ArithmeticUtil.isPositive(0.001); // true
boolean isNegative = ArithmeticUtil.isNegative(-0.001); // true
```

### 7. Type Checks

Checks if a number is an integer or has a decimal part.

```java
boolean isInt1 = ArithmeticUtil.isInteger(123.00); // true
boolean isInt2 = ArithmeticUtil.isInteger(123.01); // false

boolean isDec1 = ArithmeticUtil.isDecimal(123.01); // true
boolean isDec2 = ArithmeticUtil.isDecimal(123.00); // false
```

### 8. Aggregate Operations

Finds the minimum or maximum value among a variable number of arguments.

```java
BigDecimal minVal = ArithmeticUtil.min(10, 20, -5, 15.5); // -5
BigDecimal maxVal = ArithmeticUtil.max(10, 20, -5, 15.5); // 20
```
- `null` values are excluded from the calculation. Returns `BigDecimal.ZERO` if no arguments or only `null` arguments are provided.

---

## Building

To build the project and run tests, use the following Maven command.

```shell
mvn clean install
```
