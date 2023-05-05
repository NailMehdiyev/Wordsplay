package com.nailm.soztapmaca;

import java.util.ArrayList;

public class Market {

    private String basliqyazi;
    private int imgresm;
    private float qiymet;

    public Market() {
    }


    public Market(String basliqyazi, int imgresm, float qiymet) {
        this.basliqyazi = basliqyazi;
        this.imgresm = imgresm;
        this.qiymet = qiymet;
    }


    public String getBasliqyazi() {
        return basliqyazi;
    }

    public void setBasliqyazi(String basliqyazi) {
        this.basliqyazi = basliqyazi;
    }

    public int getImgresm() {
        return imgresm;
    }

    public void setImgresm(int imgresm) {
        this.imgresm = imgresm;
    }

    public float getQiymet() {
        return qiymet;
    }

    public void setQiymet(float qiymet) {
        this.qiymet = qiymet;
    }


    public  static  ArrayList<Market> getdata(){


        ArrayList<Market> marketlist =new ArrayList<>();

        String [] basliqlist={"5 Ədəd Can","20 Ədəd Can","50 Ədəd Can ","100 Ədəd Can","500 Ədəd Can"};

        float[]  qiymetler={0.99f,3.85f,9.80f,15.95f,21.70f};

        int[] resmlist={R.drawable.heart,R.drawable.shopheart1,R.drawable.shopheart2,R.drawable.shopheart2,R.drawable.shopheart1};


        for(int i=0;i< basliqlist.length;i++){


            Market market=new Market();

            market.setBasliqyazi(basliqlist[i]);

            market.setQiymet(qiymetler[i]);

            market.setImgresm(resmlist[i]);


            marketlist.add(market);


        }


        return marketlist;

    }




}
