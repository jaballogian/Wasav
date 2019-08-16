package com.example.wasav;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import me.anwarshahriar.calligrapher.Calligrapher;

public class ActivityLogin extends AppCompatActivity {

    private EditText emailEditText, passwordEditText;
    private Button loginButton;
    private String email, password;
    private FirebaseAuth mAuth;
    private TextView registerHereTextView;
    private ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEditText = (EditText) findViewById(R.id.emailEditTextActivityLogin);
        passwordEditText = (EditText) findViewById(R.id.passwordEditTextActivityLogin);
        loginButton = (Button) findViewById(R.id.loginButtonActivityLogin);

        loading = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getStringFromEditText();
                checkAllFields();
            }
        });

        registerHereTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                moveToActivityRegister();
            }
        });
    }

    private void setProgressDialog(){

        loading.setTitle("Processing");
        loading.setMessage("Please wait");
        loading.setCanceledOnTouchOutside(false);
        loading.show();
    }

    private void getStringFromEditText(){

        email = emailEditText.getText().toString();
        password = passwordEditText.getText().toString();
    }

    private void checkAllFields(){

        if(email.isEmpty() || password.isEmpty()){

            Toast.makeText(ActivityLogin.this, "Please fill all the fields", Toast.LENGTH_LONG).show();
        }
        else {

            setProgressDialog();
            logInUser();
        }
    }

    private void logInUser(){

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){

                    moveToActivityMain();
                }
                else{

                    Toast.makeText(ActivityLogin.this,"Incorrect email and password", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void moveToActivityMain(){

        Intent toActivityMain = new Intent(ActivityLogin.this, ActivityMain.class);
        toActivityMain.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(toActivityMain);
        finish();
    }

    private void moveToActivityRegister(){

        Intent toActivityRegister = new Intent(ActivityLogin.this, ActivityRegister.class);
        startActivity(toActivityRegister);
    }
}
