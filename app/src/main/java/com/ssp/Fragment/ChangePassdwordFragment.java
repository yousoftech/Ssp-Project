package com.ssp.Fragment;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.ssp.Activity.LoginActivity;
import com.ssp.R;
import com.ssp.Util.Constant;

import org.json.JSONException;
import org.json.JSONObject;

import static com.ssp.Activity.LoginActivity.PREFS_NAME;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChangePassdwordFragment extends Fragment {

    EditText edtPassword, edtNewPassword, edtReEnterPassword;
    Button btnChangePassword;
    ProgressDialog progressDialog;
    int userId;
    SharedPreferences preferences;

    public ChangePassdwordFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_change_passdword, container, false);
        preferences = getActivity().getSharedPreferences(PREFS_NAME, 0);
        userId = preferences.getInt("userId", 0);
        Toast.makeText(getContext(), "" + userId, Toast.LENGTH_SHORT).show();
        edtPassword = (EditText) view.findViewById(R.id.edt_password);
        edtNewPassword = (EditText) view.findViewById(R.id.edt_newPassword);
        edtReEnterPassword = (EditText) view.findViewById(R.id.edt_reEnterPassword);
        btnChangePassword = (Button) view.findViewById(R.id.btnSubmit);
        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edtPassword.getText().length() < 1) {
                    edtPassword.setError("Please Enter The Current Password");
                } else if (edtNewPassword.getText().length() < 1) {
                    edtNewPassword.setError("Please Enter The New Password");
                } else if (edtReEnterPassword.getText().length() < 1) {
                    edtReEnterPassword.setError("Please Enter ReEnter Password");
                } else {
                    if (edtNewPassword.getText().toString().trim().equals((edtReEnterPassword.getText().toString().trim()))) {
                        changePassword();
                    } else {
                        Toast.makeText(getActivity(), "does not match the password", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        return view;
    }

    public void changePassword() {

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        JSONObject object = new JSONObject();

        try {
            object.put("iUserDetailsId", userId);
            object.put("strNewPassword", edtNewPassword.getText().toString());
            object.put("strOldPassword", edtPassword.getText().toString());
        } catch (JSONException e) {
            Toast.makeText(getContext(), "Something take longer time please try again..!", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

        Log.d("ChangePassword", "" + object.toString());

        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.POST,
                Constant.PATH + "userMaster/updatepassword", object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.d("ChangePassword", String.valueOf(response));
                    boolean code = response.getBoolean("status");
                    if (code == true) {
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(), "Successfully Change Your Password ", Toast.LENGTH_SHORT).show();
                        SharedPreferences settings = getActivity().getSharedPreferences(PREFS_NAME, 0);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.remove("logged");
                        editor.clear();
                        editor.commit();
                        startActivity(new Intent(getContext(), LoginActivity.class));
                        getActivity().finish();

                    } else {
                        Toast.makeText(getContext(), "Something Missing", Toast.LENGTH_SHORT).show();
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
                Log.d("RESPONSE", "That didn't work!");
            }
        });
        objectRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(objectRequest);

    }
}
