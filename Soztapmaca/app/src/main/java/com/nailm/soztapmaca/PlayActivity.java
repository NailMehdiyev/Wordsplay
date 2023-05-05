package com.nailm.soztapmaca;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

public class PlayActivity extends AppCompatActivity {
    private ArrayList<String> suallarlist;
    private ArrayList<String> suallarkodlist;
    private TextView textsual, textcavab, txtureksayi;

    private EditText edittexttexmin;

    private ArrayList<String> cavablist;

    private Random kelmerandom, sualrandom, rndherfal;

    private int kelmereqem, sualreqem, rndherh, reqemsayi;

    private String kelme, rastgelecavablar, texminet, sqlSorgusu;
    ;

    private ArrayList<Character> kl;

    private Intent get_intent;


    private SQLiteDatabase database;
    private Cursor cursor;

    private int hakSayisi, sonHakSayisi;
    private SQLiteStatement statement;

    private ProgressBar sualprog, kelmeprog, sehvcavabpro;
    private TextView txtsual, txtkelme, txtsehvcavab;
    private Button esasmenyuyadon, tekraroyna;

    private LinearLayout linearLayout;

    private ImageView rsmbagla;

    private int hellolunansualsayi = 0, hellolunankelmesayi = 0, sehvcavablarsayi = 0, maksimumsualsayi, maksimumkelmesayi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        textcavab = (TextView) findViewById(R.id.text_playcavab);

        textsual = (TextView) findViewById(R.id.textplaysual);


        txtureksayi = findViewById(R.id.hg);


        edittexttexmin = findViewById(R.id.edittahmin);


        suallarlist = new ArrayList<>();

        suallarkodlist = new ArrayList<>();

        sualrandom = new Random();

        kelmerandom = new Random();

        rndherfal = new Random();


        cavablist = new ArrayList<>();


        get_intent = getIntent();

        hakSayisi = get_intent.getIntExtra("ureksayi", 0);

        txtureksayi.setText("+" + hakSayisi);


        for (Map.Entry sozler : SplashscreenActivity.sozlerlist.entrySet()) {

            suallarkodlist.add(String.valueOf(sozler.getKey()));

            suallarlist.add(String.valueOf(sozler.getValue()));

        }

        suallarial();


    }


    @Override
    public void onBackPressed() {

        Mainactiviteyegeridon();


    }

    public void herfal(View view) {

        if (hakSayisi > 0) {

            randomherffal();

            sonHakSayisi = hakSayisi;

            hakSayisi--;

            urekleriyaddasayaz(hakSayisi, sonHakSayisi);

        } else
            Toast.makeText(this, "Kifayet qeder urek sayiniz yox", Toast.LENGTH_SHORT).show();


    }


    private void urekleriyaddasayaz(int hSayisi, int sonHSayisi) {

        try {
            sqlSorgusu = "UPDATE SETTINGS SET heart =? WHERE heart=?";

            statement = database.compileStatement(sqlSorgusu);

            statement.bindString(1, String.valueOf(hSayisi));
            statement.bindString(2, String.valueOf(sonHSayisi));

            statement.execute();

            txtureksayi.setText("+" + hSayisi);


        } catch (Exception e) {

            e.printStackTrace();
        }

    }


    public void edittxminet(View view) {


        texminet = edittexttexmin.getText().toString();

        if (!TextUtils.isEmpty(texminet)) {
            if (texminet.matches(rastgelecavablar)) {

                Toast.makeText(this, "Tebrikler cehdiniz ugurlu oldu", Toast.LENGTH_LONG).show();

                edittexttexmin.setText("");

                hellolunankelmesayi++;

                hellolunansualsayi++;


                if (suallarlist.size() > 0) {

                    suallarial();



                } else {

                    Toast.makeText(this, "Suallar bitdi", Toast.LENGTH_LONG).show();
                    maksimumstatis("oyunbitdi");
                }


            } else {

                if (hakSayisi > 0) {

                    sonHakSayisi = hakSayisi;

                    hakSayisi--;

                    //sehvcavablarsayi ++;

                    urekleriyaddasayaz(hakSayisi, sonHakSayisi);

                    Toast.makeText(this, "Ugursuz cehd ve  caniniz 1 azaldi ", Toast.LENGTH_LONG).show();

                } else

                    Toast.makeText(this, "Davam etmek ucun yeterli  caniniz yoxdur, /nOyun bitdi ", Toast.LENGTH_LONG).show();


                sehvcavablarsayi++;


                maksimumstatis("oyunbitdi");


                new CountDownTimer(4100, 1000) {
                    @Override
                    public void onTick(long l) {

                    }

                    @Override
                    public void onFinish() {

                        Mainactiviteyegeridon();

                    }
                }.start();


            }


        } else

            Toast.makeText(this, "Texmininizi daxil edin", Toast.LENGTH_SHORT).show();


    }


    private void randomherffal() {


        if (kl.size() > 0) {

            rndherh = rndherfal.nextInt(kl.size());

            System.out.println("gelenherfler:" + kl.get(rndherh));


            //   char herf = kl.get(rndherh);


            String[] txtaltxett = textcavab.getText().toString().split(" ");

            char[] txtherfler = rastgelecavablar.toCharArray();


            for (int h = 0; h < rastgelecavablar.length(); h++) {

                if (txtaltxett[h].equals("_") && txtherfler[h] == kl.get(rndherh)) {

                    txtaltxett[h] = String.valueOf(kl.get(rndherh));


                    kelme = "";


                    for (int j = 0; j < txtaltxett.length; j++) {

                        if (j < txtaltxett.length - 1)

                            kelme += txtaltxett[j] + " ";

                        else

                            kelme += txtaltxett[j];

                    }

                    break;

                }

            }

            textcavab.setText(kelme);


            kl.remove(rndherh);


        }


    }


    private void Mainactiviteyegeridon() {


        Intent intent = new Intent(this, MainActivity.class);

        finish();
        startActivity(intent);

        overridePendingTransition(R.anim.anim_out, R.anim.anim_in);


    }


    private void suallarial() {


        sualreqem = sualrandom.nextInt(suallarlist.size());

        String rastgelesuallar = suallarlist.get(sualreqem);

        String Sualkod = suallarkodlist.get(sualreqem);

        suallarlist.remove(sualreqem);
        suallarkodlist.remove(sualreqem);


        textsual.setText(rastgelesuallar);


        try {

            database = this.openOrCreateDatabase("Sozoyunu", MODE_PRIVATE, null);

            cursor = database.rawQuery("SELECT * FROM kelmeler WHERE kelmekod =?", new String[]{Sualkod});

            int index = cursor.getColumnIndex("kelme");

            while (cursor.moveToNext()) {

                cavablist.add(cursor.getString(index));

                //  System.out.println(cursor.getString(index));

            }
            cursor.close();


        } catch (Exception e) {

            e.printStackTrace();

        }


        kelmereqem = kelmerandom.nextInt(cavablist.size());
        // System.out.println(cavablist.get(kelmereqem));

        rastgelecavablar = cavablist.get(kelmereqem);

        cavablist.remove(kelmereqem);

        kelme = "";

        for (int k = 0; k < rastgelecavablar.length(); k++) {


            if (k < rastgelecavablar.length() - 1)

                kelme += "_ ";

            else

                kelme += "_";

        }

        textcavab.setText(kelme);

        System.out.println("Rastgelenler:" + rastgelecavablar);

        kl = new ArrayList<Character>();

        for (char c : rastgelecavablar.toCharArray()) {


            kl.add(c);

        }


        if (rastgelecavablar.length() > 3 && rastgelecavablar.length() <= 5) {

            reqemsayi = 2;

        } else if (rastgelecavablar.length() >= 6 && rastgelecavablar.length() <= 8) {

            reqemsayi = 3;
        }


        for (int i = 0; i < reqemsayi; i++) {

            randomherffal();


        }


    }


    public void statistikaoster(View view) {

        maksimumstatis("");


    }


    private void statistictablo(String oyundurum, int maksimumkelmesayi, int maksimumsualsayi, int hellolunankelmesayi, int hellolunansualsayi, int sehvcavablarsayi) {

        Dialog statisticdialo = new Dialog(this);

        WindowManager.LayoutParams pr = new WindowManager.LayoutParams();

        pr.copyFrom(statisticdialo.getWindow().getAttributes());

        pr.width = (WindowManager.LayoutParams.MATCH_PARENT);

        pr.height = WindowManager.LayoutParams.WRAP_CONTENT;

        statisticdialo.setContentView(R.layout.custom_statistik);


        sualprog = statisticdialo.findViewById(R.id.prog1);
        kelmeprog = statisticdialo.findViewById(R.id.prog2);

        sehvcavabpro = statisticdialo.findViewById(R.id.prog3);


        txtsual = statisticdialo.findViewById(R.id.ssual);

        txtkelme = statisticdialo.findViewById(R.id.skelme);
        txtsehvcavab = statisticdialo.findViewById(R.id.sehvtexmin);

        esasmenyuyadon = statisticdialo.findViewById(R.id.esas);

        tekraroyna = statisticdialo.findViewById(R.id.tekraroyna);

        linearLayout = statisticdialo.findViewById(R.id.linerlayout_statistic);

        rsmbagla = statisticdialo.findViewById(R.id.bagla3);


        txtsual.setText(hellolunansualsayi + "/" + maksimumsualsayi);

        txtkelme.setText(hellolunankelmesayi + "/" + maksimumkelmesayi);

        txtsehvcavab.setText(sehvcavablarsayi + "/" + maksimumkelmesayi);


        sualprog.setProgress(hellolunansualsayi);

        kelmeprog.setProgress(hellolunankelmesayi);

        sehvcavabpro.setProgress(sehvcavablarsayi);


        if (oyundurum.matches("oyunbitdi")) {

            statisticdialo.setCancelable(false);

            rsmbagla.setVisibility(View.INVISIBLE);

            linearLayout.setVisibility(View.VISIBLE);
        }


        rsmbagla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                statisticdialo.dismiss();

            }
        });


        tekraroyna.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent h = new Intent(PlayActivity.this, PlayActivity.class);

                h.putExtra("ureksayi",txtureksayi.getText().toString());

                finish();

                startActivity(h);

            }
        });


        esasmenyuyadon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Mainactiviteyegeridon();


            }
        });


        statisticdialo.getWindow().setAttributes(pr);
        statisticdialo.show();


    }


    private void maksimumstatis(String oyundurum) {

        try {


            cursor = database.rawQuery("SELECT * FROM  kelmeler,suallar WHERE kelmeler.kelmekod=suallar.sualkod", null);


            maksimumkelmesayi = cursor.getCount();


            cursor = database.rawQuery("SELECT * FROM  suallar", null);


            maksimumsualsayi = cursor.getCount();

            cursor.close();


        } catch (Exception e) {

            e.printStackTrace();
        }


        statistictablo(oyundurum, maksimumkelmesayi, maksimumsualsayi, hellolunankelmesayi, hellolunansualsayi, sehvcavablarsayi);


    }


}

