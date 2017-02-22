package com.lacee.wordgame;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by lacee on 26/01/2017.
 */
////Welcome screen finished
public class Welcome extends AppCompatActivity
{
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_screen);
        Button play = (Button)findViewById(R.id.button1);
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

                //// TODO: 26/01/2017 start Start Screen Activity
                Intent i = new Intent(Welcome.this,StartScreen.class);
                startActivity(i);
                finish();
            }
        });
    }
}
