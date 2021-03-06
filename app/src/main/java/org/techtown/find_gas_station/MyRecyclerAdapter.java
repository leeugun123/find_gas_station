package org.techtown.find_gas_station;

import static android.content.ContentValues.TAG;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.internal.Constants;
import com.kakao.kakaonavi.KakaoNaviParams;
import com.kakao.kakaonavi.KakaoNaviService;
import com.kakao.kakaonavi.Location;
import com.kakao.kakaonavi.NaviOptions;
import com.kakao.kakaonavi.options.CoordType;
import com.kakao.kakaonavi.options.RpOption;
import com.kakao.kakaonavi.options.VehicleType;
import com.kakao.sdk.navi.NaviClient;

import org.techtown.find_gas_station.GPS.GpsTracker;

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

        Button navi_button_kakao;


        UserListRecyclerClickListener mClickListener;

        public ViewHolder(@NonNull View itemView,UserListRecyclerClickListener clickListener) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.name);
            price = (TextView) itemView.findViewById(R.id.price);
            distance = (TextView) itemView.findViewById(R.id.distance);
            oil_kind = (TextView) itemView.findViewById(R.id.oil_kind);
            oil_image = itemView.findViewById(R.id.oil_image);
            navi_button_kakao = itemView.findViewById(R.id.navi_button_kakao);

            mClickListener = clickListener;
            itemView.setOnClickListener(this);

        }

        public void onBind(oil_list oil_list) {

            name.setText(oil_list.get_oil_name());
            price.setText(oil_list.getPrice());
            distance.setText(oil_list.getDistance());
            oil_kind.setText(oil_list.getOil_kind());
            oil_image.setImageResource(oil_list.get_image());

            navi_button_kakao.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    Log.i(TAG, "??????????????? ??????");

                    //????????? navi API ??????
                    Location destination = Location.newBuilder(oil_list.get_oil_name(),oil_list.getWgs84X(), oil_list.getWgs84Y()).build();
                    //?????? ????????? ????????? ????????? ??????????? ????????? ????????? ?????? ,x,y??????

                    NaviOptions options = NaviOptions.newBuilder().setCoordType(CoordType.WGS84).setVehicleType(VehicleType.FIRST)
                            .setRpOption(RpOption.SHORTEST).build();
                    // ?????? ?????? ?????? , ?????? ?????? , 1??? ??????, ?????????/???????????? ??????
                    // 1??? ?????????/???????????????/???????????????

                    KakaoNaviParams params = KakaoNaviParams.newBuilder(destination)
                            .setNaviOptions(options)
                            .build();

                    KakaoNaviService.getInstance().navigate(view.getContext(),params);




                }
            }); //????????? navi ????????? ????????????





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
