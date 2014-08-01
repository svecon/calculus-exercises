package cz.svecon.calculus;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

/**
 * Activity that behaves as a homepage for the whole application.
 *
 * User can start practicing division or multiplication directly from this activity.
 */
public class HomeActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }


    public void startMultiplication(View view){
        Intent intent = new Intent(this, MultiplicationActivity.class);
        startActivity(intent);
    }

    public void startDivision(View view){
        Intent intent = new Intent(this, DivisionActivity.class);
        startActivity(intent);
    }
}
