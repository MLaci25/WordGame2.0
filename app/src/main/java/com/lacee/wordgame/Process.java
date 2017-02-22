package com.lacee.wordgame;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.media.CamcorderProfile.get;

/**
 * Created by lacee on 13/01/2017.
 * Code revision by https://github.com/K-Chester-O
 */

public class Process extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_process);
        //get the list containing the inputted 7 words from previous activity
        ArrayList<String> inputtedWords = getIntent().getExtras().getStringArrayList("valueList");
        //get the sourceword from previous activity
        String sourceWord = getIntent().getExtras().getString("sourceWord");

        String valid = "Valid";
        boolean allOK = true;
        ArrayList<String> finalResult;
        if (inputtedWords.contains("")) {
            String error = "Guess words list is empty!";
            Context context = getApplicationContext();
            int duration = Toast.LENGTH_LONG;

            Toast toast = Toast.makeText(context, error, duration);
            toast.show();
        } else {
            //call funct to check if the inputted words are valid
            finalResult = checkIfWordIsValid(inputtedWords, sourceWord);

            int i = 0;
            do // instant loop as there is no need to check the length, checkIfWordIsValid() always creates a Array[6]
            {
                //all the words inputted are valid
                if (!finalResult.get(i).equals(valid)) // safer to use equals() with a !
                {
                    allOK = false;
                }
                // ELSE is not required, allOK is initialised to TRUE
                i++;
            }
            while (allOK && finalResult.size() <= 6);// this way if you come across a mistake, loop ends and code moves onto ELSE case of next IF
            if (allOK) {
                //call activity that show all words valid message
                Context context = getApplicationContext();
                int duration = Toast.LENGTH_LONG;
                String msg = "All words are valid! Well done";

                Toast toast = Toast.makeText(context, msg, duration);
                toast.show();
            } else {
                //call activity that will show some words were invalid message
                Context context = getApplicationContext();
                int duration = Toast.LENGTH_LONG;
                String msg = "Some words are invalid! Not good, try again";

                Toast toast = Toast.makeText(context, msg, duration);
                toast.show();
            }
        }

    }

    public ArrayList<String> checkIfWordIsValid(ArrayList<String> words, String source) {
        ArrayList<String> result = new ArrayList<String>();
        //check for words duplicates
        boolean duplicates = false;
        for (int j = 0; j < words.size(); j++) {
            for (int k = j + 1; k < words.size(); k++) {
                if (k != j && words.get(k).equals(words.get(j))) {
                    duplicates = true;
                    result.add("Duplicate");
                } else {
                    result.add("Valid");
                }
            }
        }
        if (duplicates) { //duplicate found
            String error = "Duplicated answer found!";
            Context context = getApplicationContext();
            int duration = Toast.LENGTH_LONG;

            Toast toast = Toast.makeText(context, error, duration);
            toast.show();
        } else { //no duplicates
            result.clear();
            boolean l = true;
            int i = 0;
            while (l && i < 7) {
                for (String word : words) {
                    l = checkLetters(word, source);
                    i++;
                }
            }
            if (l) {
                try {
                    String line = null;
                    List<String> guessWords = new ArrayList<String>();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(getAssets().open("guesswords.txt")));
                    while ((line = reader.readLine()) != null) {
                        guessWords.add(line);
                    }
                    // close the BufferedReader when we're done
                    reader.close();
                    //send words and guessWords for validity check
                    for (String word : words) {
                        if (!guessWords.contains(word)) {
                            result.add("Invalid");
                        } else {
                            result.add("Valid");
                        }
                    }
                } catch (IOException error) {
                    //TODO ERROR HANDLING
                    //display error
                    //toast breaks activity during debug
                    //Toast toastTwo = Toast.makeText(context,"Error",Toast.LENGTH_SHORT);
                    //toastTwo.show();
                }
            } else {
                for (String word : words) {
                    result.add("Invalid");
                }
                /*String error="Extra letter used in an answer!";
                Context context = getApplicationContext();
                int duration = Toast.LENGTH_LONG;

                Toast toast = Toast.makeText(context,error,duration);
                toast.show();*/
            }

        }
        return result;
    }

    public boolean checkLetters(String word, String source) {
        char[] wordLetters = word.toCharArray();
        char[] sourceLetters = source.toCharArray();
        Arrays.sort(wordLetters);
        Arrays.sort(sourceLetters);
        String sortedWord = new String(wordLetters);
        String sortedSource = new String(sourceLetters);
        int i = 0;
        while (sortedWord.length() != 0 && i < sortedSource.length()) {
            if (sortedWord.charAt(0) == sortedSource.charAt(i)) {
                sortedWord = sortedWord.substring(1);
                sortedSource = sortedSource.substring(0, i) + sortedSource.substring(i + 1);
                i = 0;
            } else {
                i++;
            }
        }
        if (sortedWord.length() > 0) {
            return false;
        } else {
            return true;
        }
    }
}