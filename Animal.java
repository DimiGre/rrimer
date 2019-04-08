package com.example.mityha.rrimer;

import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Random;
import java.util.Stack;

import static android.content.Context.MODE_PRIVATE;

abstract public class Animal implements Serializable{
    private static final long serialVersionUID = 1L;
    private HashSet<Integer> unused;
    protected Forebot forebot;
    private boolean alive;
    public String name;
    protected int memory[][] = new int[128][2];  //32 ячейки действия
    protected int food;            //  _________ - Аничков     |Не
    protected int posX = -1, posY = -1;     //  проход между зданиями   |вс
    protected DrawThread field;             //  |М|    |Х|              |к
    private Random random;                //  |О|    |У|              |и
    abstract int moveUp();                  //  |Й|    |Д|              |й
    abstract int moveDown();
    abstract int moveLeft();
    abstract int moveRight();
    abstract int lookUp();
    abstract int lookDown();
    abstract int lookLeft();
    abstract int lookRight();
    abstract int eatUp();
    abstract int eatDown();
    abstract int eatLeft();
    abstract int eatRight();
    abstract int attackUp();
    abstract int attackDown();
    abstract int attackLeft();
    abstract int attackRight();

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

    public Animal(DrawThread _field, Forebot _forebot){
        random = new Random();
        unused = new HashSet<>();
        field = _field;
        forebot = _forebot;
        field.regMeForXY(this);
        for(int i = 0; i < 128; i++) {
            if (i % 4 == 0) {
                memory[i][0] = random.nextInt(16) + 1; //16 возможжных дейстивий
                memory[i][1] = 0;
                unused.add(i / 4);
            } else {
                memory[i][0] = random.nextInt(32); // переходы на них
                memory[i][1] = 0;
            }
        }
        alive = true;
        food = Settings.startStock;
    }

    public Animal(DrawThread _field, Forebot _forebot, int[][] _memory){
        random = new Random();
        unused = new HashSet<>();
        for(int i = 0, j = 0; i < 32; i++, j++)
                unused.add(i);
        field = _field;
        forebot = _forebot;
        field.regMeForXY(this);
        memory = _memory;
        alive = true;
        food = Settings.startStock;
    }

    public int[][] getMemory() {
        return memory;
    }

    public boolean AmIalive() {
        return alive;
    }

    public void setPosXYRand(int maxPosX, int maxPosY) {
        posX = random.nextInt(maxPosX);
        posY = random.nextInt(maxPosY);
        field.setTo(posX, posY, field.ENEMY);
        forebot.IamReady(this);
    }

    public void setPosXY(int posX, int posY) {
        this.posX = posX;
        this.posY = posY;
    }

    public void IamDead(){
        field.setTo(posX, posY, DrawThread.FOOD);
        alive = false;
        field.mainActivity.AndryhaUNasTrup();
    }

    public void act() {
       Stack<Integer> actions = new Stack<>();

       int currentAction = 0;
       if(food < 0){
            forebot.ItsKIlledBy(posX, posY, this);
            return;
        }
        for(int i = 0; i < Settings.turnLen; i++) {
           unused.remove(currentAction);
            int f = food  - Settings.hunger;
            int res = doIt(memory[currentAction * 4][0]);
            actions.push(memory[currentAction * 4][0]);
            currentAction = memory[currentAction * 4 + res][0];
            food -= Settings.hunger;
            if(food > f)
                for(int j = 1; !actions.empty(); j++)
                    memory[actions.pop()][1] += (Settings.startStock / (Math.max(Settings.costView ,Math.max(Math.max(Settings.hunger, Settings.costMiss), Math.max(Settings.costSAttack,Settings.costStep))))) * j;
        }
        for(int i = 0; i < Settings.turnLen; i++) memory[i][1] -= 1;
    }

    public void teach(){
        Integer[] unsedActions = unused.toArray(new Integer[unused.size()]);
        for(int i = 0, ui = 0; i < 128; i++) {
            if(memory[i][1] < 0)
            if (i % 4 == 0) {
                memory[i][0] = random.nextInt(16) + 1; //16 возможжных дейстивий
                memory[i][1] = 0;

            } else {
                if(unused.size() > ui) {
                    memory[i][0] = unsedActions[ui];
                    ui++;
                } else {
                    memory[i][0] = random.nextInt(32); // переходы на них
                }
                memory[i][1] = 0;
            }
        }
    }

    private int doIt(int i){
        if(i == 1) i =  moveUp();//
        if(i == 2) i = lookUp();
        if(i == 3) i = eatUp();
        if(i == 4) i = attackUp();
        if(i == 5) i = moveDown();//
        if(i == 6) i = lookDown();
        if(i == 7) i = eatDown();
        if(i == 8) i = attackDown();
        if(i == 9) i = moveRight();//
        if(i == 10) i = lookRight();
        if(i == 11) i = eatRight();
        if(i == 12) i = attackRight();
        if(i == 13) i = moveLeft();//
        if(i == 14) i = lookLeft();
        if(i == 15) i = eatLeft();
        if(i == 16) i = attackLeft();

        if (i == DrawThread.NOTHING)
            i = 1;
        if (i == DrawThread.ENEMY)
            i = 2;
        if (i == DrawThread.FOOD)
            i = 3;

        return i;
    }

    public void SaveMeToFile(int myNumber, int generation){
        try
        {
            name = Settings.maxPopulation + " " + Settings.minPopulation + " " +
                    Settings.foodChance + " " + Settings.nutritionalVal + " " +
                    Settings.startStock + " " + Settings.turnLen + " " +
                    Settings.costSAttack + " " + Settings.costMiss + " " +
                    Settings.costStep + " " + Settings.costView + Settings.numberOfClones +
                    Settings.pause + " " + myNumber;
            ObjectOutputStream oos = new ObjectOutputStream(field.mainActivity.openFileOutput("rat-" + myNumber + " " + generation + ".dat", MODE_PRIVATE));
            oos.writeObject(memory);
            oos.close();
        }
        catch(Exception e){
        }
    }
}
