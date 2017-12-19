package com.ssp.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.ssp.Fragment.YatraSummaryFragment;
import com.ssp.Fragment.YatriStatusFragment;
import com.ssp.R;

import static com.ssp.Activity.LoginActivity.PREFS_NAME;

public class MainAdminActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    boolean doubleBackToExitPressedOnce = false;
    Toolbar toolbar;
    TextView txt;
    Button btnLogout;
    SharedPreferences preferences;
    private TextView mTextMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_admin);
        preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        btnLogout = (Button) findViewById(R.id.btnLogout);
        mTextMessage = (TextView) findViewById(R.id.message);
        txt = (TextView) findViewById(R.id.txtTitle);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                Fragment selectedFragment = null;
                switch (item.getItemId()) {
                    case R.id.YatraSummary:
                        selectedFragment = YatraSummaryFragment.newInstance();
                        break;
                    case R.id.yatriStatus:
                        selectedFragment = YatriStatusFragment.newInstance();
                        break;
                }
                android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frameLayout, selectedFragment);
                transaction.commit();
                return true;
            }
        });
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameLayout, YatraSummaryFragment.newInstance());
        transaction.commit();
        setupToolbar("Admin");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    private void setupToolbar(String title) {
        setSupportActionBar(toolbar);
        txt.setText(title);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainAdminActivity.this);
                LayoutInflater inflater = MainAdminActivity.this.getLayoutInflater();
                dialogBuilder.setMessage("Are you sure you want to logout ? ");

                dialogBuilder.setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        SharedPreferences.Editor editor = preferences.edit();
                        editor.remove("logged");
                        editor.clear();
                        editor.commit();
                        finish();
                        Toast.makeText(getApplicationContext(), "Bye Bye", Toast.LENGTH_SHORT).show();
                    }
                });
                dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });

                AlertDialog a = dialogBuilder.create();
                a.show();
            }
        });
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("");
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setHomeButtonEnabled(false);
        }
    }

}
