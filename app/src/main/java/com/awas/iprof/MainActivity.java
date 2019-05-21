package com.awas.iprof;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.awas.iprof.activity.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private Button logout;

    private String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        // Controllo se Sessione FireBase già presente
        if(mAuth.getCurrentUser() == null){
            // Ritorno al Login se non presente sessione Firebase
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        }

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(user != null){
            email = user.getEmail();
        }

        TextView emailuser = findViewById(R.id.email_user);
        emailuser.setText(email);

        logout = findViewById(R.id.logout_button);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();

                Toast.makeText(getApplicationContext(), "Logout OK", Toast.LENGTH_LONG).show();;

                startActivity(new Intent(MainActivity.this, LoginActivity.class));

              }
        });

    }
}
