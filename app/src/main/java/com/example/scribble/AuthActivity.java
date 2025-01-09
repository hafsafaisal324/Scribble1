package com.example.scribble;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class AuthActivity extends AppCompatActivity {

    private Button signUpButton, loginButton, continueWithoutLoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.auth_xml);

        signUpButton = findViewById(R.id.signUpButton);
        loginButton = findViewById(R.id.loginButton);
        continueWithoutLoginButton = findViewById(R.id.continueWithoutLoginButton);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new SignUpFragment());
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new LoginFragment());
            }
        });

        continueWithoutLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Continue without login logic
                Toast.makeText(AuthActivity.this, "Continuing without login", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AuthActivity.this, NotesActivity.class);
                startActivity(intent);
                finish(); // Optional: finish the current activity
            }
        });

    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainer, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
