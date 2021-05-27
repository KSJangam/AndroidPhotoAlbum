package com.example.androidphotos07;

import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.URI;
import java.util.ArrayList;

import photos.Album;
import photos.Photo;
import photos.Tag;
import photos.User;

public class ShowAlbum extends AppCompatActivity {

    private LinearLayout gallery;
    private TextView name;
    private Album album;
    private User user;
    private ImageView selected;
    private Photo selectedPhoto;
    public static final String PHOTO = "photo";
    public static final String USER = "user";
    public static final int DISPLAY_PHOTO = 1;
    public static final int PICKFILE_RESULT_CODE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_album);
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // get info from bundle
        Bundle bundle = getIntent().getExtras();
        album = (Album) bundle.getSerializable(Photos.ALBUM);
        user = (User) bundle.getSerializable(Photos.USER);

        // get objects from xml
        gallery = findViewById(R.id.gallery);
        name = findViewById(R.id.name);

        // set up
        name.setText(album.getName());
        for(Photo p : album.getPhotos()) {
            ImageView i = new ImageView(this);
            Uri uri = Uri.parse(p.getFilePath());
            try {
                i.setImageBitmap(MediaStore.Images.Media.getBitmap(getContentResolver(), uri));
            } catch (IOException e) {
                e.printStackTrace();
            }
            i.setOnClickListener(v -> {
                selected = (ImageView)v;
                selectedPhoto = p;
            });
            gallery.addView(i);
        }

    }

    public void add(View view) {
        // add photo
        Intent chooseFile = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        chooseFile.addCategory(Intent.CATEGORY_OPENABLE);
        chooseFile.setType("image/*");
        chooseFile = Intent.createChooser(chooseFile, "Choose a file");
        Log.d("STATE", "Photos Here1");
        startActivityForResult(chooseFile, PICKFILE_RESULT_CODE);
    }

    public void remove(View view) {
        if(selected != null) {
            album.removePhoto(selectedPhoto);
            user.updateAlbum(album);
            gallery.removeView(selected);
        }
        else {
            Toast.makeText(ShowAlbum.this, "Please select an image to remove", Toast.LENGTH_LONG).show();
        }
    }

    public void display(View view) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(PHOTO, selectedPhoto);
        bundle.putSerializable(Photos.ALBUM, album);
        bundle.putSerializable(Photos.USER, user);
        Intent intent = new Intent(this, DisplayPhoto.class);
        intent.putExtras(bundle);
        startActivityForResult(intent, DISPLAY_PHOTO);
    }

    public void back(View view) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("user", user);
        Intent intent = new Intent();
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        finish();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        Log.d("STATE", "NEWTHING");
        if(requestCode == DISPLAY_PHOTO) {
            Log.d("STATE", "RESULTGOOD");
            Bundle bundle = getIntent().getExtras();
            //user = (User) bundle.getSerializable("user");
            try {
                FileInputStream fis = this.openFileInput("data2.ser");
                ObjectInputStream is = new ObjectInputStream(fis);
                user = (User) is.readObject();
                is.close();
                fis.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            album = user.getAlbums().get(user.getAlbums().indexOf(album));
            Log.d("STATE", user.getAlbums().get(user.getAlbums().indexOf(album)).getPhotos().get(0).getTags().get(0).toString());
            gallery.removeAllViews();
            for (Photo p : album.getPhotos()) {
                ImageView i = new ImageView(this);
                Uri uri = Uri.parse(p.getFilePath());
                try {
                    i.setImageBitmap(MediaStore.Images.Media.getBitmap(getContentResolver(), uri));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                i.setOnClickListener(v -> {
                    selected = (ImageView) v;
                    selectedPhoto = p;
                });
                gallery.addView(i);
            }
        }
        else if(requestCode == PICKFILE_RESULT_CODE){
            Uri uri = intent.getData();
            String src = uri.toString();
            Log.d("STATE", uri.toString());
            Photo toAdd = new Photo(src);
            if(album.addPhoto(toAdd)) {
                user.updateAlbum(album);
                ImageView i = new ImageView(this);
                try {
                    i.setImageBitmap(MediaStore.Images.Media.getBitmap(getContentResolver(), uri));
                } catch(Exception e) {
                    e.printStackTrace();
                }
                i.setOnClickListener(v -> {
                    selected = (ImageView)v;
                    selectedPhoto = toAdd;
                });
                gallery.addView(i);

            }
            else {
                Toast.makeText(ShowAlbum.this, "Please select a valid image to add", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onNewIntent(final Intent intent) {
        super.onNewIntent(intent);
        Log.d("STATE", "HERE!!!");
        this.setIntent(intent);
    }

}
