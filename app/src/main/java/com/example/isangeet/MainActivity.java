package com.example.isangeet;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView=findViewById(R.id.listView);
        //step 1: first give the uses permission android in manifests.
        //step 2: search dexter android on google then go to first link copy "    implementation 'com.karumi:dexter:6.2.3'"
        //from there and paste in the gradle scripts  in the modulo parts.
        //step 3: take dexter rest code from there and u have make some changes in that (basically this is use for the permission and all
        // it is ask if want to allow app to access the particular folder or files or something else).
        Dexter.withContext(this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        //if permission is granted then set the Toast
//                        Toast.makeText(MainActivity.this, "Permission is granted", Toast.LENGTH_SHORT).show();
                        //step 6: now for showing all fetching songs
                        ArrayList<File> myfiles=fetchSong(Environment.getExternalStorageDirectory());
                        String[] items=new String[myfiles.size()];
                        for(int i=0;i<myfiles.size();i++){
                            items[i]=myfiles.get(i).getName().replace(".mp3","");
                        }
                        //for showing in the listview using arrayAdapter
                        ArrayAdapter<String> adapter=new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1,items);
                        listView.setAdapter(adapter);

                        //step 7: if we click on any item of adapter(song) on listView ,it will direct us
                        // to next screen
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                             //step 8: create the intent for the next screen which has PlaySong class
                                Intent intent=new Intent(MainActivity.this,Playsong.class);
                                String currentSong=listView.getItemAtPosition(i).toString();
                                intent.putExtra("songList",myfiles);
                                intent.putExtra("currentSong",currentSong);
                                intent.putExtra("position",i);
                                startActivity(intent);


                            }
                        });
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                    // step 4:if someone open the app again then it will ask for the permission for that below line help
                        permissionToken.continuePermissionRequest();
                    }
                })
                .check();

    }
    //step 5:write a function which fetch the data from our internal or external memory
    public ArrayList<File> fetchSong(File file){
        ArrayList arrayList=new ArrayList();
        File[] songs=file.listFiles();
        if(songs!=null){
            for(File myfile:songs){
                //if there is folder in which we have music playlist then the below condition statement will execute
                if(!myfile.isHidden() && myfile.isDirectory()){
                    arrayList.addAll(fetchSong(myfile));
                }
                //or if there is no hidden files and folder and we have the   music file then we add that in our arraylist
                else{
                    if( myfile.getName().endsWith(".mp3") &&!myfile.getName().startsWith(".")){
                        arrayList.add(myfile);
                    }

                }
            }
        }
        return arrayList;
    }

}