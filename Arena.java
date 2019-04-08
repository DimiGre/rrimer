package com.example.mityha.rrimer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import java.io.File;

public class Arena extends AppCompatActivity {

    private int generations = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_arena);
    LinearLayout layout = findViewById(R.id.layoutArena);

    generations = getIntent().getIntExtra("generation", -1);

    for (int gen = 0; gen < generations; gen += Settings.saveFrequency)
        if((new File(getFilesDir(), "rat-" + 0 + " " + gen + ".dat")).exists()) {
            CheckBox checkBox = new CheckBox(getApplicationContext());
            checkBox.setId(gen);
            checkBox.setChecked(false);
            checkBox.setText("Использовать поколение " + gen);
            layout.addView(checkBox);
        }
    }

    public void onStart(View v){
        Intent intent = new Intent();
        int c = 0;
        for(int i = 0; i < generations; i += Settings.saveFrequency){
            CheckBox checkBox = ((CheckBox)findViewById(i));
            if(checkBox != null)
            if(checkBox.isChecked())
                intent.putExtra("gen" + i, i);
        }
        setResult(RESULT_OK, intent);
        finish();
    }
}
