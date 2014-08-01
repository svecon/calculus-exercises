package cz.svecon.calculus;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.GridLayout;

import java.util.LinkedList;

/**
 * Activity for practicing division.
 */
public class DivisionActivity extends ProblemsActivity {

    /**
     * Initial padding for division - how many digits to take for first digit
     */
    private int initialPadding;

    @Override
    void generateNewProblem() {
        Context context = getApplicationContext();
        final SharedPreferences sharedPref = context.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        int factorSettings = Helpers.settings(sharedPref, getString(R.string.settings_factor));

        while ((secondNumber = Helpers.randomNumber(factorSettings)) == 1) {}

        int dividendSettings = Helpers.settings(sharedPref, getString(R.string.settings_dividend));

        firstNumber = Helpers.randomNumber(dividendSettings);

        result = firstNumber / secondNumber;
        firstNumber = result * secondNumber; // discard residue
    }

    @Override
    void solveMatrix() {

        int resultLength = String.valueOf(result).length();
        int dividendLength = String.valueOf(firstNumber).length();

        matrix = new int[resultLength][dividendLength];
        int[] valueRemainsA = Helpers.numberToDigits(firstNumber);

        // find initial padding
        initialPadding = 0;
        int tempPart = 0;
        for (int i = 0; i < dividendLength; i++) {
            tempPart = tempPart * 10 + valueRemainsA[i];
            if (tempPart >= secondNumber) {
                initialPadding = i;
                break;
            }
        }

        // compute remainders
        for (int y = 0; y < resultLength; y++) {

            int smallestPart = 0;
            for (int i = 0; i < initialPadding + y + 1; i++) {
                matrix[y][i] += valueRemainsA[i];
                smallestPart = smallestPart * 10 + valueRemainsA[i];

                if (smallestPart >= secondNumber) {
                    long tempResult = smallestPart / secondNumber;
                    Helpers.substractArrays(valueRemainsA, Helpers.numberToDigits(tempResult * secondNumber), i);
                    break;
                }
            }
        }
    }

    @Override
    void prepareNumberSequence() {
        numberSequence = new LinkedList<HiddenNumber>();
        LinkedList<HiddenNumber> hiddenNumbersResult = new LinkedList<>();

        String[] factorA = Helpers.numberToStringArray(secondNumber);
        String[] resultA = Helpers.numberToStringArray(result);
        for (int i = 0; i < resultA.length; i++) {
            hiddenNumbersResult.add(new HiddenNumber(i + String.valueOf(firstNumber).length() + factorA.length + 2, 0, resultA[i]));
        }

        // add to queue
        for (int i = 1; i < matrix.length; i++) {
            // take result num
            numberSequence.add(hiddenNumbersResult.pollFirst());

            for (int j = initialPadding + i - 1; j >= 0; j--) {
                if (matrix[i][j] == 0 && j < i) {
                    break;
                }

                numberSequence.add(new HiddenNumber(j, i, String.valueOf(matrix[i][j])));
            }
            // rewrite last num one down
            numberSequence.add(new HiddenNumber(initialPadding + i, i, String.valueOf(matrix[i][initialPadding + i])));
        }
        // take the last result num
        numberSequence.add(hiddenNumbersResult.pollFirst());

        // write 0 at the bottom
        numberSequence.add(new HiddenNumber(matrix[0].length - 1, matrix.length, "0"));
    }

    @Override
    void prepareGUI() {
        GridLayout computation = (GridLayout) findViewById(R.id.problem_container);

        String[] dividendA = Helpers.numberToStringArray(firstNumber);
        for (int i = 0; i < dividendA.length; i++) {
            computation.addView(Helpers.createDigit(dividendA[i].toString(), this), Helpers.gridLayout(i, 0));
        }

        computation.addView(Helpers.createDigit(":", this), Helpers.gridLayout(dividendA.length, 0));

        String[] factorA = Helpers.numberToStringArray(secondNumber);
        for (int i = 0; i < factorA.length; i++) {
            computation.addView(Helpers.createDigit(factorA[i].toString(), this), Helpers.gridLayout(i + dividendA.length + 1, 0));
        }

        computation.addView(Helpers.createDigit("=", this), Helpers.gridLayout(dividendA.length + factorA.length + 1, 0));
    }

    @Override
    protected String getToolbarTitle() {
        return "Division";
    }
}
