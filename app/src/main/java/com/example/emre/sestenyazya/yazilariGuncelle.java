package com.example.emre.sestenyazya;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.Toast;

import java.io.EOFException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;

public class yazilariGuncelle extends AppCompatActivity {

    public ImageView kameraAc,galeriAc,yardimAc,resmim;
    public Switch komutVer;
    public ImageButton kaydetNotu;
    SharedPreferences SonKomut;
    Boolean gelenKomut=true;
    public EditText baslikYazi,icerikYazi;
    SpannableStringBuilder builder=null;
    Boolean kameraMı=false;
    ScrollView scroll;
    String yazıCek;
    String mImagePath="";
    int en,boy;
    View view;
    String dosyaAdım="stt_img";
    ContentValues values;
    Uri imageUri;
    String gelenYazılar,gelenBaslik;
    public String dosyaYoluKayıtım="";

    String oncekiYazi;
    String[] sorular = {" misin", " mısın", " musun", "nasılsın", " mi", " mı", " mu", "neredesin ",
            " kim", " hangisi", " nereden", " nereye", " kime", " kimden", " hangisinden", " hangisine", "ne ",
            "neden ", "kaç ", "nerede"};

    String Zaman;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yazilari_guncelle);

        baslikYazi=(EditText) findViewById(R.id.baslikGuncelle);
        icerikYazi=(EditText) findViewById(R.id.icerikGuncelle);

        Intent intent=getIntent();

        AnaEkran.yazilarSQL.YazıCek(AnaEkran.notID);

        baslikYazi.setText(YazilarSQL.baslik);
        icerikYazi.setText(YazilarSQL.icerik);
        yazıCek=YazilarSQL.icerik;
        gelenYazılar=YazilarSQL.icerik;
        gelenBaslik=YazilarSQL.baslik;
        dosyaYoluKayıtım=YazilarSQL.dosyayolu;

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // ActionBar da Bulunan Kısayolları Tanımlıyoruz..
        kameraAc=(ImageView) findViewById(R.id.kameraGuncelle);
        galeriAc=(ImageView) findViewById(R.id.galeriGuncelle);
        yardimAc=(ImageView) findViewById(R.id.yardimGuncelle);
        komutVer=(Switch) findViewById(R.id.komutGuncelle);
        kaydetNotu=(ImageButton) findViewById(R.id.kaydetGuncelle);
        scroll=(ScrollView) findViewById(R.id.scroll);

        resmim=(ImageView) findViewById(R.id.resimGuncelle);
        baslikYazi=(EditText) findViewById(R.id.baslikGuncelle);
        icerikYazi=(EditText) findViewById(R.id.icerikGuncelle);

            FotoDuzelt();
        final SpeechRecognizer mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        final Intent mSpeechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH); //Sesi yazıya çevirme servisi
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);                                          //Dil modeli
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,
                Locale.getDefault());                                            //Dil ataması Default olması cihazın dilini seçer
        mSpeechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {                       //Sesi Dinlemeye Hazır olduğunda

            }

            @Override
            public void onBeginningOfSpeech() {                                 //Konuşmaya başlandığında

            }

            @Override
            public void onRmsChanged(float v) {

            }

            @Override
            public void onBufferReceived(byte[] bytes) {

            }

            @Override
            public void onEndOfSpeech() {                                   //Konuşma bittiğinde

            }

            @Override
            public void onError(int i) {                                    //Konuşma Esnasında Hata olduğu zaman
                Toast.makeText(getApplicationContext(), "Hata Oluştu, Tekrar Deneyin", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResults(Bundle bundle) {                          //Sonuçların Gönderildiği Fonksiyon
                ArrayList<String> eslesme = bundle
                        .getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);//Tanımlama Sonucu eslesme dizisine aktarılıyor
                if(eslesme!=null){
                    //Komut Veya Soru Var mı Kontrol İşlemi

                    //Düz Yazıysa
                    String yazılar = eslesme.get(0);
                    if (!YaziDuzelt(yazılar)) {
                        oncekiYazi = icerikYazi.getText().toString();     //editText içindeki yazılar stringe aktarılıyor. Ardından
                        char c = Character.toUpperCase(eslesme.get(0).charAt(0));
                        yazılar = c + eslesme.get(0).substring(1);
                        yazılar=eslesme.get(0);
                        if(icerikYazi.getText().toString().indexOf("✕")!=-1 && icerikYazi.getText().length()>0){
                            builder.append(yazılar + ".");       //Yeni gelen sonuçlarla beraber yazılıyor.
                            icerikYazi.setText(builder);
                            icerikYazi.setSelection(icerikYazi.length());
                            scroll.post(new Runnable() {
                                @Override
                                public void run() {
                                    scroll.fullScroll(ScrollView.FOCUS_DOWN);
                                }
                            });
                            scroll.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    scroll.fullScroll(ScrollView.FOCUS_DOWN);
                                }
                            },100);
                        }
                        else{
                            icerikYazi.setText(oncekiYazi+" "+yazılar+".");
                            icerikYazi.setSelection(icerikYazi.length());
                            scroll.post(new Runnable() {
                                @Override
                                public void run() {
                                    scroll.fullScroll(ScrollView.FOCUS_DOWN);
                                }
                            });
                            scroll.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    scroll.fullScroll(ScrollView.FOCUS_DOWN);
                                }
                            },100);
                        }

                    }
                }
            }


            @Override
            public void onPartialResults(Bundle bundle) {

            }

            @Override
            public void onEvent(int i, Bundle bundle) {

            }
        });
        findViewById(R.id.sesYaziGuncelle).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_UP:
                        mSpeechRecognizer.stopListening();
                        // editText.setHint("Konuştuğunuz Burada Gözükecek...");
                        break;

                    case MotionEvent.ACTION_DOWN:
                        mSpeechRecognizer.startListening(mSpeechRecognizerIntent);
                        // editText.setText("");
                        icerikYazi.setHint("Dinleniyor");
                        break;
                }
                return false;
            }
        });


        SonKomut = getSharedPreferences("SesliKomut",MODE_PRIVATE);


        galeriAc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent=new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    startActivityForResult(intent,AnaEkran.izinKodu);
                }catch (Exception e){;}


            }
        });

        //Komut Aktif veya Pasif Olduğunda Yapılan İşlemler
        komutVer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    gelenKomut=isChecked;
                    SonKomut.edit().putBoolean("isChecked",true).commit();
                    Toast.makeText(getApplicationContext(),"Sesli Komut Aktif",Toast.LENGTH_SHORT).show();
                }
                else {
                    gelenKomut=false;
                    SonKomut.edit().putBoolean("isChecked",false).commit();
                    Toast.makeText(getApplicationContext(),"Sesli Komut Devre Dışı",Toast.LENGTH_LONG).show();
                    //gelenKomut=SonKomut.getBoolean("isChecked",false);  // SharedPrefarences'ten veri almak için kullanılır.
                }
            }
        });

        kameraAc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date zaman = new Date();
                SimpleDateFormat dakika = new SimpleDateFormat("_ddMMyyyy_HHmm");
                Zaman = dakika.format(zaman);

                values = new ContentValues();
                values.put(MediaStore.Images.Media.TITLE, dosyaAdım+Zaman);
                values.put(MediaStore.Images.Media.DESCRIPTION, "Sesten Yazıya");
                values.put(MediaStore.Images.Media.ORIENTATION,90);
                imageUri = getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(intent,33);
                kameraMı = true;
            }

        });

        yardimAc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YardımEt(yazilariGuncelle.this);
            }
        });

        komutVer.setChecked(SonKomut.getBoolean("isChecked",true));  //Açılışta Komut Bilgimizi Aktardık..

    }

    private void FotoDuzelt() {

        String fotoYolu="";
        boolean fotoEklendi=false;
        String yazılar="";
        String YAZI="";
        int sıra=0;
        if(yazıCek.indexOf('✓')!=-1){
            icerikYazi.setText("");
            String[] fotolar=dosyaYoluKayıtım.split("◆");

            for(int i=0;i<yazıCek.length();i++){
                if(fotoEklendi==true){

                    if(yazıCek.indexOf('✓')!=-1){
                        yazılar=yazıCek.substring(0,yazıCek.indexOf('✓')-2);
                            builder.append(yazılar);
                            icerikYazi.setText(builder);
                        mImagePath=fotolar[sıra];
                        try {
                            resmim.setImageURI(Uri.fromFile(new File(fotolar[sıra])));
                            BitmapDrawable drawable = (BitmapDrawable) resmim.getDrawable();
                            Bitmap bitmap = drawable.getBitmap();
                            if(bitmap.getWidth()<bitmap.getHeight()) {
                                en=860;
                                boy=1200;
                            }
                            else {
                                en=1300;
                                boy=860;
                            }
                            Bitmap image = Bitmap.createScaledBitmap(bitmap, en, boy, true);
                            ResimDondur(image);
                            Drawable cizim=resmim.getDrawable();
                            sıra++;
                            addImageBetweentext(cizim);
                        }catch (Exception hata){hata.printStackTrace();}

                        yazıCek=yazıCek.substring(yazıCek.indexOf('✕')+2);

                        if(yazıCek.indexOf('✓')==-1){
                            builder.append(yazıCek);
                            icerikYazi.setText(builder);
                            icerikYazi.setSelection(icerikYazi.length());
                            scroll.post(new Runnable() {
                                @Override
                                public void run() {
                                    scroll.fullScroll(ScrollView.FOCUS_DOWN);
                                }
                            });
                            scroll.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    scroll.fullScroll(ScrollView.FOCUS_DOWN);
                                }
                            },100);
                            break;
                        }
                    }
                    else{
                        builder.append(yazıCek);
                        icerikYazi.setText(builder);
                        icerikYazi.setSelection(icerikYazi.length());
                        scroll.post(new Runnable() {
                            @Override
                            public void run() {
                                scroll.fullScroll(ScrollView.FOCUS_DOWN);
                            }
                        });
                        scroll.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                scroll.fullScroll(ScrollView.FOCUS_DOWN);
                            }
                        },100);
                    }

                }
                else{
                    yazılar=yazıCek.substring(0,yazıCek.indexOf('✓')-2);
                    icerikYazi.setText(yazılar);
                    mImagePath=fotolar[sıra];
                    try {
                        resmim.setImageURI(Uri.fromFile(new File(mImagePath)));

                        BitmapDrawable drawable = (BitmapDrawable) resmim.getDrawable();
                        Bitmap bitmap = drawable.getBitmap();
                        if(bitmap.getWidth()<bitmap.getHeight()) {
                            en=860;
                            boy=1200;
                        }
                        else {
                            en=1300;
                            boy=860;
                        }
                        Bitmap image = Bitmap.createScaledBitmap(bitmap, en, boy, true);
                        ResimDondur(image);
                        Drawable cizim=resmim.getDrawable();
                        sıra++;
                        addImageBetweentext(cizim);
                    }catch (Exception hata){hata.printStackTrace();}


                    yazıCek=yazıCek.substring(yazıCek.indexOf('✕')+2);
                    if(yazıCek.indexOf('✓')==-1){
                        if(builder!=null){
                            builder.append(yazıCek);
                            icerikYazi.setText(builder);
                        }

                        icerikYazi.setSelection(icerikYazi.length());
                        scroll.post(new Runnable() {
                            @Override
                            public void run() {
                                scroll.fullScroll(ScrollView.FOCUS_DOWN);
                            }
                        });
                        scroll.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                scroll.fullScroll(ScrollView.FOCUS_DOWN);
                            }
                        },100);
                        break;
                    }
                    fotoEklendi=true;
                }
            }

        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        CikisKontrol();
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode==AnaEkran.izinKodu && data!=null){
            try{
                Uri uri=data.getData();

                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(uri, filePath, null, null, null);
                cursor.moveToFirst();
                mImagePath = cursor.getString(cursor.getColumnIndex(filePath[0]));

                InputStream is=getContentResolver().openInputStream(uri);
                Bitmap bitmap= BitmapFactory.decodeStream(is);
                if(bitmap.getWidth()<bitmap.getHeight()) {
                    en=860;
                    boy=1200;
                }
                else {
                    en=1300;
                    boy=860;
                }
                Bitmap image = Bitmap.createScaledBitmap(bitmap, en, boy, true);
                ResimDondur(image);
                Drawable cizim;
                cizim=resmim.getDrawable();
                addImageBetweentext(cizim);
                builder.append("\n");
                icerikYazi.setText(builder);


            }catch (FileNotFoundException hata){hata.printStackTrace();}
        }

        if(requestCode==33 && resultCode==RESULT_OK){
            try {
                Bitmap thumbnail = MediaStore.Images.Media.getBitmap(
                        getContentResolver(), imageUri);
                mImagePath = DosyaYolunuBul(imageUri);
                resmim.setImageURI(Uri.fromFile(new File(mImagePath)));
                Bitmap image=Bitmap.createScaledBitmap(thumbnail,1200
                        ,860,true);
                ResimDondur(image);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Drawable cizim;
            cizim=resmim.getDrawable();
            addImageBetweentext(cizim);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    public void ResimDondur(Bitmap bitmap){
        ExifInterface exifInterface=null;
        try {
            exifInterface=new ExifInterface(mImagePath);
        }catch (IOException ht){ht.printStackTrace(); }
        int oriantation=exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,ExifInterface.ORIENTATION_UNDEFINED);
        Matrix matrix=new Matrix();
        switch (oriantation){
            case ExifInterface.ORIENTATION_ROTATE_90: matrix.setRotate(90);break;
            case ExifInterface.ORIENTATION_ROTATE_180: matrix.setRotate(180);break;
        }
        Bitmap rotatedbitmap=Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);
        resmim.setImageBitmap(rotatedbitmap);

    }

    public String DosyaYolunuBul(Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    public boolean YaziDuzelt(String gelenler){

        //Komut verme Aktif ise
        if(SonKomut.getBoolean("isChecked",true)==true) {

            //Kamerayı Açıyoruz
            if (gelenler.toLowerCase().contains("kamerayı aç")) {
                Date zaman = new Date();
                SimpleDateFormat dakika = new SimpleDateFormat("_ddMMyyyy_HHmm");
                Zaman = dakika.format(zaman);

                values = new ContentValues();
                values.put(MediaStore.Images.Media.TITLE, dosyaAdım+Zaman);
                values.put(MediaStore.Images.Media.DESCRIPTION, "Sesten Yazıya");
                values.put(MediaStore.Images.Media.ORIENTATION,90);
                imageUri = getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(intent,33);
                kameraMı = true;
                return true;
            }

            //Galeriyi Açıyoruz
            if (gelenler.toLowerCase().contains("galeriyi aç")) {

                try {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    startActivityForResult(intent, AnaEkran.izinKodu);
                } catch (Exception e) {
                    ;
                }
                return true;
            }

            //Kaydetme Komutu
            if (gelenler.toLowerCase().contains("kaydet")) {
                View view=null;
                onGuncelle(view);
                startActivity(new Intent(this,AnaEkran.class));
                finish();
                return true;
            }

            //Tüm Yazıyı Silme Komutu
            if (gelenler.toLowerCase().contains("hepsini sil") || gelenler.toLowerCase().contains("tümünü sil")) {
                oncekiYazi = "";
                icerikYazi.setText("");//builder kısmınıda temizle
                if(kameraMı==true){builder.clear();kameraMı=false;}
                return true;
            }

            //Çıkış Yapma Komutu
            if (gelenler.toLowerCase().contains("çıkış yap")) {

                CikisKontrol();
                return true;
            }
        }

        //Cümlede Soru Var Mı Kontrol İşlemi
        for (int i = 0; i < sorular.length; i++) {
            if (gelenler.toLowerCase().contains(sorular[i])) {
                oncekiYazi = icerikYazi.getText().toString();
                char c = Character.toUpperCase(gelenler.charAt(0));
                gelenler = c + gelenler.substring(1);
                if (icerikYazi.getText().toString().indexOf("✕")!=-1 && icerikYazi.getText().length() > 0) {
                    builder.append(gelenler + " " + "?");
                    icerikYazi.setText(builder);
                    icerikYazi.setSelection(icerikYazi.length());
                    scroll.post(new Runnable() {
                        @Override
                        public void run() {
                            scroll.fullScroll(ScrollView.FOCUS_DOWN);
                        }
                    });
                    scroll.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            scroll.fullScroll(ScrollView.FOCUS_DOWN);
                        }
                    }, 100);

                } else {
                    icerikYazi.setText(oncekiYazi + " " + gelenler + "?");
                    icerikYazi.setSelection(icerikYazi.length());
                    scroll.post(new Runnable() {
                        @Override
                        public void run() {
                            scroll.fullScroll(ScrollView.FOCUS_DOWN);
                        }
                    });
                    scroll.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            scroll.fullScroll(ScrollView.FOCUS_DOWN);
                        }
                    }, 100);
                }
                return true;
            }
        }


        return false;

    }

    private void CikisKontrol() {
        if(new String(baslikYazi.getText().toString()).equals(gelenBaslik)
                && new String(icerikYazi.getText().toString()).equals(gelenYazılar)) {
            onBackPressed();
        }
        else{

            AlertDialog.Builder dialog=new AlertDialog.Builder(yazilariGuncelle.this);
            dialog.setTitle("Dikkat !!");
            dialog.setMessage("Yapılan Değişiklikler Kaydedilsin Mi ?");
            dialog.setPositiveButton("Kaydet", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    onGuncelle(view);
                }
            });
            dialog.setNegativeButton("Kaydetme", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startActivity(new Intent(yazilariGuncelle.this,AnaEkran.class));
                    finish();
                }
            });
            dialog.setNeutralButton("İptal", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    dialog.dismiss();
                }
            });
            dialog.show();
        }
    }

    //Yardım Butonuna Basılınca Olan Olaylar
    public void YardımEt(Activity activity){
        Dialog dialog=new Dialog(activity);
        dialog.setContentView(R.layout.yardim_et);
        dialog.setTitle("YardımEt");
        int en=(int) (activity.getResources().getDisplayMetrics().widthPixels*0.99);
        int boy=(int) (activity.getResources().getDisplayMetrics().heightPixels*0.85);
        dialog.getWindow().setLayout(en,boy);
        dialog.show();
    }

    //Kaydet Butonuna Basınca Çalışır..
    public void onGuncelle(View view){

        dosyaYoluKayıtım="";

        String yazıAyarla=icerikYazi.getText().toString();

        for(int i=0;i<icerikYazi.getText().toString().length();i++){
            if(yazıAyarla.charAt(i)=='✓'){
                i+=1;
                for(int j=i;yazıAyarla.charAt(j)!='✕';j++){
                    dosyaYoluKayıtım=dosyaYoluKayıtım+String.valueOf(yazıAyarla.charAt(j));
                    i=j;
                }
            }
            else if(yazıAyarla.charAt(i)=='✕'){
                dosyaYoluKayıtım+="◆";
                i+=1;
            }
        }

        try {
            Date zaman = new Date();
            SimpleDateFormat dakika = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            Zaman = dakika.format(zaman);

            Cursor cursor = AnaEkran.yazilarSQL.verileriCek("SELECT Id FROM NOTLAR");
            ArrayList<Integer> IDLER = new ArrayList<Integer>();
            while (cursor.moveToNext()) {
                IDLER.add(cursor.getInt(0));
            }
            AnaEkran.yazilarSQL.veriGuncelle(
                    baslikYazi.getText().toString(),
                    icerikYazi.getText().toString(),
                    Zaman,
                    dosyaYoluKayıtım,
                    AnaEkran.notID
            );
        }catch (Exception hata){Toast.makeText(getApplicationContext(),hata.getMessage(),Toast.LENGTH_SHORT).show();}
        ListeyiGuncelle();
        //Notların GöründüğüActivity'e geçiş Yapıyoruz.
        startActivity(new Intent(yazilariGuncelle.this,AnaEkran.class));
        finish();

    }

  public static void ListeyiGuncelle(){
      Cursor cursor = AnaEkran.yazilarSQL.verileriCek("SELECT * FROM NOTLAR");
      AnaEkran.notListe.clear();

      while (cursor.moveToNext()) {
          int id = cursor.getInt(0);
          String baslik = cursor.getString(1);
          String icerik = cursor.getString(2);
          String tarih = cursor.getString(3);
          int arkaplan = cursor.getInt(4);
          int notResim = cursor.getInt(5);

          AnaEkran.notListe.add(new YazıYaz(id, baslik, icerik, tarih, arkaplan, notResim));
      }
      if(AnaEkran.notListe.size()==0){
          AnaEkran.listele.setBackgroundResource(R.drawable.baslangic);
      }
      AnaEkran.yazıAdapter.notifyDataSetChanged();
  }



    public void addImageBetweentext(Drawable drawable) {
        drawable .setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        int selectionCursor = icerikYazi.length();
        icerikYazi.getText().insert(selectionCursor, "\n\n✓"+mImagePath+"✕");
        selectionCursor = icerikYazi.length();

        builder = new SpannableStringBuilder(icerikYazi.getText());
        builder.setSpan(new ImageSpan(drawable), selectionCursor - ("✓"+mImagePath+"✕").length(), selectionCursor,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.append("\n");
        icerikYazi.setText(builder);

        icerikYazi.setSelection(icerikYazi.length());
        scroll.post(new Runnable() {
            @Override
            public void run() {
                scroll.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
        scroll.postDelayed(new Runnable() {
            @Override
            public void run() {
                scroll.fullScroll(ScrollView.FOCUS_DOWN);
            }
        },100);

    }

}