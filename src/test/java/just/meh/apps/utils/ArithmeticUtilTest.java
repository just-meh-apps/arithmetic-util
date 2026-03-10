// Copyright (c) 2026 just.meh.apps@gmail.com
// SPDX-License-Identifier: MIT

package just.meh.apps.utils;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ArithmeticUtilTest {

    @Nested
    class ToBigDecimalTests {
        @Test
        void shouldReturnZeroWhenNull() {
            assertThat(ArithmeticUtil.toBigDecimal(null)).isEqualByComparingTo(BigDecimal.ZERO);
        }

        @Test
        void shouldConvertInteger() {
            assertThat(ArithmeticUtil.toBigDecimal(123)).isEqualByComparingTo(new BigDecimal("123"));
        }

        @Test
        void shouldConvertDouble() {
            assertThat(ArithmeticUtil.toBigDecimal(123.456)).isEqualByComparingTo(BigDecimal.valueOf(123.456));
        }

        @Test
        void shouldConvertFloat() {
            assertThat(ArithmeticUtil.toBigDecimal(123.456f)).isEqualByComparingTo(new BigDecimal("123.456"));
        }

        @Test
        void shouldConvertLong() {
            assertThat(ArithmeticUtil.toBigDecimal(123456789012345L)).isEqualByComparingTo(new BigDecimal("123456789012345"));
        }

        @Test
        void shouldConvertShort() {
            assertThat(ArithmeticUtil.toBigDecimal((short) 123)).isEqualByComparingTo(new BigDecimal("123"));
        }

        @Test
        void shouldConvertByte() {
            assertThat(ArithmeticUtil.toBigDecimal((byte) 123)).isEqualByComparingTo(new BigDecimal("123"));
        }

        @Test
        void shouldHandleExistingBigDecimal() {
            BigDecimal existing = new BigDecimal("123.456789");
            assertThat(ArithmeticUtil.toBigDecimal(existing)).isSameAs(existing); // Should return the same object
        }

        @Test
        void shouldConvertBigInteger() {
            BigInteger bigInt = new BigInteger("123456789012345678901234567890");
            assertThat(ArithmeticUtil.toBigDecimal(bigInt)).isEqualByComparingTo(new BigDecimal(bigInt));
        }

        @Test
        void shouldHandleZero() {
            assertThat(ArithmeticUtil.toBigDecimal(0)).isEqualByComparingTo(BigDecimal.ZERO);
            assertThat(ArithmeticUtil.toBigDecimal(0.0)).isEqualByComparingTo(BigDecimal.ZERO);
            assertThat(ArithmeticUtil.toBigDecimal(0L)).isEqualByComparingTo(BigDecimal.ZERO);
        }

        @Test
        void shouldHandleNegativeNumbers() {
            assertThat(ArithmeticUtil.toBigDecimal(-123)).isEqualByComparingTo(new BigDecimal("-123"));
            assertThat(ArithmeticUtil.toBigDecimal(-123.456)).isEqualByComparingTo(BigDecimal.valueOf(-123.456));
        }
    }

    @Nested
    class ToStringTests {
        @Test
        void defaultShouldStripTrailingZeros() {
            assertThat(ArithmeticUtil.toString(new BigDecimal("10.00"))).isEqualTo("10");
            assertThat(ArithmeticUtil.toString(1.23E+2)).isEqualTo("123"); // from 123.0
            assertThat(ArithmeticUtil.toString(null)).isEqualTo("0");
        }

        @Test
        void withStripTrueShouldStripTrailingZeros() {
            assertThat(ArithmeticUtil.toString(new BigDecimal("123.4500"), true)).isEqualTo("123.45");
            assertThat(ArithmeticUtil.toString(10.0, true)).isEqualTo("10");
        }

        @Test
        void withStripFalseShouldKeepTrailingZeros() {
            assertThat(ArithmeticUtil.toString(new BigDecimal("123.4500"), false)).isEqualTo("123.4500");
            assertThat(ArithmeticUtil.toString(10.0, false)).isEqualTo("10.0");
            assertThat(ArithmeticUtil.toString(new BigDecimal("10"), false)).isEqualTo("10");
        }

        @Test
        void shouldAlwaysReturnPlainString() {
            assertThat(ArithmeticUtil.toString(1.23E-5, true)).isEqualTo("0.0000123");
            assertThat(ArithmeticUtil.toString(1.23E-5, false)).isEqualTo("0.0000123");
            assertThat(ArithmeticUtil.toString(1.23E+5, true)).isEqualTo("123000");
            assertThat(ArithmeticUtil.toString(1.23E+5, false)).isEqualTo("123000.0");
        }
    }

    @Nested
    class ArithmeticOperationsTests {
        @Test
        void addShouldSumUpNumbers() {
            assertThat(ArithmeticUtil.add(10, 20)).isEqualByComparingTo("30");
            assertThat(ArithmeticUtil.add(-10, 20)).isEqualByComparingTo("10");
            assertThat(ArithmeticUtil.add(1.23, 4.56)).isEqualByComparingTo("5.79");
            assertThat(ArithmeticUtil.add(10, null)).isEqualByComparingTo("10");
            assertThat(ArithmeticUtil.add(null, 20)).isEqualByComparingTo("20");
        }

        @Test
        void subtractShouldFindTheDifference() {
            assertThat(ArithmeticUtil.subtract(30, 20)).isEqualByComparingTo("10");
            assertThat(ArithmeticUtil.subtract(10, -20)).isEqualByComparingTo("30");
            assertThat(ArithmeticUtil.subtract(5.79, 4.56)).isEqualByComparingTo("1.23");
            assertThat(ArithmeticUtil.subtract(10, null)).isEqualByComparingTo("10");
            assertThat(ArithmeticUtil.subtract(null, 20)).isEqualByComparingTo("-20");
        }

        @Test
        void multiplyShouldFindTheProduct() {
            assertThat(ArithmeticUtil.multiply(10, 20)).isEqualByComparingTo("200");
            assertThat(ArithmeticUtil.multiply(-10, 20)).isEqualByComparingTo("-200");
            assertThat(ArithmeticUtil.multiply(1.5, 2)).isEqualByComparingTo("3.0");
            assertThat(ArithmeticUtil.multiply(10, null)).isEqualByComparingTo("0");
            assertThat(ArithmeticUtil.multiply(null, 20)).isEqualByComparingTo("0");
        }

        @Test
        void multiplyWithScaleShouldRoundTheProduct() {
            assertThat(ArithmeticUtil.multiply(1.234, 2.345, 2, RoundingMode.HALF_UP)).isEqualByComparingTo("2.89");
            assertThat(ArithmeticUtil.multiply(1.1, 1.1, Scale.INTEGER, RoundingMode.HALF_UP)).isEqualByComparingTo("1");
        }

        @Test
        void divideWithScaleShouldRoundTheQuotient() {
            assertThat(ArithmeticUtil.divide(10, 3, 2, RoundingMode.HALF_UP)).isEqualByComparingTo("3.33");
            assertThat(ArithmeticUtil.divide(20, 3, 0, RoundingMode.HALF_UP)).isEqualByComparingTo("7");
            assertThat(ArithmeticUtil.divide(10, 8, Scale.CURRENCY, RoundingMode.HALF_UP)).isEqualByComparingTo("1.25");
        }

        @Test
        void divideShouldReturnZeroWhenDividendIsZero() {
            assertThat(ArithmeticUtil.divide(0, 10)).isEqualByComparingTo(BigDecimal.ZERO);
            assertThat(ArithmeticUtil.divide(0, -10)).isEqualByComparingTo(BigDecimal.ZERO);
            assertThat(ArithmeticUtil.divide(null, 10)).isEqualByComparingTo(BigDecimal.ZERO);
        }

        @Test
        void divideByDefaultShouldUseDefaultScale() {
            assertThat(ArithmeticUtil.divide(10, 3).scale()).isEqualTo(Scale.DEFAULT.getValue());
            assertThat(ArithmeticUtil.divide(10, 3).toString()).startsWith("3.33333333333333333333333333333333");
        }

        @Test
        void divideByZeroShouldThrowException() {
            assertThatThrownBy(() -> ArithmeticUtil.divide(10, 0))
                    .isInstanceOf(ArithmeticException.class)
                    .satisfies(e -> assertThat(e.getMessage().toLowerCase()).contains("by zero"));

            assertThatThrownBy(() -> ArithmeticUtil.divide(10, null, 2, RoundingMode.HALF_UP))
                    .isInstanceOf(ArithmeticException.class)
                    .satisfies(e -> assertThat(e.getMessage().toLowerCase()).contains("by zero"));
        }
    }

    @Nested
    class RoundingTests {
        @Test
        void ceilShouldAlwaysRoundUpToPositiveInfinity() {
            assertThat(ArithmeticUtil.ceil(1.234, 2)).isEqualByComparingTo("1.24");
            assertThat(ArithmeticUtil.ceil(-1.239, 2)).isEqualByComparingTo("-1.23");
            assertThat(ArithmeticUtil.ceil(10, 2)).isEqualByComparingTo("10.00");
            assertThat(ArithmeticUtil.ceil(null, 2)).isEqualByComparingTo("0.00");
            assertThat(ArithmeticUtil.ceil(1.234, Scale.CURRENCY)).isEqualByComparingTo("1.24");
        }

        @Test
        void roundShouldRoundToNearestNeighbor() {
            assertThat(ArithmeticUtil.round(1.235, 2)).isEqualByComparingTo("1.24");
            assertThat(ArithmeticUtil.round(1.234, 2)).isEqualByComparingTo("1.23");
            assertThat(ArithmeticUtil.round(-1.235, 2)).isEqualByComparingTo("-1.24");
            assertThat(ArithmeticUtil.round(-1.234, 2)).isEqualByComparingTo("-1.23");
            assertThat(ArithmeticUtil.round(null, 2)).isEqualByComparingTo("0.00");
            assertThat(ArithmeticUtil.round(1.235, Scale.CURRENCY)).isEqualByComparingTo("1.24");
        }

        @Test
        void floorShouldAlwaysRoundDownToNegativeInfinity() {
            assertThat(ArithmeticUtil.floor(1.239, 2)).isEqualByComparingTo("1.23");
            assertThat(ArithmeticUtil.floor(-1.234, 2)).isEqualByComparingTo("-1.24");
            assertThat(ArithmeticUtil.floor(10, 2)).isEqualByComparingTo("10.00");
            assertThat(ArithmeticUtil.floor(null, 2)).isEqualByComparingTo("0.00");
            assertThat(ArithmeticUtil.floor(1.239, Scale.CURRENCY)).isEqualByComparingTo("1.23");
        }
    }

    @Nested
    class UnaryOperatorTests {
        @Test
        void absShouldReturnAbsoluteValue() {
            assertThat(ArithmeticUtil.abs(-10)).isEqualByComparingTo("10");
            assertThat(ArithmeticUtil.abs(10)).isEqualByComparingTo("10");
            assertThat(ArithmeticUtil.abs(0)).isEqualByComparingTo("0");
            assertThat(ArithmeticUtil.abs(null)).isEqualByComparingTo("0");
            assertThat(ArithmeticUtil.abs(-12.34)).isEqualByComparingTo("12.34");
        }

        @Test
        void negateShouldReverseTheSign() {
            assertThat(ArithmeticUtil.negate(10)).isEqualByComparingTo("-10");
            assertThat(ArithmeticUtil.negate(-10)).isEqualByComparingTo("10");
            assertThat(ArithmeticUtil.negate(0)).isEqualByComparingTo("0");
            assertThat(ArithmeticUtil.negate(null)).isEqualByComparingTo("0");
            assertThat(ArithmeticUtil.negate(-12.34)).isEqualByComparingTo("12.34");
            assertThat(ArithmeticUtil.negate(12.34)).isEqualByComparingTo("-12.34");
        }
    }

    @Nested
    class ComparisonTests {
        @Test
        void isEqualShouldCompareValues() {
            assertThat(ArithmeticUtil.isEqual(10, 10.0)).isTrue();
            assertThat(ArithmeticUtil.isEqual(new BigDecimal("10.0"), 10)).isTrue();
            assertThat(ArithmeticUtil.isEqual(10, 11)).isFalse();
            assertThat(ArithmeticUtil.isEqual(null, 0)).isTrue();
            assertThat(ArithmeticUtil.isEqual(null, null)).isTrue();
        }

        @Test
        void isGreaterThanShouldCompareValues() {
            assertThat(ArithmeticUtil.isGreaterThan(11, 10)).isTrue();
            assertThat(ArithmeticUtil.isGreaterThan(10, 10)).isFalse();
            assertThat(ArithmeticUtil.isGreaterThan(10, 11)).isFalse();
            assertThat(ArithmeticUtil.isGreaterThan(0.1, null)).isTrue();
        }

        @Test
        void isGreaterThanOrEqualShouldCompareValues() {
            assertThat(ArithmeticUtil.isGreaterThanOrEqual(11, 10)).isTrue();
            assertThat(ArithmeticUtil.isGreaterThanOrEqual(10, 10.0)).isTrue();
            assertThat(ArithmeticUtil.isGreaterThanOrEqual(9, 10)).isFalse();
            assertThat(ArithmeticUtil.isGreaterThanOrEqual(null, 0.0)).isTrue();
        }

        @Test
        void isLessThanShouldCompareValues() {
            assertThat(ArithmeticUtil.isLessThan(10, 11)).isTrue();
            assertThat(ArithmeticUtil.isLessThan(10, 10)).isFalse();
            assertThat(ArithmeticUtil.isLessThan(11, 10)).isFalse();
            assertThat(ArithmeticUtil.isLessThan(null, 0.1)).isTrue();
        }

        @Test
        void isLessThanOrEqualShouldCompareValues() {
            assertThat(ArithmeticUtil.isLessThanOrEqual(10, 11)).isTrue();
            assertThat(ArithmeticUtil.isLessThanOrEqual(10, 10.0)).isTrue();
            assertThat(ArithmeticUtil.isLessThanOrEqual(11, 10)).isFalse();
            assertThat(ArithmeticUtil.isLessThanOrEqual(0.0, null)).isTrue();
        }
    }

    @Nested
    class ValueCheckTests {
        @Test
        void isZeroShouldWorkForAllTypes() {
            assertThat(ArithmeticUtil.isZero(0)).isTrue();
            assertThat(ArithmeticUtil.isZero(0.0f)).isTrue();
            assertThat(ArithmeticUtil.isZero(new BigDecimal("0.00"))).isTrue();
            assertThat(ArithmeticUtil.isZero(null)).isTrue();
            assertThat(ArithmeticUtil.isZero(1)).isFalse();
        }

        @Test
        void isPositiveShouldBeTrueForValuesGreaterThanZero() {
            assertThat(ArithmeticUtil.isPositive(1)).isTrue();
            assertThat(ArithmeticUtil.isPositive(0.001)).isTrue();
            assertThat(ArithmeticUtil.isPositive(0)).isFalse();
            assertThat(ArithmeticUtil.isPositive(null)).isFalse();
            assertThat(ArithmeticUtil.isPositive(-1)).isFalse();
        }

        @Test
        void isNegativeShouldBeTrueForValuesLessThanZero() {
            assertThat(ArithmeticUtil.isNegative(-1)).isTrue();
            assertThat(ArithmeticUtil.isNegative(-0.001)).isTrue();
            assertThat(ArithmeticUtil.isNegative(0)).isFalse();
            assertThat(ArithmeticUtil.isNegative(null)).isFalse();
            assertThat(ArithmeticUtil.isNegative(1)).isFalse();
        }
    }

    @Nested
    class TypeCheckTests {
        @Test
        void isIntegerShouldIdentifyIntegerValues() {
            assertThat(ArithmeticUtil.isInteger(123)).isTrue();
            assertThat(ArithmeticUtil.isInteger(123L)).isTrue();
            assertThat(ArithmeticUtil.isInteger(new BigInteger("123"))).isTrue();
            assertThat(ArithmeticUtil.isInteger(123.0)).isTrue();
            assertThat(ArithmeticUtil.isInteger(new BigDecimal("123.00"))).isTrue();
            assertThat(ArithmeticUtil.isInteger(null)).isTrue();
            assertThat(ArithmeticUtil.isInteger(0)).isTrue();
        }

        @Test
        void isIntegerShouldReturnFalseForDecimals() {
            assertThat(ArithmeticUtil.isInteger(123.01)).isFalse();
            assertThat(ArithmeticUtil.isInteger(new BigDecimal("0.0001"))).isFalse();
        }

        @Test
        void isDecimalShouldIdentifyDecimalValues() {
            assertThat(ArithmeticUtil.isDecimal(123.45)).isTrue();
            assertThat(ArithmeticUtil.isDecimal(new BigDecimal("123.001"))).isTrue();
        }

        @Test
        void isDecimalShouldReturnFalseForIntegers() {
            assertThat(ArithmeticUtil.isDecimal(123)).isFalse();
            assertThat(ArithmeticUtil.isDecimal(123.0f)).isFalse();
            assertThat(ArithmeticUtil.isDecimal(new BigDecimal("123.000"))).isFalse();
            assertThat(ArithmeticUtil.isDecimal(null)).isFalse();
        }
    }

    @Nested
    class MinMaxTests {
        @Test
        void minShouldReturnTheSmallestValue() {
            assertThat(ArithmeticUtil.min(10, 20, 30)).isEqualByComparingTo("10");
            assertThat(ArithmeticUtil.min(-10, 0, 10)).isEqualByComparingTo("-10");
            assertThat(ArithmeticUtil.min(10.5, 10.4, 10.6)).isEqualByComparingTo("10.4");
            assertThat(ArithmeticUtil.min(10, null, 20)).isEqualByComparingTo("10");
            assertThat(ArithmeticUtil.min(10)).isEqualByComparingTo("10");
        }

        @Test
        void minShouldReturnZeroForNullOrEmpty() {
            assertThat(ArithmeticUtil.min()).isEqualByComparingTo(BigDecimal.ZERO);
            assertThat(ArithmeticUtil.min((Number) null)).isEqualByComparingTo(BigDecimal.ZERO);
            assertThat(ArithmeticUtil.min(null, null)).isEqualByComparingTo(BigDecimal.ZERO);
        }

        @Test
        void maxShouldReturnTheLargestValue() {
            assertThat(ArithmeticUtil.max(10, 20, 30)).isEqualByComparingTo("30");
            assertThat(ArithmeticUtil.max(-10, 0, 10)).isEqualByComparingTo("10");
            assertThat(ArithmeticUtil.max(10.5, 10.4, 10.6)).isEqualByComparingTo("10.6");
            assertThat(ArithmeticUtil.max(10, null, 20)).isEqualByComparingTo("20");
            assertThat(ArithmeticUtil.max(10)).isEqualByComparingTo("10");
        }

        @Test
        void maxShouldReturnZeroForNullOrEmpty() {
            assertThat(ArithmeticUtil.max()).isEqualByComparingTo(BigDecimal.ZERO);
            assertThat(ArithmeticUtil.max((Number) null)).isEqualByComparingTo(BigDecimal.ZERO);
            assertThat(ArithmeticUtil.max(null, null)).isEqualByComparingTo(BigDecimal.ZERO);
        }
    }
}
