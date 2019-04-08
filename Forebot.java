package com.example.mityha.rrimer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;


public class Forebot extends Thread{

    public boolean allDone = false;
    private HashMap<Integer, HashMap<Integer, Animal>> hitList;

    private ArrayList<Animal> animals;

    public Forebot(){
        animals = new ArrayList<>();
        hitList = new HashMap<>();
    }

    public void IamReady(Animal animal){
        synchronized (animals) {
            animals.add(animal);
        }
    }

    public void ItsKIlledBy(int x, int y, Animal killer){
        if(hitList.get(x) == null)  hitList.put(x, new HashMap<Integer, Animal>());
        hitList.get(x).put(y, killer);
    }

    boolean i = false;

    @Override
    public void interrupt() {
        super.interrupt();
        i = true;
    }

    @Override
    public void run() {
        i = false;
        while (!isInterrupted())
            if (allDone && !i)
            synchronized (animals) {
                if(!animals.isEmpty()) {
                    Iterator<Animal> iterator = animals.iterator();
                    while (iterator.hasNext() && !i) {
                            Animal animal = iterator.next();
                            if(hitList.get(animal.posX) != null)
                                if(hitList.get(animal.posX).get(animal.posY) != null) {
                                    animal.IamDead();
                                    animals.remove(animal);
                                    hitList.get(animal.posX).remove(animal.posY);
                                    break;
                            }
                            try {
                                Thread.sleep(Settings.pause);
                            } catch(Exception e){}
                            animal.act();
                    }
                }
            }
        }
    }
