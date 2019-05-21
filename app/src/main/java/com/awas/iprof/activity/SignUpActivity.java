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

        // Crezione instanza di FireBase
        mAuth = FirebaseAuth.getInstance();

        // Controllo se esiste ed è attiva una sessione di Firebase
        if(mAuth.getCurrentUser() != null){
            startActivity(new Intent(SignUpActivity.this, MainActivity.class));
        }

        // Link dei tag XML ad oggetti Java
        signUpButton = findViewById(R.id.sign_up_button);
        linkSignUp2Login = findViewById(R.id.link_signup2login);

        email = findViewById(R.id.text_email_signup);
        password = findViewById(R.id.text_password_signup);

        progressBar = findViewById(R.id.progressBar);

        // Link dei listener di eventi ai bottoni
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

                // Rende la ProgressBar Visibile
                progressBar.setVisibility(View.VISIBLE);

                mAuth.createUserWithEmailAndPassword(emailString, passwordString).addOnCompleteListener(
                        SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        progressBar.setVisibility(View.GONE);

                        // Controllo se la creazione dell'utente è andata a buon fine
                        if(task.isSuccessful()){
                            Toast.makeText(getApplicationContext(), "SignUp Ok", Toast.LENGTH_LONG).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                            startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                        }else {
                            // PWD da 6 ma errore in creazione dell'utente (Errore FireBase)
                            Toast.makeText(getApplicationContext(), getString(R.string.auth_failed), Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }
}