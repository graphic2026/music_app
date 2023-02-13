package com.example.isangeet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class Playsong extends AppCompatActivity {

    //step 3: if we want to stop music when we came back from second screen through the previous arrow in navbar
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
        mediaPlayer.release();
        updateseek.interrupt();
    }

    TextView textView;
    ImageView imageView,play,previous,next,pause;
    MediaPlayer mediaPlayer;
    ArrayList<File> songs;
    int position;
    Thread updateseek; //for update our seekbar we have initialize like this
    SeekBar seekBar;
    //
    String textContent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playsong);
        textView=findViewById(R.id.textView);
//        pause=findViewById(R.id.pause);
        previous=findViewById(R.id.previous);
//        play=findViewById(R.id.play);
        next=findViewById(R.id.next);
        seekBar=findViewById(R.id.seekBar);
        play=findViewById(R.id.play);

        // step 1: now we take intent from the mainActivity class for showing the music name in textview and
        //do for do other operation also........
        Intent intent=getIntent();
        //
        Bundle bundle=intent.getExtras();
        songs=(ArrayList)bundle.getParcelableArrayList("songList");
        textContent=intent.getStringExtra("currentSong");
        textView.setText(textContent);
        //below line of code needed for move the name of song horizontal with the step
        textView.setSelected(true);
        //step 2: now for playing the song ,we need the position of that particular song and
        // we get that using the intent.getIntExtra
        position=intent.getIntExtra("position",0);
        Uri uri=Uri.parse(songs.get(position).toString());
        mediaPlayer=MediaPlayer.create(this,uri);
        mediaPlayer.start();
        seekBar.setMax(mediaPlayer.getDuration());
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //if we change the position of seekbar and below line of code help us to change the music with it.
                mediaPlayer.seekTo(seekBar.getProgress());
            }
        });

        //for  update the seekbar with the song we will use the  updateseek means as the music is playing
        //seekbar is  also moving with the music
        updateseek =new Thread(){
            @Override
            public void run() {
                int currentPosition=0;
                try {
                    while(currentPosition<mediaPlayer.getDuration()){
                        //if we want our seekbar start from starting and going with the music then we
                        //have to put getcurrenPosition in inplace of getDureation.
                        currentPosition=mediaPlayer.getCurrentPosition();
                        seekBar.setProgress(currentPosition);
                        sleep(800);
                    }
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }
        };
        updateseek.start();



        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mediaPlayer.isPlaying()){
                    play.setImageResource(R.drawable.play);
                    mediaPlayer.pause();
                }
                else{
                    play.setImageResource(R.drawable.pause);
                    mediaPlayer.start();
                }
//                mediaPlayer.pause();
            }
        });

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.stop();
                mediaPlayer.release();
                if(position!=0){
                    position=position-1;
                }
                else{
                    position=songs.size()-1;
                }
                Uri uri=Uri.parse(songs.get(position).toString());
                mediaPlayer=MediaPlayer.create(getApplicationContext(),uri);
                mediaPlayer.start();
                play.setImageResource(R.drawable.pause);
                seekBar.setMax(mediaPlayer.getDuration());
                textContent=songs.get(position).getName().toString();
                textView.setText(textContent);
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.stop();
                mediaPlayer.release();
                if(position!=songs.size()-1){
                    position=position+1;
                }
                else{
                    position=0;
                }
                Uri uri=Uri.parse(songs.get(position).toString());
                mediaPlayer=MediaPlayer.create(getApplicationContext(),uri);
                mediaPlayer.start();
                play.setImageResource(R.drawable.pause);
                seekBar.setMax(mediaPlayer.getDuration());
//                for the name of the songn we have to write below line of code
                textContent=songs.get(position).getName().toString();
                textView.setText(textContent);
            }
        });

        //step 3:
        //if we want that our song move horizontally then we have to do some modify in the
        //activity_playsong xml like write some line of code given below
        //
//        android:marqueeRepeatLimit="marquee_forever"
//        android:ellipsize="marquee"
//        android:scrollHorizontally="true"
//        android:singleLine="true"
//        android:fadingEdge="horizontal"

        //step 4:
        //we can change our icon of the app ->go to file->new->image asset->clip art,
        //then change the according to our need.

    }
}