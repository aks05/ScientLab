package com.example.scientlab;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.icu.text.CaseMap;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.os.Handler;
import android.view.View;

import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.view.Menu;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    String title, message;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LocalBroadcastManager.getInstance(this).registerReceiver(mHandler, new IntentFilter("com.example.scientlab_FCM-MESSAGE"));
        setContentView(R.layout.activity_main);
        if (getIntent().getExtras()!=null) {
            for(String key: getIntent().getExtras().keySet())
            {
                if(key.equals("title"))
                    title= getIntent().getExtras().getString(key);
                else if(key.equals("message"))
                    message= getIntent().getExtras().getString(key);

            }
            Intent intent= new Intent(getApplicationContext(), ShowProject.class);
            intent.putExtra("message", message);
            startActivity(intent);
            finish();
        }
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Intent intent;

        if (id == R.id.nav_home) {

        } else if (id == R.id.nav_register) {
            intent= new Intent(MainActivity.this, RegisterActivity.class);
            startActivity(intent);
            finish();

        } else if (id == R.id.nav_showProject) {
            intent= new Intent(MainActivity.this, ShowProject.class);
            startActivity(intent);
            finish();

        } else if (id == R.id.nav_contactUs) {
            intent= new Intent(MainActivity.this, ContactUs.class);
            startActivity(intent);
            finish();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private BroadcastReceiver mHandler= new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            message= intent.getStringExtra("message");
            Intent mIntent= new Intent(getApplicationContext(), ShowProject.class);
            intent.putExtra("message", message);
            startActivity(mIntent);
            finish();

        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mHandler);
    }
}
