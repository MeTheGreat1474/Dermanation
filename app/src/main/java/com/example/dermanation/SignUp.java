package com.example.dermanation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUp extends AppCompatActivity {

    EditText editTextEmail, editTextPassword, editName, editConfirmPassword;

    Button buttonReg;

    FirebaseAuth mAuth;

    TextView textView;

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mAuth = FirebaseAuth.getInstance();
        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);
        editName = findViewById(R.id.name);
        editConfirmPassword = findViewById(R.id.confirmPassword);
        buttonReg = findViewById(R.id.btn_register);
        textView = findViewById(R.id.loginNow);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        buttonReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email, password, name, confirmPassword;
                email = String.valueOf(editTextEmail.getText());
                password = String.valueOf(editTextPassword.getText());
                confirmPassword = String.valueOf(editConfirmPassword.getText());
                name = String.valueOf(editName.getText());

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(SignUp.this, "Enter email", Toast.LENGTH_LONG).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(SignUp.this, "Enter password", Toast.LENGTH_LONG).show();
                    return;
                }

                if (TextUtils.isEmpty(name)) {
                    Toast.makeText(SignUp.this, "Enter name", Toast.LENGTH_LONG).show();
                    return;
                }

                if (TextUtils.isEmpty(confirmPassword)) {
                    Toast.makeText(SignUp.this, "Confirm your password", Toast.LENGTH_LONG).show();
                    return;
                }

                if (!password.equals(confirmPassword)) {
                    Toast.makeText(SignUp.this, "Passwords do not match", Toast.LENGTH_LONG).show();
                    return;
                }

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {

                                    // Account created successfully
                                    FirebaseUser user = mAuth.getCurrentUser();

                                    if ( user != null ) {
                                      // Save the name to realtime database
                                      String userId = user.getUid();
                                      FirebaseDatabase database = FirebaseDatabase.getInstance();
                                      DatabaseReference userRef = database.getReference("Users").child(userId);

                                      // Create a user object to store data
                                      User newUser = new User(name,email);
                                      userRef.setValue(newUser)
                                              .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                  @Override
                                                  public void onComplete(@NonNull Task<Void> task) {
                                                      if (task.isSuccessful()) {
                                                          Toast.makeText(
                                                                  SignUp.this,
                                                                  "Authentication created.",
                                                                  Toast.LENGTH_SHORT
                                                          ).show();
                                                          Intent intent = new Intent(getApplicationContext(), SignIn.class);
                                                          startActivity(intent);
                                                          finish();
                                                      }
                                                      else {
                                                          Toast.makeText(SignUp.this, "Failed to save user data.", Toast.LENGTH_SHORT).show();
                                                      }
                                                  }
                                              });
                                    }

                                } else {
                                    Toast.makeText(
                                            SignUp.this,
                                            "Authentication failed.",
                                            Toast.LENGTH_SHORT
                                    ).show();
                                }
                            }
                        });

            }
        });
    }
}

