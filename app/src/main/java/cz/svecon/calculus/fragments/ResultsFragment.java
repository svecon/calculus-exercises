package cz.svecon.calculus.fragments;



import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.Date;

import cz.svecon.calculus.R;

/**
 * Fragment which displays results of your skills.
 * Shows number of calculated problems, error count, time spent per problem.
 */
public class ResultsFragment extends Fragment {
    // Name of arguments
    private static final String ARG_NUMBEROFPROBLEMS = "ARG_NUMBEROFPROBLEMS";
    private static final String ARG_NUMBEROFERRORS = "ARG_NUMBEROFERRORS";
    private static final String ARG_NUMBEROFPROBLEMERRORS = "ARG_NUMBEROFPROBLEMERRORS";
    private static final String ARG_START = "ARG_START";
    private static final String ARG_FINISH = "ARG_FINISH";

    /**
     * Number of problems the user calculated
     */
    private int numberOfProblems;

    /**
     * Number of errors the user did during calculation
     */
    private int numberOfErrors;

    /**
     * Number of problems in which user did a mistake
     */
    private int numberOfProblemErrors;

    /**
     * When user start calculations
     */
    private Date start;

    /**
     * When user finished calculations.
     */
    private Date finish;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ResultsFragment.
     */
    public static ResultsFragment newInstance(int numberOfProblems, int numberOfErrors, int numberOfProblemErrors, Date start, Date finish) {
        ResultsFragment fragment = new ResultsFragment();

        Bundle args = new Bundle();
        args.putInt(ARG_NUMBEROFPROBLEMS, numberOfProblems);
        args.putInt(ARG_NUMBEROFERRORS, numberOfErrors);
        args.putInt(ARG_NUMBEROFPROBLEMERRORS, numberOfProblemErrors);
        args.putSerializable(ARG_START, start);
        args.putSerializable(ARG_FINISH, finish);

        fragment.setArguments(args);
        return fragment;
    }

    public ResultsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            numberOfProblems = getArguments().getInt(ARG_NUMBEROFPROBLEMS);
            numberOfErrors = getArguments().getInt(ARG_NUMBEROFERRORS);
            numberOfProblemErrors = getArguments().getInt(ARG_NUMBEROFPROBLEMERRORS);
            start = (Date) getArguments().getSerializable(ARG_START);
            finish = (Date) getArguments().getSerializable(ARG_FINISH);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_results, container, false);

        Date now = finish;
        long totalMili = now.getTime() - start.getTime();
        double totalSeconds = (1.0 * totalMili) / 1000;

        DecimalFormat df2 = new DecimalFormat("#.##");
        DecimalFormat df1 = new DecimalFormat("#.#");

        ((TextView)view.findViewById(R.id.results_problems_solved_successfully)).setText(numberOfProblems+"");
        ((TextView)view.findViewById(R.id.results_problems_with_errors)).setText(numberOfProblemErrors+"");
        ((TextView)view.findViewById(R.id.results_number_of_errors)).setText(numberOfErrors+"");
        ((TextView)view.findViewById(R.id.results_success_rate)).setText(
                df2.format((1.0 * (numberOfProblems - numberOfProblemErrors)) / numberOfProblems * 100) + "%"
        );
        ((TextView)view.findViewById(R.id.results_time_per_problem)).setText(
                df1.format(totalSeconds / numberOfProblems) + "s"
        );
        ((TextView)view.findViewById(R.id.results_time_overall)).setText(
                df1.format(totalSeconds) + "s"
        );

        return view;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity.
     */
    public interface OnNewGameButtonClicked
    {
        void onNewGameButtonClicked(View view);
    }


}
