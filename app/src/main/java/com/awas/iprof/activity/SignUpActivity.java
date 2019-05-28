package com.awas.iprof.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.awas.iprof.MainActivity;
import com.awas.iprof.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUpActivity extends AppCompatActivity {

    private Button signUpButton, linkSignUp2Login;

    private TextInputEditText email, password;

    private ProgressBar progressBar;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Creation FireBase Instance
        mAuth = FirebaseAuth.getInstance();

        // Checking FireBase Session
        if(mAuth.getCurrentUser() != null){
            startActivity(new Intent(SignUpActivity.this, MainActivity.class));
        }

        signUpButton = findViewById(R.id.sign_up_button);
        linkSignUp2Login = findViewById(R.id.link_signup2login);

        email = findViewById(R.id.text_email_signup);
        password = findViewById(R.id.text_password_signup);

        progressBar = findViewById(R.id.progressBar);

        // Button Listener (as LoginActivity)
        linkSignUp2Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String emailString = email.getText().toString().trim();
                final String passwordString = password.getText().toString().trim();

                // Help Toast if Email or Password are empty
                if(TextUtils.isEmpty(emailString)){
                    Toast.makeText(getApplicationContext(), getString(R.string.hint_email), Toast.LENGTH_LONG).show();
                    return;
                }

                if(TextUtils.isEmpty(passwordString)){
                    Toast.makeText(getApplicationContext(), getString(R.string.hint_pwd), Toast.LENGTH_LONG).show();
                    return;
                }

                // Help Toast if Password is shorter than 6 characters, else FireBase Error
                if(passwordString.length() < 6){
                    Toast.makeText(getApplicationContext(), getString(R.string.minimum_password), Toast.LENGTH_LONG).show();
                    return;
                }

                // ProgressBar Visibility
                progressBar.setVisibility(View.VISIBLE);

                mAuth.createUserWithEmailAndPassword(emailString, passwordString).addOnCompleteListener(
                        SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        // ProgressBar Visibility
                        progressBar.setVisibility(View.GONE);

                        // Check if User Signup is OK
                        if(task.isSuccessful()){
                            //Toast.makeText(getApplicationContext(), "SignUp Ok", Toast.LENGTH_LONG).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                            startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                        }else {
                            // FireBase Error in account creating
                            Toast.makeText(getApplicationContext(), getString(R.string.auth_failed), Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }
}