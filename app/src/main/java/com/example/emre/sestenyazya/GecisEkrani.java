package com.example.emre.sestenyazya;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class GecisEkrani extends AppCompatActivity {

    private TextView baslik,soz;
    private ImageView logo,resim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_gecis_ekrani);
        baslik=(TextView) findViewById(R.id.baslikGecis);
        soz=(TextView) findViewById(R.id.sozGecis);
        logo=(ImageView) findViewById(R.id.logoGecis);
        resim=(ImageView) findViewById(R.id.resimGecis);

        Animation animasyon = AnimationUtils.loadAnimation(this, R.anim.animasyonum); //Geçiş Ekranı Açılış Animasyonu
        //Animasyonları Başlatıyoruz..
        baslik.startAnimation(animasyon);
        resim.startAnimation(animasyon);
        soz.startAnimation(animasyon);
        logo.startAnimation(animasyon);
        //Asıl Ekrana Geçiş Yapalım..
        final Intent i = new Intent(this, AnaEkran.class);
        Thread timer = new Thread() {
            public void run() {
                try {
                    sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    startActivity(i);
                    finish();
                }
            }
        };
        timer.start();

    }
}
