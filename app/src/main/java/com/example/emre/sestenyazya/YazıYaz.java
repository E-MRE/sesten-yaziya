package com.example.emre.sestenyazya;

public class YazıYaz {
    private int id;
    private String baslik;
    private String icerik;
    private String tarih;
    private int arkaplan;
    private int notresim;


    public YazıYaz(int id, String baslik, String icerik,String tarih,int arkaplan,int notresim) {
        this.id = id;
        this.baslik = baslik;
        this.icerik = icerik;
        this.tarih = tarih;
        this.arkaplan=arkaplan;
        this.notresim=notresim;
    }

    public int getArkaplan() {
        return arkaplan;
    }

    public void setArkaplan(int arkaplan) {
        this.arkaplan = arkaplan;
    }

    public int getNotresim() {
        return notresim;
    }

    public void setNotresim(int notresim) {
        this.notresim = notresim;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBaslik() {
        return baslik;
    }

    public void setBaslik(String baslik) {
        this.baslik = baslik;
    }

    public String getIcerik() {
        return icerik;
    }

    public void setIcerik(String icerik) {
        this.icerik = icerik;
    }

    public String getTarih() {
        return tarih;
    }

    public void setTarih(String tarih) {
        this.tarih = tarih;
    }
}
