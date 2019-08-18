package com.example.scientlab;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.widget.EditText;
import android.widget.Toast;


public class RegisterActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private EditText etName, etRollNo, etContact, etEmail, etProjectTitle, etProjectIdea;
    private String Name, RollNo, Contact, Email, ProjectTitle, ProjectIdea, Token;

    private DatabaseReference mDatabaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        etName= findViewById(R.id.etName);
        etRollNo= findViewById(R.id.etRoll);
        etContact= findViewById(R.id.etContact);
        etEmail= findViewById(R.id.etEmail);
        etProjectTitle= findViewById(R.id.etProjectTitle);
        etProjectIdea= findViewById(R.id.etProjectIdea);

        mDatabaseReference= FirebaseDatabase.getInstance().getReference();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Submit Project", Snackbar.LENGTH_LONG)
                        .setAction("Ok", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Name = etName.getText().toString();
                                Contact=etContact.getText().toString();
                                RollNo= etRollNo.getText().toString();
                                Email = etEmail.getText().toString();
                                ProjectTitle = etProjectTitle.getText().toString();
                                ProjectIdea = etProjectIdea.getText().toString();

                                if (TextUtils.isEmpty(Name) || TextUtils.isEmpty(Email)
                                        || TextUtils.isEmpty(ProjectIdea)
                                        || TextUtils.isEmpty(ProjectTitle) || TextUtils.isEmpty(Contact)
                                        || TextUtils.isEmpty(RollNo)){
                                    Toast.makeText(getApplicationContext(),
                                            "Fill all the Entries", Toast.LENGTH_SHORT).show();
                                }

                                else {
                                    FirebaseInstanceId.getInstance().getInstanceId()
                                            .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                                                @Override
                                                public void onComplete(@NonNull Task<InstanceIdResult> task) {
                                                    if (!task.isSuccessful()) {
                                                        Toast.makeText(getApplicationContext(), "Failed to Submit", Toast.LENGTH_SHORT).show();
                                                        return;
                                                    }

                                                    Token = task.getResult().getToken();
                                                    Project project= new Project(Name, RollNo, Contact, Email, ProjectTitle, ProjectIdea, Token);

                                                    mDatabaseReference.child(Token).setValue(project).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            Toast.makeText(getApplicationContext(), "Project Submitted", Toast.LENGTH_SHORT).show();
                                                            Intent intent= new Intent(getApplicationContext(), ShowProject.class);
                                                            startActivity(intent);
                                                            RegisterActivity.this.finish();
                                                        }
                                                    });
                                                }
                                            });
                                }

                            }
                        }).show();
            }
        });
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
            intent= new Intent(RegisterActivity.this, MainActivity.class);
            startActivity(intent);
            finish();

        } else if (id == R.id.nav_register) {


        } else if (id == R.id.nav_showProject) {
            intent= new Intent(RegisterActivity.this, ShowProject.class);
            startActivity(intent);
            finish();

        } else if (id == R.id.nav_contactUs) {
            intent= new Intent(RegisterActivity.this, ContactUs.class);
            startActivity(intent);
            finish();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
