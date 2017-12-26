package com.ssp.Fragment;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ssp.Adapter.adapterYatra;
import com.ssp.Adapter.adapterYatri;
import com.ssp.Model.YatriDetails;
import com.ssp.Model.spotYatra;
import com.ssp.R;
import com.ssp.Util.ConnectionDetector;
import com.ssp.Util.Constant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class YatraSummaryFragment extends Fragment {

    Spinner spinnerAdmin1, spinnerAdmin2;
    Button btnShow;
    RecyclerView recyclerView;
    ProgressDialog progressDialog;
    ConnectionDetector detector;
    spotYatra spotYatra;
    ArrayList<spotYatra> arraySpot;
    ArrayList<String> spotArr;
    int SpotId1, SpotId2;
    //yatriNumber yNumber;
    YatriDetails yatriDetails;
    //ArrayList<yatriNumber> event;
    ArrayList<YatriDetails> YatriEvent;
    adapterYatri aYatri;
    adapterYatra ayatraDetails;

    RadioGroup radioGroup;
    RadioButton radioButton;
    String sp = "In";

    public YatraSummaryFragment() {
        // Required empty public constructor
    }

    public static YatraSummaryFragment newInstance() {
        YatraSummaryFragment fragment = new YatraSummaryFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_yatra_summary, container, false);
        detector = new ConnectionDetector(getContext());
        spinnerAdmin1 = (Spinner) view.findViewById(R.id.spinnerAdminSpot);
        radioGroup = (RadioGroup) view.findViewById(R.id.radioGroup);
        btnShow = (Button) view.findViewById(R.id.btnSubmitReport);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerAdmin);
        spinnerAdmin1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                spotYatra spotYatra = arraySpot.get(i);
                SpotId1 = spotYatra.getSpotId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

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
        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getYatris();

            }
        });
        spot();
        //  event = new ArrayList<yatriNumber>();
        YatriEvent = new ArrayList<YatriDetails>();
        return view;
    }

    public void spot() {
        if (detector.isConnectingToInternet()) {

            progressDialog = new ProgressDialog(getContext());
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Loading...");
            progressDialog.show();
            RequestQueue requestQueue = Volley.newRequestQueue(getContext());


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
                                    ArrayAdapter sp = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, spotArr);
                                    sp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    //Setting the ArrayAdapter data on the Spinner
                                    spinnerAdmin1.setAdapter(sp);
                                } else {
                                    progressDialog.dismiss();
                                    Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
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

    public void getYatris() {
        if (detector.isConnectingToInternet()) {
            progressDialog = new ProgressDialog(getContext());
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Loading...");
            progressDialog.show();

            JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET,
                    Constant.PATH + "Reports/getdetailsbetweenspots?iSpotFrom=" + SpotId1 + "&strUpOrDown=" + sp, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.d("yatra", response.toString());
                    try {
                        boolean code = response.getBoolean("status");
                        if (code == true) {
                            progressDialog.dismiss();
                            JSONArray array = response.getJSONArray("data");
                            Log.d("yatridetails", array.toString());
                            for (int n = 0; n < array.length(); n++) {
                                JSONObject obj = array.getJSONObject(n);
                                YatriDetails yatra = new YatriDetails();
                                int yatraNo = obj.getInt("iYatraNo");
                                int spotNo = obj.getInt("iSpotNo");
                                String UserCode = obj.getString("strUserCode");
                                String strUserName = obj.getString("strUserName");
                                int iUserid = obj.getInt("iUserDetailsId");
                                String upDown = obj.getString("strUpOrDown");
                                Log.d("yatraNo", yatraNo + "");
                                Log.d("spotNo", spotNo + "");
                                Log.d("UserCode", UserCode);
                                Log.d("strUserName", strUserName);
                                Log.d("upDown", upDown);
                                Log.d("iUserid", iUserid + "");

                                yatra.setiSpotId(spotNo);
                                yatra.setiYatraNo(yatraNo);
                                yatra.setiUserDetailsId(iUserid);
                                yatra.setStrUserCode(UserCode);
                                yatra.setStrUserName(strUserName);
                                yatra.setStrUpOrDown(upDown);
                                YatriEvent.add(yatra);
                                aYatri = new adapterYatri(getContext(), YatriEvent);
                                //ayatraDetails = new adapterYatra(getContext(), YatriEvent);
                                recyclerView.setAdapter(aYatri);
                                recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                                progressDialog.dismiss();
                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    if (progressDialog != null)
                        progressDialog.dismiss();
                }
            });
            objectRequest.setRetryPolicy(new DefaultRetryPolicy(
                    30000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            RequestQueue requestQueue = Volley.newRequestQueue(getContext());
            requestQueue.add(objectRequest);
        } else {
            Toast.makeText(getContext(), "Please check your internet connection before verification..!", Toast.LENGTH_LONG).show();
        }
    }

//    public void YatriSpotDetail() {
//
//        if (detector.isConnectingToInternet()) {
//            progressDialog = new ProgressDialog(getContext());
//            progressDialog.setCancelable(false);
//            progressDialog.setMessage("Loading...");
//            progressDialog.show();
//
//            JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET,
//                    Constant.PATH + "Spot/getspotdetailsbyyatri?iUserDetailsId=", null, new Response.Listener<JSONObject>() {
//                @Override
//                public void onResponse(JSONObject response) {
//                    Log.d("yatra", response.toString());
//                    try {
//                        boolean code = response.getBoolean("status");
//                        if (code == true) {
//                            progressDialog.dismiss();
//                            JSONArray array = response.getJSONArray("data");
//                            Log.d("yatra", array.toString());
//                            for (int n = 0; n < array.length(); n++) {
//                                JSONObject obj = array.getJSONObject(n);
//                                yNumber = new yatriNumber();
//                                int yatraNo = obj.getInt("iYatraNo");
//                                yNumber.setYatriNumber(yatraNo);
//                                event.add(yNumber);
//                                aYatri = new adapterYatri(getContext(), event);
//                                recyclerView.setAdapter(aYatri);
//                                recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
//                                progressDialog.dismiss();
//                            }
//                        }
//
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//
//                }
//            }, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
//
//                    if (progressDialog != null)
//                        progressDialog.dismiss();
//                }
//            });
//            objectRequest.setRetryPolicy(new DefaultRetryPolicy(
//                    30000,
//                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//            RequestQueue requestQueue = Volley.newRequestQueue(getContext());
//            requestQueue.add(objectRequest);
//        } else {
//            Toast.makeText(getContext(), "Please check your internet connection before verification..!", Toast.LENGTH_LONG).show();
//        }
//
//    }
}
