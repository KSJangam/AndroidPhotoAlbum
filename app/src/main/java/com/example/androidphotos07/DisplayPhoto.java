package com.example.androidphotos07;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import photos.Album;
import photos.Photo;
import photos.Tag;
import photos.User;

public class DisplayPhoto extends AppCompatActivity {

    private TextView photoName, tags;
    private ImageView image;
    private Spinner tagName, albums;
    private EditText tagValue;
    private Photo photo;
    private Album album;
    private User user;

    public static final int BACK = 1;
    public static final int MOVE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_photo);

        photoName = findViewById(R.id.photoName);
        tags = findViewById(R.id.tags);
        image = findViewById(R.id.image);
        tagName = findViewById(R.id.tagName);
        albums = findViewById(R.id.albums);
        tagValue = findViewById(R.id.tagValue);

        // get info from bundle
        Bundle bundle = getIntent().getExtras();
        photo = (Photo) bundle.getSerializable(ShowAlbum.PHOTO);
        album = (Album) bundle.getSerializable(Photos.ALBUM);
        user = (User) bundle.getSerializable(Photos.USER);

        photoName.setText(photo.getFilePath());
        Uri uri = Uri.parse(photo.getFilePath());
        try {
            image.setImageBitmap(MediaStore.Images.Media.getBitmap(getContentResolver(), uri));
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(photo.getTags().size()!=0) {
            tags.setText("");
            for(Tag t : photo.getTags()) {
                if(tags.getText().equals("")) {
                    tags.setText(t.tagName  + " = " + t.tagValue);
                }
                else
                    tags.setText(tags.getText() +  ", " + t.tagName  + " = " + t.tagValue);
            }
        }
        else {
            tags.setText("Add a tag!");
        }

        ArrayList<String> als = new ArrayList<>();
        for (Album a : user.getAlbums()) {
            als.add(a.getName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.list_item, als);
        albums.setAdapter(adapter);

        ArrayList<String> tagNames = new ArrayList<>();
        tagNames.add("person");
        tagNames.add("location");
        ArrayAdapter<String> tagAdapter = new ArrayAdapter<>(this, R.layout.list_item, tagNames);
        tagName.setAdapter(tagAdapter);
    }

    public void backPhoto(View view) {
        Bundle bundle = new Bundle();
        try {
            FileOutputStream fos = this.openFileOutput("data2.ser", Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(user);
            os.close();
            fos.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
        Intent intent = new Intent();
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        finish();
    }

    public void prev(View view) {
        int index = album.getPhotos().indexOf(photo);
        if(index != 0) {
            photo = album.getPhotos().get(index-1);
            photoName.setText(photo.getFilePath());
            Uri uri = Uri.parse(photo.getFilePath());
            try {
                image.setImageBitmap(MediaStore.Images.Media.getBitmap(getContentResolver(), uri));
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(photo.getTags().size()!=0) {
                tags.setText("");
                for(Tag t : photo.getTags()) {
                    if(tags.getText().equals("")) {
                        tags.setText(t.tagName  + " = " + t.tagValue);
                    }
                    else
                        tags.setText(tags.getText() +  ", " + t.tagName  + " = " + t.tagValue);
                }
            }
            else {
                tags.setText("Add a tag!");
            }
        }
        else {
            Toast.makeText(DisplayPhoto.this, "This is the first photo in the album", Toast.LENGTH_LONG).show();
        }
    }

    public void next(View view) {
        int index = album.getPhotos().indexOf(photo);
        if(index != album.getPhotos().size()-1) {
            photo = album.getPhotos().get(index+1);
            photoName.setText(photo.getFilePath());
            Uri uri = Uri.parse(photo.getFilePath());
            try {
                image.setImageBitmap(MediaStore.Images.Media.getBitmap(getContentResolver(), uri));
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(photo.getTags().size()!=0) {
                tags.setText("");
                for(Tag t : photo.getTags()) {
                    if(tags.getText().equals("")) {
                        tags.setText(t.tagName  + " = " + t.tagValue);
                    }
                    else
                        tags.setText(tags.getText() +  ", " + t.tagName  + " = " + t.tagValue);
                }
            }
            else {
                tags.setText("Add a tag!");
            }
        }
        else {
            Toast.makeText(DisplayPhoto.this, "This is the last photo in the album", Toast.LENGTH_LONG).show();
        }
    }

    public void addTag(View view) {
        if(!tagValue.getText().toString().isEmpty()) {
            Tag t = new Tag((String)tagName.getSelectedItem(), tagValue.getText().toString());
            if(photo.addTag(t)) {
                Log.d("STATE", "PHOTO:" + photo.getTags().size()+"");
                album.updatePhoto(photo);
                Log.d("STATE", "ALBUM:" + album.getPhotos().get(0).getTags().size()+"");
                user.updateAlbum(album);
                Log.d("STATE", "USER:" + user.getAlbums().get(0).getPhotos().get(0).getTags().size()+"");

                if(tags.getText().equals("Add a tag!")) {
                    tags.setText(t.tagName + " = " + t.tagValue);
                }
                else {
                    tags.setText(tags.getText() + ", " + t.tagName + " = " + t.tagValue);
                }
            }
            else {
                Toast.makeText(DisplayPhoto.this, "Please do not enter a duplicate tag", Toast.LENGTH_LONG).show();
            }
        }
        else {
            Toast.makeText(DisplayPhoto.this, "Please enter a value for this tag", Toast.LENGTH_LONG).show();
        }
    }

    public void removeTag(View view) {
        if(!tagValue.getText().toString().isEmpty()) {
            Tag tag = new Tag((String)tagName.getSelectedItem(), tagValue.getText().toString());
            if(photo.removeTag(tag)) {
                album.updatePhoto(photo);
                user.updateAlbum(album);
                tags.setText("");
                if (photo.getTags().size() == 0) {
                    tags.setText("Add a tag!");
                    return;
                }
                for (Tag t : photo.getTags()) {
                    if (tags.getText().equals("")) {
                        tags.setText(t.tagName + " = " + t.tagValue);
                    } else
                        tags.setText(tags.getText() + ", " + t.tagName + " = " + t.tagValue);
                }
            }
            else {
                Toast.makeText(DisplayPhoto.this, "Please enter a tag that is on this photo to remove", Toast.LENGTH_LONG).show();
            }
        }
        else {
            Toast.makeText(DisplayPhoto.this, "Please enter the value of the tag you want to remove", Toast.LENGTH_LONG).show();
        }

    }

    public void move(View view) {
        if(album.movePhoto(photo, user.getAlbums().get(albums.getSelectedItemPosition()))) {
            user.updateAlbum(album);
            backPhoto(view);
        }
        else {
            Toast.makeText(DisplayPhoto.this, "The photo is already in the selected album", Toast.LENGTH_LONG).show();
        }
    }
}