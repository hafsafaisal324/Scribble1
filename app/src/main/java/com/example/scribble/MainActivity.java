
package com.example.scribble;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final int SPLASH_SCREEN_DELAY = 3000; // 3 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Redirect to the next screen after a delay
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(MainActivity.this, AuthActivity.class);
            startActivity(intent);
            finish(); // Close splash screen activity
        }, SPLASH_SCREEN_DELAY);
    }
}
