package com.delta.pragyan16;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QRScreen extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrscreen);
        ImageView qrCodeImage = (ImageView) findViewById(R.id.qr_code_image);
        qrCodeImage.setVisibility(View.VISIBLE);
        SaveImage saveImage = new SaveImage(Utilities.pragyan_mail, null);              //TODO change it to pid
        Bitmap bitmap = saveImage.loadFromCacheFile();
        qrCodeImage.setImageBitmap(bitmap);
        //GetQR();
        //new qrAsyncTask().execute();

    }

public void GetQR() {

    final ProgressDialog pDialog = new ProgressDialog(this);
    pDialog.setMessage("Getting QR...");
    pDialog.setCancelable(false);
    pDialog.setCanceledOnTouchOutside(false);
    pDialog.show();

    StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://www.pragyan.org/quadtest/getQR", new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            byte[] imageAsBytes = Base64.decode(response.getBytes(), Base64.DEFAULT);
            ImageView image = (ImageView) QRScreen.this.findViewById(R.id.qr_code_image);
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
            image.setVisibility(View.VISIBLE);
            image.setImageBitmap(bitmap);
            SaveImage save =  new SaveImage(Utilities.pragyan_mail, null);              //TODO change it to pid
            save.saveToCacheFile(bitmap);
            addImageToGallery(save.getCacheFilename(), QRScreen.this);

            Toast.makeText(QRScreen.this, "Image saved to gallery", Toast.LENGTH_LONG).show();
            pDialog.dismiss();
        }
    }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            pDialog.dismiss();
            error.printStackTrace();
            Toast.makeText(QRScreen.this, "Please check your internet and try again", Toast.LENGTH_LONG).show();

        }
    }) {
        @Override
        protected Map<String, String> getParams()
        {
            Map<String, String> params = new HashMap<>();
            // the POST parameters:
            int pid = 2;                                        //TODO get pid
            params.put("id", String.valueOf(pid));
            return params;
        }

    };

    int socketTimeout = 10000;//10 seconds
    RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
    stringRequest.setRetryPolicy(policy);
    Volley.newRequestQueue(this).add(stringRequest);

}

    public static void addImageToGallery(final String filePath, final Context context) {

        ContentValues values = new ContentValues();

        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        values.put(MediaStore.MediaColumns.DATA, filePath);

        context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
    }
    class qrAsyncTask extends AsyncTask<String, Void, Bitmap> {
        ProgressDialog myPd_ring = null;

        @Override
        protected void onPreExecute() {

            myPd_ring = new ProgressDialog(QRScreen.this);
            myPd_ring.setMessage("Getting QR...");
            myPd_ring.show();

        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            HttpClient httpclient = new DefaultHttpClient();
            Bitmap image = null;
            HttpPost httppost = new HttpPost(Utilities.url_qr);
            try {
                List nameValuePairs = new ArrayList();
                nameValuePairs.add(new BasicNameValuePair("user_roll", Utilities.pragyan_mail));
                nameValuePairs.add(new BasicNameValuePair("user_pass", Utilities.pragyan_pass));

                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));

                HttpResponse response = httpclient.execute(httppost);
                HttpEntity httpEntity = null;

                httpEntity = response.getEntity();
                byte[] img = EntityUtils.toByteArray(httpEntity);
                image = BitmapFactory.decodeByteArray(img, 0, img.length);


            } catch (ClientProtocolException e) {
            } catch (IOException e) {
            }
            return image;


        }

        @Override
        protected void onPostExecute(Bitmap bmp) {
            super.onPostExecute(bmp);
            myPd_ring.dismiss();
            ImageView show_image = (ImageView) findViewById(R.id.qr_code_image);
            show_image.setImageBitmap(bmp);
            SaveImage save =  new SaveImage(Utilities.pragyan_mail, null);              //TODO change it to pid
            save.saveToCacheFile(bmp);
            addImageToGallery(save.getCacheFilename(), QRScreen.this);

            Toast.makeText(QRScreen.this, "Image saved to gallery", Toast.LENGTH_LONG).show();
        }
    }
}