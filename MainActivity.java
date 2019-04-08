package com.example.mityha.rrimer;

import android.content.Intent;
import android.os.Looper;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public ImageView imageView;
    public Forebot forebot;
    private DrawThread df;
    private int RatsLeft = 0;
    private Animal[] animals;
    private int generation = 0;
    private int oldSaveFr = Settings.saveFrequency;

    @Override
    protected void onActivityResult(int reqestCode, int resultCode, Intent data) {
        if(reqestCode == 1)
        if(resultCode == 1)
        if (df != null || forebot != null) {
            df.interrupt();
            forebot.interrupt();
            startornot = true;
            animals = null;
            boolean flag = true;
            for (int gen = 0; gen < generation; gen += oldSaveFr) {
                for (int i = 0; flag; i++) {
                    flag = (new File(getFilesDir(), "rat-" + i + " " + gen + ".dat")).delete();
                }
            }
            oldSaveFr = Settings.saveFrequency;
            start();
        }
        if(reqestCode == 2)
            if(resultCode == RESULT_OK){
            int rC = 0;
                ArrayList<Animal> animalArrayList = new ArrayList<>();
                df  = new DrawThread(this);
                forebot = new Forebot();
                for (int gen = 0; gen < generation; gen++){
                    Object obj = data.getExtras().get("gen" + gen);
                    if(obj != null){
                        for (int i = 0; (new File(getFilesDir(), "rat-" + i + " " + ((int)obj) + ".dat")).exists(); i++)
                            try {
                                ObjectInputStream ois = new ObjectInputStream(this.openFileInput("rat-" + i + " " + ((int)obj) + ".dat"));
                                Animal animal = new Monkey(df, forebot, (int[][])ois.readObject());
                                animal.name = ((int)obj) + "";
                                animalArrayList.add(animal);
                                rC++;
                                ois.close();
                            } catch (Exception e) {}
                    }
                }
             animals = animalArrayList.toArray(new Animal[animalArrayList.size()]);
                RatsLeft = rC;
                df.start();
                for (int i = 0; i < 8333*Settings.maxPopulation; i++);
                forebot.start();
                first = true;
        }

    }

    String message;

    public void AndryhaUNasTrup(){
        RatsLeft--;
        if(!arenaMode)
        if(RatsLeft == Settings.minPopulation){
            int gi = 0;

            forebot.interrupt();
            df.interrupt();

            forebot = new Forebot();
            df = new DrawThread(this);

            Animal[] goodRats = new Animal[Settings.minPopulation];
            for(int i = 0; i < Settings.maxPopulation; i++){
                if(animals[i].AmIalive()){
                    goodRats[gi] = animals[i];
                    gi++;
                }
                animals[i] = null;
            }

            generation++;
            if((generation-1)%Settings.saveFrequency == 0){
                for(int i = 0; i < Settings.minPopulation; i++) goodRats[i].SaveMeToFile(i, generation);
            }

            try
            {
                ObjectOutputStream oos = new ObjectOutputStream(this.openFileOutput("generation.dat", MODE_PRIVATE));
                oos.writeObject(generation);
                oos.close();
            }
            catch(Exception e){
            }

            for (gi = 0; gi < Settings.minPopulation; gi++)
                for(int ai = 0; ai < Settings.numberOfClones; ai++){
                    if(ai == Settings.numberOfClones / 2) goodRats[gi].teach();
                    animals[gi*Settings.numberOfClones+ai] = new Monkey(df, forebot, goodRats[gi].getMemory());
                }
            for(int ai = gi * Settings.numberOfClones; ai < Settings.maxPopulation; ai++){
                animals[ai] = new Monkey(df, forebot);
            }
        RatsLeft = Settings.maxPopulation;
            if(Settings.save)
            for(int i = 0; i < Settings.maxPopulation; i++)
                animals[i].SaveMeToFile(i, 0);
        df.start();
        for (int i = 0; i < 8333*Settings.maxPopulation; i++);
        forebot.start();
        }


        if(arenaMode){
            if(RatsLeft == Settings.minPopulation || RatsLeft == 1){
                forebot.interrupt();
                df.interrupt();
                boolean flag = true;
                message = "Выжили крысы из поколений: ";
                for(int i = 0;flag; i++){
                    flag = false;
                    try{
                        if(animals[i].AmIalive() && message.indexOf(animals[i].name) == -1) message += animals[i].name + " ";
                        flag = true;
                    } catch (Exception e){}
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        (new AlertDialog.Builder(MainActivity.this)).setMessage(message).create().show();
                    }
                });
                arenaMode = false;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            ObjectInputStream ois = new ObjectInputStream(this.openFileInput("generation.dat"));
            generation = (int)ois.readObject();
            ois.close();
        }
        catch(Exception e){}

        imageView = findViewById(R.id.imageView);
        animals = null;
        Settings.context = getApplicationContext();
        Settings.readObj();
    }

    boolean startornot = true;
    boolean first = true;

    private void start(){
        forebot = new Forebot();
        df = new DrawThread(this);
        animals = new Animal[Settings.maxPopulation];
        int i = 0;
            if(Settings.save){
                boolean flag = true;
                for(i = 0; flag && i < Settings.maxPopulation; i++) {
                    flag = false;
                    try {
                        ObjectInputStream ois = new ObjectInputStream(this.openFileInput("rat-" + i + " 0.dat"));
                        animals[i] =  new Monkey(df, forebot, (int[][])ois.readObject());
                        ois.close();
                        flag = true;
                    } catch (Exception e) {
                        i--;
                    }
                }
            }
        for(;i < Settings.maxPopulation; i++) animals[i] = new Monkey(df, forebot);
        RatsLeft = i;
    }

    public void stop(){
        if(df != null || forebot != null) {
            df.interrupt();
            forebot.interrupt();
            ((Button)findViewById(R.id.button)).setText("Старт");
            startornot = true;
        }
    }

    public void onClickBtn(View v){
        if(first){
        start();
        first = false;
        }

        if(startornot){
        df.start();
        forebot.start();
        ((Button)v).setText("Пауза");
        startornot = false;
        } else {
            stop();
        }
    }

    public void onChangeSettings(View v){
        stop();
        startActivityForResult(new Intent(this, Settings.class), 1);
    }

    boolean arenaMode = false;
    public void onStartArena(View v){
            stop();
            arenaMode = true;
            Intent intent = new Intent(this, Arena.class);
            intent.putExtra("generation", generation);
            startActivityForResult(intent, 2);
    }
}
