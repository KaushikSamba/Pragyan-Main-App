package com.delta.pragyan16;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class ProfileActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources()
                .getColor(R.color.background_floating_material_dark)));
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#ffffff'> Profile </font>"));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            //Logout
            SharedPreferences.Editor editor = Utilities.sp.edit();
            editor.putInt("status", 0);
            editor.putString("pragyan_mail", null);
            editor.putString("pragyan_pass", null);
            editor.apply();
            Intent intent = new Intent(ProfileActivity.this, SplashScreen.class);
            startActivity(intent);
            finish();

            return true;
        }
        if (id==R.id.qr_code)
        {
            //Open QR Code activity
            Intent intent = new Intent(ProfileActivity.this, QRScreen.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}