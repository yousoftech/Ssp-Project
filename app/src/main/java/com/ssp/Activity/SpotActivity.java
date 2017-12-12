package com.ssp.Activity;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ssp.R;
import com.ssp.Util.ConnectionDetector;
import com.ssp.Util.Constant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.ssp.Activity.LoginActivity.PREFS_NAME;

public class SpotActivity extends AppCompatActivity {

    EditText edtYatriNumber;
    TextView txtYatriNumber, txtYatriName, txtYatriEmailId, txtYatriMobileNo, txt;
    Button btnYatriDone, btnYatri, btnLogout;
    CardView cardViewBottom;
    Spinner spinnerSpot, spinnerYatra;
    ProgressDialog progressDialog;
    ConnectionDetector detector;
    SharedPreferences preferences;
    Toolbar toolbar;
    String yatraNumber;
    String spotNumber;

    String[] spot = {"1", "5", "10",};

    String[] yatra = {"1", "2", "3", "4", "5", "6", "7",};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_squad);
        preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        detector = new ConnectionDetector(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        btnYatri = (Button) findViewById(R.id.btnYatri);
        btnYatriDone = (Button) findViewById(R.id.btnYatriDone);
        edtYatriNumber = (EditText) findViewById(R.id.edtYatriNumber);
        txt = (TextView) findViewById(R.id.txtTitle);
        txtYatriName = (TextView) findViewById(R.id.txtYatriName);
        txtYatriNumber = (TextView) findViewById(R.id.txtYatriNumber);
        txtYatriEmailId = (TextView) findViewById(R.id.txtYatriEmailId);
        txtYatriMobileNo = (TextView) findViewById(R.id.txtYatriMobileNo);
        cardViewBottom = (CardView) findViewById(R.id.cardBottom);
        spinnerSpot = (Spinner) findViewById(R.id.spinnerSpot);
        spinnerYatra = (Spinner) findViewById(R.id.spinnerYatra);
        btnLogout = (Button) findViewById(R.id.btnLogout);

        btnYatri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edtYatriNumber.getText().length() < 1) {
                    edtYatriNumber.setError("Please Enter UserName");
                } else {
                    yatriFind();
                }
            }
        });
        btnYatriDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                yatriDone();
            }
        });
        setupToolbar("Spot Work");

        ArrayAdapter sp = new ArrayAdapter(this, android.R.layout.simple_spinner_item, spot);
        sp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spinnerSpot.setAdapter(sp);
        spinnerSpot.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                spotNumber = spinnerSpot.getSelectedItem().toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, yatra);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spinnerYatra.setAdapter(aa);
        spinnerYatra.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                yatraNumber = spinnerYatra.getSelectedItem().toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spot();
    }

    public void yatriFind() {
        if (detector.isConnectingToInternet()) {

            progressDialog = new ProgressDialog(this);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Loading...");
            progressDialog.show();
            RequestQueue requestQueue = Volley.newRequestQueue(this);

            final JSONObject object = new JSONObject();
            try {
                object.put("iUserDetailId", edtYatriNumber.getText().toString());

            } catch (JSONException e) {
                Toast.makeText(SpotActivity.this, "Something take longer time please try again..!", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }

            final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                    Constant.PATH + "yatri/getbyid/" + edtYatriNumber.getText().toString(), object,
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
                                    progressDialog.dismiss();
                                    JSONObject obj = response.getJSONObject("data");
                                    cardViewBottom.setVisibility(View.VISIBLE);
                                    btnYatriDone.setVisibility(View.VISIBLE);
                                    int yatriNo = obj.getInt("iUserDetailsId");
                                    String firstName = obj.getString("strUserFirstName");
                                    String lastName = obj.getString("strUserLastName");
                                    String emailId = obj.getString("strUserEmailId");
                                    String MobileNo = obj.getString("strMobileNo");
                                    txtYatriName.setText(firstName + " " + lastName);
                                    txtYatriNumber.setText("" + yatriNo);
                                    txtYatriEmailId.setText(emailId);
                                    txtYatriMobileNo.setText(MobileNo);
                                } else {
                                    progressDialog.dismiss();
                                    Toast.makeText(SpotActivity.this, "Sorry", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                progressDialog.dismiss();
                                Toast.makeText(SpotActivity.this, "Something take longer time please try again..!", Toast.LENGTH_LONG).show();
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

    public void yatriDone() {
        if (detector.isConnectingToInternet()) {

            progressDialog = new ProgressDialog(this);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Loading...");
            progressDialog.show();
            RequestQueue requestQueue = Volley.newRequestQueue(this);

            final JSONObject object = new JSONObject();
            try {
                object.put("iUserDetailsId", edtYatriNumber.getText().toString());
                object.put("iSpotId", spotNumber);
                object.put("iYatraNo", yatraNumber);

            } catch (JSONException e) {
                Toast.makeText(SpotActivity.this, "Something take longer time please try again..!", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }

            final JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                    Constant.PATH + "yatri/addyatrispotdetails", object,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("RESPONSE", response.toString());

                            try {
                                boolean code = response.getBoolean("status");

                                String msg = response.getString("message");
                                // Toast.makeText(this, ""+code, Toast.LENGTH_SHORT).show();
                                if (code == true) {
                                    progressDialog.dismiss();
                                    Toast.makeText(SpotActivity.this, "SuccessFull Done", Toast.LENGTH_SHORT).show();
                                    edtYatriNumber.setText("");
                                    cardViewBottom.setVisibility(View.GONE);
                                    btnYatriDone.setVisibility(View.GONE);
                                } else if (code == false) {
                                    String msg1 = response.getString("message");
                                    progressDialog.dismiss();
                                    Toast.makeText(SpotActivity.this, msg1, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                progressDialog.dismiss();
                                Toast.makeText(SpotActivity.this, "Something take longer time please try again..!", Toast.LENGTH_LONG).show();
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

    public void spot() {

        if (detector.isConnectingToInternet()) {

            progressDialog = new ProgressDialog(this);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Loading...");
            progressDialog.show();
            RequestQueue requestQueue = Volley.newRequestQueue(this);


            final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                    Constant.PATH + "spot/getall", null,
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
                                    progressDialog.dismiss();
                                    JSONArray obj = response.getJSONArray("data");
                                    for (int i = 0; i < obj.length(); i++) {
                                        JSONObject jresponse = obj.getJSONObject(i);
                                        int spotId = jresponse.getInt("iSpotId");
                                        String spotName = jresponse.getString("strSpotName");
                                        Log.d("nickname", "" + spotId + " " + spotName);
                                    }
                                } else {
                                    progressDialog.dismiss();
                                    Toast.makeText(SpotActivity.this, "Sorry", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                progressDialog.dismiss();
                                Toast.makeText(SpotActivity.this, "Something take longer time please try again..!", Toast.LENGTH_LONG).show();
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
