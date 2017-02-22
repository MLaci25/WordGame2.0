package com.lacee.wordgame;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// TODO PASS THE RANDOM WORD THE PROCESS
// TODO READ IN THE 7 WORDS INTO AN ARRAY LIST AND THEN PASS THEM TO PROCESS

public class StartScreen extends AppCompatActivity
{
    String sourceWord= "";
    ArrayList<String> guessWords = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);
        TextView source = (TextView) findViewById(R.id.textView2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button submitWords  = (Button) findViewById(R.id.submit);
        Button clear = (Button) findViewById(R.id.clear);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                finish();
                startActivity(getIntent());

            }
        });

        //store the ids of the fileds in a array
        final int[] ids = new int[]  {R.id.editText2,
                                R.id.editText3,
                                R.id.editText4,
                                R.id.editText5,
                                R.id.editText6,
                                R.id.editText7,
                                R.id.editText8};




        submitWords. setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                //using a for loop store the values of the fields in an array list.
                for(int id : ids)
                {
                    EditText t = (EditText) findViewById(id);
                    guessWords.add(t.getText().toString());
                }

                callProcess(guessWords,sourceWord);
            }
        });
        //on clear button clear the vaules of the text fields
        clear.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                    for(int id : ids)
                    {
                        EditText t = (EditText) findViewById(id);
                        t.setText("hello");
                    }
                //display toest for setting/later clearing words
                Context context = getApplicationContext();
                Toast toast = Toast.makeText(context,"Words set",Toast.LENGTH_SHORT);
                toast.show();

            }
        });

        //Snackbar.make(view, "Floating Action button", Snackbar.LENGTH_LONG).setAction("Action", null).show();

        sourceWord = loadRandom();
        source.setText(sourceWord);

    }
// issue seems to be here
    public void callProcess(ArrayList<String> array, String string)
    {
        Intent myIntent = new Intent(StartScreen.this,Process.class);
        myIntent.putExtra("valueList",array);
        myIntent.putExtra("sourceWord",string);
        startActivity(myIntent);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.start_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    //read in a txt file
    //then choose a line from it randomly
    //this random word is then the source word.
    public String loadRandom()
    {
        try
        {
            BufferedReader reader = new BufferedReader(new InputStreamReader(getAssets().open("sourcewords.txt")));
            String line = reader.readLine();
            ArrayList<String> lines = new ArrayList<String>();

            while (line != null)
            {
                lines.add(line);
                line = reader.readLine();
            }
            reader.close();
            Random r = new Random();
            String randomLine = lines.get(r.nextInt(lines.size()));

                return randomLine;
        }
        catch(IOException error)
        {
            //TODO ERROR HANDLING
            Context context = getApplicationContext();
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, (CharSequence) error,duration);
            toast.show();
        }
        return "Error reading sourcewords";
    }

}