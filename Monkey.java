package com.example.mityha.rrimer;


import java.io.Serializable;

public class Monkey extends Animal implements Serializable {
    private static final long serialVersionUID = 1L;
    public Monkey(DrawThread _field, Forebot forebot) {
        super(_field, forebot);
    }
    public Monkey(DrawThread _field, Forebot forebot, int[][] _memory){
        super(_field, forebot, _memory);
    }

    @Override
    int moveUp(){
        int re = lookUp();
        field.moveMeTo(this , posX, posY - 1);
        food -= Settings.costStep;
        return re;
    }

    @Override
    int moveDown(){
        int re = lookDown();
        field.moveMeTo(this, posX, posY + 1);
        food -= Settings.costStep;
        return re;
    }

    @Override
    int moveLeft(){
        int re = lookLeft();
        field.moveMeTo(this, posX - 1, posY);
        food -= Settings.costStep;
        return re;
    }

    @Override
    int moveRight(){
        int re = lookRight();
        field.moveMeTo(this, posX + 1, posY);
        food -= Settings.costStep;
        return re;
    }

    @Override
    int lookUp() {
        food -= Settings.costView;
        return field.getAt(posX, posY - 1);
    }

    @Override
    int lookDown() {
        food -= Settings.costView;
        return field.getAt(posX, posY + 1);
    }

    @Override
    int lookLeft() {
        food -= Settings.costView;
        return field.getAt(posX - 1, posY);
    }

    @Override
    int lookRight() {
        food -= Settings.costView;
        return field.getAt(posX + 1, posY);
    }

    @Override
    int eatUp() {
        int re = lookUp();
        if(lookUp() == field.FOOD){
            food += Settings.nutritionalVal;
            field.removeFrom(posX, posY - 1);
        }
        return re;
    }

    @Override
    int eatDown() {
        int re = lookDown();
        if(lookDown() == field.FOOD){
            food += Settings.nutritionalVal;
            field.removeFrom(posX, posY + 1);
        }
        return re;
    }

    @Override
    int eatLeft() {
        int re = lookLeft();
        if(lookLeft() == field.FOOD){
            food += Settings.nutritionalVal;
            field.removeFrom(posX - 1, posY);
        }
        return re;
    }

    @Override
    int eatRight() {
        int re = lookRight();
        if(lookRight() == field.FOOD){
            food += Settings.nutritionalVal;
            field.removeFrom(posX + 1, posY);
        }
        return re;
    }

    @Override
    int attackUp() {
        int re = lookUp();
        if(lookRight() == field.ENEMY){
            field.removeFrom(posX, posY - 1);
            forebot.ItsKIlledBy(posX, posY - 1, this);
            food -= Settings.costSAttack;
        } else food -= Settings.costMiss;
        return re;
    }

    @Override
    int attackDown() {
        int re = lookDown();
        if(lookRight() == field.ENEMY){
            field.removeFrom(posX, posY + 1);
            forebot.ItsKIlledBy(posX, posY + 1, this);
            food -= Settings.costSAttack;
        } else food -= Settings.costMiss;
        return re;
    }

    @Override
    int attackLeft() {
        int re = lookLeft();
        if(lookRight() == field.ENEMY){
            field.removeFrom(posX - 1, posY);
            forebot.ItsKIlledBy(posX  - 1, posY, this);
            food -= Settings.costSAttack;
        } else food -= Settings.costMiss;
        return re;
    }

    @Override
    int attackRight() {
        int re = lookLeft();
        if(lookRight() == field.ENEMY){
            field.removeFrom(posX + 1, posY);
            forebot.ItsKIlledBy(posX + 1, posY, this);
            food -= Settings.costSAttack;
        } else food -= Settings.costMiss;
        return re;
    }
}
