package com.nailm.soztapmaca;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.billingclient.api.AcknowledgePurchaseParams;
import com.android.billingclient.api.AcknowledgePurchaseResponseListener;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.vungle.mediation.VungleAdapter;
import com.vungle.mediation.VungleExtrasBuilder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements PurchasesUpdatedListener {

    private TextView hearttext, nametxt;
    private int ureksayi, duration = 4000, sonureksayidurumu;
    private AdView banner;

    private Bundle extras;

    private AdRequest adRequest;

    private AdRequest Request;


    private RewardedAd odullureklam;


    private ConstraintLayout constraintLayout;
    private ImageView imgheart;

    private ConstraintLayout.LayoutParams layoutParams;
    private ImageView resmheartdesign;

    private float positionXanimation, positionYanimation;

    private SQLiteDatabase database;
    private Cursor cursor;
    private SQLiteStatement statement;


    //settingsdialog
    private Dialog settingsdialog;
    private ImageView bagla;
    private Button addeyisbutton, resmdeyisbutton;

    private WindowManager.LayoutParams params;


    private CircleImageView circleImageView;


    // addeyisdialog

    private Dialog addeyisdialog;

    private EditText txteditaddeyis;

    private ImageView close;

    private Button namechange;

    private byte[] resmbyte, ikincibyte;
    private Bitmap resmbitmap, getResmbitmap;

    private String yenile;

    private Uri resmuri;

    private RadioButton radiobuttonac, radiobuttonbagla;

    private SharedPreferences sharedPreferences;

    private SharedPreferences.Editor editor;
    private boolean durum;

    private Dialog marketdialog;
    private ImageView closeimg;

    private RecyclerView recycler;

    private ArrayList<Market> marketlist;

    private BillingClient client;
    private ArrayList<String> skulist;

    private ArrayList<Integer> heartlist;

    private int heartpos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        hearttext = findViewById(R.id.hearttxt);

        banner = findViewById(R.id.adView);

        resmheartdesign = findViewById(R.id.urekresm);


        nametxt = findViewById(R.id.main_activity_name);

        constraintLayout = findViewById(R.id.main_constrant);

        circleImageView = findViewById(R.id.daire);


        sharedPreferences = this.getSharedPreferences("com.nailm.soztapmaca", MODE_PRIVATE);

        durum = sharedPreferences.getBoolean("Musiqidurumu", true);



        skulist = new ArrayList<>();

        heartlist = new ArrayList<>();


        skulist.add("buy_heart1");
        skulist.add("buy_heart2");
        skulist.add("buy_heart3");
        skulist.add("buy_heart4");
        skulist.add("buy_heart5");


        heartlist.add(5);
        heartlist.add(20);
        heartlist.add(50);
        heartlist.add(100);
        heartlist.add(500);


        client = BillingClient.newBuilder(this)
                .setListener(this)
                .enablePendingPurchases()
                .build();


        client.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingServiceDisconnected() {

                Toast.makeText(MainActivity.this, "Odeme sistemi halhazirda kecerli deyil", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onBillingSetupFinished(@NonNull BillingResult billingResult) {

                if (billingResult.getResponseCode() != BillingClient.BillingResponseCode.OK) {

                    Toast.makeText(MainActivity.this, "Odeme sistemi ucun google play hesabinizi yoxlayin", Toast.LENGTH_SHORT).show();

                }


            }
        });


        adRequest = new AdRequest.Builder().build();

        banner.loadAd(adRequest);


        odullureklamal();


        imgheart = new ImageView(this);

        Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.heart);

        imgheart.setImageBitmap(bitmap);


        layoutParams = new ConstraintLayout.LayoutParams(96, 96);

        imgheart.setLayoutParams(layoutParams);
        imgheart.setX(0);
        imgheart.setY(0);
        imgheart.setVisibility(View.INVISIBLE);

        constraintLayout.addView(imgheart);


        try {


            database = this.openOrCreateDatabase("Sozoyunu", MODE_PRIVATE, null);


            cursor = database.rawQuery("SELECT * FROM SETTINGS", null);

            int heartindex = cursor.getColumnIndex("heart");

            int userindex = cursor.getColumnIndex("username");

            int profilindex = cursor.getColumnIndex("profilresm");


            cursor.moveToFirst();


            resmbyte = cursor.getBlob(profilindex);

            if (resmbyte != null) {

                getResmbitmap = BitmapFactory.decodeByteArray(resmbyte, 0, resmbyte.length);

                circleImageView.setImageBitmap(getResmbitmap);


            }


            ureksayi = Integer.valueOf(cursor.getString(heartindex));

            hearttext.setText("+" + ureksayi);


            nametxt.setText(cursor.getString(userindex));

            cursor.close();


        } catch (Exception e) {

            e.printStackTrace();

        }


    }


    public void btnclick(View view) {
        switch (view.getId()) {

            case R.id.play:
                Intent intent = new Intent(MainActivity.this, PlayActivity.class);
                finish();

                intent.putExtra("ureksayi", ureksayi);
                finish();
                startActivity(intent);

                overridePendingTransition(R.anim.anim_out, R.anim.anim_in);

                break;


            case R.id.markt:

                marketdialog();


                break;


            case R.id.exit:

                moveTaskToBack(true);
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(0);

                break;
        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Sual-Cavab Oyunu");
        alert.setIcon(R.drawable.herii);
        alert.setCancelable(false);

        alert.setMessage("Oyundan çıxmaq istədiyinizə əminsinizmi?");
        alert.setPositiveButton("Bəli", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                moveTaskToBack(true);

                android.os.Process.killProcess(android.os.Process.myPid());

                System.exit(0);

            }
        });


        alert.setNegativeButton("Xeyr", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.dismiss();

            }
        });

        alert.show();


    }

    public void videoyukle(View view) {

        if (odullureklam != null) {
            odullureklam.show(MainActivity.this, new OnUserEarnedRewardListener() {
                @Override
                public void onUserEarnedReward(@NonNull RewardItem rewardItem) {

                    imgheart.setX(constraintLayout.getPivotX());

                    imgheart.setY(constraintLayout.getPivotY());

                    imgheart.setVisibility(View.VISIBLE);


                    positionXanimation = (resmheartdesign.getX() + (resmheartdesign.getWidth() / 2f) - 48);//burada X ve Y postionlari heartdesign olan xal lovhesinin sifirdan bawlayan veziyyeti ucun goturulur veustune yarisi elave edilir

                    positionYanimation = (resmheartdesign.getY() + (resmheartdesign.getHeight() / 2f) - 48);


                    ObjectAnimator objectAnimatorX = ObjectAnimator.ofFloat(imgheart, "x", positionXanimation);
                    objectAnimatorX.setDuration(duration);


                    ObjectAnimator objectAnimatorY = ObjectAnimator.ofFloat(imgheart, "y", positionYanimation);

                    objectAnimatorY.setDuration(duration);


                    AnimatorSet animatorSet = new AnimatorSet();

                    animatorSet.playTogether(objectAnimatorX);

                    animatorSet.playTogether(objectAnimatorY);

                    animatorSet.start();

                    animatorSet.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animator) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animator) {

                            imgheart.setVisibility(View.INVISIBLE);

                            Toast.makeText(MainActivity.this, "Yeni can qazandiniz", Toast.LENGTH_SHORT).show();


                            sonureksayidurumu = ureksayi;

                            ureksayi++;

                            Mainactivitidekiurekmiqdariupdate(sonureksayidurumu, ureksayi);


                        }

                        @Override
                        public void onAnimationCancel(Animator animator) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animator) {

                        }
                    });


                }
            });


        }

    }


    private void odullureklamal() {

        Bundle extras = new VungleExtrasBuilder(new String[]{"MAINREWARDED-0415386"})
                .setUserId("test_user")
                .build();
        Request = new AdRequest.Builder()
                .addNetworkExtrasBundle(VungleAdapter.class, extras)
                .build();


        RewardedAd.load(MainActivity.this, "ca-app-pub-3940256099942544/5224354917", Request, new RewardedAdLoadCallback() { // burdaki kod test modundadi bu orginal rewardedle deyismelidi
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {

                if (odullureklam == null) {

                    Toast.makeText(MainActivity.this, "Reklam yuklenmedi", Toast.LENGTH_SHORT).show();


                }


            }

            @Override
            public void onAdLoaded(@NonNull RewardedAd rewardedAd) {

                odullureklam = rewardedAd;

                Toast.makeText(MainActivity.this, "Video Yükləndi", Toast.LENGTH_SHORT).show();

                odullureklamal();


                odullureklam.setFullScreenContentCallback(new FullScreenContentCallback() {
                    @Override
                    public void onAdDismissedFullScreenContent() {

                        Toast.makeText(MainActivity.this, "İndi Can qazanmaq zamanıdır", Toast.LENGTH_LONG).show();




                    }
                });

            }


        });


    }


    private void Mainactivitidekiurekmiqdariupdate(int ilkoncekiureksayisi, int deyisenureksayi) {


        try {

            yenile = "UPDATE SETTINGS SET heart=? WHERE heart=?";

            statement = database.compileStatement(yenile);

            statement.bindString(1, String.valueOf(deyisenureksayi));

            statement.bindString(2, String.valueOf(ilkoncekiureksayisi));

            statement.execute();


            hearttext.setText("+" + deyisenureksayi);


        } catch (Exception e) {

            e.printStackTrace();
        }


    }


    public void ayarlatbtn(View view) {

        ayarlarigoster();

    }


    private void ayarlarigoster() {

        settingsdialog = new Dialog(this);

        params = new WindowManager.LayoutParams();

        params.copyFrom(settingsdialog.getWindow().getAttributes());

        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;


        settingsdialog.setCancelable(false);
        settingsdialog.setContentView(R.layout.ayarlardialoq_custom);

        bagla = settingsdialog.findViewById(R.id.bagla);

        addeyisbutton = settingsdialog.findViewById(R.id.addeyisbutton);

        resmdeyisbutton = settingsdialog.findViewById(R.id.resmdeyisbutton);

        radiobuttonac = settingsdialog.findViewById(R.id.radioac);

        radiobuttonbagla = settingsdialog.findViewById(R.id.radiobagla);


        if(durum){

            radiobuttonac.setChecked(durum);
        }else

            radiobuttonbagla.setChecked(!durum);


        radiobuttonac.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                    musiqidurumlariniyaddasayaz(b);



            }
        });


        radiobuttonbagla.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (b)
                    musiqidurumlariniyaddasayaz(!b);


            }
        });


        addeyisbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                settingsdialog.dismiss();

                addeyissetting();


            }
        });


        resmdeyisbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);

                } else {

                    Intent resmideyis = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                    startActivityForResult(resmideyis, 1);

                }


            }
        });


        bagla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                settingsdialog.dismiss();


            }
        });


        settingsdialog.getWindow().setAttributes(params);

        settingsdialog.show();

    }


    private void addeyissetting() {


        addeyisdialog = new Dialog(this);

        params = new WindowManager.LayoutParams();

        params.copyFrom(addeyisdialog.getWindow().getAttributes());

        params.width = WindowManager.LayoutParams.WRAP_CONTENT;

        params.height = WindowManager.LayoutParams.WRAP_CONTENT;

        addeyisdialog.setCancelable(false);

        addeyisdialog.setContentView(R.layout.addeyis_custom);


        txteditaddeyis = addeyisdialog.findViewById(R.id.editaddeyis);

        namechange = addeyisdialog.findViewById(R.id.addeyisbutton2);

        close = addeyisdialog.findViewById(R.id.bagla2);


        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addeyisdialog.dismiss();

            }
        });


        namechange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String ad = txteditaddeyis.getText().toString();


                if (!TextUtils.isEmpty(ad)) {

                    if (!ad.matches(nametxt.getText().toString())) {

                        adlariyenile(ad, nametxt.getText().toString());

                        Toast.makeText(MainActivity.this, "Adiniz ugurla deyisdi", Toast.LENGTH_LONG).show();

                    } else

                        Toast.makeText(MainActivity.this, "Onsuzda bu ad movcuddur", Toast.LENGTH_SHORT).show();

                } else

                    Toast.makeText(MainActivity.this, "Adinizi qeyd edin", Toast.LENGTH_SHORT).show();

            }
        });


        addeyisdialog.getWindow().setAttributes(params);


        addeyisdialog.show();


    }


    private void adlariyenile(String yenilenenad, String ilkoncekiad) {

        try {

            yenile = "UPDATE SETTINGS SET username=? WHERE username=?";

            statement = database.compileStatement(yenile);


            statement.bindString(1, yenilenenad);

            statement.bindString(2, ilkoncekiad);

            statement.execute();

            nametxt.setText(yenilenenad);


        } catch (Exception e) {

            e.printStackTrace();

        }


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {


        if (requestCode == 0) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Intent resmideyis = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(resmideyis, 1);


            }


        }


        super.onRequestPermissionsResult(requestCode, permissions, grantResults);


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {


        if (requestCode == 1) {

            if (resultCode == RESULT_OK && data != null) {


                resmuri = data.getData();


                AlertDialog.Builder alert = new AlertDialog.Builder(this);

                alert.setTitle("Soz Oyunu");

                alert.setIcon(R.mipmap.ic_launcher);

                alert.setMessage("Resminizi deyisdirmek isteyirsiniz?");

                alert.setCancelable(false);

                alert.setPositiveButton("Beli", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        try {

                            if (Build.VERSION.SDK_INT >= 28) {

                                ImageDecoder.Source source = ImageDecoder.createSource(MainActivity.this.getContentResolver(), resmuri);

                                resmbitmap = ImageDecoder.decodeBitmap(source);

                                circleImageView.setImageBitmap(resmbitmap);

                                resmiyaddasayaz(resmbitmap);


                            } else {
                                resmbitmap = MediaStore.Images.Media.getBitmap(MainActivity.this.getContentResolver(), resmuri);

                                circleImageView.setImageBitmap(resmbitmap);


                                resmiyaddasayaz(resmbitmap);


                            }


                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                });


                alert.setNegativeButton("Xeyr", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        dialogInterface.dismiss();


                    }
                });

                alert.show();

            }

        }


        super.onActivityResult(requestCode, resultCode, data);
    }


    private void resmiyaddasayaz(Bitmap bitmap) {

        try {


            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            bitmap.compress(Bitmap.CompressFormat.PNG, 75, outputStream);

            resmbyte = outputStream.toByteArray();


            yenile = "UPDATE SETTINGS SET profilresm=? WHERE username=?";

            statement = database.compileStatement(yenile);

            statement.bindBlob(1, resmbyte);

            statement.bindString(2, nametxt.getText().toString());
            statement.execute();


            if (settingsdialog.isShowing())

                settingsdialog.dismiss();


            Toast.makeText(MainActivity.this, "Resminiz ugurla deyisdi", Toast.LENGTH_LONG).show();


        } catch (Exception e) {

            System.out.println(e.getMessage());
        }

    }


    private void musiqidurumlariniyaddasayaz(boolean b) {


        editor = sharedPreferences.edit();

        editor.putBoolean("Musiqidurumu", b);

        editor.apply();

        Toast.makeText(getApplicationContext(), "Uğurla qeyd edildi\nAktiv olması üçün tedbiqi yeniden açın.", Toast.LENGTH_LONG).show();

    }





    private void marketdialog() {


        marketdialog = new Dialog(this);

        WindowManager.LayoutParams m = new WindowManager.LayoutParams();

        m.copyFrom(marketdialog.getWindow().getAttributes());

        m.width = WindowManager.LayoutParams.MATCH_PARENT;

        m.height = WindowManager.LayoutParams.WRAP_CONTENT;

        marketdialog.setCancelable(false);

        marketdialog.setContentView(R.layout.custom_dialog_market);


        close = marketdialog.findViewById(R.id.bagla4);
        recycler = marketdialog.findViewById(R.id.recyclerview);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                marketdialog.dismiss();

            }
        });


        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 3);

        recycler.setLayoutManager(gridLayoutManager);


        MarketAdapter adapter = new MarketAdapter(Market.getdata(), getApplicationContext());


        recycler.setAdapter(adapter);

        adapter.setOnclicklistener(new MarketAdapter.OnItemclicklistener() {
            @Override
            public void OnItemclick(Market market, int position) {

                Toast.makeText(MainActivity.this, market.getBasliqyazi(), Toast.LENGTH_SHORT).show();

                if (client.isReady()) {


                    SkuDetailsParams.Builder builder = SkuDetailsParams.newBuilder();

                    builder.setSkusList(Collections.singletonList(skulist.get(position)))
                            .setType(BillingClient.SkuType.INAPP);


                    client.querySkuDetailsAsync(builder.build(), new SkuDetailsResponseListener() {
                        @Override
                        public void onSkuDetailsResponse(@NonNull BillingResult billingResult, @Nullable List<SkuDetails> list) {

                            if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && list != null) {

                                BillingFlowParams paramsg = BillingFlowParams.newBuilder()
                                        .setSkuDetails(list.get(0))
                                        .build();


                                client.launchBillingFlow(MainActivity.this, paramsg);


                            }


                        }
                    });


                }


            }
        });


        recycler.addItemDecoration(new Itemdecor(3, 7));


        marketdialog.getWindow().setAttributes(m);

        marketdialog.show();


    }


    @Override
    public void onPurchasesUpdated(@NonNull BillingResult billingResult, @Nullable List<Purchase> list) {

        if (billingResult.getResponseCode() == Purchase.PurchaseState.PURCHASED) {


            handlePurchase(list.get(0));

        }


    }

    private void handlePurchase(Purchase purchase) {


        if (purchase.getPurchaseState() == Purchase.PurchaseState.PURCHASED) {
            if (!purchase.isAcknowledged()) {
                AcknowledgePurchaseParams purchaseParams = AcknowledgePurchaseParams.newBuilder()
                        .setPurchaseToken(purchase.getPurchaseToken())
                        .build();

                AcknowledgePurchaseResponseListener acknowledgePurchaseResponseListener = new AcknowledgePurchaseResponseListener() {
                    @Override
                    public void onAcknowledgePurchaseResponse(BillingResult billingResult) {
                        Toast.makeText(getApplicationContext(), "Negd Alma tedbiqi ugurlu oldu", Toast.LENGTH_SHORT).show();

                        sonureksayidurumu += heartlist.get(heartpos);
                        Mainactivitidekiurekmiqdariupdate(Integer.valueOf(hearttext.getText().toString()), sonureksayidurumu);
                    }
                };

                client.acknowledgePurchase(purchaseParams, acknowledgePurchaseResponseListener);
            }
        }

    }


    private class Itemdecor extends RecyclerView.ItemDecoration {


        private int spanCount = 3;
        private int spacing = 7;


        public Itemdecor(int spanCount, int spacing) {
            this.spanCount = spanCount;
            this.spacing = spacing;
        }


        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {

            int position = parent.getChildAdapterPosition(view);

            int column = position % spanCount;


            outRect.left = (column + 1) * spacing / spanCount;

            outRect.right = (column + 1) * spacing / spanCount;

            outRect.bottom = spacing;


        }
    }


}