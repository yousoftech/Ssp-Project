package com.ssp.Fragment;


import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ssp.Activity.AdminActivity;
import com.ssp.R;
import com.ssp.Util.ConnectionDetector;
import com.ssp.Util.Constant;

import org.json.JSONException;
import org.json.JSONObject;

import static android.content.Context.MODE_PRIVATE;
import static com.ssp.Activity.LoginActivity.PREFS_NAME;

/**
 * A simple {@link Fragment} subclass.
 */
public class YatriStatusFragment extends Fragment {

    Button btnLogout, btnSubmit;
    EditText edtEnterYatraNumber;
    CardView cardViewBottom, cardView;
    TextView txtYatraName, txtYatraNumber, txtYatraMobileNo, txtYatraEmailId, txtLastYatra, txtLastSopt, txtLastCrossSpotTime;
    TextView txtYatraNumber1, txtYatraSpot1, txtYatraTime, txtUpDown;
    SharedPreferences preferences;
    ProgressDialog progressDialog;
    ConnectionDetector detector;


    public YatriStatusFragment() {
        // Required empty public constructor
    }

    public static YatriStatusFragment newInstance() {
        YatriStatusFragment fragment = new YatriStatusFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_yatri_status, container, false);
        detector = new ConnectionDetector(getContext());
        preferences = getContext().getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        cardViewBottom = (CardView) view.findViewById(R.id.cardAdminBottom);
        btnLogout = (Button) view.findViewById(R.id.btnLogout);
        btnSubmit = (Button) view.findViewById(R.id.btnAdminSubmit);
        cardView = (CardView) view.findViewById(R.id.cardView);
        txtYatraNumber1 = (TextView) view.findViewById(R.id.txtAdminYatraNumber);
        txtYatraSpot1 = (TextView) view.findViewById(R.id.txtAdminSNumber);
        txtYatraTime = (TextView) view.findViewById(R.id.txtAdminDateTime);
        txtUpDown = (TextView) view.findViewById(R.id.txtAdminUpDown);
        edtEnterYatraNumber = (EditText) view.findViewById(R.id.edtAdminYatriNumber);
        txtYatraName = (TextView) view.findViewById(R.id.txtAdminYatriName);
        txtYatraNumber = (TextView) view.findViewById(R.id.txtAdminYatriNumber);
        txtYatraMobileNo = (TextView) view.findViewById(R.id.txtAdminYatriMobileNo);
        txtYatraEmailId = (TextView) view.findViewById(R.id.txtAdminYatriEmailId);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edtEnterYatraNumber.getText().length() < 1) {
                    edtEnterYatraNumber.setError("Please Enter UserName");
                } else {
                    yatriDetail();
                }
            }
        });
        return view;
    }

    private void yatriDetail() {

        if (detector.isConnectingToInternet()) {

            progressDialog = new ProgressDialog(getContext());
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Loading...");
            progressDialog.show();
            RequestQueue requestQueue = Volley.newRequestQueue(getContext());


            final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                    Constant.PATH + "Reports/getcurrentspot?strUserCode=" + edtEnterYatraNumber.getText().toString(), null,
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
                                    JSONObject obj1 = obj.getJSONObject("vuser");
                                    JSONObject obj2 = obj.getJSONObject("yatraDetails");
                                    cardViewBottom.setVisibility(View.VISIBLE);
                                    cardView.setVisibility(View.VISIBLE);
                                    int yatriNo = obj1.getInt("strUserCode");
                                    String firstName = obj1.getString("strUserFirstName");
                                    String lastName = obj1.getString("strUserLastName");
                                    String emailId = obj1.getString("strUserEmailId");
                                    String MobileNo = obj1.getString("strMobileNo");
                                    int YatraNo = obj2.getInt("iYatraNo");
                                    int lastSpotNo = obj2.getInt("iSpotNo");
                                    String lastTime = obj2.getString("datetimeYatraTime");
                                    String upDown = obj2.getString("strUpOrDown");
                                    txtYatraName.setText("Name: " + firstName + " " + lastName);
                                    txtYatraNumber.setText("Yatri Number: " + yatriNo);
                                    txtYatraEmailId.setText("EmailId: " + emailId);
                                    txtYatraMobileNo.setText("Mobile No: " + MobileNo);
                                    txtYatraNumber1.setText("" + YatraNo);
                                    txtYatraSpot1.setText("" + lastSpotNo);
                                    txtYatraTime.setText("" + lastTime);
                                    txtUpDown.setText(upDown);

                                } else {
                                    progressDialog.dismiss();
                                    Toast.makeText(getContext(), "Sorry", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                progressDialog.dismiss();
                                Toast.makeText(getContext(), "Something take longer time please try again..!", Toast.LENGTH_LONG).show();
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
            Toast.makeText(getContext(), "Please check your internet connection before verification..!", Toast.LENGTH_LONG).show();
        }

    }

}
