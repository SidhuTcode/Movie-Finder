package com.example.moviefinder.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.moviefinder.R;
import com.example.moviefinder.databinding.ActivityLoginBinding;
import com.example.moviefinder.databinding.ActivityRegisterBinding;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    // View binding instance for accessing UI elements
    ActivityLoginBinding binding;

    // Firebase Authentication instance for managing user login
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate the layout using view binding and set it as the content view
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize FirebaseAuth instance
        firebaseAuth = FirebaseAuth.getInstance();

        // Handle Login Button Click
        binding.loginButton.setOnClickListener(view -> {
            // Get the email and password entered by the user
            String email = binding.loginEmail.getText().toString().trim();
            String password = binding.loginPassword.getText().toString().trim();

            // Validate if email and password are entered
            if (email.isEmpty()) {
                binding.loginEmail.setError("Enter your email");
                return;
            }
            if (password.isEmpty()) {
                binding.loginPassword.setError("Enter your password");
                return;
            }

            // Use Firebase Authentication to sign in with email and password
            firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // Show success message and navigate to MainActivity
                            Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        } else {
                            // Show error message if login fails
                            Toast.makeText(this, "Login failed: Enter valid Credential", Toast.LENGTH_SHORT).show();
                        }
                    });

        });

        // Register Button: Navigate to RegisterActivity when clicked
        Button registerButton = findViewById(R.id.register_btn);
        registerButton.setOnClickListener(view -> {
            startActivity(new Intent(this, RegisterActivity.class));
        });

        // Show/Hide Password functionality: Toggle password visibility
        binding.showPassword.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                // Show password when checkbox is checked
                binding.loginPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            } else {
                // Hide password when checkbox is unchecked
                binding.loginPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
            // Keep the cursor at the end of the password field
            binding.loginPassword.setSelection(binding.loginPassword.getText().length());
        });
    }
}