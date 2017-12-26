package com.ssp.Activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.ssp.Model.spotYatra;
import com.ssp.R;
import com.ssp.Util.ConnectionDetector;
import com.ssp.Util.Constant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.ssp.Activity.LoginActivity.PREFS_NAME;

public class SpotActivity extends AppCompatActivity {

    EditText edtYatriNumber;
    TextView txtYatriNumber, txtYatriName, txtYatriEmailId, txtYatriMobileNo, txt, SpotNo, txtStatus, txtYatraNo;
    Button btnYatriDone, btnYatri, btnLogout;
    CardView cardViewBottom;
    Spinner spinnerSpot, spinnerYatra;
    ProgressDialog progressDialog;
    ConnectionDetector detector;
    SharedPreferences preferences;
    Toolbar toolbar;
    String yatraNumber;
    String spotNumber;
    RadioGroup radioGroup;
    RadioButton radioButton;
    spotYatra spotYatra;
    ArrayList<spotYatra> arraySpot;
    int SpotId;
    ArrayList<String> spotArr;
    boolean doubleBackToExitPressedOnce = false;

    String[] spot = {"1", "5", "10",};
    String sp = "In";
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
        SpotNo = (TextView) findViewById(R.id.SpotNo);
        txtYatraNo = (TextView) findViewById(R.id.txtYatraNo);
        txtStatus = (TextView) findViewById(R.id.txtStatus);

        cardViewBottom = (CardView) findViewById(R.id.cardBottom);
        spinnerSpot = (Spinner) findViewById(R.id.spinnerSpot);
        spinnerYatra = (Spinner) findViewById(R.id.spinnerYatra);
        btnLogout = (Button) findViewById(R.id.btnLogout);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);

     /*   sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    sp = sw.getTextOn().toString();
                } else {
                    sp = sw.getTextOff().toString();
                }
            }
        });*/

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                RadioButton rb = (RadioButton) radioGroup.findViewById(checkedId);
                if (null != rb && checkedId > -1) {

                    sp = String.valueOf(rb.getText());

                    Log.d("Event", sp);

                }
            }
        });


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

        spinnerSpot.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //spotNumber = spinnerSpot.getSelectedItem().toString();
                spotYatra spotYatra = arraySpot.get(i);
                SpotId = spotYatra.getSpotId();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


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
                object.put("strUserCode", edtYatriNumber.getText().toString());
                txtStatus.setText("" + sp);
                SpotNo.setText("" + SpotId);
                txtYatraNo.setText("" + yatraNumber);
                Log.d("SpotNo", "asd " + SpotId);
                Log.d("yatrano", "asd " + yatraNumber);
                Log.d("status", "asd " + sp);

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
                                    int yatriNo = obj.getInt("strUserCode");
                                    String firstName = obj.getString("strUserFirstName");
                                    String lastName = obj.getString("strUserLastName");
                                    String emailId = obj.getString("strUserEmailId");
                                    String MobileNo = obj.getString("strMobileNo");
                                    txtYatriName.setText(firstName + " " + lastName);
                                    txtYatriNumber.setText("" + yatriNo);
                                    txtYatriEmailId.setText(emailId);
                                    txtYatriMobileNo.setText(MobileNo);
                                    Log.d("SpotNo", "asd " + SpotId);
                                    Log.d("yatrano", "asd " + yatraNumber);
                                    Log.d("status", "asd " + sp);


                                } else {
                                    progressDialog.dismiss();
                                    Toast.makeText(SpotActivity.this, msg, Toast.LENGTH_SHORT).show();
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
                object.put("strUserCode", edtYatriNumber.getText().toString());
                object.put("iSpotId", SpotId);
                Log.d("spot", "Spot" + SpotId);
                object.put("iYatraNo", yatraNumber);
                object.put("strUpOrDown", sp);

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
                                    Toast.makeText(SpotActivity.this, msg, Toast.LENGTH_SHORT).show();
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
                                    arraySpot = new ArrayList<>();
                                    spotArr = new ArrayList<>();

                                    progressDialog.dismiss();
                                    JSONArray obj = response.getJSONArray("data");
                                    for (int i = 0; i < obj.length(); i++) {
                                        JSONObject jresponse = obj.getJSONObject(i);
                                        int spotId = jresponse.getInt("iSpotId");
                                        String spotNo = jresponse.getString("iSpotNo");
                                        spotYatra = new spotYatra();
                                        spotYatra.setSpotId(spotId);
                                        spotYatra.setSpotNo(spotNo);
                                        spotArr.add("Spot No: " + spotYatra.getSpotNo());
                                        arraySpot.add(spotYatra);
                                        Log.d("nickname", "" + spotId + " " + spotNo);
                                    }
                                    ArrayAdapter sp = new ArrayAdapter(SpotActivity.this, android.R.layout.simple_spinner_item, spotArr);
                                    sp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    //Setting the ArrayAdapter data on the Spinner
                                    spinnerSpot.setAdapter(sp);


                                    ArrayAdapter aa = new ArrayAdapter(SpotActivity.this, android.R.layout.simple_spinner_item, yatra);
                                    aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    //Setting the ArrayAdapter data on the Spinner
                                    spinnerYatra.setAdapter(aa);

                                } else {
                                    progressDialog.dismiss();
                                    Toast.makeText(SpotActivity.this, msg, Toast.LENGTH_SHORT).show();
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

                final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(SpotActivity.this);
                LayoutInflater inflater = SpotActivity.this.getLayoutInflater();
                dialogBuilder.setMessage("Are you sure you want to logout ?");

                dialogBuilder.setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        SharedPreferences.Editor editor = preferences.edit();
                        editor.remove("logged");
                        editor.clear();
                        editor.commit();
                        finish();
                        startActivity(new Intent(SpotActivity.this, LoginActivity.class));
                        Toast.makeText(getApplicationContext(), "Logout", Toast.LENGTH_SHORT).show();
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

}
