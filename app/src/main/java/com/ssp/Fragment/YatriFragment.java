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

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ssp.Adapter.adapterYatra;
import com.ssp.Model.beanYatra;
import com.ssp.R;
import com.ssp.Util.Constant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class YatriFragment extends Fragment {

    RecyclerView recyclerView;
    ProgressDialog progressDialog;
    beanYatra yatra;
    ArrayList<beanYatra> event;
    adapterYatra aYatra;

    public YatriFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_yatri, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recylerViewYatri);
        event = new ArrayList<beanYatra>(

        );
        detailYatra();
        return view;
    }

    public void detailYatra() {
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET,
                Constant.PATH + "Spot/getspotdetailsbyyatri?iUserDetailsId=3", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("yatra", response.toString());
                try {
                    boolean code = response.getBoolean("status");
                    if (code == true) {
                        progressDialog.dismiss();
                        JSONArray array = response.getJSONArray("data");
                        Log.d("yatra", array.toString());
                        for (int n=0;n<array.length();n++){
                            JSONObject obj = array.getJSONObject(n);
                            yatra=new beanYatra();
                            int yatraNo=obj.getInt("iYatraNo");
                            int spotNo = obj.getInt("iSpotNo");
                            String dateTime=obj.getString("datetimeYatraTime");
                            String upDown = obj.getString("strUpOrDown");
                            yatra.setYatraUpDown(upDown);
                            yatra.setYatraNo(yatraNo);
                            yatra.setSpotNo(spotNo);
                            yatra.setYatraTime(dateTime);
                            event.add(yatra);
                            aYatra = new adapterYatra(getContext(), event);
                            recyclerView.setAdapter(aYatra);
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
    }

}
