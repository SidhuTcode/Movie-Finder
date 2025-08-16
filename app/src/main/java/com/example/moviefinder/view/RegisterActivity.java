package com.example.moviefinder.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.moviefinder.R;
import com.example.moviefinder.databinding.ActivityMovieDetailsBinding;
import com.example.moviefinder.databinding.ActivityRegisterBinding;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    // View binding instance for accessing UI elements
    ActivityRegisterBinding binding;

    // Firebase Authentication instance for managing user registration
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate the layout using view binding and set it as the content view
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize FirebaseAuth instance
        firebaseAuth = FirebaseAuth.getInstance();

        // Handle Register Button Click
        binding.registerButton.setOnClickListener(view -> {
            // Get the email and password entered by the user
            String email = binding.registerEmail.getText().toString();
            String password = binding.registerPassword.getText().toString();

            // Validate if email is valid and password is at least 6 characters long
            if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.registerEmail.setError("Enter a valid email");
                return;
            }
            if (password.isEmpty() || password.length() < 6) {
                binding.registerPassword.setError("Password must be at least 6 characters");
                return;
            }

            // Use Firebase Authentication to create a new user with email and password
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // Show success message and navigate to LoginActivity
                            Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(this, LoginActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        } else {
                            // Show error message if registration fails
                            Toast.makeText(this, "Registration failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

        });

        // Handle Cancel Button Click: Finish activity and return to previous screen
        binding.cancelButton.setOnClickListener(view -> finish());

        // Show/Hide Password functionality: Toggle password visibility
        binding.showPassword.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                // Show password when checkbox is checked
                binding.registerPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            } else {
                // Hide password when checkbox is unchecked
                binding.registerPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
            // Keep the cursor at the end of the password field
            binding.registerPassword.setSelection(binding.registerPassword.getText().length());
        });
    }
}