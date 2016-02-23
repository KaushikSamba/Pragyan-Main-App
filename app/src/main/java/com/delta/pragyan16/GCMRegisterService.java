package com.delta.pragyan16;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.util.HashMap;
import java.util.Map;

public class GCMRegisterService extends IntentService {
    static Context context;
    static String msg;
    Handler toastHandler;
    public static void register(Context cont) {
        context = cont;
        Intent intent = new Intent(cont, GCMRegisterService.class);
        cont.startService(intent);
    }

    public GCMRegisterService() {
        super("GCMRegisterService");
    }
    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i("gcm_status","intent service called");
        InstanceID instanceID = InstanceID.getInstance(this);
        try {
            final String token = instanceID.getToken(getString(R.string.gcm_defaultSenderId),
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            Log.i("gcm_status",token.toString());
            String serverUrl = Utilities.url_gcm;
            Log.e("gcm_status", "Attempt to register");
            StringRequest stringRequest = new StringRequest(Request.Method.POST, serverUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    msg = response;
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    Log.e("gcm_status", "Failed to register :" + error);
                    toastHandler.post(new DisplayToast(context, "Please check your internet and try again"));
                }
            }){
                @Override
                protected Map<String, String> getParams()
                {
                    Map<String, String> params = new HashMap<>();
                    // the POST parameters:
                    params.put("p_id", "1024");//Utilities._id);
                    params.put("gcm_id", token);
                    return params;
                }
            };
            // Add the request to the queue
            int socketTimeout = 10000;//10 seconds
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            stringRequest.setRetryPolicy(policy);
            Volley.newRequestQueue(context).add(stringRequest);
        }catch(Exception e){
            e.printStackTrace();
        }

        /*
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_FOO.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionFoo(param1, param2);
            } else if (ACTION_BAZ.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionBaz(param1, param2);
            }
        }*/
    }
    private void handleActionBaz(String param1, String param2) {
        // TODO: Handle action Baz
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
