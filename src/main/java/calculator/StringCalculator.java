package calculator;

import java.util.regex.*;

public class StringCalculator {

    // 개행일 경우 "(?:\\\\n|\\R)"
    private static final Pattern CUSTOM_HEADER = Pattern.compile("^//(.)\\\\n(.*)$");

    public int add(String input) {
        if (input == null || input.isEmpty()) return 0;

        String delimiter = "[,:]"; // 기본 구분자
        String numbers = input;

        // 커스텀 구분자 파싱
        Matcher m = CUSTOM_HEADER.matcher(input);
        if (m.find()) {
            delimiter = Pattern.quote(m.group(1)); // 한 글자 커스텀 구분자
            numbers = m.group(2);
        }

        String[] tokens = numbers.split(delimiter, -1);

        int sum = 0;
        for (String token : tokens) {
            if (token.isEmpty()) {
                throw new IllegalArgumentException("빈 숫자 토큰이 있습니다.(구분자와 숫자의 갯수가 올바르지 않습니다.)");
            }

            // 숫자만 허용(양의 정수)
            if (!token.chars().allMatch(Character::isDigit)) {
                throw new IllegalArgumentException("양의 정수만 입력할 수 있습니다. 잘못된 입력값: [" + token + "]");
            }

            // 파싱 시 int 범위 초과 처리
            final int value;
            try {
                value = Integer.parseInt(token);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("int 범위를 초과했습니다. 잘못된 입력값: [" + token + "]");
            }

            // 합산 시 오버플로 처리
            try {
                sum = Math.addExact(sum, value);
            } catch (ArithmeticException e) {
                throw new IllegalArgumentException("합계가 int 범위를 초과했습니다.");
            }
        }
        return sum;
    }
}
