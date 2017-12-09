package com.ssp.Activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ssp.MainActivity;
import com.ssp.R;
import com.ssp.Util.ConnectionDetector;
import com.ssp.Util.Constant;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    EditText edtUserName, edtPassword;
    ProgressDialog progressDialog;
    ConnectionDetector detector;
    SharedPreferences preferences;
    public static final String PREFS_NAME = "LoginPrefs";
    Button btnLogin;

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        detector = new ConnectionDetector(this);
        edtUserName = (EditText) findViewById(R.id.EdtUserEmailId);
        edtPassword = (EditText) findViewById(R.id.EdtUserPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);

        preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        if (preferences.getString("logged", "").equals("logged")) {
            if (preferences.getString("userType", "").equalsIgnoreCase("Admin")) {
                startActivity(new Intent(LoginActivity.this, AdminActivity.class));
                finish();
            } else if (preferences.getString("userType", "").equalsIgnoreCase("Spot Boys")) {
                startActivity(new Intent(LoginActivity.this, SpotActivity.class));
                finish();
            } else if (preferences.getString("userType", "").equalsIgnoreCase("Yatri")) {
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            }
        }

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtUserName.getText().length() < 1) {
                    edtUserName.setError("Please Enter UserName");
                } else if (edtPassword.getText().length() < 1) {
                    edtPassword.setError("Please Enter Password");
                } else {
                    checkLogin();
                }
            }
        });
    }

    public void checkLogin() {
        if (detector.isConnectingToInternet()) {

            progressDialog = new ProgressDialog(this);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Loading...");
            progressDialog.show();
            RequestQueue requestQueue = Volley.newRequestQueue(this);

            final JSONObject object = new JSONObject();
            try {
                object.put("strUserName", edtUserName.getText().toString());
                object.put("strPassword", edtPassword.getText().toString());
            } catch (JSONException e) {
                Toast.makeText(LoginActivity.this, "Something take longer time please try again..!", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }

            final JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                    Constant.PATH + "Login/login", object,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("RESPONSE", response.toString());

                            try {
                                boolean code = response.getBoolean("status");
                                Log.d("Login", "" + code);
                                String msg = response.getString("message");
                                // Toast.makeText(this, ""+code, Toast.LENGTH_SHORT).show();
                                if (code == true) {
                                    JSONObject obj = response.getJSONObject("data");
                                    progressDialog.dismiss();
                                    preferences = getApplicationContext().getSharedPreferences(PREFS_NAME, 0);
                                    SharedPreferences.Editor editor = preferences.edit();
                                    editor.putString("logged", "logged");
                                    editor.commit();
                                    Log.d("Login", "data" + obj);
                                    int id = obj.getInt("iUserDetailsId");
                                    String firstName = obj.getString("strUserFirstName");
                                    String lastName = obj.getString("strUserLastName");
                                    String emailId = obj.getString("strUserEmailId");
                                    String userType = obj.getString("strUserType");
                                    if (userType.equalsIgnoreCase("Admin")) {
                                        startActivity(new Intent(LoginActivity.this, AdminActivity.class));
                                        editor.putString("logged", "logged");
                                        editor.putString("userType", userType);
                                        editor.commit();
                                        finish();
                                    } else if (userType.equalsIgnoreCase("Spot Boys")) {
                                        startActivity(new Intent(LoginActivity.this, SpotActivity.class));
                                        editor.putString("logged", "logged");
                                        editor.putString("userType", userType);
                                        editor.commit();
                                        finish();
                                    } else if (userType.equalsIgnoreCase("Yatri")) {
                                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                        editor.putString("logged", "logged");
                                        editor.putInt("userId",id);
                                        editor.putString("userType", userType);
                                        editor.commit();
                                        finish();
                                    }
                                } else if (code == false) {
                                    String msg1 = response.getString("message");
                                    progressDialog.dismiss();
                                    Toast.makeText(LoginActivity.this, ""+msg1, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                progressDialog.dismiss();
                                Toast.makeText(LoginActivity.this, "Something take longer time please try again..!", Toast.LENGTH_LONG).show();
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (progressDialog != null)
                        progressDialog.dismiss();
                    Log.d("RESPONSE", "That didn't work!");
                }
            });
            request.setRetryPolicy(new DefaultRetryPolicy(
                    30000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(request);
        } else {
            Toast.makeText(this, "Please check your internet connection before verification..!", Toast.LENGTH_LONG).show();
        }

    }
}
