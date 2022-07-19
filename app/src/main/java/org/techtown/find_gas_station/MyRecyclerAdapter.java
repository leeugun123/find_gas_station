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
        Button navi_button_Tmap;

        UserListRecyclerClickListener mClickListener;

        public ViewHolder(@NonNull View itemView,UserListRecyclerClickListener clickListener) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.name);
            price = (TextView) itemView.findViewById(R.id.price);
            distance = (TextView) itemView.findViewById(R.id.distance);
            oil_kind = (TextView) itemView.findViewById(R.id.oil_kind);
            oil_image = itemView.findViewById(R.id.oil_image);
            navi_button_kakao = itemView.findViewById(R.id.navi_button_kakao);
            navi_button_Tmap = itemView.findViewById(R.id.navi_button_Tmap);

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


                    Log.i(TAG, "카카오내비 설치");

                    //카카오 navi API 코드
                    Location destination = Location.newBuilder(oil_list.get_oil_name(),oil_list.getWgs84X(), oil_list.getWgs84Y()).build();
                    //현재 위치는 고려할 필요가 없는가?? 목적지 주유소 이름 ,x,y좌표

                    NaviOptions options = NaviOptions.newBuilder().setCoordType(CoordType.WGS84).setVehicleType(VehicleType.FIRST)
                            .setRpOption(RpOption.SHORTEST).build();
                    // 네비 경로 설정 , 카텍 좌표 , 1종 차량, 빠른길/최단거리 안내
                    // 1종 승용차/소형승합차/소형화물차

                    KakaoNaviParams params = KakaoNaviParams.newBuilder(destination)
                            .setNaviOptions(options)
                            .build();

                    KakaoNaviService.getInstance().navigate(view.getContext(),params);

                    /*
                    if (NaviClient.getInstance().isKakaoNaviInstalled(view.getContext())) {

                        Log.i(TAG, "카카오내비 설치");

                        //카카오 navi API 코드
                        Location destination = Location.newBuilder(oil_list.get_oil_name(),oil_list.getWgs84X(), oil_list.getWgs84Y()).build();
                        //현재 위치는 고려할 필요가 없는가?? 목적지 주유소 이름 ,x,y좌표

                        NaviOptions options = NaviOptions.newBuilder().setCoordType(CoordType.WGS84).setVehicleType(VehicleType.FIRST)
                                .setRpOption(RpOption.SHORTEST).build();
                        // 네비 경로 설정 , 카텍 좌표 , 1종 차량, 빠른길/최단거리 안내
                        // 1종 승용차/소형승합차/소형화물차

                        KakaoNaviParams params = KakaoNaviParams.newBuilder(destination)
                                .setNaviOptions(options)
                                .build();

                        KakaoNaviService.getInstance().navigate(view.getContext(),params);


                    } else {



                    }
                   */



                }
            }); //카카오 navi 버튼을 눌렀을때



            navi_button_Tmap.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {

                    //T맵 navi API 코드

                }
            });//티맵 버튼을 눌렀을때


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
