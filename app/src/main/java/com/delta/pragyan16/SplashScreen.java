package com.delta.pragyan16;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class SplashScreen extends Activity {

    ImageView images[] = new ImageView[6];
    String regid = new String();
    GoogleCloudMessaging gcm = null;
    Animation animations[] = new Animation[6];
    ImageView bigLogo;
    int i;
    String msg = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        GetEventsAPI g = new GetEventsAPI(SplashScreen.this, getApplicationContext());
        g.execute();
        GCMRegisterService.register(this);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startAnimations();
            }
        }, 1000);
    }

    private void startAnimations() {

        bigLogo = (ImageView) findViewById(R.id.pragyan_logo_large);
        for (i = 0; i < 6; i++) {
            images[i] = (ImageView) findViewById(getResources().getIdentifier("logo_app_" + i, "id", "com.delta.pragyan16"));
            animations[i] = AnimationUtils.loadAnimation(SplashScreen.this, R.anim.zoom_chakra_in);
            images[i].startAnimation(animations[i]);
            animations[i].setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    for (int i = 0; i < 6; i++) {
                        if (i % 2 == 0)
                            animations[i] = AnimationUtils.loadAnimation(SplashScreen.this, R.anim.zoom_chakra_out_left);
                        else
                            animations[i] = AnimationUtils.loadAnimation(SplashScreen.this, R.anim.zoom_chakra_out_right);
                        images[i].startAnimation(animations[i]);
                        animations[i].setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                for (int i = 0; i < 6; i++)
                                    images[i].setVisibility(View.INVISIBLE);
                                bigLogo.setVisibility(View.VISIBLE);
                                animations[0] = AnimationUtils.loadAnimation(SplashScreen.this, R.anim.pragyan_logo_bounce);
                                animations[0].setAnimationListener(new Animation.AnimationListener() {
                                    @Override
                                    public void onAnimationStart(Animation animation) {

                                    }

                                    @Override
                                    public void onAnimationEnd(Animation animation) {

                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                Utilities.sp = getSharedPreferences("llep", 0);
                                                Utilities.status = Utilities.sp.getInt("status", 0);
                                                switch (Utilities.status) {
                                                    case 0: //Not registered/logged in, go to LoginActivity

                                                        SplashScreen.this.startActivity(new Intent(SplashScreen.this, LoginActivity.class));
                                                        SplashScreen.this.finish();
                                                        break;

                                                    case 1: //Logged in, go to home page.

                                                        SplashScreen.this.startActivity(new Intent(SplashScreen.this, MainActivity.class));
                                                        SplashScreen.this.finish();
                                                        break;
                                                }


                                            }
                                        }, 1000);
                                    }

                                    @Override
                                    public void onAnimationRepeat(Animation animation) {

                                    }
                                });
                                bigLogo.setAnimation(animations[0]);
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });
                    }
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }

    }

    class GetEventsAPI extends AsyncTask<Void, Void, Boolean> {                            //   todo call once if the database is empty
        //ProgressDialog dialog;
        JSONObject jsonObject = null, descriptionObject = null;
        EventsAdapter eventsAdapter;
        Context context;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //dialog.setMessage("Fetching Events Data...");
            //dialog.setCancelable(false);
            //dialog.show();
            eventsAdapter = new EventsAdapter(context);
        }

        public GetEventsAPI(Activity activity, Context c) {
            this.context = c;
            //dialog = new ProgressDialog(activity);
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            String jsonstring = null;
            String description = null;
            HttpClient client = new DefaultHttpClient();
            try {
                HttpGet request = new HttpGet(MainActivity.URL + "/events/list");
                HttpResponse response = client.execute(request);
                HttpEntity httpEntity = response.getEntity();
                if (httpEntity != null) {
                    InputStream inputStream = httpEntity.getContent();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"), 8);
                    StringBuilder sb = new StringBuilder();
                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    inputStream.close();
                    jsonstring = sb.toString();
                    Log.i("json", jsonstring);
                    jsonObject = new JSONObject(jsonstring);
                }
                request = new HttpGet(MainActivity.URL + "/events/desclist");
                response = client.execute(request);
                httpEntity = response.getEntity();
                if (httpEntity != null) {
                    InputStream inputStream = httpEntity.getContent();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"), 8);
                    StringBuilder sb = new StringBuilder();
                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    inputStream.close();
                    description = sb.toString();
                    Log.i("json", jsonstring);
                    descriptionObject = new JSONObject(description);
                }
            } catch (IOException e) {
                Log.e("Buffer Error", "Error parsing result " + e.toString());
            } catch (JSONException e) {
                Log.e("JSON Error", "Error parsing result " + e.toString());
            }
            if ((jsonstring != null && jsonObject != null) || (description != null && descriptionObject != null)) {
                try {
                    if (jsonObject.getJSONArray("data") != null && descriptionObject.getJSONArray("data") != null)
                        return true;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            //if(dialog.isShowing())
            // dialog.dismiss();
            if (aBoolean) {
                try {
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    JSONArray descArray = descriptionObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject tempjsonObject = jsonArray.getJSONObject(i);
                        JSONObject tempdescObject = descArray.getJSONObject(i);
                        if (tempdescObject.getInt("event_id") != tempjsonObject.getInt("event_id"))
                            Log.i("JSON Events", "Mismatched contents in the API");
                        EventInfo eventInfo = new EventInfo();
                        eventInfo.name = tempjsonObject.getString("event_name");
                        eventInfo.id = tempjsonObject.getInt("event_id");
                        eventInfo.cluster = tempjsonObject.getString("event_cluster").toUpperCase();
                        eventInfo.start_time = tempjsonObject.getString("event_start_time");
                        eventInfo.end_time = tempjsonObject.getString("event_end_time");
                        eventInfo.last_update_time = tempjsonObject.getString("event_last_update_time");
                        eventInfo.venue = tempjsonObject.getString("event_venue");
                        eventInfo.date = tempjsonObject.getString("event_date");
                        eventInfo.description = tempdescObject.getString("event_desc");
                        eventsAdapter.add_event(eventInfo);
                    }

                    Log.i("Cluster", eventsAdapter.getCluster().toString());
                    Log.i("Event", eventsAdapter.getEventnamesOfCluster("Amalgam").toString());
                    Log.i("Event Info", eventsAdapter.getEventInfo("Inspinature").description);

                } catch (JSONException e) {
                    Log.i("JSON", " Json exception in on post Execute");
                }
            } else {
                Toast.makeText(getApplicationContext(), "Poor connectivity. Failed to update!", Toast.LENGTH_SHORT).show();
            }
        }
    }


}
