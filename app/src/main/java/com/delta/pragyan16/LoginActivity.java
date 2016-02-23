package com.delta.pragyan16;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends ActionBarActivity {


    String emailIdString, passwordString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        Button signInButton = (Button) findViewById(R.id.signInButton);
        TextView registerText = (TextView) findViewById(R.id.registerText);


        final EditText emailIdEdit, passwordEdit;
        emailIdEdit = (EditText) findViewById(R.id.emailIdEdit);
        passwordEdit = (EditText) findViewById(R.id.password);

        CheckBox checkBox = (CheckBox)findViewById(R.id.showPasswordCheckBox);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    passwordEdit.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                if (!isChecked) {
                    passwordEdit.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performSignIn(); //TODO: delete this CRap
                emailIdString = emailIdEdit.getText().toString();
                passwordString = passwordEdit.getText().toString();
                if (emailIdString.length() == 0) emailIdEdit.setError("Invalid username");
                else if (passwordString.length() == 0) passwordEdit.setError("Invalid password");
//                else performSignIn();
            }
        });

        registerText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent i = new Intent(LoginActivity.this,RegisterActivity.class);
                    startActivity(i);
            }
        });
    }

    public void performSignIn() {

        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.show();

        StringRequest postRequest = new StringRequest(Request.Method.POST, Utilities.url_auth,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            int status = jsonResponse.getInt("status");
                            String error = jsonResponse.getString("data");
                            pDialog.dismiss();
                            switch (status) {
                                case 0:
                                    Toast.makeText(LoginActivity.this, "There was a problem connecting to the server. Please check your username and password and try again.", Toast.LENGTH_LONG).show();
                                    break;
                                case 1:
//STORING USER LOGIN DATA IN SHARED PREFERENCES
                                    SharedPreferences.Editor editor = Utilities.sp.edit();
                                    Utilities.status = 1;
                                    editor.putInt("status", Utilities.status);
                                    editor.putString("pragyan_mail", emailIdString);
                                    Utilities.pragyan_mail = emailIdString;
                                    editor.putString("pragyan_pass", passwordString);
                                    Utilities.pragyan_pass = passwordString;
                                    editor.apply();


                                    break;

                                case 3:
                                    Toast.makeText(LoginActivity.this, error, Toast.LENGTH_SHORT).show();
                                    break;
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.dismiss();
                        error.printStackTrace();
                        Toast.makeText(LoginActivity.this, "Please check your internet and try again", Toast.LENGTH_LONG).show();

                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                // the POST parameters:
                params.put("user_email", emailIdString);
                params.put("user_pass", passwordString);
                return params;
            }
        };
        int socketTimeout = 10000; //10 seconds
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        Volley.newRequestQueue(this).add(postRequest);
    }


}
