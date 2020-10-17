package com.example.freechats;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class ImageMessage extends AppCompatActivity {

    Intent intentImage;
    String ImageIntent;
    ImageView ImageMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_message);

        intentImage = getIntent();
        ImageIntent = intentImage.getStringExtra("imageURL");
        ImageMessage = findViewById(R.id.ImageMessage);
        Glide.with(getApplicationContext()).load(ImageIntent).into(ImageMessage);
    }
}
