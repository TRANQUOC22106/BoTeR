package com.example.boter.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.example.boter.BuildConfig;
import com.example.boter.MainActivity;
import com.example.boter.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class EditProfile extends AppCompatActivity {

    EditText profileFullname, profileEmail,profilePhone,profileTemp;
    public static EditText profileStudentID;
    Button saveBtn;
    ImageView profileImageView;
    TextView profileDate;

    //Firebaseを呼び出す
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    FirebaseUser user;
    StorageReference storageReference;

    private Uri cameraUri;
    private final static String TAG = "boter";
    private File cameraFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        Intent data = getIntent();
        //MainActivityからデータをもらう
        String fullname = data.getStringExtra("fullname");
        String email = data.getStringExtra("email");
        String phone = data.getStringExtra("phone");
        String temp = data.getStringExtra("temp");
        String studentID = data.getStringExtra("studentID");

        profileEmail = findViewById(R.id.profileEmail);
        profileFullname = findViewById(R.id.profileName);
        profilePhone = findViewById(R.id.profilePhone);
        profileTemp = findViewById(R.id.profileTemp);
        profileStudentID = findViewById(R.id.profileStudentID);
        profileImageView = findViewById(R.id.profileImageView);
        saveBtn = findViewById(R.id.profileSaveBtn);
        profileDate = findViewById(R.id.editProfile_date);

        //firebaseのAuthとFirestoreをインスタンスする
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        user = fAuth.getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference();


        //今選択しているUserのプロフィールの写真をとるためのメソッド
        StorageReference profileRef = storageReference.child("usersprofile/"+fAuth.getCurrentUser().getUid()+"/profile.jpg");

        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(profileImageView);
            }
        });
        profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File cFolder = getExternalFilesDir(Environment.DIRECTORY_DCIM);
                String fileDate = new SimpleDateFormat("yyyy年MM月dd日のHH分mm秒", Locale.US).format(new Date());
                String fileName = String.format(TAG + "_%s.jpg", fileDate);
                profileDate.setText(fileDate);
//
                cameraFile = new File(cFolder, fileName);
                cameraUri = FileProvider.getUriForFile(EditProfile.this,
                        BuildConfig.APPLICATION_ID + ".provider",cameraFile);

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, cameraUri);
                startActivityForResult(intent, 1000);
            }
        });
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //FirebaseのデータベースをUpdateする、空がある場合、returnする。
                if (profileFullname.getText().toString().isEmpty() || profileEmail.getText().toString().isEmpty() ||
                        profilePhone.getText().toString().isEmpty() || profileStudentID.getText().toString().isEmpty() ||
                        profileTemp.getText().toString().isEmpty() || profileDate.getText().toString().equals("Date")){
                    Toast.makeText(EditProfile.this, "One or Many fields are empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                final String email = profileEmail.getText().toString();
                user.updateEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        DocumentReference docRef = fStore.collection("UserList").document(user.getUid());
                        Map<String,Object> edited = new HashMap<>();
                        edited.put("email", email);
                        edited.put("fullname", profileFullname.getText().toString());
                        edited.put("phone", profilePhone.getText().toString());
                        edited.put("temp", profileTemp.getText().toString());
                        edited.put("studentID", profileStudentID.getText().toString());
                        edited.put("date", profileDate.getText().toString());
                        docRef.update(edited).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(EditProfile.this, "Profile updated", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                finish();
                            }
                        });

                        Toast.makeText(EditProfile.this, "Email is changed", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(EditProfile.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        //Sets the text that this EditView is to display
        profilePhone.setText(phone);
        profileFullname.setText(fullname);
        profileEmail.setText(email);
        profileStudentID.setText(studentID);
        profileTemp.setText(temp);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //setImage
        if (requestCode == 1000){
            if (cameraUri == null){
                return;
//                assert data != null;
//                Uri imageUri = data.getData();
////                profileImage.setImageURI(imageUri);
//
//                uploadImageToFirebase(imageUri);
            }
            uploadImageToFirebase(cameraUri);
        }
    }
    private void uploadImageToFirebase(Uri imageUri) {
        // upload image to firebase storage
        final StorageReference fileRef = storageReference.child("usersprofile/"+fAuth.getCurrentUser().getUid()+"/profile.jpg");
        fileRef.putFile(imageUri).addOnSuccessListener(
                new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        fileRef.getDownloadUrl().addOnSuccessListener(
                                new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        Picasso.get().load(uri).into(profileImageView);
                                    }
                                }
                        );
                    }
                }
        ).addOnFailureListener(
                new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(EditProfile.this, "Failed", Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }
}
