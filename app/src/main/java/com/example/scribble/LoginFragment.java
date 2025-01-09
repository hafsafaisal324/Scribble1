package com.example.scribble;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.fragment.app.Fragment;

public class LoginFragment extends Fragment {

    private EditText inputUsername, inputPassword;
    private Button loginButton;
    private UserDatabaseHelper dbHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        inputUsername = view.findViewById(R.id.inputUsername);
        inputPassword = view.findViewById(R.id.inputPassword);
        loginButton = view.findViewById(R.id.loginButton);
        dbHelper = new UserDatabaseHelper(requireContext()); // Use requireContext() instead of getContext()

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = inputUsername.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();

                if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)) {
                    if (dbHelper.validateUser(username, password)) {
                        Toast.makeText(getContext(), "Login successful!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getContext(), NotesActivity.class);
                        intent.putExtra("username", username);  // Put the username extra
                        startActivity(intent);

                    } else {
                        Toast.makeText(getContext(), "Invalid username or password!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }
}
