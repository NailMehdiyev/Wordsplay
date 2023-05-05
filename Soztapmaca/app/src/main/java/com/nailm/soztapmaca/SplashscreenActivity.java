package com.nailm.soztapmaca;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

public class SplashscreenActivity extends AppCompatActivity {
    private ProgressBar progressBar;
    private TextView textView;
    private SQLiteDatabase database;
    public static HashMap<String, String> sozlerlist;
    public static float maxdeyer = 100f, artacagdeyer, progresdeyeri = 0;
    private String[] suallarlist = {"Ədəbiyyatımızda türk vəzni adıyla tanınan vəzn?", "Ərazisinə gorə dunyada ikinci olan dövlət?", "Qızıl Top mükafatına layiq görülmüş yeganə qaradərili futbolçu?","E.ə.331-ci il oktyabrın 1-də Makedoniyalı İsgəndər ilə III Daranın qoşunları arasında baş veren döyüş?","Zamanın saxlanma qanununu həyata praktiki olaraq keçirdən yeganə professor?","Palmali şirkət Prezidenti M.Mənsimov tərəfindən yük gəmisinə adı verilmiş şəhid Generalımız? "};

    private String[] suallarkodlisti = {"kod1", "kod2", "kod3","kod4","kod5","kod6"};

    private String[] kelmelerlist = {"Heca", "Kanada", "Weah","Qavqamela","Enşteyn","Polad"};

    private String[] kelmelerkodlist = {"kod1", "kod2", "kod3","kod4","kod5","kod6"};

    private Cursor cursor;
    private MediaPlayer gameplay;

    private boolean durum;

    private SharedPreferences sharedPreferences;

    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        textView = findViewById(R.id.txtmesaj);
        progressBar = findViewById(R.id.prog);

        sozlerlist = new HashMap<>();

        gameplay=MediaPlayer.create(this,R.raw.naomi_scott);


        gameplay.setLooping(true);


        sharedPreferences=this.getSharedPreferences("com.nailm.soztapmaca",MODE_PRIVATE);

         durum=sharedPreferences.getBoolean("Musiqidurumu",true);



        try {

            database = this.openOrCreateDatabase("Sozoyunu", MODE_PRIVATE, null);

            database.execSQL("CREATE TABLE IF NOT EXISTS SETTINGS(username VARCHAR,heart VARCHAR,profilresm BLOB)");


            cursor = database.rawQuery("SELECT * FROM SETTINGS", null);


            if (cursor.getCount() < 1) {

                database.execSQL("INSERT INTO SETTINGS (username,heart) VALUES ('Oyuncu','0') ");

            }


            database.execSQL("CREATE TABLE IF NOT EXISTS suallar(id INTEGER PRIMARY KEY,sualkod VARCHAR UNIQUE,sual VARCHAR )");

            database.execSQL("DELETE FROM suallar");

            SQLsuallarlisti();






            database.execSQL("CREATE TABLE IF NOT EXISTS kelmeler(id INTEGER PRIMARY KEY,kelmekod VARCHAR,kelme VARCHAR, FOREIGN KEY (kelmekod)  REFERENCES suallar(sualkod))");

            database.execSQL("DELETE FROM kelmeler");

            Sqlkelmelerlist();





            Cursor cursor = database.rawQuery("SELECT * FROM suallar", null);

            artacagdeyer = maxdeyer / cursor.getColumnCount();


            int sualkodindex = cursor.getColumnIndex("sualkod");
            int sualindex = cursor.getColumnIndex("sual");


            textView.setText("Suallar Yüklənir....");

            while (cursor.moveToNext()) {

                sozlerlist.put(cursor.getString(sualkodindex), cursor.getString(sualindex));

                progresdeyeri += artacagdeyer;

                progressBar.setProgress((int) progresdeyeri);


            }


            textView.setText("Suallar yükləndi\nProqram açılır.....");

            cursor.close();

            new CountDownTimer(2100, 1000) {
                @Override
                public void onTick(long l) {

                }

                @Override
                public void onFinish() {

                    Intent intent = new Intent(SplashscreenActivity.this, MainActivity.class);

                    startActivity(intent);

                }
            }.start();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    protected void onResume() {

        super.onResume();

        if(durum){

            gameplay.start();


        }





    }






    private void SQLsuallarlisti() {

        // database.execSQL("INSERT INTO sozler(cavab,sual) VALUES ('Heca','Edebiyyatimizda turk vezni adiyla taninan vezn?')");
        // database.execSQL("INSERT INTO sozler(cavab,sual) VALUES ('Kanada','')");


        try {

            for (int s = 0; s < suallarlist.length; s++) {

                String sl = "INSERT INTO suallar(sualkod,sual) VALUES (?,?)";

                SQLiteStatement sqLiteStatement = database.compileStatement(sl);

                sqLiteStatement.bindString(1, suallarkodlisti[s]);
                sqLiteStatement.bindString(2, suallarlist[s]);

                sqLiteStatement.execute();

            }

        } catch (Exception e) {

            e.printStackTrace();

        }

    }


    private void Sqlkelmelerlist() {


        try {


            for (int k = 0; k < kelmelerlist.length; k++) {

                String klm = "INSERT INTO kelmeler(kelmekod,kelme) VALUES (?,?)";

                SQLiteStatement sqLiteStatement = database.compileStatement(klm);

                sqLiteStatement.bindString(1, kelmelerkodlist[k]);
                sqLiteStatement.bindString(2, kelmelerlist[k]);

                sqLiteStatement.execute();


            }


        } catch (Exception e) {

            e.printStackTrace();
        }


    }

}