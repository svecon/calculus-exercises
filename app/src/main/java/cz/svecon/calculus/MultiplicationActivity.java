package cz.svecon.calculus;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.GridLayout;

import java.util.LinkedList;

/**
 * Activity for practicing multiplication.
 */
public class MultiplicationActivity extends ProblemsActivity {

    @Override
    void generateNewProblem() {
        Context context = getApplicationContext();
        final SharedPreferences sharedPref = context.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        int multiplierSettings = Helpers.settings(sharedPref, getString(R.string.settings_multiplier));

        while ((secondNumber = Helpers.randomNumber(multiplierSettings)) == 1) {}

        int multiplicandSettings = Helpers.settings(sharedPref, getString(R.string.settings_multiplicand));

        firstNumber = Helpers.randomNumber(multiplicandSettings);

        result = firstNumber * secondNumber;
    }

    @Override
    void solveMatrix() {
        //     222
        //    *333
        //   -----
        //     666
        //    666
        //   666
        //   -----
        //   73926

        int[] multiple1A = Helpers.numberToDigits(firstNumber);
        int[] multiple2A = Helpers.numberToDigits(secondNumber);
        int resultLength = String.valueOf(result).length();

        matrix = new int[multiple2A.length][resultLength];

        int j = 0;
        for (int x = multiple2A.length - 1; x >= 0; x--) {
            int i = j;
            for (int y = multiple1A.length - 1; y >= 0; y--) {
                matrix[j][resultLength - 1 - i] += multiple1A[y] * multiple2A[x];
                i++;
            }
            Helpers.shiftOverflowLeft(matrix[j]);
            j++;
        }
    }

    @Override
    void prepareNumberSequence() {
        numberSequence = new LinkedList<>();

        String[] multiple1A = Helpers.numberToStringArray(firstNumber);
        String[] multiple2A = Helpers.numberToStringArray(secondNumber);
        String[] resultA = Helpers.numberToStringArray(result);

        for (int y = 0; y < multiple2A.length; y++) {

            int findLatestIndex = 0;

            for (int x = 0; x < resultA.length; x++) {
                if (matrix[y][x] != 0) {
                    break;
                }
                findLatestIndex++;
            }

            for (int x = resultA.length - 1 - y; x >= findLatestIndex; x--) {
                numberSequence.add(new HiddenNumber(x, 3 + y, String.valueOf(matrix[y][x])));
            }
        }

        if (secondNumber < 10) {
            return; // dont create adding step
        }

        numberSequence.add(new HiddenLine(resultA.length, 3 + multiple1A.length, ""));

        for (int i = resultA.length - 1; i >= 0; i--) {
            numberSequence.add(new HiddenNumber(i, 4 + multiple1A.length, String.valueOf(resultA[i])));
        }

    }

    @Override
    void prepareGUI() {

        GridLayout computation = (GridLayout) findViewById(R.id.problem_container);

        String[] multiple1A = Helpers.numberToStringArray(firstNumber);
        String[] multiple2A = Helpers.numberToStringArray(secondNumber);
        int resultLength = String.valueOf(result).length();

        for (int i = 0; i < multiple1A.length; i++) {
            computation.addView(Helpers.createDigit(multiple1A[i].toString(), this), Helpers.gridLayout(resultLength - multiple1A.length + i, 0));
        }

        for (int i = 0; i < multiple2A.length; i++) {
            computation.addView(Helpers.createDigit(multiple2A[i].toString(), this), Helpers.gridLayout(resultLength - multiple2A.length + i, 1));
        }

        drawLine(resultLength, 2);

        computation.addView(Helpers.createDigit("*", this), Helpers.gridLayout(resultLength - multiple2A.length - 1, 1));
    }

    @Override
    protected String getToolbarTitle() {
        return "Multiplication";
    }
}
