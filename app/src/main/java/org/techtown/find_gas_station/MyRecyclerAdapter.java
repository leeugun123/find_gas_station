package org.techtown.find_gas_station;

import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.ViewHolder> {

    private ArrayList<oil_list> oil_lists = new ArrayList<>();
    private UserListRecyclerClickListener mClickListener;

    public MyRecyclerAdapter(ArrayList<oil_list> Oil_lists,UserListRecyclerClickListener clickListener){
        oil_lists = Oil_lists;
        mClickListener = clickListener;
    }

    @NonNull
    @Override
    public MyRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recyclerview,parent,false);
        return new ViewHolder(view,mClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyRecyclerAdapter.ViewHolder holder, int position) {
        holder.onBind(oil_lists.get(position));
    }

    public void setOil_lists(ArrayList<oil_list> list){
        this.oil_lists = list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return oil_lists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView name;
        TextView price;
        TextView distance;
        TextView oil_kind;
        ImageView oil_image;

        UserListRecyclerClickListener mClickListener;

        public ViewHolder(@NonNull View itemView,UserListRecyclerClickListener clickListener) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.name);
           price =(TextView) itemView.findViewById(R.id.price);
            distance=(TextView) itemView.findViewById(R.id.distance);
            oil_kind = (TextView) itemView.findViewById(R.id.oil_kind);
            oil_image = itemView.findViewById(R.id.oil_image);


            mClickListener = clickListener;
            itemView.setOnClickListener(this);

        }

        public void onBind(oil_list oil_list) {
            name.setText(oil_list.get_oil_name());
            price.setText(oil_list.getPrice());
            distance.setText(oil_list.getDistance());
            oil_kind.setText(oil_list.getOil_kind());
            oil_image.setImageResource(oil_list.get_image());
        }

        @Override
        public void onClick(View view) {
            mClickListener.onUserClicked(getAdapterPosition());
        }

    }

    public interface UserListRecyclerClickListener{
        void onUserClicked(int position);
    }

}
