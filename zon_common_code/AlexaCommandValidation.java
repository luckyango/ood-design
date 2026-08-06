// # Alexa Command Validator

// ## Problem

// Design a validator for Alexa commands.

// A command must satisfy multiple validation rules, for example:

// 1. The first word must be `"Alexa"`.
// 2. The command cannot contain two identical consecutive words.
// 3. The command cannot exceed a maximum number of words.
// 4. More validation rules may be added in the future.

// Examples:

// ```text
// "Alexa play music"        -> valid
// "Please Alexa play music" -> invalid
// "Alexa play play music"   -> invalid
// ```

// ## Design

// Instead of placing every condition inside one large method, I define a common `ValidationRule` interface.

// Each validation rule is implemented as a separate class.

// The `AlexaCommandValidator` stores a list of rules and applies them one by one. If any rule fails, the command is invalid.

// This design makes the validator extensible because a new rule can be added without modifying the existing validator logic.



import java.util.*;

public class AlexaCommandValidation {

    /*
     * Strategy interface.
     *
     * Every validation rule implements this interface.
     */
    public interface ValidationRule {
        boolean isValid(String[] words);
    }

    /*
     * Rule 1:
     * The first word must be "Alexa".
     */
    public static class StartsWithAlexaRule
            implements ValidationRule {

        @Override
        public boolean isValid(String[] words) {
            return words.length > 0
                    && words[0].equalsIgnoreCase("Alexa");
        }
    }

    /*
     * Rule 2:
     * Two consecutive words cannot be the same.
     *
     * Example:
     * "Alexa play play music" is invalid.
     */
    public static class NoConsecutiveDuplicateRule
            implements ValidationRule {

        @Override
        public boolean isValid(String[] words) {
            for (int i = 1; i < words.length; i++) {
                if (words[i].equalsIgnoreCase(words[i - 1])) {
                    return false;
                }
            }

            return true;
        }
    }

    /*
     * Rule 3:
     * The command cannot contain more than a configured
     * maximum number of words.
     */
    public static class MaximumWordCountRule
            implements ValidationRule {

        private final int maximumWordCount;

        public MaximumWordCountRule(int maximumWordCount) {
            this.maximumWordCount = maximumWordCount;
        }

        @Override
        public boolean isValid(String[] words) {
            return words.length <= maximumWordCount;
        }
    }

    /*
     * Rule 4:
     * Alexa must be followed by at least one command word.
     *
     * "Alexa" alone is invalid.
     */
    public static class HasCommandAfterAlexaRule
            implements ValidationRule {

        @Override
        public boolean isValid(String[] words) {
            return words.length >= 2;
        }
    }

    /*
     * The validator composes multiple validation strategies.
     */
    public static class AlexaCommandValidator {

        private final List<ValidationRule> rules;

        public AlexaCommandValidator(
                List<ValidationRule> rules) {

            this.rules = new ArrayList<>(rules);
        }

        public boolean isValid(String command) {
            if (command == null || command.trim().isEmpty()) {
                return false;
            }

            String[] words = command.trim().split("\\s+");

            for (ValidationRule rule : rules) {
                if (!rule.isValid(words)) {
                    return false;
                }
            }

            return true;
        }
    }

    public static void main(String[] args) {

        List<ValidationRule> rules = Arrays.asList(
                new StartsWithAlexaRule(),
                new HasCommandAfterAlexaRule(),
                new NoConsecutiveDuplicateRule(),
                new MaximumWordCountRule(10)
        );

        AlexaCommandValidator validator =
                new AlexaCommandValidator(rules);

        System.out.println(
                validator.isValid("Alexa play music")
        ); // true

        System.out.println(
                validator.isValid("Please Alexa play music")
        ); // false

        System.out.println(
                validator.isValid("Alexa play play music")
        ); // false

        System.out.println(
                validator.isValid("Alexa")
        ); // false

        System.out.println(
                validator.isValid("   Alexa    turn on the lights   ")
        ); // true
    }
}


// ## Design Pattern

// The primary design pattern is the **Strategy Pattern**.

// Each validation rule represents an independent validation strategy:

// ```java
// ValidationRule
// ```

// Different implementations provide different validation behaviors:

// ```java
// StartsWithAlexaRule
// NoConsecutiveDuplicateRule
// MaximumWordCountRule
// HasCommandAfterAlexaRule
// ```

// The validator does not need to know how each rule works internally. It only depends on the common interface:

// ```java
// for (ValidationRule rule : rules) {
//     if (!rule.isValid(words)) {
//         return false;
//     }
// }
// ```

// This allows validation strategies to be added, removed, or replaced independently.

// The design also uses **composition** because the validator contains a collection of rule objects and combines their behavior.

// ## Open/Closed Principle

// This design follows the Open/Closed Principle:

// > Software should be open for extension but closed for modification.

// Suppose we want to add a rule that prevents certain words from appearing in a command.

// We can create a new class:

// ```java
// public static class ForbiddenWordsRule
//         implements ValidationRule {

//     private final Set<String> forbiddenWords;

//     public ForbiddenWordsRule(Set<String> forbiddenWords) {
//         this.forbiddenWords = new HashSet<>();

//         for (String word : forbiddenWords) {
//             this.forbiddenWords.add(word.toLowerCase());
//         }
//     }

//     @Override
//     public boolean isValid(String[] words) {
//         for (String word : words) {
//             if (forbiddenWords.contains(word.toLowerCase())) {
//                 return false;
//             }
//         }

//         return true;
//     }
// }
// ```

// Then we add it to the rule list:

// ```java
// List<ValidationRule> rules = Arrays.asList(
//         new StartsWithAlexaRule(),
//         new HasCommandAfterAlexaRule(),
//         new NoConsecutiveDuplicateRule(),
//         new MaximumWordCountRule(10),
//         new ForbiddenWordsRule(
//                 new HashSet<>(Arrays.asList("delete", "shutdown"))
//         )
// );
// ```

// We do not need to modify `AlexaCommandValidator`.

// ## Is This Chain of Responsibility?

// This design is somewhat similar to the Chain of Responsibility Pattern because rules are evaluated one after another and validation stops when one rule fails.

// However, it is not a classic Chain of Responsibility implementation.

// In a traditional Chain of Responsibility design, each handler stores a reference to the next handler and passes the request to it:

// ```java
// rule1 -> rule2 -> rule3
// ```

// In this implementation, the validator centrally loops through a list of rules:

// ```java
// for (ValidationRule rule : rules)
// ```

// Therefore, the most accurate description is:

// > Strategy Pattern with composition.

// It has behavior similar to a validation chain, but it is not a traditional Chain of Responsibility implementation.

// ## Time and Space Complexity

// Assume the command contains `n` words and there are `r` validation rules.

// Most rules scan the words once, so the total time complexity is:

// ```text
// O(r × n)
// ```

// If the number of rules is considered constant, the time complexity simplifies to:

// ```text
// O(n)
// ```

// Splitting the command into words requires:

// ```text
// O(n)
// ```

// additional space.

// Therefore:

// ```text
// Time complexity:  O(r × n), or O(n) for a fixed number of rules
// Space complexity: O(n)
// ```

// ## Interview Explanation

// I would first clarify whether the validation rules are fixed or whether the system should support adding new rules later.

// If extensibility is required, I would avoid putting all conditions into one large method. Instead, I would define a common `ValidationRule` interface and implement each rule as a separate class.

// For example, one rule checks whether the first word is Alexa, another rule checks for consecutive duplicate words, and another rule checks the maximum command length.

// The validator receives a list of rules and applies them one by one. If any rule fails, it immediately returns false.

// This is mainly the Strategy Pattern because every validation rule is an interchangeable validation strategy. The validator uses composition to combine multiple strategies.

// The design also follows the Open/Closed Principle. To support a new validation requirement, I can add another implementation of `ValidationRule` and register it with the validator without modifying the existing validation logic.

// The time complexity is O(r times n), where r is the number of rules and n is the number of words. If the number of rules is fixed, it simplifies to O(n).
