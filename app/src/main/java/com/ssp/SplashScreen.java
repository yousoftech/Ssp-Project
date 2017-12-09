package com.ssp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.ssp.Activity.LoginActivity;
import com.ssp.Util.ConnectionDetector;

public class SplashScreen extends AppCompatActivity {

    ConnectionDetector detector;
    Button btnretry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        final Button btnretry = (Button) findViewById(R.id.btnRetry);
        detector = new ConnectionDetector(this);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    if (detector.isConnectingToInternet()) {
                        btnretry.setVisibility(View.GONE);
                        startActivity(new Intent(SplashScreen.this, LoginActivity.class));
                        finish();
                    } else {
                        btnretry.setVisibility(View.VISIBLE);
                        btnretry.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (detector.isConnectingToInternet()) {
                                    startActivity(new Intent(SplashScreen.this, LoginActivity.class));
                                } else {
                                    Toast.makeText(SplashScreen.this, "Check Your Internet Connection", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        Toast.makeText(SplashScreen.this, "Check Your Internet Connection", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 3000);
    }

}
