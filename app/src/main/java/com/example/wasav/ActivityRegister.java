package com.example.wasav;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import me.anwarshahriar.calligrapher.Calligrapher;

public class ActivityRegister extends AppCompatActivity {

    private EditText fullnameEditText, phoneNumberEditText, emailEditText, passwordEditText;
    private Button registerButton;
    private String fullname, phoneNumber, email, password, uID;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private DatabaseReference databaseReference;
    private HashMap<String, Object> userIdentity;
    private ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        fullnameEditText = (EditText) findViewById(R.id.fullnameEditTextActivityRegister);
        phoneNumberEditText = (EditText) findViewById(R.id.phoneNumberEditTextActivityRegister);
        emailEditText = (EditText) findViewById(R.id.emailEditTextActivityRegister);
        passwordEditText = (EditText) findViewById(R.id.passwordEditTextActivityRegister);
        registerButton = (Button) findViewById(R.id.registerButtonActivityRegister);

        loading = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getStringFromEditText();
                checkAllFields();
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

        fullname = fullnameEditText.getText().toString();
        phoneNumber = phoneNumberEditText.getText().toString();
        email = emailEditText.getText().toString();
        password = passwordEditText.getText().toString();
    }

    private void checkAllFields(){

        if(fullname.isEmpty() || phoneNumber.isEmpty() || email.isEmpty() || password.isEmpty()){

            Toast.makeText(ActivityRegister.this, "Please fill all the fields", Toast.LENGTH_LONG).show();
        }
        else {

            setProgressDialog();
            registerUser();
        }
    }

    private void registerUser() {

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){

                    loading.dismiss();
                    saveUserToDatabase();

                }
                else {

                    loading.hide();
                    Toast.makeText(ActivityRegister.this, "Choose different email and password", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void saveUserToDatabase(){

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        uID = currentUser.getUid();

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(uID).child("Profile");

        userIdentity = new HashMap<>();
        userIdentity.put("fullname", fullname);
        userIdentity.put("email", email);
        userIdentity.put("password", password);
        userIdentity.put("phonenumber", phoneNumber);

        databaseReference.setValue(userIdentity).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful()){

                    moveToActivityMain();
                    Toast.makeText(ActivityRegister.this, "Welcome", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void moveToActivityMain(){

        Intent toActivityRegisterDevice = new Intent(ActivityRegister.this, ActivityMain.class);
        toActivityRegisterDevice.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(toActivityRegisterDevice);
        finish();
    }
}
