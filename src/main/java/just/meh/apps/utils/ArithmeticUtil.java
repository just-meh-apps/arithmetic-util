// Copyright (c) 2026 just.meh.apps@gmail.com
// SPDX-License-Identifier: MIT

package just.meh.apps.utils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Objects;

/**
 * <p>A utility class for performing arithmetic operations with high precision, using {@link BigDecimal}.
 * This class is designed to handle various number types safely and prevent common floating-point errors.</p>
 * <p>정밀한 산술 연산을 위한 유틸리티 클래스입니다. {@link BigDecimal}을 사용하여 부동소수점 오류 없이 안전하게 숫자를 다룰 수 있도록 설계되었습니다.</p>
 *
 * @author just.meh.apps@gmail.com
 * @version 1.0
 * @since 1.0
 */
public final class ArithmeticUtil {
    
    /**
     * Private constructor to prevent instantiation of this utility class.
     * <p>
     * 이 유틸리티 클래스의 인스턴스화를 방지하기 위한 private 생성자입니다.
     */
    private ArithmeticUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
    
    /**
     * <p>Converts a {@link Number} to a {@link BigDecimal}.
     * If the input number is {@code null}, it returns {@link BigDecimal#ZERO}.
     * It handles {@link BigDecimal}, {@link BigInteger}, {@link Double}, {@link Float}, and other {@link Number} types.</p>
     * <p>{@link Number}를 {@link BigDecimal}로 변환합니다.
     * 입력된 숫자가 {@code null}이면 {@link BigDecimal#ZERO}를 반환합니다.
     * {@link BigDecimal}, {@link BigInteger}, {@link Double}, {@link Float} 등 다양한 {@link Number} 타입을 처리합니다.</p>
     *
     * @param number The number to convert. Can be {@code null}.
     *               <br>변환할 숫자. {@code null}일 수 있습니다.
     * @return The converted {@link BigDecimal}, or {@link BigDecimal#ZERO} if the input is {@code null}.
     *         <br>변환된 {@link BigDecimal}. 입력이 {@code null}이면 {@link BigDecimal#ZERO}를 반환합니다.
     * @see #toString(Number, boolean)
     * @see #add(Number, Number)
     * @see #subtract(Number, Number)
     * @see #multiply(Number, Number)
     * @see #divide(Number, Number)
     *
     * @example
     * <pre>{@code
     * ArithmeticUtil.toBigDecimal(123.45);        // returns new BigDecimal("123.45")
     * ArithmeticUtil.toBigDecimal("123.45");      // returns new BigDecimal("123.45")
     * ArithmeticUtil.toBigDecimal(null);          // returns BigDecimal.ZERO
     * }</pre>
     */
    public static BigDecimal toBigDecimal(Number number) {
        // 입력된 숫자가 null이면 BigDecimal.ZERO를 반환하여 NullPointerException을 방지합니다.
        if (number == null) {
            return BigDecimal.ZERO;
        }
        // 이미 BigDecimal 타입인 경우, 그대로 반환하여 불필요한 객체 생성을 피합니다.
        if (number instanceof BigDecimal) {
            return (BigDecimal) number;
        }
        // BigInteger인 경우, BigDecimal로 변환합니다.
        if (number instanceof BigInteger) {
            return new BigDecimal((BigInteger) number);
        }
        // Double이나 Float의 경우, 부동소수점 오류를 피하기 위해 문자열로 변환한 후 BigDecimal을 생성합니다.
        // 예를 들어, new BigDecimal(0.1)은 실제로는 0.1000000000000000055...와 같은 값을 가지지만,
        // new BigDecimal("0.1")은 정확히 0.1을 나타냅니다.
        if (number instanceof Double || number instanceof Float) {
            return new BigDecimal(number.toString());
        }
        // 그 외 Long, Integer, Short, Byte 타입의 숫자는 long 값으로 변환하여 처리합니다.
        return BigDecimal.valueOf(number.longValue());
    }

    /**
     * <p>Converts a {@link Number} to a plain string representation.
     * If {@code stripTrailingZeros} is {@code true}, it removes trailing zeros from the fractional part.
     * This method avoids scientific notation.</p>
     * <p>{@link Number}를 일반 숫자 형식의 문자열로 변환합니다.
     * {@code stripTrailingZeros}가 {@code true}이면, 소수 부분의 끝에 있는 불필요한 0들을 제거합니다.
     * 이 메소드는 지수 표기법(scientific notation)을 사용하지 않습니다.</p>
     *
     * @param number              The number to convert. Can be {@code null}.
     *                            <br>변환할 숫자. {@code null}일 수 있습니다.
     * @param stripTrailingZeros  If {@code true}, removes trailing zeros. For example, "10.00" becomes "10".
     *                            <br>{@code true}이면, 끝에 있는 0을 제거합니다. 예: "10.00" -> "10".
     * @return The plain string representation of the number. Returns "0" if the input is {@code null}.
     *         <br>숫자의 일반 문자열 표현. 입력이 {@code null}이면 "0"을 반환합니다.
     *
     * @example
     * <pre>{@code
     * ArithmeticUtil.toString(new BigDecimal("123.4500"), true);  // "123.45"
     * ArithmeticUtil.toString(new BigDecimal("123.00"), true);   // "123"
     * ArithmeticUtil.toString(new BigDecimal("123.4500"), false); // "123.4500"
     * ArithmeticUtil.toString(1.23E+5, true);                  // "123000"
     * ArithmeticUtil.toString(1.23E-5, false);                 // "0.0000123"
     * }</pre>
     */
    public static String toString(Number number, boolean stripTrailingZeros) {
        // toBigDecimal을 통해 입력값을 안전하게 BigDecimal로 변환합니다. null은 ZERO가 됩니다.
        BigDecimal bd = toBigDecimal(number);
        // stripTrailingZeros가 true이면, 소수점 끝의 불필요한 0들을 제거합니다.
        // 예를 들어, 123.4500은 123.45가 되고, 123.00은 123이 됩니다.
        if (stripTrailingZeros) {
            bd = bd.stripTrailingZeros();
        }
        // toPlainString()을 사용하여 지수 표기법(e.g., 1.23E+5)이 아닌 일반 숫자 문자열(e.g., "123000")로 변환합니다.
        return bd.toPlainString();
    }

    /**
     * <p>Converts a {@link Number} to a plain string, with trailing zeros stripped.
     * This is a convenience method equivalent to {@code toString(number, true)}.</p>
     * <p>{@link Number}를 끝에 있는 0들이 제거된 일반 문자열로 변환합니다.
     * 이 메소드는 {@code toString(number, true)}와 동일하게 동작하는 편의 메소드입니다.</p>
     *
     * @param number The number to convert. Can be {@code null}.
     *               <br>변환할 숫자. {@code null}일 수 있습니다.
     * @return The plain string representation of the number with trailing zeros stripped.
     *         <br>끝에 있는 0들이 제거된 숫자의 일반 문자열 표현.
     * @see #toString(Number, boolean)
     *
     * @example
     * <pre>{@code
     * ArithmeticUtil.toString(new BigDecimal("10.00")); // "10"
     * ArithmeticUtil.toString(1.23E+2);              // "123"
     * ArithmeticUtil.toString(null);                 // "0"
     * }</pre>
     */
    public static String toString(Number number) {
        return toString(number, true);
    }

    /**
     * <p>Adds two numbers. Handles {@code null} inputs by treating them as zero.</p>
     * <p>두 숫자를 더합니다. {@code null} 입력은 0으로 처리됩니다.</p>
     *
     * @param a The first number. Can be {@code null}.
     *          <br>첫 번째 숫자. {@code null}일 수 있습니다.
     * @param b The second number. Can be {@code null}.
     *          <br>두 번째 숫자. {@code null}일 수 있습니다.
     * @return The sum of the two numbers as a {@link BigDecimal}.
     *         <br>두 숫자의 합을 담은 {@link BigDecimal}.
     *
     * @example
     * <pre>{@code
     * ArithmeticUtil.add(10, 20);       // 30
     * ArithmeticUtil.add(1.23, 4.56);   // 5.79
     * ArithmeticUtil.add(10, null);     // 10
     * ArithmeticUtil.add(null, 20);     // 20
     * }</pre>
     */
    public static BigDecimal add(Number a, Number b) {
        // 입력된 두 숫자를 안전하게 BigDecimal로 변환합니다.
        BigDecimal bdA = toBigDecimal(a);
        BigDecimal bdB = toBigDecimal(b);
        // BigDecimal의 add 메소드를 사용하여 덧셈을 수행합니다.
        return bdA.add(bdB);
    }

    /**
     * <p>Subtracts the second number from the first. Handles {@code null} inputs by treating them as zero.</p>
     * <p>첫 번째 숫자에서 두 번째 숫자를 뺍니다. {@code null} 입력은 0으로 처리됩니다.</p>
     *
     * @param a The number to be subtracted from (minuend). Can be {@code null}.
     *          <br>뺄셈을 당하는 수 (피감수). {@code null}일 수 있습니다.
     * @param b The number to subtract (subtrahend). Can be {@code null}.
     *          <br>빼는 수 (감수). {@code null}일 수 있습니다.
     * @return The difference between the two numbers as a {@link BigDecimal}.
     *         <br>두 숫자의 차를 담은 {@link BigDecimal}.
     *
     * @example
     * <pre>{@code
     * ArithmeticUtil.subtract(30, 20);   // 10
     * ArithmeticUtil.subtract(5.79, 4.56); // 1.23
     * ArithmeticUtil.subtract(10, null);   // 10
     * ArithmeticUtil.subtract(null, 20);   // -20
     * }</pre>
     */
    public static BigDecimal subtract(Number a, Number b) {
        // 입력된 두 숫자를 안전하게 BigDecimal로 변환합니다.
        BigDecimal bdA = toBigDecimal(a);
        BigDecimal bdB = toBigDecimal(b);
        // BigDecimal의 subtract 메소드를 사용하여 뺄셈을 수행합니다.
        return bdA.subtract(bdB);
    }

    /**
     * <p>Multiplies two numbers. If either input is {@code null}, the result is zero.</p>
     * <p>두 숫자를 곱합니다. 입력 중 하나라도 {@code null}이면 결과는 0이 됩니다.</p>
     *
     * @param a The first number. Can be {@code null}.
     *          <br>첫 번째 숫자. {@code null}일 수 있습니다.
     * @param b The second number. Can be {@code null}.
     *          <br>두 번째 숫자. {@code null}일 수 있습니다.
     * @return The product of the two numbers as a {@link BigDecimal}.
     *         <br>두 숫자의 곱을 담은 {@link BigDecimal}.
     *
     * @example
     * <pre>{@code
     * ArithmeticUtil.multiply(10, 20);     // 200
     * ArithmeticUtil.multiply(1.5, 2);     // 3.0
     * ArithmeticUtil.multiply(10, null);   // 0
     * ArithmeticUtil.multiply(null, 20);   // 0
     * }</pre>
     */
    public static BigDecimal multiply(Number a, Number b) {
        // 곱셈에서는 null을 0으로 처리하는 것이 수학적으로 자연스럽습니다.
        // toBigDecimal은 null을 0으로 변환하므로, 추가적인 null 체크 없이 바로 변환하여 사용합니다.
        BigDecimal bdA = toBigDecimal(a);
        BigDecimal bdB = toBigDecimal(b);
        // BigDecimal의 multiply 메소드를 사용하여 곱셈을 수행합니다.
        return bdA.multiply(bdB);
    }
    
    /**
     * <p>Multiplies two numbers and rounds the result to a specified scale.</p>
     * <p>두 숫자를 곱하고 지정된 소수점 자릿수로 결과를 반올림합니다.</p>
     *
     * @param a The first number. Can be {@code null}.
     *          <br>첫 번째 숫자. {@code null}일 수 있습니다.
     * @param b The second number. Can be {@code null}.
     *          <br>두 번째 숫자. {@code null}일 수 있습니다.
     * @param scale The number of digits to the right of the decimal point.
     *              <br>결과의 소수점 이하 자릿수.
     * @param roundingMode The rounding mode to apply.
     *                     <br>적용할 반올림 모드.
     * @return The rounded product as a {@link BigDecimal}.
     *         <br>반올림된 곱셈 결과를 담은 {@link BigDecimal}.
     *
     * @example
     * <pre>{@code
     * ArithmeticUtil.multiply(1.234, 2.345, 2, RoundingMode.HALF_UP); // 2.89
     * ArithmeticUtil.multiply(1.1, 1.1, Scale.INTEGER, RoundingMode.HALF_UP); // 1
     * }</pre>
     */
    public static BigDecimal multiply(Number a, Number b, int scale, RoundingMode roundingMode) {
        BigDecimal product = multiply(a, b);
        // 곱셈 결과에 대해 setScale을 사용하여 지정된 소수점 자릿수와 반올림 모드를 적용합니다.
        return product.setScale(scale, roundingMode);
    }

    /**
     * <p>Multiplies two numbers and rounds the result to a specified scale.</p>
     * <p>두 숫자를 곱하고 지정된 소수점 자릿수로 결과를 반올림합니다.</p>
     *
     * @param a The first number. Can be {@code null}.
     *          <br>첫 번째 숫자. {@code null}일 수 있습니다.
     * @param b The second number. Can be {@code null}.
     *          <br>두 번째 숫자. {@code null}일 수 있습니다.
     * @param scale The {@link Scale} constant defining the number of digits.
     *              <br>소수점 자릿수를 정의하는 {@link Scale} 상수.
     * @param roundingMode The rounding mode to apply.
     *                     <br>적용할 반올림 모드.
     * @return The rounded product as a {@link BigDecimal}.
     *         <br>반올림된 곱셈 결과를 담은 {@link BigDecimal}.
     */
    public static BigDecimal multiply(Number a, Number b, Scale scale, RoundingMode roundingMode) {
        return multiply(a, b, scale.getValue(), roundingMode);
    }

    /**
     * <p>Divides the first number by the second, using the default scale (32) and {@link RoundingMode#HALF_UP}.</p>
     * <p>첫 번째 숫자를 두 번째 숫자로 나눕니다. 기본 소수점 자릿수(32)와 {@link RoundingMode#HALF_UP} 반올림 모드를 사용합니다.</p>
     *
     * @param dividend The number to be divided. Can be {@code null}.
     *                 <br>나누어지는 수 (피제수). {@code null}일 수 있습니다.
     * @param divisor The number to divide by. Can be {@code null}.
     *                <br>나누는 수 (제수). {@code null}일 수 있습니다.
     * @return The quotient as a {@link BigDecimal}.
     *         <br>나눗셈의 몫을 담은 {@link BigDecimal}.
     * @throws ArithmeticException if the divisor is zero.
     *                             <br>제수가 0인 경우.
     *
     * @example
     * <pre>{@code
     * // The result will have a scale of 32.
     * ArithmeticUtil.divide(10, 3); // 3.33333333333333333333333333333333
     *
     * // Throws ArithmeticException
     * ArithmeticUtil.divide(10, 0);
     * }</pre>
     */
    public static BigDecimal divide(Number dividend, Number divisor) {
        // 정밀도가 필요한 나눗셈을 위해 기본 스케일과 반올림 모드를 사용하여 다른 divide 메소드를 호출합니다.
        return divide(dividend, divisor, Scale.DEFAULT.getValue(), RoundingMode.HALF_UP);
    }
    
    /**
     * <p>Divides the first number by the second, with a specified scale and rounding mode.</p>
     * <p>첫 번째 숫자를 두 번째 숫자로 나누고, 지정된 소수점 자릿수와 반올림 모드를 적용합니다.</p>
     *
     * @param dividend The number to be divided. Can be {@code null}.
     *                 <br>나누어지는 수 (피제수). {@code null}일 수 있습니다.
     * @param divisor The number to divide by. Can be {@code null}.
     *                <br>나누는 수 (제수). {@code null}일 수 있습니다.
     * @param scale The number of digits to the right of the decimal point.
     *              <br>결과의 소수점 이하 자릿수.
     * @param roundingMode The rounding mode to apply.
     *                     <br>적용할 반올림 모드.
     * @return The quotient as a {@link BigDecimal}.
     *         <br>나눗셈의 몫을 담은 {@link BigDecimal}.
     * @throws ArithmeticException if the divisor is zero.
     *                             <br>제수가 0인 경우.
     *
     * @example
     * <pre>{@code
     * ArithmeticUtil.divide(10, 3, 2, RoundingMode.HALF_UP);   // 3.33
     * ArithmeticUtil.divide(20, 3, 0, RoundingMode.HALF_UP);   // 7
     * ArithmeticUtil.divide(10, 8, Scale.CURRENCY, RoundingMode.HALF_UP); // 1.25
     * ArithmeticUtil.divide(0, 10); // 0
     * }</pre>
     */
    public static BigDecimal divide(Number dividend, Number divisor, int scale, RoundingMode roundingMode) {
        // 제수가 0인지 확인하는 것이 나눗셈에서 가장 중요합니다.
        // isZero 메소드를 사용하여 제수가 null이거나 0인 경우를 모두 확인합니다.
        if (isZero(divisor)) {
            // 제수가 0이면 ArithmeticException을 발생시킵니다.
            throw new ArithmeticException("Division by zero");
        }
        // 피제수가 0이면, 제수에 상관없이 결과는 항상 0입니다.
        if(isZero(dividend)) {
            return BigDecimal.ZERO;
        }

        // 입력값들을 안전하게 BigDecimal로 변환합니다.
        BigDecimal bdDividend = toBigDecimal(dividend);
        BigDecimal bdDivisor = toBigDecimal(divisor);

        // BigDecimal의 divide 메소드를 사용하여 나눗셈을 수행합니다.
        return bdDividend.divide(bdDivisor, scale, roundingMode);
    }
    
    /**
     * <p>Divides the first number by the second, with a specified scale and rounding mode.</p>
     * <p>첫 번째 숫자를 두 번째 숫자로 나누고, 지정된 소수점 자릿수와 반올림 모드를 적용합니다.</p>
     *
     * @param dividend The number to be divided. Can be {@code null}.
     *                 <br>나누어지는 수 (피제수). {@code null}일 수 있습니다.
     * @param divisor The number to divide by. Can be {@code null}.
     *                <br>나누는 수 (제수). {@code null}일 수 있습니다.
     * @param scale The {@link Scale} constant defining the number of digits.
     *              <br>소수점 자릿수를 정의하는 {@link Scale} 상수.
     * @param roundingMode The rounding mode to apply.
     *                     <br>적용할 반올림 모드.
     * @return The quotient as a {@link BigDecimal}.
     *         <br>나눗셈의 몫을 담은 {@link BigDecimal}.
     * @throws ArithmeticException if the divisor is zero.
     *                             <br>제수가 0인 경우.
     */
    public static BigDecimal divide(Number dividend, Number divisor, Scale scale, RoundingMode roundingMode) {
        return divide(dividend, divisor, scale.getValue(), roundingMode);
    }

    /**
     * <p>Rounds a number to a specified scale using {@link RoundingMode#HALF_UP}.</p>
     * <p>{@link RoundingMode#HALF_UP}(반올림)을 사용하여 숫자를 지정된 소수점 자릿수로 반올림합니다.</p>
     *
     * @param number The number to round. Can be {@code null}.
     *               <br>반올림할 숫자. {@code null}일 수 있습니다.
     * @param scale The number of digits to the right of the decimal point.
     *              <br>소수점 이하 자릿수.
     * @return The rounded number as a {@link BigDecimal}.
     *         <br>반올림된 숫자를 담은 {@link BigDecimal}.
     *
     * @example
     * <pre>{@code
     * ArithmeticUtil.round(1.235, 2);  // "1.24"
     * ArithmeticUtil.round(1.234, 2);  // "1.23"
     * ArithmeticUtil.round(null, 2);   // "0.00"
     * }</pre>
     */
    public static BigDecimal round(Number number, int scale) {
        // 입력값을 BigDecimal로 변환하고, setScale을 이용해 지정된 자릿수로 반올림합니다.
        return toBigDecimal(number).setScale(scale, RoundingMode.HALF_UP);
    }
    
    /**
     * <p>Rounds a number to a specified {@link Scale} using {@link RoundingMode#HALF_UP}.</p>
     * <p>{@link RoundingMode#HALF_UP}(반올림)을 사용하여 숫자를 지정된 {@link Scale}로 반올림합니다.</p>
     *
     * @param number The number to round. Can be {@code null}.
     *               <br>반올림할 숫자. {@code null}일 수 있습니다.
     * @param scale The {@link Scale} constant.
     *              <br>{@link Scale} 상수.
     * @return The rounded number as a {@link BigDecimal}.
     *         <br>반올림된 숫자를 담은 {@link BigDecimal}.
     */
    public static BigDecimal round(Number number, Scale scale) {
        return round(number, scale.getValue());
    }

    /**
     * <p>Rounds a number up to a specified scale (ceiling).</p>
     * <p>숫자를 지정된 소수점 자릿수로 올림(ceiling)합니다.</p>
     *
     * @param number The number to round up. Can be {@code null}.
     *               <br>올림할 숫자. {@code null}일 수 있습니다.
     * @param scale The number of digits to the right of the decimal point.
     *              <br>소수점 이하 자릿수.
     * @return The rounded up number as a {@link BigDecimal}.
     *         <br>올림 처리된 숫자를 담은 {@link BigDecimal}.
     *
     * @example
     * <pre>{@code
     * ArithmeticUtil.ceil(1.234, 2);  // "1.24"
     * ArithmeticUtil.ceil(-1.239, 2); // "-1.23"
     * }</pre>
     */
    public static BigDecimal ceil(Number number, int scale) {
        // setScale과 RoundingMode.CEILING을 사용하여 올림을 수행합니다.
        return toBigDecimal(number).setScale(scale, RoundingMode.CEILING);
    }
    
    /**
     * <p>Rounds a number up to a specified {@link Scale} (ceiling).</p>
     * <p>숫자를 지정된 {@link Scale}로 올림(ceiling)합니다.</p>
     *
     * @param number The number to round up. Can be {@code null}.
     *               <br>올림할 숫자. {@code null}일 수 있습니다.
     * @param scale The {@link Scale} constant.
     *              <br>{@link Scale} 상수.
     * @return The rounded up number as a {@link BigDecimal}.
     *         <br>올림 처리된 숫자를 담은 {@link BigDecimal}.
     */
    public static BigDecimal ceil(Number number, Scale scale) {
        return ceil(number, scale.getValue());
    }

    /**
     * <p>Rounds a number down to a specified scale (floor).</p>
     * <p>숫자를 지정된 소수점 자릿수로 내림(floor)합니다.</p>
     *
     * @param number The number to round down. Can be {@code null}.
     *               <br>내림할 숫자. {@code null}일 수 있습니다.
     * @param scale The number of digits to the right of the decimal point.
     *              <br>소수점 이하 자릿수.
     * @return The rounded down number as a {@link BigDecimal}.
     *         <br>내림 처리된 숫자를 담은 {@link BigDecimal}.
     *
     * @example
     * <pre>{@code
     * ArithmeticUtil.floor(1.239, 2);  // "1.23"
     * ArithmeticUtil.floor(-1.234, 2); // "-1.24"
     * }</pre>
     */
    public static BigDecimal floor(Number number, int scale) {
        // setScale과 RoundingMode.FLOOR를 사용하여 내림을 수행합니다.
        return toBigDecimal(number).setScale(scale, RoundingMode.FLOOR);
    }
    
    /**
     * <p>Rounds a number down to a specified {@link Scale} (floor).</p>
     * <p>숫자를 지정된 {@link Scale}로 내림(floor)합니다.</p>
     *
     * @param number The number to round down. Can be {@code null}.
     *               <br>내림할 숫자. {@code null}일 수 있습니다.
     * @param scale The {@link Scale} constant.
     *              <br>{@link Scale} 상수.
     * @return The rounded down number as a {@link BigDecimal}.
     *         <br>내림 처리된 숫자를 담은 {@link BigDecimal}.
     */
    public static BigDecimal floor(Number number, Scale scale) {
        return floor(number, scale.getValue());
    }
    
    /**
     * <p>Returns the absolute value of a number.</p>
     * <p>숫자의 절대값을 반환합니다.</p>
     *
     * @param number The number. Can be {@code null}.
     *               <br>숫자. {@code null}일 수 있습니다.
     * @return The absolute value as a {@link BigDecimal}.
     *         <br>절대값을 담은 {@link BigDecimal}.
     *
     * @example
     * <pre>{@code
     * ArithmeticUtil.abs(-10);     // 10
     * ArithmeticUtil.abs(null);    // 0
     * }</pre>
     */
    public static BigDecimal abs(Number number) {
        // BigDecimal의 abs() 메소드를 사용하여 절대값을 구합니다.
        return toBigDecimal(number).abs();
    }

    /**
     * <p>Returns the negated value of a number.</p>
     * <p>숫자의 부호를 반전시킨 값을 반환합니다.</p>
     *
     * @param number The number. Can be {@code null}.
     *               <br>숫자. {@code null}일 수 있습니다.
     * @return The negated value as a {@link BigDecimal}.
     *         <br>부호가 반전된 값을 담은 {@link BigDecimal}.
     *
     * @example
     * <pre>{@code
     * ArithmeticUtil.negate(10);  // -10
     * ArithmeticUtil.negate(-10); // 10
     * ArithmeticUtil.negate(null); // 0
     * }</pre>
     */
    public static BigDecimal negate(Number number) {
        // BigDecimal의 negate() 메소드를 사용하여 부호를 반전시킵니다.
        return toBigDecimal(number).negate();
    }
    
    /**
     * <p>Finds the minimum value among the given numbers. {@code null} values are ignored.</p>
     * <p>주어진 숫자들 중에서 최소값을 찾습니다. {@code null} 값은 무시됩니다.</p>
     *
     * @param numbers The numbers to compare.
     *                <br>비교할 숫자들.
     * @return The minimum value, or {@link BigDecimal#ZERO} if no valid numbers are provided.
     *         <br>최소값. 유효한 숫자가 없으면 {@link BigDecimal#ZERO}를 반환합니다.
     *
     * @example
     * <pre>{@code
     * ArithmeticUtil.min(10, 20, -5, 15.5); // -5
     * ArithmeticUtil.min(10, null, 20);     // 10
     * ArithmeticUtil.min();                 // 0
     * }</pre>
     */
    public static BigDecimal min(Number... numbers) {
        // numbers 배열이 null이거나 비어있으면 0을 반환합니다.
        if (numbers == null || numbers.length == 0) {
            return BigDecimal.ZERO;
        }
        // Java Stream API를 사용하여 최소값을 효율적으로 찾습니다.
        return Arrays.stream(numbers)
                .filter(Objects::nonNull) // 스트림에서 null 값을 제거합니다.
                .map(ArithmeticUtil::toBigDecimal) // 각 숫자를 BigDecimal로 변환합니다.
                .min(BigDecimal::compareTo) // BigDecimal의 compareTo 메소드를 기준으로 최소값을 찾습니다.
                .orElse(BigDecimal.ZERO); // 스트림이 비어있을 경우(모든 요소가 null인 경우) 0을 반환합니다.
    }

    /**
     * <p>Finds the maximum value among the given numbers. {@code null} values are ignored.</p>
     * <p>주어진 숫자들 중에서 최대값을 찾습니다. {@code null} 값은 무시됩니다.</p>
     *
     * @param numbers The numbers to compare.
     *                <br>비교할 숫자들.
     * @return The maximum value, or {@link BigDecimal#ZERO} if no valid numbers are provided.
     *         <br>최대값. 유효한 숫자가 없으면 {@link BigDecimal#ZERO}를 반환합니다.
     *
     * @example
     * <pre>{@code
     * ArithmeticUtil.max(10, 20, -5, 15.5); // 20
     * ArithmeticUtil.max(10, null, 20);     // 20
     * ArithmeticUtil.max();                 // 0
     * }</pre>
     */
    public static BigDecimal max(Number... numbers) {
        // numbers 배열이 null이거나 비어있으면 0을 반환합니다.
        if (numbers == null || numbers.length == 0) {
            return BigDecimal.ZERO;
        }
        // Java Stream API를 사용하여 최대값을 효율적으로 찾습니다.
        return Arrays.stream(numbers)
                .filter(Objects::nonNull) // 스트림에서 null 값을 제거합니다.
                .map(ArithmeticUtil::toBigDecimal) // 각 숫자를 BigDecimal로 변환합니다.
                .max(BigDecimal::compareTo) // BigDecimal의 compareTo 메소드를 기준으로 최대값을 찾습니다.
                .orElse(BigDecimal.ZERO); // 스트림이 비어있을 경우(모든 요소가 null인 경우) 0을 반환합니다.
    }
    
    /**
     * <p>Checks if a number is equal to zero. A {@code null} input is considered zero.</p>
     * <p>숫자가 0과 같은지 확인합니다. {@code null} 입력은 0으로 간주됩니다.</p>
     *
     * @param number The number to check. Can be {@code null}.
     *               <br>확인할 숫자. {@code null}일 수 있습니다.
     * @return {@code true} if the number is zero or {@code null}, otherwise {@code false}.
     *         <br>숫자가 0이거나 {@code null}이면 {@code true}, 그렇지 않으면 {@code false}.
     *
     * @example
     * <pre>{@code
     * ArithmeticUtil.isZero(0);              // true
     * ArithmeticUtil.isZero(new BigDecimal("0.00")); // true
     * ArithmeticUtil.isZero(null);           // true
     * ArithmeticUtil.isZero(1);              // false
     * }</pre>
     */
    public static boolean isZero(Number number) {
        // null은 0으로 간주합니다.
        if (number == null) {
            return true;
        }
        // BigDecimal의 compareTo 메소드는 값이 같으면 0을 반환합니다.
        // 이를 통해 0.0, 0.00 등 표현이 다른 0들도 모두 0으로 인식할 수 있습니다.
        return toBigDecimal(number).compareTo(BigDecimal.ZERO) == 0;
    }
    
    /**
     * <p>Checks if a number is positive (greater than zero).</p>
     * <p>숫자가 양수(0보다 큼)인지 확인합니다.</p>
     *
     * @param number The number to check. Can be {@code null}.
     *               <br>확인할 숫자. {@code null}일 수 있습니다.
     * @return {@code true} if the number is positive, otherwise {@code false}.
     *         <br>숫자가 양수이면 {@code true}, 그렇지 않으면 {@code false}.
     *
     * @example
     * <pre>{@code
     * ArithmeticUtil.isPositive(1);      // true
     * ArithmeticUtil.isPositive(0.001);  // true
     * ArithmeticUtil.isPositive(0);      // false
     * ArithmeticUtil.isPositive(null);   // false
     * ArithmeticUtil.isPositive(-1);     // false
     * }</pre>
     */
    public static boolean isPositive(Number number) {
        // null은 0으로 간주되므로 양수가 아닙니다.
        if (number == null) return false;
        // signum() 메소드는 양수일 때 1, 0일 때 0, 음수일 때 -1을 반환합니다.
        return toBigDecimal(number).signum() > 0;
    }

    /**
     * <p>Checks if a number is negative (less than zero).</p>
     * <p>숫자가 음수(0보다 작음)인지 확인합니다.</p>
     *
     * @param number The number to check. Can be {@code null}.
     *               <br>확인할 숫자. {@code null}일 수 있습니다.
     * @return {@code true} if the number is negative, otherwise {@code false}.
     *         <br>숫자가 음수이면 {@code true}, 그렇지 않으면 {@code false}.
     *
     * @example
     * <pre>{@code
     * ArithmeticUtil.isNegative(-1);     // true
     * ArithmeticUtil.isNegative(-0.001); // true
     * ArithmeticUtil.isNegative(0);      // false
     * ArithmeticUtil.isNegative(null);   // false
     * ArithmeticUtil.isNegative(1);      // false
     * }</pre>
     */
    public static boolean isNegative(Number number) {
        // null은 0으로 간주되므로 음수가 아닙니다.
        if (number == null) return false;
        // signum() 메소드가 -1을 반환하는 경우 음수입니다.
        return toBigDecimal(number).signum() < 0;
    }
    
    /**
     * <p>Compares two numbers for equality. {@code null} is treated as zero.</p>
     * <p>두 숫자가 같은 값인지 비교합니다. {@code null}은 0으로 처리됩니다.</p>
     *
     * @param a The first number. Can be {@code null}.
     *          <br>첫 번째 숫자. {@code null}일 수 있습니다.
     * @param b The second number. Can be {@code null}.
     *          <br>두 번째 숫자. {@code null}일 수 있습니다.
     * @return {@code true} if the numbers are numerically equal, otherwise {@code false}.
     *         <br>두 숫자의 값이 같으면 {@code true}, 그렇지 않으면 {@code false}.
     *
     * @example
     * <pre>{@code
     * ArithmeticUtil.isEqual(10, 10.0);       // true
     * ArithmeticUtil.isEqual(null, 0);        // true
     * ArithmeticUtil.isEqual(10, 11);         // false
     * }</pre>
     */
    public static boolean isEqual(Number a, Number b) {
        // compareTo는 두 BigDecimal의 값이 같으면 0을 반환합니다.
        // toBigDecimal을 통해 null과 다른 숫자 타입을 일관되게 비교합니다.
        return toBigDecimal(a).compareTo(toBigDecimal(b)) == 0;
    }

    /**
     * <p>Checks if the first number is greater than the second. {@code null} is treated as zero.</p>
     * <p>첫 번째 숫자가 두 번째 숫자보다 큰지 확인합니다. {@code null}은 0으로 처리됩니다.</p>
     *
     * @param a The first number. Can be {@code null}.
     *          <br>첫 번째 숫자. {@code null}일 수 있습니다.
     * @param b The second number. Can be {@code null}.
     *          <br>두 번째 숫자. {@code null}일 수 있습니다.
     * @return {@code true} if the first number is greater than the second, otherwise {@code false}.
     *         <br>첫 번째 숫자가 더 크면 {@code true}, 그렇지 않으면 {@code false}.
     *
     * @example
     * <pre>{@code
     * ArithmeticUtil.isGreaterThan(11, 10);   // true
     * ArithmeticUtil.isGreaterThan(10, 10);   // false
     * ArithmeticUtil.isGreaterThan(0.1, null); // true
     * }</pre>
     */
    public static boolean isGreaterThan(Number a, Number b) {
        // compareTo는 첫 번째 값이 더 크면 1을 반환합니다.
        return toBigDecimal(a).compareTo(toBigDecimal(b)) > 0;
    }

    /**
     * <p>Checks if the first number is greater than or equal to the second. {@code null} is treated as zero.</p>
     * <p>첫 번째 숫자가 두 번째 숫자보다 크거나 같은지 확인합니다. {@code null}은 0으로 처리됩니다.</p>
     *
     * @param a The first number. Can be {@code null}.
     *          <br>첫 번째 숫자. {@code null}일 수 있습니다.
     * @param b The second number. Can be {@code null}.
     *          <br>두 번째 숫자. {@code null}일 수 있습니다.
     * @return {@code true} if the first number is greater than or equal to the second, otherwise {@code false}.
     *         <br>첫 번째 숫자가 크거나 같으면 {@code true}, 그렇지 않으면 {@code false}.
     *
     * @example
     * <pre>{@code
     * ArithmeticUtil.isGreaterThanOrEqual(11, 10);    // true
     * ArithmeticUtil.isGreaterThanOrEqual(10, 10.0);  // true
     * ArithmeticUtil.isGreaterThanOrEqual(null, 0.0); // true
     * }</pre>
     */
    public static boolean isGreaterThanOrEqual(Number a, Number b) {
        // compareTo는 값이 크거나 같으면 1 또는 0을 반환합니다.
        return toBigDecimal(a).compareTo(toBigDecimal(b)) >= 0;
    }

    /**
     * <p>Checks if the first number is less than the second. {@code null} is treated as zero.</p>
     * <p>첫 번째 숫자가 두 번째 숫자보다 작은지 확인합니다. {@code null}은 0으로 처리됩니다.</p>
     *
     * @param a The first number. Can be {@code null}.
     *          <br>첫 번째 숫자. {@code null}일 수 있습니다.
     * @param b The second number. Can be {@code null}.
     *          <br>두 번째 숫자. {@code null}일 수 있습니다.
     * @return {@code true} if the first number is less than the second, otherwise {@code false}.
     *         <br>첫 번째 숫자가 더 작으면 {@code true}, 그렇지 않으면 {@code false}.
     *
     * @example
     * <pre>{@code
     * ArithmeticUtil.isLessThan(10, 11);   // true
     * ArithmeticUtil.isLessThan(10, 10);   // false
     * ArithmeticUtil.isLessThan(null, 0.1); // true
     * }</pre>
     */
    public static boolean isLessThan(Number a, Number b) {
        // compareTo는 첫 번째 값이 더 작으면 -1을 반환합니다.
        return toBigDecimal(a).compareTo(toBigDecimal(b)) < 0;
    }

    /**
     * <p>Checks if the first number is less than or equal to the second. {@code null} is treated as zero.</p>
     * <p>첫 번째 숫자가 두 번째 숫자보다 작거나 같은지 확인합니다. {@code null}은 0으로 처리됩니다.</p>
     *
     * @param a The first number. Can be {@code null}.
     *          <br>첫 번째 숫자. {@code null}일 수 있습니다.
     * @param b The second number. Can be {@code null}.
     *          <br>두 번째 숫자. {@code null}일 수 있습니다.
     * @return {@code true} if the first number is less than or equal to the second, otherwise {@code false}.
     *         <br>첫 번째 숫자가 작거나 같으면 {@code true}, 그렇지 않으면 {@code false}.
     *
     * @example
     * <pre>{@code
     * ArithmeticUtil.isLessThanOrEqual(10, 11);    // true
     * ArithmeticUtil.isLessThanOrEqual(10, 10.0);  // true
     * ArithmeticUtil.isLessThanOrEqual(0.0, null); // true
     * }</pre>
     */
    public static boolean isLessThanOrEqual(Number a, Number b) {
        // compareTo는 값이 작거나 같으면 -1 또는 0을 반환합니다.
        return toBigDecimal(a).compareTo(toBigDecimal(b)) <= 0;
    }
    
    /**
     * <p>Checks if a number represents an integer value (has no fractional part).</p>
     * <p>숫자가 정수 값(소수 부분 없음)을 나타내는지 확인합니다.</p>
     *
     * @param number The number to check. Can be {@code null}.
     *               <br>확인할 숫자. {@code null}일 수 있습니다.
     * @return {@code true} if the number is an integer or {@code null}, otherwise {@code false}.
     *         <br>숫자가 정수이거나 {@code null}이면 {@code true}, 그렇지 않으면 {@code false}.
     *
     * @example
     * <pre>{@code
     * ArithmeticUtil.isInteger(123.0);       // true
     * ArithmeticUtil.isInteger(new BigDecimal("123.00")); // true
     * ArithmeticUtil.isInteger(null);          // true
     * ArithmeticUtil.isInteger(123.01);      // false
     * }</pre>
     */
    public static boolean isInteger(Number number) {
        // 0이나 null은 정수로 간주합니다.
        if (isZero(number)) {
            return true;
        }
        BigDecimal bd = toBigDecimal(number);
        // stripTrailingZeros()는 123.00을 123으로 만듭니다.
        // 이 작업 후 scale()이 0 이하면 소수 부분이 없는 정수입니다.
        return bd.stripTrailingZeros().scale() <= 0;
    }

    /**
     * <p>Checks if a number has a fractional part.</p>
     * <p>숫자에 소수 부분이 있는지 확인합니다.</p>
     *
     * @param number The number to check. Can be {@code null}.
     *               <br>확인할 숫자. {@code null}일 수 있습니다.
     * @return {@code true} if the number has a non-zero fractional part, otherwise {@code false}.
     *         <br>0이 아닌 소수 부분이 있으면 {@code true}, 그렇지 않으면 {@code false}.
     *
     * @example
     * <pre>{@code
     * ArithmeticUtil.isDecimal(123.45);      // true
     * ArithmeticUtil.isDecimal(123.00);      // false
     * ArithmeticUtil.isDecimal(null);          // false
     * }</pre>
     */
    public static boolean isDecimal(Number number) {
        // 0이 아니고 정수가 아니면 소수로 간주합니다.
        // isZero는 null도 true를 반환하므로 null 케이스도 자동으로 처리됩니다.
        return !isZero(number) && !isInteger(number);
    }
}
