package com.example.emre.sestenyazya;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.CardView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class YazılarınAdapter extends BaseAdapter {

   Context context;
    private int layout;
    private ArrayList<YazıYaz> yaziListem;

    public YazılarınAdapter(Context context, int layout, ArrayList<YazıYaz> yaziListem) {
        this.context = context;
        this.layout = layout;
        this.yaziListem = yaziListem;
    }

    @Override
    public int getCount() {
        return yaziListem.size();
    }

    @Override
    public Object getItem(int position) {
        return yaziListem.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder{
        TextView Baslik,Tarih;
        ImageView Arkaplan,Resim;
        GridView gridView;
        ImageButton Sil;
        public CardView cardView;

        public void setListeners(final int position) {
            try {

                cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AnaEkran.notID=position;
                        Intent intent=new Intent(context,yazilariGuncelle.class);
                        context.startActivity(intent);
                    }
                });

                Sil.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        AlertDialog.Builder dialog=new AlertDialog.Builder(context);
                        dialog.setTitle("Dikkat !!");
                        dialog.setMessage("Bu Notu Silmek İstediğinizden Emin Misiniz ?");

                        dialog.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(getCount()==1){
                                    AnaEkran.notID=position;
                                    AnaEkran.yazilarSQL.verileriSil(position);
                                    yazilariGuncelle.ListeyiGuncelle();
                                    AnaEkran.yazıAdapter.notifyDataSetChanged();

                                   // Toast.makeText(context,String.valueOf(AnaEkran.notID),Toast.LENGTH_SHORT).show();
                                }
                                    else{
                                    AnaEkran.yazilarSQL.verileriSil(position);
                                    yazilariGuncelle.ListeyiGuncelle();
                                    AnaEkran.yazıAdapter.notifyDataSetChanged();
                                }


                            }

                        });

                        dialog.setNegativeButton("Hayır", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                dialog.dismiss();
                            }
                        });
                        dialog.show();
                    }
                });

            }catch (Exception hatam){hatam.printStackTrace();}

        }
    }



    @Override
    public View getView(int position, final View convertView, ViewGroup parent) {



        View row=convertView;
        ViewHolder holder=new ViewHolder();

        if(row==null){
            LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row=inflater.inflate(layout,null);

            holder.Arkaplan=(ImageView) row.findViewById(R.id.reSim);
            holder.Resim=(ImageView) row.findViewById(R.id.reSim);
            holder.Baslik=(TextView) row.findViewById(R.id.baSlik);
            holder.Tarih=(TextView) row.findViewById(R.id.icErik);
            holder.gridView=(GridView) row.findViewById(R.id.listele);
            holder.Sil=(ImageButton) row.findViewById(R.id.sil);
            holder.cardView=(CardView) row.findViewById(R.id.cardView);
            row.setTag(holder);
        }
        else {
            holder=(ViewHolder) row.getTag();
        }

        YazıYaz yazıYaz=yaziListem.get(position);
        holder.setListeners(yazıYaz.getId());
        holder.Baslik.setText(yazıYaz.getBaslik());
        holder.Tarih.setText(yazıYaz.getTarih());
        holder.Arkaplan.setBackgroundResource(yazıYaz.getArkaplan());
        holder.Resim.setImageResource(yazıYaz.getNotresim());



        return row;
    }

}
