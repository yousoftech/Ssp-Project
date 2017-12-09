package com.ssp.Activity;

import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ssp.R;

import static com.ssp.Activity.LoginActivity.PREFS_NAME;

public class AdminActivity extends AppCompatActivity {

    Toolbar toolbar;
    TextView txt;
    Button btnLogout;
    SharedPreferences preferences;
     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
         preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
         txt = (TextView) findViewById(R.id.txtTitle);
         btnLogout = (Button) findViewById(R.id.btnLogout);
         setupToolbar("Admin");
    }
    private void setupToolbar(String title) {
        setSupportActionBar(toolbar);
        txt.setText(title);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = preferences.edit();
                editor.remove("logged");
                editor.clear();
                editor.commit();
                finish();
                Toast.makeText(getApplicationContext(), "Logout", Toast.LENGTH_SHORT).show();
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
