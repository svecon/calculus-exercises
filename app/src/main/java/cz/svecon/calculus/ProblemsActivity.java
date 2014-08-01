package cz.svecon.calculus;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import android.graphics.Color;

import java.util.Date;
import java.util.LinkedList;

import cz.svecon.calculus.fragments.KeyboardFragment;
import cz.svecon.calculus.fragments.ResultsFragment;

import static cz.svecon.calculus.fragments.KeyboardFragment.OnFragmentInteractionListener;

/**
 * @TODO Default settings
 * @TODO Results fragment align
 */

/**
 * Abstract activity class for practicing both division and multiplication.
 */
public abstract class ProblemsActivity extends BaseActivity implements OnFragmentInteractionListener, ResultsFragment.OnNewGameButtonClicked {

    /**
     * Number that is displayed first in the problem.
     * (dividend or multiplicator)
     */
    long firstNumber;

    /**
     * Number that is displayed second in the problem.
     * (factor or multiplicand)
     */
    long secondNumber;

    /**
     * Result of the problem
     */
    long result;

    /**
     * Matrix of digits: precalculation for given problem
     */
    int[][] matrix;

    /**
     * Number of total mistakes during testing
     */
    int mistakes;

    /**
     * Time when the testing started
     */
    Date start;

    /**
     * Time when the testing finished
     */
    Date finish;

    /**
     * Number of current problem
     */
    int currentProblem;

    /**
     * Number of problems during which there were mistakes
     */
    int wrongProblems;

    /**
     * Was there already a mistake in current problem?
     */
    boolean isMistakeInCurrent;

    /**
     * List of correct numbers to complete a problem
     */
    LinkedList<HiddenNumber> numberSequence;

    /**
     * Current TextView, cursor
     */
    TextView cursor;

    /**
     * Correct answers in one problem (how many digits user computed correctly)
     *
     * For redrawing purposes (orientation change).
     */
    int correctAnswers;

    /**
     * Wrapper class for correct Number Sequence List
     */
    static class HiddenNumber {

        public int x;
        public int y;
        public String label;

        public HiddenNumber(int x, int y, String label) {
            this.x = x;
            this.y = y;
            this.label = label;
        }
    }

    /**
     * Wrapper class for Horizontal line made of dashes
     */
    static class HiddenLine extends HiddenNumber {

        public HiddenLine(int x, int y, String label) {
            super(x, y, label);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_problems);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        KeyboardFragment fragment = new KeyboardFragment();
        if (savedInstanceState == null) {
            fragmentTransaction.add(R.id.keyboard_container, fragment);
            fragmentTransaction.commit();
        }

        if (savedInstanceState == null) {
            reset();
            setToolbarTitle();
        } else {
            currentProblem = savedInstanceState.getInt("currentProblem");
            firstNumber = savedInstanceState.getLong("firstNumber");
            secondNumber = savedInstanceState.getLong("secondNumber");
            result = savedInstanceState.getLong("result");
            correctAnswers = savedInstanceState.getInt("correctAnswers");

            mistakes = savedInstanceState.getInt("mistakes");
            wrongProblems = savedInstanceState.getInt("wrongProblems");
            start = (Date) savedInstanceState.getSerializable("start");
            finish = (Date) savedInstanceState.getSerializable("finish");
            isMistakeInCurrent = savedInstanceState.getBoolean("isMistakeInCurrent");

            setToolbarTitle();

            Context context = getApplicationContext();
            final SharedPreferences sharedPref = context.getSharedPreferences(
                    getString(R.string.preference_file_key), Context.MODE_PRIVATE);

            // already finished - show ResultsFragment
            if (finish != null) {
                ((ViewGroup)findViewById(R.id.keyboard_container)).setVisibility(View.GONE);
                return;
            }

            // redraw the whole problem
            solveMatrix();
            prepareNumberSequence();
            prepareGUI();
            moveCursor(true);

            GridLayout computation = (GridLayout) findViewById(R.id.problem_container);

            for (int i = 0; i < correctAnswers; i++){
                HiddenNumber hn = numberSequence.pollFirst();

                computation.addView(Helpers.createDigit(hn.label, this), Helpers.gridLayout(hn.x, hn.y));
                moveCursor(false);
            }
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        outState.putLong("firstNumber", firstNumber);
        outState.putLong("secondNumber", secondNumber);
        outState.putLong("result", result);
        outState.putInt("correctAnswers", correctAnswers);
        outState.putInt("currentProblem", currentProblem);

        outState.putInt("wrongProblems", wrongProblems);
        outState.putInt("mistakes", mistakes);
        outState.putSerializable("start", start);
        outState.putSerializable("finish", finish);
        outState.putBoolean("isMistakeInCurrent", isMistakeInCurrent);

        super.onSaveInstanceState(outState);
    }

    /**
     * Resets counters for new competition
     */
    public void reset() {
        mistakes = 0;
        start = new Date();
        currentProblem = 0;
        wrongProblems = 0;

        initializeNextProblem();
    }

    /**
     * Resets and initializes everything for next problem
     */
    void initializeNextProblem() {
        currentProblem++;
        correctAnswers = 0;
        isMistakeInCurrent = false;

        GridLayout computation = (GridLayout) findViewById(R.id.problem_container);
        if (computation != null) {
            computation.removeAllViewsInLayout();
        }

        generateNewProblem();
        solveMatrix();
        prepareNumberSequence();
        prepareGUI();
        moveCursor(true);
    }

    /**
     * Solves problem and prepares matrix of digits for evaluation
     */
    abstract void solveMatrix();

    /**
     * Creates a list with correct sequence of digits to solve current problem
     */
    abstract void prepareNumberSequence();

    /**
     * Randomly generate new problem with given Settings
     */
    abstract void generateNewProblem();

    /**
     * Creates Grid, labels and buttons for the computation
     */
    abstract void prepareGUI();

    /**
     * Draws a horizontal line made of dashes in a GridBagLayout
     *
     * @param length horizontal width
     * @param y y-coordinate
     */
    void drawLine(int length, int y) {
        GridLayout computation = (GridLayout) findViewById(R.id.problem_container);
        for (int i = 0; i < length; i++) {
            computation.addView(Helpers.createDigit("—", this), Helpers.gridLayout(i, y));
        }
    }

    /**
     * Move cursor one step in a NumberSequence
     *
     * @param init is this a first step?
     */
    void moveCursor(boolean init) {

        GridLayout computation = (GridLayout) findViewById(R.id.problem_container);

        if (numberSequence.isEmpty()) {
            computation.removeView(cursor);
            return;
        }

        if (init) {
            cursor = Helpers.createDigit("_", this);
//            cursor.setOpaque(true);
        }

        HiddenNumber hn = numberSequence.peekFirst();

        if (hn instanceof HiddenLine) {
            drawLine(hn.x, hn.y);
            numberSequence.pollFirst();
            hn = numberSequence.peekFirst();
        }

        cursor.setBackgroundColor(Color.WHITE);
        cursor.setTextColor(Color.BLACK);
        cursor.setText("_");

        if (!init) {
            computation.removeView(cursor);
        }

        computation.addView(cursor, Helpers.gridLayout(hn.x, hn.y));
    }

    @Override
    public void onKeyboardInteraction(String keyboard) {

        GridLayout computation = (GridLayout) findViewById(R.id.problem_container);

        Context context = getApplicationContext();
        final SharedPreferences sharedPref = context.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        if (numberSequence.isEmpty()) {
            if (currentProblem == sharedPref.getInt(getString(R.string.settings_problems_count), 15)) {
                finish = new Date();
                paintResults();
            } else {
                initializeNextProblem();
                setToolbarTitle();
            }
            return;
        }

        HiddenNumber hn = numberSequence.peekFirst();

        if (hn.label.equals(keyboard)) {
            computation.addView(Helpers.createDigit(hn.label, this), Helpers.gridLayout(hn.x, hn.y));

            correctAnswers++;
            numberSequence.pollFirst();
            moveCursor(false);
        } else {
            cursor.setText(keyboard);
            cursor.setBackgroundColor(Color.WHITE);
            cursor.setTextColor(Color.RED);

            mistakes++;
            if (isMistakeInCurrent == false) {
                wrongProblems++;
            }
            isMistakeInCurrent = true;
        }

        setToolbarTitle();
    }

    /**
     * Paints the result fragment and hides everything else.
     */
    protected void paintResults(){
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        ((ViewGroup)getWindow().getDecorView().findViewById(android.R.id.content)).removeAllViews();

        ResultsFragment fragment = ResultsFragment.newInstance(currentProblem, mistakes, wrongProblems, start, finish);

        fragmentTransaction.add(android.R.id.content, fragment);
        fragmentTransaction.commit();
    }

    /**
     * Returns Toolbar title for the activity
     */
    abstract protected String getToolbarTitle();

    protected void setToolbarTitle()
    {
        Context context = getApplicationContext();
        final SharedPreferences sharedPref = context.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        int outOf = sharedPref.getInt(getString(R.string.settings_problems_count), 15);

        setTitle(getToolbarTitle() + " (" + currentProblem + "/" + outOf + ")");
    }

    /**
     * Handle a keyboard click
     * @param view View
     */
    public void keyboardClick(View view)
    {
        Button b = (Button) view;
        onKeyboardInteraction(b.getText().toString());
    }

    @Override
    public void onNewGameButtonClicked(View view) {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }
}
