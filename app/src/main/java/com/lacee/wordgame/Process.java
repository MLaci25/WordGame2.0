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

import static android.media.CamcorderProfile.get;

/**
 * Created by lacee on 13/01/2017.
 */

public class Process extends AppCompatActivity
{

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_process);
        //get the list containing the inputted 7 words from previous activity
        ArrayList<String> inputtedWords = getIntent().getExtras().getStringArrayList("valueList");
        //get the sourceword from previous activity
        String sourceWord = getIntent().getExtras().getString("sourceWord");

        String valid = "Valid";
        boolean allOK = true;
        String [] finalResult;
        if(inputtedWords.contains(""))
        {
            String error="Guess words list is empty!";
            Context context = getApplicationContext();
            int duration = Toast.LENGTH_LONG;

            Toast toast = Toast.makeText(context,error,duration);
            toast.show();
        }
        else {
            //call funct to check if the inputted words are valid
            finalResult = checkIfWordIsValid(inputtedWords, sourceWord);

            int i = 0;
            do // instant loop as there is no need to check the length, checkIfWordIsValid() always creates a Array[6]
            {
                //all the words inputted are valid
                if (!finalResult[i].equals(valid)) // safer to use equals() with a !
                {
                    allOK = false;
                }
                // ELSE is not required, allOK is initialised to TRUE
                i++;
            }
            while (allOK && finalResult.length <= 6);// this way if you come across a mistake, loop ends and code moves onto ELSE case of next IF
            if (allOK) {
                //call activity that show all words valid message
                Context context = getApplicationContext();
                int duration = Toast.LENGTH_LONG;
                String msg = "All words are valid! Well done";

                Toast toast = Toast.makeText(context, msg, duration);
                //toast.show();
            } else {
                //call activity that will show some words were invalid message
                Context context = getApplicationContext();
                int duration = Toast.LENGTH_LONG;
                String msg = "Some words are invalid! Not good, try again";

                Toast toast = Toast.makeText(context, msg, duration);
                //toast.show();
            }
        }

    }

    public String[] checkIfWordIsValid(ArrayList<String> words, String source){
        String result [] = new String[6];
        //check for words duplicates
        boolean duplicates = false;
        for (int j = 0; j < words.size(); j++) {
            for (int k = j + 1; k < words.size(); k++) {
                if (k != j && words.get(k).equals(words.get(j))) {
                    duplicates = true;
                }
            }
        }
        if (duplicates) { //duplicate found
            String error="Duplicated answer found!";
            Context context = getApplicationContext();
            int duration = Toast.LENGTH_LONG;

            Toast toast = Toast.makeText(context,error,duration);
            toast.show();
            /*
            * NEED TO ADD result[] POPULATION TO OUTLINE DUPLICATES
            * MIGHT REQUIRE SMALL CHANGES TO DUPLICATES LOOP ABOVE
            * AT THE MOMENT APP CRASHES AS result[] IS NULLABLE
            * TOAST DOES NOT SHOW AT ALL
            * */
        } else { //no duplicates
            boolean l = true;
            int i = 0;
            while (l && i < 7) {
                for (String word : words) {
                    l = checkLetters(word, source);
                    i++;
                }
            }
            System.out.println("TEMP");
            if(l){
                try
                {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(getAssets().open("guesswords.txt")));
                    String line = reader.readLine();
                    /*************************************************
                     ***************CODE WORKS THIS FAR***************
                     **BUT CHANGES MIGHT BE NEEDED TO PREVIOUS STEPS**
                     ***I HAVE BASED CODE ON MY PYTHON/WEB APPROACH***
                     ****COMPARISON WITH TEXT FILE TO BE CODED********
                     *************************************************/
                }
                catch(IOException error)
                {
                    //TODO ERROR HANDLING
                    //display error
                    //toast breaks activity during debug
                    //Toast toastTwo = Toast.makeText(context,"Error",Toast.LENGTH_SHORT);
                    //toastTwo.show();
                }
            }
            else{
                String error="Extra letter used in an answer!";
                Context context = getApplicationContext();
                int duration = Toast.LENGTH_LONG;

                Toast toast = Toast.makeText(context,error,duration);
                //toast.show();
            }

        }
        return result;
    }

    public boolean checkLetters(String word, String source){
        char[] wordLetters = word.toCharArray();
        char[] sourceLetters = source.toCharArray();
        Arrays.sort(wordLetters);
        Arrays.sort(sourceLetters);
        String sortedWord = new String(wordLetters);
        String sortedSource = new String(sourceLetters);
        int i = 0;
        while(sortedWord.length()!=0 && i<sortedSource.length()){
            if(sortedWord.charAt(0)==sortedSource.charAt(i)){
                sortedWord = sortedWord.substring(1);
                sortedSource = sortedSource.substring(0, i) + sortedSource.substring(i + 1);
                i=0;
            }
            else{
                i++;
            }
        }
        if(sortedWord.length()>0){
            return false;
        }
        else{
            return true;
        }
    }
/*
  _____         ______       _______  _____  ______  _______      ______  _______         _____  _  _  _
 |     | |      |     \      |       |     | |     \ |______      |_____] |______ |      |     | |  |  |
 |_____| |_____ |_____/      |_____  |_____| |_____/ |______      |_____] |______ |_____ |_____| |__|__|

*/
    /*//check if word inputted is valid or not
    public String[] checkIfWordIsValid(ArrayList<String> words, String source)
    {
        String temp [] = new String[6];
        String result [] = new String[6];
        int size = words.size();

        String checkWord = "";
        ArrayList<String> duplicated;

        boolean checDictResult = false;
        boolean checkCharResult = false;

        for(int i = 0; i < size ;i++)
        {
            checkWord = words.get(i);
            checDictResult = checkAgainstDict(checkWord);
            //using sample word hello the result = false for the above/should be true
            checkCharResult = checkForCharacters(checkWord,source);

            if(checDictResult == true && checkCharResult == true)
            {
                duplicated = checkForDuplicates(words);
                if(duplicated.isEmpty())
                {
                    temp[i] = "Valid";
                }
                else
                {
                    temp[i] = "Invalid";
                }
                result[i] = temp[i];
            }
        }
        return result;
    }
    //check inputted word in dictionary
    public boolean checkAgainstDict(String string)
    {
        //display toast
        //toast breaks activity during debug
        Context context = getApplicationContext();
        Toast toast = Toast.makeText(context,"func checkagainstdict",Toast.LENGTH_SHORT);
        //toast.show();

        int control = 1 ;

        try
        {
            BufferedReader reader = new BufferedReader(new InputStreamReader(getAssets().open("guesswords.txt")));
            String line = reader.readLine();

            while (line != null || control != 0)
            {
                if(line.trim().isEmpty() == false && string.trim() == line.trim() && string.length() == line.length())
                {
                    control = 0;
                }
                line = reader.readLine();
            }
            reader.close();
            return true;
        }
        catch(IOException error)
        {
            //TODO ERROR HANDLING
            //display error
            //toast breaks activity during debug
            Toast toastTwo = Toast.makeText(context,"Error",Toast.LENGTH_SHORT);
            //toastTwo.show();

        }
        return false;
    }
    //check the characters from the inputted words are in the source word
    public boolean checkForCharacters(String string,String source)
    {
        //display toast
        Context context = getApplicationContext();
        Toast toastThree = Toast.makeText(context,"func checkforchars",Toast.LENGTH_SHORT);
        //toastThree.show();
        //toast breaks activity during debug
        //check if string is part of source
        if(string.indexOf(source)!=-1)
        {
            return true;
        }
        else {
            return false;
        }
    }
    //check for duplicate words
    public ArrayList<String> checkForDuplicates(ArrayList<String> list)
    {
        ArrayList<String> duplicates = new ArrayList<>();
        ArrayList<String> output = new ArrayList<>();

        for(int i= 0; i < list.size(); i++ )
        {
            //check if the first word is in output
            //if its not, add to it
            //if it is then add to duplicates
            if(!list.get(i).equals(output.get(i)))
            {
                output.add(String.valueOf(i));
            }
            else
            {
                duplicates.add(String.valueOf(i));
            }
        }
        return duplicates;
    }*/
}