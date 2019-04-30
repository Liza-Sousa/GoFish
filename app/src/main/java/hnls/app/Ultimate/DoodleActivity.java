package hnls.app.Ultimate;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import hnls.app.Ultimate.R;

public class DoodleActivity extends AppCompatActivity {
    // configures the screen orientation for this app
    int score = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doodle);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        // Set the content to appear under the system bars so that the
                        // content doesn’t resize when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);


        Intent mIntent = getIntent();
        score = mIntent.getIntExtra("score", 0);

        // determine screen size
        int screenSize =
                getResources().getConfiguration().screenLayout &
                        Configuration.SCREENLAYOUT_SIZE_MASK;

        // use landscape for extra large tablets; otherwise, use portrait
        if (screenSize == Configuration.SCREENLAYOUT_SIZE_XLARGE)
            setRequestedOrientation(
                    ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        else
            setRequestedOrientation(
                    ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }
    // handle choice from options menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // switch based on the MenuItem id
        switch (item.getItemId()) {
            case R.id.change_game:
                Intent myIntent = new Intent(this, MainActivity.class);
                myIntent.putExtra("score", score);
                startActivity(myIntent);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}