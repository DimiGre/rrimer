package com.example.mityha.rrimer;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.SurfaceHolder;
import android.widget.ImageView;

import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.Random;

import javax.crypto.SecretKey;

class DrawThread extends Thread implements Serializable{

    public MainActivity mainActivity;
    protected Random random;
    public static final int NOTHING = Color.BLACK;//Color.BLACK;
    public static final int ENEMY = Color.GREEN;//Color.GREEN;
    public static final int FOOD = Color.GRAY;//Color.YELLOW;
    private ArrayDeque<Animal> queue = null;
    private int[][] field = null;
    private int height;
    private int width;
    private Canvas canvas;
    private final Bitmap bitmap;

    public int getAt(int whereX, int whereY) {
        if(whereX < height && whereY < width && whereX > -1 && whereY > -1)
        return field[whereX][whereY];
        else return NOTHING;
    }

    public void removeFrom(int x, int y) {
        setTo(x, y, NOTHING);
    }

    public int moveMeTo(Animal me, int toX, int toY) {
            if (getAt(toX, toY) == ENEMY) return -1; //враг
            if (getAt(toX, toY) == FOOD) return 0; //еда
            if (getAt(toX, toY) == NOTHING) { //пусто
                removeFrom(me.getPosX(), me.getPosY());
                Pendent pendent = setTo(toX, toY, ENEMY);
                me.setPosXY(pendent.X, pendent.Y);
            }
        return 1;
    }

    public Pendent setTo(int x, int y, int val) {
        if(x == height){
            x = 0;
        }
        if(y == width){
            y = 0;
        }
        if(x == -1){
            x = height - 1;
        }
        if(y == -1){
            y = width - 1;
        }
        synchronized (field) {
            field[x][y] = val;
        }

        Paint p = new Paint();
        p.setColor(val);
        p.setStrokeWidth(4);
        canvas.drawPoint(x * 10, y * 10, p);

        return new Pendent(x, y);
    }

    public void regMeForXY(Animal animal){
        if (queue == null) queue = new ArrayDeque<>();
        queue.add(animal);
    }


    public DrawThread(MainActivity _mainActivity){
        mainActivity = _mainActivity;
        height = mainActivity.imageView.getWidth() / 10;
        width = mainActivity.imageView.getHeight() / 10;
        random = new Random();

        bitmap = Bitmap.createBitmap(mainActivity.imageView.getWidth(), mainActivity.imageView.getHeight(), Bitmap.Config.RGB_565);
        canvas = new Canvas(bitmap);
        canvas.drawColor(Color.BLACK);

        Paint p = new Paint();
        p.setColor(Color.RED);
        p.setStrokeWidth(1);

        for (int y = 0; y <= width; y++)
            canvas.drawLine(2, (y * 10) + 2, (height * 10) - 2, (y * 10) + 2, p); //горизнталные
        for (int x = 0; x <= height; x++)
            canvas.drawLine((x * 10) + 2, 2, (x * 10) + 2, (width * 10) - 2, p);  //вертикальные

        field = new int[height][width];
        synchronized (field) {
            for (int x = 0; x < height; x++)
                for (int y = 0; y < width; y++) {
                    field[x][y] = NOTHING;
                    if (random.nextInt(100) < Settings.foodChance) setTo(x, y, FOOD);
                }
        }
    }


    boolean flag = true;

    @Override
    public void run() {
        while (!(isInterrupted())) {
            if (flag) {
                if (queue != null && field != null)
                    while (queue.isEmpty() == false)
                        queue.pollFirst().setPosXYRand(height, width);

                flag = false;
                mainActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mainActivity.imageView.setImageBitmap(bitmap);
                        mainActivity.imageView.invalidate();
                        mainActivity.forebot.allDone = true;
                        flag = true;
                    }
                });
            }
        }
    }
}