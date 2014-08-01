package cz.svecon.calculus;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.widget.GridLayout;
import android.widget.TextView;

import java.util.Random;

/**
 * Class with static helper methods of all kinds.
 */
public class Helpers {

    /**
     * Prepare standard random generator
     */
    static Random r = new Random();

    /**
     * Returns random number between given bounds
     *
     * @param max higher bound (9, 99, 999, 9999, ...)
     * @return Number between max and its tenth (= lower bound)
     */
    static int randomNumber(int max) {
        int maxLength = String.valueOf(max).length();

        int min = 1;
        for (int i = 0; i < maxLength - 1; i++) {
            min *= 10;
        }

        int num;
        while ((num = r.nextInt(max)) < min) {
        }

        return num;
    }

    /**
     * Creates a TextView with some default settings.
     *
     * @param text to be displayed
     * @param context activity context
     * @return TextView
     */
    public static TextView createDigit(String text, Context context){
        TextView lDigit = new TextView(context);
        lDigit.setTextAppearance(context, android.R.style.TextAppearance_Large);
        lDigit.setText(text);
        lDigit.setPadding(5,0,5,0);
        lDigit.setBackgroundColor(Color.WHITE);
        return lDigit;
    }

    /**
     * Creates a layout settings for GridLayout with some default parameters.
     *
     * @param col int
     * @param row int
     * @return GridLayout.LayoutParams
     */
    public static GridLayout.LayoutParams gridLayout(int col, int row)
    {
        GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams();
        layoutParams.rowSpec = GridLayout.spec(row);
        layoutParams.columnSpec = GridLayout.spec(col);
        layoutParams.setMargins(0, 0, 10, 10);

        return layoutParams;
    }


    /**
     * Takes a number and converts it into an array of digits
     *
     * @param num number to convert
     * @return array of digits
     */
    public static int[] numberToDigits(long num) {
        int length = String.valueOf(num).length();

        int[] arr = new int[length];

        for (int i = length - 1; i >= 0; i--) {
            arr[i] = (int) (num % 10);
            num /= 10;
        }

        return arr;
    }

    /**
     * Subtract one array of digits from another
     *
     * @param from    Array to subtract from
     * @param what    Array to subtract with
     * @param padding
     */
    public static void substractArrays(int[] from, int[] what, int padding) {
        padding = padding + 1 - what.length;

        for (int i = 0; i < what.length; i++) {
            from[i + padding] -= what[i];
        }

        for (int i = from.length - 1; i >= 0; i--) {
            if (from[i] < 0) {
                from[i] += 10;
                from[i - 1]--;
            }
        }
    }

    /**
     * Sum two array of digits
     *
     * @param to      one array
     * @param what    second array
     * @param padding how much is seconds array shifted from the first
     */
    public static void addArrays(int[] to, int[] what, int padding) {
        for (int i = 0; i < what.length; i++) {
            to[i + padding] += what[i];
        }

        for (int i = to.length - 1; i >= 0; i--) {
            if (to[i] >= 10) {
                to[i] -= 10;
                to[i - 1]++;
            }
        }
    }

    /**
     * Correct number of digits by shifting overflows to left
     *
     * @param arr number of digits
     */
    public static void shiftOverflowLeft(int[] arr) {
        for (int i = arr.length - 1; i >= 1; i--) {
            arr[i - 1] += arr[i] / 10;
            arr[i] %= 10;
        }
    }

    /**
     * Converts a number to array of digits as strings
     *
     * @param num number to convert
     * @return array of string digits
     */
    public static String[] numberToStringArray(long num) {
        return String.valueOf(num).split("(?<=.)");
    }

    /**
     * Converts zero based settings into numbers which represent bounds for given settings
     *
     * @param sp SharedPreferences
     * @param key String
     * @return int max bounds
     */
    public static int settings(SharedPreferences sp, String key){
        int max = 9;
        int val;
        if (key.equals("settings_factor")) {
            val = sp.getInt(key, 0);
        } else {
            val = sp.getInt(key, 1);
        }
        switch (key){
            case "settings_factor":
                if (val == 0){ max = 9; }
                else if (val == 1){ max = 99; }
                else { max = 999; }
                break;
            case "settings_dividend":
                if (val == 0){ max = 9; }
                else if (val == 1){ max = 99; }
                else if (val == 2){ max = 999; }
                else if (val == 3){ max = 9999; }
                else { max = 9999999; }
                break;
            case "settings_multiplier":
                if (val == 0){ max = 9; }
                else if (val == 1){ max = 99; }
                else { max = 999; }
                break;
            case "settings_multiplicand":
                if (val == 0){ max = 9; }
                else if (val == 1){ max = 99; }
                else if (val == 2){ max = 999; }
                else if (val == 3){ max = 9999; }
                else { max = 9999999; }
                break;
        }

        return max;
    }
}
