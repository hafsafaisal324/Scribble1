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

import java.util.Objects;

public class SignUpFragment extends Fragment {

    private EditText inputUsername, inputPassword,inputEmail;
    private Button signUpButton;
    private UserDatabaseHelper dbHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);

        inputEmail = view.findViewById(R.id.inputEmail);
        inputUsername = view.findViewById(R.id.inputUsername);
        inputPassword = view.findViewById(R.id.inputPassword);
        signUpButton = view.findViewById(R.id.signUpButton);
        dbHelper = new UserDatabaseHelper(requireContext());

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = inputEmail.getText().toString().trim();
                String username = inputUsername.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();

                if (!TextUtils.isEmpty(email)&&!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)) {
                    if (dbHelper.insertUser(email,username,password)) {
                        Toast.makeText(requireContext(), "Sign up successful!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(requireContext(), NotesActivity.class);
                        intent.putExtra("username", username);  // Put the username extra
                        startActivity(intent);
                    } else {
                        Toast.makeText(getContext(), "Username already exists!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }
}
