package com.example.mityha.rrimer;

import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Settings extends AppCompatActivity implements Serializable{

    static public Context context = null;
    static public int maxPopulation = 120;
    static public int minPopulation = 10;
    static public int foodChance = 1;
    static public int nutritionalVal = 2048;
    static public int startStock = 2048;
    static public int turnLen = 16;
    static public int costSAttack = 1;
    static public int costMiss = 1;
    static public int costStep = 1;
    static public int costView = 0;
    static public int numberOfClones = 10;
    static public int pause = 0;
    static public int hunger = 0;
    static public int saveFrequency = 0;
    static public boolean save = false;

    static private int restartCode = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        TextView view = findViewById(R.id.editText);
        view.setText(maxPopulation + "");
        view = findViewById(R.id.editText3);
        view.setText(minPopulation + "");
        view = findViewById(R.id.editText4);
        view.setText(foodChance + "");
        view = findViewById(R.id.editText5);
        view.setText(nutritionalVal + "");
        view = findViewById(R.id.editText6);
        view.setText(startStock + "");
        view = findViewById(R.id.editText7);
        view.setText(turnLen + "");
        view = findViewById(R.id.editText8);
        view.setText(costSAttack + "");
        view = findViewById(R.id.editText11);
        view.setText(costMiss + "");
        view = findViewById(R.id.editText9);
        view.setText(costStep + "");
        view = findViewById(R.id.editText10);
        view.setText(costView + "");
        view = findViewById(R.id.editText2);
        view.setText(numberOfClones + "");
        view = findViewById(R.id.editText13);
        view.setText(hunger + "");
        view = findViewById(R.id.editText12);
        view.setText(pause + "");
        view = findViewById(R.id.editText14);
        view.setText(saveFrequency + "");
        ((CheckBox)findViewById(R.id.checkBox)).setChecked(save);
    }

    public void onRestart(View v){
        if(restartCode == 0){
            restartCode = 1;
            ((Button)v).setText("Не перезапускать");
        } else {
            restartCode = 0;
            ((Button)v).setText("Перезапустить");
        }
    }


    public void onSave(View v) {
        if ((Integer.parseInt(((EditText)findViewById(R.id.editText3)).getText().toString()) *
                Integer.parseInt(((EditText)findViewById(R.id.editText2)).getText().toString())) >
                Integer.parseInt(((EditText)findViewById(R.id.editText)).getText().toString())) {
            (new AlertDialog.Builder(Settings.this)).setMessage("Невозможно сохранить. Произведение минимального размера на количество клонов долшно быть не больше размера популяции").create().show();
        } else
            if((Integer.parseInt(((EditText)findViewById(R.id.editText)).getText().toString()) != maxPopulation) ||
                    (Integer.parseInt(((EditText)findViewById(R.id.editText3)).getText().toString()) != minPopulation)){
                (new AlertDialog.Builder(Settings.this)).setMessage("При изменении значения максимольного или минимального размера популяции необходим перезапуск симуляции. Вы согласны?")
                        .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                restartCode = 1;
                                saveSettings();
                                dialog.cancel();
                            }
                        })
                        .setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                               dialog.cancel();
                            }
                        }).create().show();
            } else
                saveSettings();
        }

        private void saveSettings(){
            EditText val = findViewById(R.id.editText);
            maxPopulation = Integer.parseInt(val.getText().toString());
            val = findViewById(R.id.editText3);
            minPopulation = Integer.parseInt(val.getText().toString());
            val = findViewById(R.id.editText4);
            foodChance = Integer.parseInt(val.getText().toString());
            val = findViewById(R.id.editText5);
            nutritionalVal = Integer.parseInt(val.getText().toString());
            val = findViewById(R.id.editText6);
            startStock = Integer.parseInt(val.getText().toString());
            val = findViewById(R.id.editText7);
            turnLen = Integer.parseInt(val.getText().toString());
            val = findViewById(R.id.editText8);
            costSAttack = Integer.parseInt(val.getText().toString());
            val = findViewById(R.id.editText11);
            costMiss = Integer.parseInt(val.getText().toString());
            val = findViewById(R.id.editText9);
            costStep = Integer.parseInt(val.getText().toString());
            val = findViewById(R.id.editText10);
            costView = Integer.parseInt(val.getText().toString());
            val = findViewById(R.id.editText2);
            numberOfClones = Integer.parseInt(val.getText().toString());
            val = findViewById(R.id.editText12);
            pause = Integer.parseInt(val.getText().toString());
            val = findViewById(R.id.editText13);
            hunger = Integer.parseInt(val.getText().toString());
            val = findViewById(R.id.editText14);
            saveFrequency = Integer.parseInt(val.getText().toString());
            save = ((CheckBox)findViewById(R.id.checkBox)).isChecked();
            SaveObj();
            setResult(restartCode);
            finish();
    }

    private static  void saveField(String name, int value){
        try
        {
            ObjectOutputStream oos = new ObjectOutputStream(context.openFileOutput("settings-" + name + ".dat", MODE_PRIVATE));
            oos.writeObject(value);
            oos.close();
        }
        catch(Exception e){
        }
    }
    private static  int readField(String name){
        int val = -1;
        try {
            ObjectInputStream ois = new ObjectInputStream(context.openFileInput("settings-" + name + ".dat"));
            val = (int)ois.readObject();
            ois.close();
        }
        catch(Exception e){}
        return val;
    }


    public static void SaveObj(){
        saveField("Saved", 1);
        saveField("maxPopulation", maxPopulation);
        saveField("minPopulation", minPopulation);
        saveField("foodChance", foodChance);
        saveField("nutritionalVal", nutritionalVal);
        saveField("startStock", startStock);
        saveField("turnLen", turnLen);
        saveField("costSAttack", costSAttack);
        saveField("costMiss", costMiss);
        saveField("costStep", costStep);
        saveField("costView", costView);
        saveField("numberOfClones", numberOfClones);
        saveField("hunger", hunger);
        saveField("pause", pause);
        saveField("saveFrequency", saveFrequency);
        saveField("save", save?1:0);
    }

    public static void readObj(){
        if(readField("Saved") == 1) {
            maxPopulation = readField("maxPopulation");
            minPopulation = readField("minPopulation");
            foodChance = readField("foodChance");
            nutritionalVal = readField("nutritionalVal");
            startStock = readField("startStock");
            turnLen = readField("turnLen");
            costSAttack = readField("costSAttack");
            costMiss = readField("costMiss");
            costStep = readField("costStep");
            costView = readField("costView");
            numberOfClones = readField("numberOfClones");
            hunger = readField("hunger");
            pause = readField("pause");
            saveFrequency = readField("saveFrequency");
            save = readField("save") == 1?true:false;
        }
    }

}
