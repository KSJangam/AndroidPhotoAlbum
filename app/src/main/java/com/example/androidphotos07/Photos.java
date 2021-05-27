package com.example.androidphotos07;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.nio.file.Paths;
import java.util.ArrayList;

import photos.Album;
import photos.Tag;
import photos.User;

public class Photos extends AppCompatActivity {

    private ListView listView;
    private EditText albumName;
    private User user;
    private int position = -1;
    private ArrayAdapter<String> adapter;
    public static final String ALBUM = "album";
    public static final String USER = "user";
    public static final int EDIT_ALBUM = 1;
    public static final int SEARCH = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);

        // read in if file exists
        if (new File(Photos.this.getFilesDir(),"data.ser").exists()) {
            try {
                FileInputStream fis = this.openFileInput("data.ser");
                ObjectInputStream is = new ObjectInputStream(fis);
                user = (User) is.readObject();
                is.close();
                fis.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else {
            user = new User();
        }

        // set up stuff from xml
        albumName = findViewById(R.id.albumName);

        // set up list view
        ArrayList<String> albums = new ArrayList<>();
        for (Album a : user.getAlbums()) {
            albums.add(a.getName());
        }
        adapter = new ArrayAdapter<>(this, R.layout.list_item, albums);
        listView = findViewById(R.id.albums_list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener((parent, view, pos, id) -> position = pos);

    }

    public void open(View view) {
        if(position != -1) {
            Bundle bundle = new Bundle();
            bundle.putSerializable(ALBUM, user.getAlbums().get(position));
            bundle.putSerializable(USER, user);
            Intent intent = new Intent(this, ShowAlbum.class);
            intent.putExtras(bundle);
            startActivityForResult(intent, EDIT_ALBUM);
        }
        else {
            Toast.makeText(Photos.this, "Please select an album to open", Toast.LENGTH_LONG).show();
        }
    }

    public void create(View view) {
        if(!albumName.getText().toString().isEmpty()) {
            Album a = new Album(albumName.getText().toString());
            if(user.createAlbum(a)) {
                adapter.add(a.getName());
                listView.setAdapter(adapter);
            }
            else {
                Toast.makeText(Photos.this, "Please enter an unused album name", Toast.LENGTH_LONG).show();
            }
        }
        else {
            Toast.makeText(Photos.this, "Please enter an album name", Toast.LENGTH_LONG).show();
        }
    }

    public void delete(View view) {
        if(position != -1) {
            Album a = user.getAlbums().get(position);
            user.removeAlbum(a);
            adapter.remove(a.getName());
            listView.setAdapter(adapter);
            position = -1;
        }
        else {
            Toast.makeText(Photos.this, "Please select an album to delete", Toast.LENGTH_LONG).show();
        }
    }

    public void rename(View view) {
        String newName = albumName.getText().toString();
        if(!newName.isEmpty() && position != -1) {
            Album a = user.getAlbums().get(position);
            if(user.renameAlbum(a, newName)) {
                adapter.remove(adapter.getItem(position));
                adapter.add(newName);
                listView.setAdapter(adapter);
            }
            else {
                Toast.makeText(Photos.this, "Please select an unused album name to rename this album", Toast.LENGTH_LONG).show();
            }
        }
        else if(position == -1) {
            Toast.makeText(Photos.this, "Please select an album to rename", Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(Photos.this, "Please enter a new name for this album", Toast.LENGTH_LONG).show();
        }
    }

    public void search(View view) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(USER, user);
        Intent intent = new Intent(this, Search.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        Log.d("STATE", "HERE");
        if(resultCode == RESULT_OK) {
            Bundle bundle = intent.getExtras();
            if(requestCode == EDIT_ALBUM) {
                user = (User)bundle.getSerializable("user");
                ArrayList<String> albums = new ArrayList<>();
                for (Album a : user.getAlbums()) {
                    albums.add(a.getName());
                }
                adapter = new ArrayAdapter<>(this, R.layout.list_item, albums);
                listView.setAdapter(adapter);
            }
        }
    }

    public void save(View view) {
        try {
            FileOutputStream fos = this.openFileOutput("data.ser", Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(user);
            os.close();
            fos.close();
            Toast.makeText(Photos.this, "Changes saved!", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    // remember to cover saving when app is quit
    // remember to delete extra back buttons
    // remember to cover adding a photo
    // fix any appearance issues
    // remove fragment stuff
    // remember to cover search

}