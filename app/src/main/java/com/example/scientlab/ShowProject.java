package com.example.scientlab;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

public class ShowProject extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    String message, Token;
    private DatabaseReference mDatabaseReference;
    TextView tvName, tvRollNo, tvContact, tvEmail, tvProjectTitle, tvProjectIdea, tvResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_project);
        if(getIntent().getExtras()!=null) {
            message= getIntent().getExtras().getString("message");
        }
        else {
            SharedPreferences sharedPreferences= getSharedPreferences("preference", Context.MODE_PRIVATE);
            message= sharedPreferences.getString("message", "No Response");
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

        tvName=findViewById(R.id.tvName);
        tvRollNo= findViewById(R.id.tvRollNo);
        tvContact= findViewById(R.id.tvContactNo);
        tvEmail= findViewById(R.id.tvEmail);
        tvProjectTitle= findViewById(R.id.tvProjectTitle);
        tvProjectIdea= findViewById(R.id.tvProjectIdea);
        tvResponse= findViewById(R.id.tvResponse);

        mDatabaseReference= FirebaseDatabase.getInstance().getReference();
        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                if (!task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Failed to Submit", Toast.LENGTH_SHORT).show();
                    return;
                }

                Token = task.getResult().getToken();

                mDatabaseReference.child(Token).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Project project= dataSnapshot.getValue(Project.class);
                        tvName.setText(project.name);
                        tvRollNo.setText(project.roll_No);
                        tvContact.setText(project.contact_No);
                        tvEmail.setText(project.email);
                        tvProjectTitle.setText(project.projectTitle);
                        tvProjectIdea.setText(project.projectDesc);
                        tvResponse.setText(message);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                        Toast.makeText(getApplicationContext(), "Failed to load", Toast.LENGTH_SHORT);
                    }
                });
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences sharedPreferences= getSharedPreferences("preference", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor= sharedPreferences.edit();

        editor.putString("message", message);
        editor.apply();
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
            intent= new Intent(ShowProject.this, MainActivity.class);
            startActivity(intent);
            finish();

        } else if (id == R.id.nav_register) {
            intent= new Intent(ShowProject.this, RegisterActivity.class);
            startActivity(intent);
            finish();

        } else if (id == R.id.nav_showProject) {


        } else if (id == R.id.nav_contactUs) {
            intent= new Intent(ShowProject.this, ContactUs.class);
            startActivity(intent);
            finish();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
