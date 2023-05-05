package com.nailm.soztapmaca;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MarketAdapter extends RecyclerView.Adapter<MarketAdapter.MarketHolder> {

    private ArrayList<Market> marketlist;

    private Context context;


    private OnItemclicklistener listener;


    public MarketAdapter(ArrayList<Market> marketlist, Context context) {
        this.marketlist = marketlist;
        this.context = context;
    }

    @NonNull
    @Override
    public MarketHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_market,parent,false);

        return new MarketHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MarketHolder holder, int position) {

        Market market=marketlist.get(position);

        holder.basliqad.setText(market.getBasliqyazi());
        holder.resm.setBackgroundResource(market.getImgresm());
        holder.qiymetler.setText(String.valueOf(market.getQiymet()));


    }

    @Override
    public int getItemCount() {

        return marketlist.size();
    }

    class MarketHolder extends RecyclerView.ViewHolder{

       private TextView basliqad;
       private ImageView resm;

       private Button qiymetler;


        public MarketHolder(@NonNull View itemView) {
            super(itemView);

            basliqad=itemView.findViewById(R.id.basliqad);
            resm=itemView.findViewById(R.id.sekilitem);
            qiymetler=itemView.findViewById(R.id.buttonqiymet);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int position=getAdapterPosition();


                    if(listener!=null && position!=RecyclerView.NO_POSITION){

                        listener.OnItemclick(marketlist.get(position),position);

                    }


                }
            });


        }
    }


    public interface OnItemclicklistener {

        void OnItemclick(Market market, int position);


    }

        public  void setOnclicklistener(OnItemclicklistener listener){

            this.listener=listener;
        }




}
