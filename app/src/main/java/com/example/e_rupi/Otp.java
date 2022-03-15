package com.example.e_rupi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

public class Otp extends AppCompatActivity {

    private Button mVerifyCodeBtn;
    private EditText otpEdit;
    private String OTP;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        mVerifyCodeBtn = findViewById(R.id.verifycode_btn);
        otpEdit = findViewById(R.id.verify_code_edit);

        firebaseAuth = FirebaseAuth.getInstance();

        OTP = getIntent().getStringExtra("auth");

        mVerifyCodeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String verification_code =  otpEdit.getText().toString();
                //If Otp is not empty,
                if(!verification_code.isEmpty()){
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(OTP, verification_code);
                    signIn(credential);
                }
                else{
                    Toast.makeText(Otp.this, "Please Enter OTP", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void signIn(PhoneAuthCredential credentials){
        firebaseAuth.signInWithCredential(credentials).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                //if task is successful then it will send user to main Activity page.
                if(task.isSuccessful()){
                    sendToMain();
                }
                //if task is not successful it will send you the exception message
                else {
                    Toast.makeText(Otp.this, "Verification Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if(currentUser != null){
            sendToMain();
        }
    }

    private void sendToMain(){
        startActivity(new Intent(Otp.this, HomePage.class ));
        finish(); // so that users can't come back
    }
}