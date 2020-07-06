package com.example.boter;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.boter.Activity.EditProfile;
import com.example.boter.ui.login.ActivityLogin;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private Button signOutButton, updateProfile;
    private TextView yourName, yourPhone, yourMail;
    private ImageView profileImage;
    private StorageReference storageReference;
    private FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_users)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        //NAV_headerを設定
        loadNavHeader(navigationView.getHeaderView(0));
    }

    private void loadNavHeader(View headerView) {
        signOutButton = headerView.findViewById(R.id.signOutButton);
        yourName = headerView.findViewById(R.id.display_name);
        yourMail = headerView.findViewById(R.id.display_email);
        yourPhone = headerView.findViewById(R.id.display_phone);
        profileImage = headerView.findViewById(R.id.display_image);
        updateProfile = headerView.findViewById(R.id.edit_profile);

        fAuth = FirebaseAuth.getInstance();
        FirebaseFirestore fStore = FirebaseFirestore.getInstance();

        // download image to firebase storage
        storageReference = FirebaseStorage.getInstance().getReference();

        StorageReference profileRef = storageReference.child("usersprofile/"+fAuth.getCurrentUser().getUid()+"/profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(profileImage);
            }
        });

        String userId = fAuth.getCurrentUser().getUid();

        final DocumentReference documentReference = fStore.collection("usersprofile").document(userId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                //set profile data to firebase
                if (documentSnapshot.exists()) {
                    yourPhone.setText(documentSnapshot.getString("phone"));
                    yourMail.setText(documentSnapshot.getString("email"));
                    yourName.setText(documentSnapshot.getString("fullname"));
                }else{
                    Log.d("trident", "onEvent: Document don't exists");
                }
            }
        });

        actionListener();
    }

    private void actionListener() {
        signOutButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Log out
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(getApplicationContext(), ActivityLogin.class));
                        finish();
                    }
                }
        );
        updateProfile.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //open gallery
                        Intent intent = new Intent(v.getContext(), EditProfile.class);
                        intent.putExtra("fullname", yourName.getText().toString());
                        intent.putExtra("email", yourMail.getText().toString());
                        intent.putExtra("phone", yourPhone.getText().toString());
                        startActivity(intent);
//                        Intent openGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                        startActivityForResult(openGalleryIntent, 1000);
                    }
                }
        );
    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

}
