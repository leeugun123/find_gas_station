package org.techtown.find_gas_station.Fragment;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import org.techtown.find_gas_station.R;
import org.techtown.find_gas_station.Retrofit.oilAvg.OIL;
import org.techtown.find_gas_station.Retrofit.oilAvg.OilAvg;
import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class OilAvgRecyclerAdapter extends RecyclerView.Adapter<OilAvgRecyclerAdapter.ViewHolder> {

    private ArrayList<OIL> oilAvg_List;


    public OilAvgRecyclerAdapter(ArrayList<OIL> oilAvg_List){
        this.oilAvg_List = oilAvg_List;
    }

    @NonNull
    @Override
    public OilAvgRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.oilavg,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OilAvgRecyclerAdapter.ViewHolder holder, int position) {
        holder.onBind(oilAvg_List.get(position),position);
    }

    @Override
    public int getItemCount() {return oilAvg_List.size();}

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView day;
        TextView price;

        TextView priceGap;



        public ViewHolder(@NonNull View itemView){

            super(itemView);

            day = itemView.findViewById(R.id.day);
            price = itemView.findViewById(R.id.price);

            priceGap = itemView.findViewById(R.id.priceGap);

        }

        @SuppressLint("ResourceAsColor")
        public void onBind(OIL oilAvg, int pos){

            day.setText(convertDateString(oilAvg.getDate()));
            price.setText(oilAvg.getPrice());

            if(pos == oilAvg_List.size() - 1){
                priceGap.setText("-");
                priceGap.setTextColor(ContextCompat.getColor(itemView.getContext(),R.color.gray));
            }else{

                int gap = priceGap(pos);

                String priceText = Integer.toString(gap);

                if(gap > 0){
                    priceGap.setTextColor(ContextCompat.getColor(itemView.getContext(),R.color.orange));
                    priceGap.setText("+"+ priceText);
                }else{
                    priceGap.setTextColor(ContextCompat.getColor(itemView.getContext(),R.color.purple_700));
                    priceGap.setText(priceText);
                }





            }




        }

        public String convertDateString(String inputDate){

            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyyMMdd");
            SimpleDateFormat outputFormat = new SimpleDateFormat("MM월 dd일");

            try {
                Date date = inputFormat.parse(inputDate);
                return outputFormat.format(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            return "";

        }



        public int priceGap(int pos){

            return Integer.parseInt(oilAvg_List.get(pos).getPrice()) -
                          Integer.parseInt(oilAvg_List.get(pos+1).getPrice());


        }



    }



}