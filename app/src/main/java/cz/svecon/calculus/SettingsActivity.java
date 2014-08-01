package cz.svecon.calculus;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.SeekBar;
import android.widget.Spinner;

/**
 * Settings activity for the application.
 *
 * User can choose how many problems there are in each practice,
 * how large numbers he wants to have for each division and multiplication
 * problems.
 */
public class SettingsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Context context = getApplicationContext();
        final SharedPreferences sharedPref = context.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);


        SeekBar problemsCount = (SeekBar)findViewById(R.id.problems_count);
        problemsCount.setProgress(sharedPref.getInt(getString(R.string.settings_problems_count), 15) - 1);
        problemsCount.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                sharedPref.edit().putInt(getString(R.string.settings_problems_count), progress + 1).commit();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        Spinner multiplication_multiplier = (Spinner)findViewById(R.id.multiplication_multiplier);
        multiplication_multiplier.setSelection(sharedPref.getInt(getString(R.string.settings_multiplier), 0));
        multiplication_multiplier.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sharedPref.edit().putInt(getString(R.string.settings_multiplier), position).commit();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Spinner multiplication_multiplicand = (Spinner)findViewById(R.id.multiplication_multiplicand);
        multiplication_multiplicand.setSelection(sharedPref.getInt(getString(R.string.settings_multiplicand), 0));
        multiplication_multiplicand.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sharedPref.edit().putInt(getString(R.string.settings_multiplicand), position).commit();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Spinner division_factor = (Spinner)findViewById(R.id.division_factor);
        division_factor.setSelection(sharedPref.getInt(getString(R.string.settings_factor), 0));
        division_factor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sharedPref.edit().putInt(getString(R.string.settings_factor), position).commit();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Spinner division_dividend = (Spinner)findViewById(R.id.division_dividend);
        division_dividend.setSelection(sharedPref.getInt(getString(R.string.settings_dividend), 0));
        division_dividend.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sharedPref.edit().putInt(getString(R.string.settings_dividend), position).commit();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }
}
