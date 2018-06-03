package com.example.emre.sestenyazya;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class AnaEkran extends AppCompatActivity{

    String[] izinler = {
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    private void IzinKontrol() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                //-- Eğer almak istediğimiz izinler daha önceden kullanıcı tarafından onaylanmış ise bu kısımda istediğimiz işlemleri yapabiliriz..

            } else {
                //-- Almak istediğimiz izinler daha öncesinde kullanıcı tarafından onaylanmamış ise bu kod bloğu harekete geçecektir.
                //-- Burada requestPermissions() metodu ile kullanıcıdan ilgili Manifest izinlerini onaylamasını istiyoruz.
                requestPermissions(izinler, izinKodu);

            }

        }
    }

    public static GridView listele;
    public static ArrayList<YazıYaz> notListe;
    public static int izinKodu = 54,notID=0;
    public static YazılarınAdapter yazıAdapter;
    public static YazilarSQL yazilarSQL;
    public static boolean guncelle=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ana_ekran);
        IzinKontrol();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        yazilarSQL=new YazilarSQL(this,"NotlarDB.sqlite",null,1);

        yazilarSQL.dataSorgu("CREATE TABLE IF NOT EXISTS NOTLAR(Id INTEGER PRIMARY KEY AUTOINCREMENT, baslik VARCHAR" +
                ",icerik VARCHAR, tarih VARCHAR,arkaplan INTEGER,notResim INTEGER,dosyayolu VARCHAR)");

        listele = (GridView) findViewById(R.id.listele);
        notListe = new ArrayList<>();
        yazıAdapter = new YazılarınAdapter(this, R.layout.not_gorunum, notListe);
        listele.setAdapter(yazıAdapter);

        Cursor cursor = yazilarSQL.verileriCek("SELECT * FROM NOTLAR");
        notListe.clear();

            while (cursor.moveToNext()) {
                int id = cursor.getInt(0);
                String baslik = cursor.getString(1);
                String icerik = cursor.getString(2);
                String tarih = cursor.getString(3);
                int arkaplan = cursor.getInt(4);
                int notResim = cursor.getInt(5);

                notListe.add(new YazıYaz(id, baslik, icerik, tarih, arkaplan, notResim));
            }
        yazıAdapter.notifyDataSetChanged();

        if(notListe.size()==0){
            listele.setBackgroundResource(R.drawable.baslangic);
        }
        else{
            listele.setBackgroundResource(0);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.yeni_not_ekle,menu);  //Not Ekleme Butonunu Oluşturduk.
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.yeniEkle:
                startActivity(new Intent(this,SestenYaziyaEkrani.class));
                finish();
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        System.exit(0);
        return;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 54: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED
                        && grantResults[2] == PackageManager.PERMISSION_GRANTED) {

                    //-- Eğer kullanıcı istemiş olduğunuz izni onaylarsa bu kod bloğu çalışacaktır.

                } else {
                    System.exit(0);
                }
                return;
            }


        }
    }
}
