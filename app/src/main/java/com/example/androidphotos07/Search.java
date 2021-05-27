package com.example.androidphotos07;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

import photos.Album;
import photos.Photo;
import photos.Tag;
import photos.User;

import android.os.Bundle;

public class Search extends AppCompatActivity {

    private LinearLayout gallery;
    private User user;
    private EditText nameTag;
    private EditText locationTag;
    private EditText andOr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        //Toolbar toolbar = findViewById(R.id.toolbar);
       // setSupportActionBar(toolbar);
       // getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras();
        user = (User) bundle.getSerializable(Photos.USER);
        gallery = findViewById(R.id.gallery);
        nameTag = findViewById(R.id.nameTag);
        locationTag = findViewById(R.id.locationTag);
        andOr = findViewById(R.id.andOr);


    }

    public void back(View view) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Photos.USER, user);
        Intent intent = new Intent();
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        finish();
    }
    public void search(View view){
        gallery.removeAllViews();
        if(!nameTag.getText().toString().isEmpty()&&!locationTag.getText().toString().isEmpty()) {
            if(andOr.getText().toString().equalsIgnoreCase("and")||andOr.getText().toString().equalsIgnoreCase("or")){
                Log.d("STATE", nameTag.getText().toString() + " " + locationTag.getText().toString() + andOr.getText().toString());
                gallery.removeAllViews();
                for(Photo p : user.searchByCombination(new Tag("person", nameTag.getText().toString()), new Tag("location", locationTag.getText().toString()), andOr.getText().toString())) {
                    ImageView i = new ImageView(this);
                    Uri uri = Uri.parse(p.getFilePath());
                    try {
                        i.setImageBitmap(MediaStore.Images.Media.getBitmap(getContentResolver(), uri));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    gallery.addView(i);
                }
            }
            else{
                Toast.makeText(Search.this, "Please enter and/or", Toast.LENGTH_LONG).show();
            }
        }
        else if(!nameTag.getText().toString().isEmpty()){
            gallery.removeAllViews();
            Log.d("STATE", "BEFORELOOP");
            for(Photo p : user.searchByTag(new Tag("person", nameTag.getText().toString()))) {
                Log.d("STATE", "INLOOP");
                ImageView i = new ImageView(this);
                Uri uri = Uri.parse(p.getFilePath());
                try {
                    i.setImageBitmap(MediaStore.Images.Media.getBitmap(getContentResolver(), uri));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                gallery.addView(i);
            }
        }
        else if(!locationTag.getText().toString().isEmpty()){
            gallery.removeAllViews();
            Log.d("STATE", "LOCATION");
            for(Photo p : user.searchByTag(new Tag("location", locationTag.getText().toString()))) {
                ImageView i = new ImageView(this);
                Uri uri = Uri.parse(p.getFilePath());
                try {
                    i.setImageBitmap(MediaStore.Images.Media.getBitmap(getContentResolver(), uri));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                gallery.addView(i);
            }
        }
        else{
            Toast.makeText(Search.this, "Please enter tags", Toast.LENGTH_LONG).show();
        }
    }


}