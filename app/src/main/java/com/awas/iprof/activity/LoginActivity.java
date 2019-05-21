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

public class LoginActivity extends AppCompatActivity {

    private Button loginButton, linklogin2signup;

    private TextInputEditText email, password;

    private ProgressBar progressBar;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        // Creazione dell'istanza per connessione a FireBase
        mAuth = FirebaseAuth.getInstance();

        // Controllo della Sessione su FireBase
        if (mAuth.getCurrentUser() != null){
            // Se presente Sessione Avvio direttamente nuova activity
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
        }

        loginButton = findViewById(R.id.login_button);
        linklogin2signup = findViewById(R.id.link_login2signup);

        email = findViewById(R.id.text_email_login);
        password = findViewById(R.id.text_password_login);

        progressBar = findViewById(R.id.progressBar);

        // Listener interno del Bottone di Login2SignUp
        linklogin2signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            }
        });

        // Listener interno del Bottone di Login
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String passwordString = password.getText().toString().trim();
                String emailString = email.getText().toString().trim();

                // Controllo se Email Pwd no vuota, se si fa apparire Toast con Help
                if(TextUtils.isEmpty(emailString)){
                    Toast.makeText(getApplicationContext(), getString(R.string.hint_email), Toast.LENGTH_LONG).show();
                    return;
                }

                if(TextUtils.isEmpty(passwordString)){
                    Toast.makeText(getApplicationContext(), getString(R.string.hint_pwd), Toast.LENGTH_LONG).show();
                    return;
                }

                if(passwordString.length() < 6){
                    Toast.makeText(getApplicationContext(), getString(R.string.minimum_password), Toast.LENGTH_LONG).show();
                    return;
                }

                // Rende ProgressBar Visibile
                progressBar.setVisibility(View.VISIBLE);

                mAuth.signInWithEmailAndPassword(emailString, passwordString).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        // Rende ProgressBar non Visibile
                        progressBar.setVisibility(View.GONE);

                        // Controllo se Login andato a buon fine
                        if (task.isSuccessful()){
                            Toast.makeText(getApplicationContext(), "Login OK", Toast.LENGTH_LONG).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                            startActivity(new Intent(LoginActivity.this, MainActivity.class)); // Avvio nuova Activity
                        }else {
                            Toast.makeText(getApplicationContext(), getString(R.string.auth_failed), Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }
}
