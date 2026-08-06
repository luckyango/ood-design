import java.util.*;
public class ConvertDecimalstoRomans {
    // I II III IV V
    // VI VII VIII IX X
    // XIV - 14     LVIII - 58    MCMXCIV - 1994
    // rule to convert/ conversion/ transformation
    // 1.normal mapping relationship
    // 2.  4 or 9
    //     4 = IV (one deduct from five); 9 = IX
    // 3. other: 
    // append format: I II III
    // power of 10 can be appended at most 3 times
    public String convertToRoman(int num) {
        // Clarify constraints: standard Roman numerals support 1 to 3999.
        if (num <= 0 || num > 3999) {
            return "";
        }

        StringBuilder ans = new StringBuilder();
        int multiplier = 1;

        while (num > 0) {
            int digit = num % 10;

            String current;

            if (multiplier == 1) {
                current = convertDigit(digit, 'I', 'V', 'X');
            } else if (multiplier == 10) {
                current = convertDigit(digit, 'X', 'L', 'C');
            } else if (multiplier == 100) {
                current = convertDigit(digit, 'C', 'D', 'M');
            } else {
                current = convertDigit(digit, 'M', '\0', '\0');
            }

            // We process from the ones place to the thousands place,
            // so each converted part must be inserted at the front.
            ans.insert(0, current);

            num /= 10;
            multiplier *= 10;
        }

        return ans.toString();
    }

    private String convertDigit(
            int digit,
            char one,
            char five,
            char ten) {

        StringBuilder result = new StringBuilder();

        if (digit <= 3) {
            for (int i = 0; i < digit; i++) {
                result.append(one);
            }
        } else if (digit == 4) {
            result.append(one).append(five);
        } else if (digit <= 8) {
            result.append(five);

            for (int i = 0; i < digit - 5; i++) {
                result.append(one);
            }
        } else {
            result.append(one).append(ten);
        }

        return result.toString();
    }

    public static void main(String[] args) {
        ConvertDecimalstoRomans solution =
                new ConvertDecimalstoRomans();

        System.out.println(solution.convertToRoman(14));   // XIV
        System.out.println(solution.convertToRoman(49));   // XLIX
        System.out.println(solution.convertToRoman(58));   // LVIII
        System.out.println(solution.convertToRoman(1994)); // MCMXCIV
    }
}
