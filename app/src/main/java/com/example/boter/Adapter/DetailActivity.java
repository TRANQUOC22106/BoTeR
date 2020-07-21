package com.example.boter.Adapter;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.boter.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    private ImageButton helpPhoneCall, detail_imageBtn_email;
    private TextView dPhone, dFullName, dEmail, dStudentID, dTemp, dDate;
    private ImageView profileImage;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        dPhone = findViewById(R.id.detail_phone);
        dFullName = findViewById(R.id.detail_fullname);
        dEmail = findViewById(R.id.detail_email);
        dStudentID = findViewById(R.id.detail_studentID);
        dTemp = findViewById(R.id.detail_temp);
        helpPhoneCall = findViewById(R.id.btn_phoneCall);
        detail_imageBtn_email = findViewById(R.id.imageBtn_Email_Detail);
        profileImage = findViewById(R.id.imageView_detailNhietKe);
        dDate = findViewById(R.id.detail_date);

        Intent data = getIntent();
        final String phone = data.getStringExtra("phone");
        final String email = data.getStringExtra("email");
        final String fullname = data.getStringExtra("fullname");
        final String studentID = data.getStringExtra("studentID");
        final String temp = data.getStringExtra("temp");
        final String idUser = data.getStringExtra("idUser");
        final String date = data.getStringExtra("date");

        dEmail.setText(email);
        dPhone.setText(phone);
        dFullName.setText(fullname);
        dStudentID.setText(studentID);
        dTemp.setText(temp);
        dDate.setText(date);

        helpPhoneCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                //電話する画面に携帯番号を持って移動する
                intent.setAction(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + phone));
                startActivity(intent);
            }
        });

        final String[] address = {email};
        detail_imageBtn_email.setOnClickListener(new TextView.OnClickListener() {
            public void onClick(View view) {
                Intent intentEmail = new Intent(Intent.ACTION_SEND);
                intentEmail.setType("*/*");
                intentEmail.putExtra(Intent.EXTRA_EMAIL, address);
                intentEmail.putExtra(Intent.EXTRA_SUBJECT, "タイトルを入力して下さい");
                intentEmail.putExtra(Intent.EXTRA_TEXT, "メッセージを入力して下さい");
                startActivity(intentEmail);
            }
        });
        // download image to firebase storage
        storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference profileRef = storageReference.child("usersprofile/"+ idUser +"/profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(profileImage);
            }
        });
    }
}
