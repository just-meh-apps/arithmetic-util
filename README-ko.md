# ArithmeticUtil - 정밀 숫자 연산 유틸리티

`ArithmeticUtil`은 Java에서 부동소수점 오류 없이 안전하고 정밀한 숫자 연산을 수행하기 위해 설계된 유틸리티 클래스입니다. 모든 연산은 내부적으로 `java.math.BigDecimal`을 사용하여 금융 및 공학 계산과 같이 높은 정밀도가 요구되는 모든 시나리오에서 신뢰할 수 있는 결과를 보장합니다.

## 핵심 개념

- **정밀도 보장**: 모든 연산은 `BigDecimal`을 통해 이루어져 `double`이나 `float` 타입에서 발생할 수 있는 계산 오류를 원천적으로 방지합니다.
- **사용 편의성**: `Integer`, `Double`, `String` 등 다양한 숫자 타입을 인자로 직접 받아 `BigDecimal`로 자동 변환하여 편리하게 사용할 수 있습니다.
- **Null 안전성**: 모든 메소드는 `null` 입력을 `BigDecimal.ZERO`로 간주하여 `NullPointerException` 발생 없이 안전하게 연산을 수행합니다.

---

## 요구 사항

- **Java 8** 이상

---

## `Scale` Enum

연산의 정밀도를 명확하고 일관되게 관리하기 위해 `Scale` 열거형을 제공합니다.

- `INTEGER(0)`: 정수 결과를 원할 때 사용합니다.
- `CURRENCY(2)`: 금융 계산과 같이 소수점 두 자리 정밀도가 필요할 때 사용합니다.
- `PERCENTAGE(4)`: 백분율 계산을 위한 소수점 네 자리 정밀도를 제공합니다.
- `DEFAULT(32)`: 일반적인 나눗셈 연산에 사용되는 기본 정밀도입니다.

---

## API 문서

### 1. 핵심 변환 및 문자열화

다양한 숫자 타입을 `BigDecimal`로 변환하거나, `BigDecimal`을 서식화된 문자열로 변환합니다.

#### `toBigDecimal(Number)`
모든 숫자 타입의 객체를 `BigDecimal`로 변환합니다. `null`은 `BigDecimal.ZERO`로 처리됩니다.

```java
BigDecimal bd1 = ArithmeticUtil.toBigDecimal(123.45); // 123.45
BigDecimal bd2 = ArithmeticUtil.toBigDecimal("123.45"); // 123.45
BigDecimal bd3 = ArithmeticUtil.toBigDecimal(null);   // 0
```

#### `toString(Number, boolean)`
숫자를 공학적 표기법이 아닌 일반 숫자 형식의 문자열로 변환합니다. 두 번째 인자로 `true`를 전달하면 소수점 이하의 불필요한 0을 제거합니다.

```java
String s1 = ArithmeticUtil.toString(new BigDecimal("123.4500"), true);  // "123.45"
String s2 = ArithmeticUtil.toString(new BigDecimal("123.00"), true);   // "123"
String s3 = ArithmeticUtil.toString(new BigDecimal("123.4500"), false); // "123.4500"
```

### 2. 기본 산술 연산

`+`, `-`, `*`, `/` 연산을 정밀하게 수행합니다.

```java
BigDecimal sum = ArithmeticUtil.add(1.1, 2.2); // 3.3
BigDecimal diff = ArithmeticUtil.subtract(10, 3.5); // 6.5
BigDecimal product = ArithmeticUtil.multiply(1.23, 100); // 123.00
BigDecimal quotient = ArithmeticUtil.divide(10, 3, Scale.CURRENCY, RoundingMode.HALF_UP); // 3.33
```
- `divide(0, 10)`의 결과는 `BigDecimal.ZERO` 입니다.
- 0으로 나누면 `ArithmeticException`이 발생합니다.

### 3. 반올림 연산

`ceil` (올림), `round` (반올림), `floor` (내림) 연산을 수행합니다.

```java
BigDecimal ceiled = ArithmeticUtil.ceil(1.234, 2);   // 1.24
BigDecimal rounded = ArithmeticUtil.round(1.235, 2); // 1.24
BigDecimal floored = ArithmeticUtil.floor(1.239, 2); // 1.23
```
- 두 번째 인자로 `int` 또는 `Scale` 타입을 사용할 수 있습니다.

### 4. 단항 연산

절대값(`abs`) 또는 부호 반전(`negate`) 연산을 수행합니다.

```java
BigDecimal absolute = ArithmeticUtil.abs(-123.45); // 123.45
BigDecimal negated = ArithmeticUtil.negate(123.45);  // -123.45
```

### 5. 비교 연산

두 숫자의 값을 정밀하게 비교합니다.

```java
boolean isEqual = ArithmeticUtil.isEqual(10, 10.0); // true
boolean isGreater = ArithmeticUtil.isGreaterThan(10.1, 10.0); // true
boolean isLess = ArithmeticUtil.isLessThan(9.9, 10.0); // true
```
- `isGreaterThanOrEqual`, `isLessThanOrEqual`도 지원합니다.

### 6. 값 검사

숫자가 0인지, 양수인지, 음수인지 검사합니다.

```java
boolean isZero = ArithmeticUtil.isZero(new BigDecimal("0.00")); // true
boolean isPositive = ArithmeticUtil.isPositive(0.001); // true
boolean isNegative = ArithmeticUtil.isNegative(-0.001); // true
```

### 7. 타입 검사

숫자가 정수 형태인지, 소수 형태인지 검사합니다.

```java
boolean isInt1 = ArithmeticUtil.isInteger(123.00); // true
boolean isInt2 = ArithmeticUtil.isInteger(123.01); // false

boolean isDec1 = ArithmeticUtil.isDecimal(123.01); // true
boolean isDec2 = ArithmeticUtil.isDecimal(123.00); // false
```

### 8. 집계 연산

가변 인자로 전달된 숫자들 중에서 최소값 또는 최대값을 찾습니다.

```java
BigDecimal minVal = ArithmeticUtil.min(10, 20, -5, 15.5); // -5
BigDecimal maxVal = ArithmeticUtil.max(10, 20, -5, 15.5); // 20
```
- `null` 값은 연산에서 제외됩니다. 인자가 없거나 모두 `null`이면 `BigDecimal.ZERO`를 반환합니다.

---

## 빌드하기

프로젝트를 빌드하고 테스트를 실행하려면 다음 Maven 명령을 사용하세요.

```shell
mvn clean install
```
