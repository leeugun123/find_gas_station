package org.techtown.find_gas_station.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.techtown.find_gas_station.R;
import org.techtown.find_gas_station.Retrofit.oilAvg.OIL;
import org.techtown.find_gas_station.Retrofit.oilAvg.OilAvg;

import java.util.ArrayList;

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
        holder.onBind(oilAvg_List.get(position));
    }

    @Override
    public int getItemCount() {return oilAvg_List.size();}

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView day;
        TextView price;


        public ViewHolder(@NonNull View itemView){

            super(itemView);

            day = itemView.findViewById(R.id.day);
            price = itemView.findViewById(R.id.price);

        }

        public void onBind(OIL oilAvg){

            day.setText(oilAvg.getDate());
            price.setText(oilAvg.getPrice());


        }


    }

}
