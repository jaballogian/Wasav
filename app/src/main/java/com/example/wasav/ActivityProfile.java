package com.example.wasav;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.ArrayList;
import java.util.Arrays;

import de.hdodenhof.circleimageview.CircleImageView;
import lecho.lib.hellocharts.model.Line;

public class ActivityProfile extends AppCompatActivity {

    private LinearLayout logoutLinearLayout;
    private RelativeLayout homeRelativeLayout, statisticRelativeLayout;
    private Spinner countrySpinner;
    private String[] countryList;
    private ArrayList<String> countryArrayList;
    private ArrayAdapter<String> countryAdapter;
    private String selectedCountry, username, uID, email, password, photo;
    private Button saveButton;
    private EditText usernameEditText, emailEditText, passwordEditText;
    private DatabaseReference profileReference, imageDatabaseReference;
    private FirebaseUser currentUser;
    private CircleImageView photoCircleImageView, changePhotoCircleImageView;
    private Uri resultUri;
    private StorageReference imageStoregeReference;
    private ProgressDialog uploadFoto;

    private static final int GALLERY_PICK = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        logoutLinearLayout = (LinearLayout) findViewById(R.id.logoutLinearLayoutActivityProfile);
        homeRelativeLayout = (RelativeLayout) findViewById(R.id.homeRelativeLayoutActivityProfile);
        statisticRelativeLayout = (RelativeLayout) findViewById(R.id.statisticRelativeLayoutActivityProfile);
        countrySpinner = (Spinner) findViewById(R.id.countrySpinnerActivitySpinner);
        saveButton = (Button) findViewById(R.id.saveButtonActivityProfile);
        usernameEditText = (EditText) findViewById(R.id.usernameEditTextActivityProfile);
        emailEditText = (EditText) findViewById(R.id.emailEditTextActivityProfile);
        passwordEditText = (EditText) findViewById(R.id.passwordEditTextActivityProfile);
        photoCircleImageView = (CircleImageView) findViewById(R.id.photoCircleImageViewActivityProfile);
        changePhotoCircleImageView = (CircleImageView) findViewById(R.id.changePhotoCircleImageViewActivityProfile);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        uID = currentUser.getUid();
        profileReference = FirebaseDatabase.getInstance().getReference().child("Users").child(uID).child("Profile");
        imageStoregeReference = FirebaseStorage.getInstance().getReference();

        countryList = getResources().getStringArray(R.array.select_country);
        countryArrayList = new ArrayList<String>(Arrays.asList(countryList));

        countryAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, countryArrayList);
        countryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        countrySpinner.setAdapter(countryAdapter);

        countrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                selectedCountry = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                // TODO Auto-generated method stub
            }
        });

        readProfileDataFromFirebase();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                checkAllFields();
            }
        });

        logoutLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseAuth.getInstance().signOut();
                Intent toActivityLogin = new Intent(ActivityProfile.this, ActivityLogin.class);
                toActivityLogin.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(toActivityLogin);
                finish();
            }
        });

        homeRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent toActivityMain = new Intent(ActivityProfile.this, ActivityMain.class);
                startActivity(toActivityMain);
            }
        });

        statisticRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent toActivityStatistic = new Intent(ActivityProfile.this, ActivityStatistic.class);
                startActivity(toActivityStatistic);
            }
        });

        changePhotoCircleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent galleryIntent = new Intent();
                galleryIntent.setType("image/*");
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(galleryIntent, "Choose a picture"), GALLERY_PICK);
            }
        });
    }

    private void checkAllFields(){

        username = usernameEditText.getText().toString();

        if(username.isEmpty() || selectedCountry.isEmpty() || selectedCountry.equals("Country")){

            Toast.makeText(ActivityProfile.this, "Please fill all the fields with valid answers", Toast.LENGTH_LONG).show();
        }
        else {

            saveDataToFirebase();
        }
    }

    private void saveDataToFirebase(){

        profileReference.child("username").setValue(username);
        profileReference.child("country").setValue(selectedCountry).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful()){

                    Toast.makeText(ActivityProfile.this, "Saved", Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    private void readProfileDataFromFirebase(){

        profileReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                username = dataSnapshot.child("username").getValue().toString();
                selectedCountry = dataSnapshot.child("country").getValue().toString();
                email = dataSnapshot.child("email").getValue().toString();
                password = dataSnapshot.child("password").getValue().toString();
                photo = dataSnapshot.child("photo").getValue().toString();

                try{

//                    Picasso.get().load(dataSnapshot.child("photo").getValue().toString()).placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher).into(photoCircleImageView);
                    Picasso.get().load(dataSnapshot.child("photo").getValue().toString()).into(photoCircleImageView);
                }
                catch (Exception ex) {

                }

                showProfileData();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void showProfileData(){

        usernameEditText.setText(username);
        emailEditText.setText(email);
        passwordEditText.setText(password);
        setSelectedSpinner(countrySpinner, selectedCountry, countryArrayList);
    }

    private void setSelectedSpinner (Spinner spinner, String input, ArrayList<String> arrayList){

        for(int i = 0; i < arrayList.size(); i++){

            if(input.equals(arrayList.get(i))){

                spinner.setSelection(i);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_PICK && resultCode == RESULT_OK){

            Uri imageUri = data.getData();

            CropImage.activity(imageUri).setAspectRatio(1,1).start(this);
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {

                uploadFoto = new ProgressDialog(this);
                uploadFoto.setTitle("Uploading image");
                uploadFoto.setMessage("Please wait");
                uploadFoto.setCanceledOnTouchOutside(false);
                uploadFoto.show();

                resultUri = result.getUri();

                StorageReference filepath = imageStoregeReference.child("Profiles").child(uID+ ".jpg");

                filepath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                        if ((task.isSuccessful())){

                            final String downloadUrl = task.getResult().getUploadSessionUri().toString();

                            imageDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(uID).child("Profile").child("photo");

                            imageDatabaseReference.setValue(resultUri.toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
//                            imageDatabaseReference.setValue(downloadUrl).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if(task.isSuccessful()){

                                        uploadFoto.dismiss();
                                        Toast.makeText(ActivityProfile.this, "Finish uploading", Toast.LENGTH_LONG).show();
                                        photoCircleImageView.setImageURI(resultUri);
//                                        session.setResultURI(String.valueOf(resultUri));
                                    }
                                }
                            });
                        }
                        else {

                            uploadFoto.dismiss();
                            Toast.makeText(ActivityProfile.this, "Sorry there is a problem, please try again", Toast.LENGTH_LONG).show();
                        }
                    }
                });

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                Exception error = result.getError();
            }
        }
    }
}
