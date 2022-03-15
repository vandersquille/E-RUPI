package com.example.e_rupi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomePage extends AppCompatActivity {

    private Button mLogOutBtn;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        mLogOutBtn = findViewById(R.id.log_out_btn);
        mAuth = FirebaseAuth.getInstance();

        mLogOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                startActivity(new Intent(HomePage.this, Login.class)); //We will send user from Main Activity to Login
                finish();
            }
        });

    }
//Method for Menu in Activity
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = new MenuInflater(this);
        inflater.inflate(R.menu.my_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
//To provide some actions to menu items
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Toast.makeText(this, item+" is selected", Toast.LENGTH_SHORT).show();
        return super.onOptionsItemSelected(item);
    }

    //This method will check if users are null or not
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        //If currentUser is null it means user has not logged in, so user can't access the main Activity. We will send user from Main Activity to Login.
        if(currentUser == null){
            startActivity(new Intent(HomePage.this, Login.class));
            finish();
        }
    }
}