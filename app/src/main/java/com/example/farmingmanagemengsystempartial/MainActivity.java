package com.example.farmingmanagemengsystempartial;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText emailInput, passwordInput;
    private Button signInBtn, regBtn;
    private TextView forgotPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        emailInput = findViewById(R.id.EditEmail);
        passwordInput = findViewById(R.id.EditPassword);
        signInBtn = findViewById(R.id.sign_in_btn);
        regBtn = findViewById(R.id.reg_btn);
        forgotPassword = findViewById(R.id.text_back_to_login);

        signInBtn.setOnClickListener(v -> loginUser());
        regBtn.setOnClickListener(v -> startActivity(new Intent(this, RegistrationActivity.class)));
        forgotPassword.setOnClickListener(v -> startActivity(new Intent(this, ForgotPasswordActivity.class)));
    }

    private void loginUser() {
        String email = emailInput.getText().toString().trim();
        String pw = passwordInput.getText().toString().trim();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(pw)) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Hardcoded login for testing
        String hardcodedEmail = "Random";
        String hardcodedPassword = "123456";

        if (email.equals(hardcodedEmail) && pw.equals(hardcodedPassword)) {
            Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, DashboardActivity.class));
            finish();
        } else {
            // Commented out Firebase authentication
            // mAuth.signInWithEmailAndPassword(email, pw)
            //         .addOnCompleteListener(task -> {
            //             if (task.isSuccessful()) {
            //                 Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show();
            //                 startActivity(new Intent(this, DashboardActivity.class));
            //                 finish();
            //             } else {
            //                 Toast.makeText(this, "Login failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
            //             }
            //         });

            Toast.makeText(this, "Login failed: Invalid credentials", Toast.LENGTH_LONG).show();
        }
    }
}