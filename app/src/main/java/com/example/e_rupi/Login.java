package com.example.e_rupi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class Login extends AppCompatActivity {

    private Button mSendOTPBtn;
    private TextView processText;
    private EditText countryCodeEdit, phoneNumberEdit;
    private FirebaseAuth auth;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mSendOTPBtn = findViewById(R.id.send_codebtn);
        processText = findViewById(R.id.text_process);
        countryCodeEdit = findViewById(R.id.input_country_code);
        phoneNumberEdit = findViewById(R.id.input_phone);

        auth = FirebaseAuth.getInstance();

        mSendOTPBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String country_code = countryCodeEdit.getText().toString();
                String phone = phoneNumberEdit.getText().toString();
                String phoneNumber = "+" + country_code + "" + phone; //Complete phone Number

                //if country code & phone number are not  empty, then set a phone numer within 60 sec time limit in login page
                if(!country_code.isEmpty() || !phone.isEmpty()){
                    PhoneAuthOptions options =  PhoneAuthOptions.newBuilder(auth).setPhoneNumber(phoneNumber).setTimeout(60L, TimeUnit.SECONDS).setActivity(Login.this).setCallbacks(mCallBacks).build();
                    PhoneAuthProvider.verifyPhoneNumber(options);
                }
                //if country code & phone no. are empty then put a message
                else{
                    processText.setText("Please Enter Country Code and Phone Number");
                    processText.setTextColor(Color.RED);
                    processText.setVisibility(View.VISIBLE);
                }
            }
        });

        mCallBacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            //this method does verification automatically, which will send user to main Activity i.e SignIn occurs
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                signIn(phoneAuthCredential);
            }

            //This method is used if verification of otp is not successful
            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
            processText.setText(e.getMessage());
            processText.setTextColor(Color.RED);
            processText.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                //sometime the code is not detected automatically
                //so user has to manually enter the code
                processText.setText("OTP has been Sent");
                processText.setVisibility(View.VISIBLE);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //Sends OTP from login page to otp page
                        Intent otpIntent = new Intent(Login.this, Otp.class);
                        otpIntent.putExtra("auth", s); //s indicates string s
                        startActivity(otpIntent);
                    }
                }, 10000);



            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
//Checks if user not eauals null, if so send it to Main Activity
        FirebaseUser user = auth.getCurrentUser();
        if(user != null){
            sendToMain();
        }

    }
    // This method sends user from login to main Activity
    private  void  sendToMain(){
        Intent mainIntent = new Intent(Login.this, HomePage.class);
        startActivity(mainIntent);
        finish(); // So that user can't get back here
    }

    private void signIn(PhoneAuthCredential credentials){
        auth.signInWithCredential(credentials).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                //if task is successful then it will send user to main Activity page.
                if(task.isSuccessful()){
                    sendToMain();
                }
                //if task is not successful it will send you the exception message
                else {
                    processText.setText(task.getException().getMessage());
                    processText.setTextColor(Color.RED);
                    processText.setVisibility(View.VISIBLE);
                }
            }
        });
    }
}