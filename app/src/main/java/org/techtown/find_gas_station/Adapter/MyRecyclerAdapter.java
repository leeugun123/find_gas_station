package org.techtown.find_gas_station.Adapter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.kakao.kakaonavi.KakaoNaviParams;
import com.kakao.kakaonavi.KakaoNaviService;
import com.kakao.kakaonavi.Location;
import com.kakao.kakaonavi.NaviOptions;
import com.kakao.kakaonavi.options.CoordType;
import com.kakao.kakaonavi.options.RpOption;
import com.kakao.kakaonavi.options.VehicleType;
import com.kakao.sdk.navi.Constants;
import com.kakao.sdk.navi.NaviClient;

import org.techtown.find_gas_station.IntelActivity;
import org.techtown.find_gas_station.OilList;
import org.techtown.find_gas_station.R;

import java.util.List;


public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.ViewHolder> {

    private List<OilList> oilLists;
    private GoogleMap recyclerMap;

    private String sort;

    public MyRecyclerAdapter(List<OilList> Oil_lists,GoogleMap map, String sort){
        oilLists = Oil_lists;
        recyclerMap = map;
        this.sort = sort;
    }

    @NonNull
    @Override
    public MyRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recyclerview,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyRecyclerAdapter.ViewHolder holder, int position) {
        holder.onBind(oilLists.get(position));
    }

    @Override
    public int getItemCount() {
        return oilLists.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView price;
        TextView distance;
        TextView oil_kind;
        ImageView oil_image;

        ImageView carWashImg;
        ImageView convenStore;

        Button navi_button_kakao;
        ImageButton intelButton;


        String lotAddress;
        //지번 주소
        String stAddress;
        //도로명 주소
        String tel;
        //전화번호
        String sector;
        //업종 구분 , N:주유소 Y:자동차주유소 C:주유소/충전소 겸업

        String carWash;
        //세차장 존재 여부
        String store;
        //편의점 존재 여부


        public ViewHolder(@NonNull View itemView) {

            super(itemView);

            name = itemView.findViewById(R.id.name);
            price =  itemView.findViewById(R.id.price);
            distance = itemView.findViewById(R.id.distance);
            oil_kind =  itemView.findViewById(R.id.oil_kind);
            oil_image = itemView.findViewById(R.id.oil_image);
            navi_button_kakao = itemView.findViewById(R.id.navi_button_kakao);

            intelButton = itemView.findViewById(R.id.intelButton);

            carWashImg = itemView.findViewById(R.id.carWashStore);
            convenStore = itemView.findViewById(R.id.conStore);

        }

        public void onBind(OilList oil_list) {


            name.setText(oil_list.get_oil_name());
            price.setText(oil_list.price +"원");

            if(sort.equals("3")){
                distance.setText(changeKm(oil_list.actDistance) + "km");
            }else if(sort.equals("4")){
                distance.setText(formatSeconds(Integer.parseInt(oil_list.spendTime)));
            }else{
                distance.setText(changeKm(oil_list.distance)+"km");
            }

            oil_kind.setText(oil_list.oil_kind);
            oil_image.setImageResource(oil_list.get_image());

            carWash = oil_list.carWash;
            store = oil_list.conStore;

            lotAddress = oil_list.lotNumberAdd;
            stAddress = oil_list.roadAdd;
            tel = oil_list.tel;
            sector = oil_list.sector;


            if(carWash.equals("Y")){
                carWashImg.setImageResource(R.drawable.car_wash);
            }
            else{
                carWashImg.setImageResource(R.color.white);
            }

            if(store.equals("Y")){
                convenStore.setImageResource(R.drawable.convenstore);
            }
            else{
                convenStore.setImageResource(R.color.white);
            }

            //구글맵에 주유소 이미지 표시


            LatLng pos = new LatLng(oil_list.getWgs84Y(),oil_list.getWgs84X());

            MarkerOptions markerOptions = new MarkerOptions();


            BitmapDrawable bitmapdraw = (BitmapDrawable) oil_image.getResources().getDrawable(oil_list.get_image());
            Bitmap b = bitmapdraw.getBitmap();
            Bitmap smallMarker = Bitmap.createScaledBitmap(b,80,80,false);

            markerOptions.position(pos).title(oil_list.get_oil_name()).snippet("현 위치로부터 거리 " + oil_list.distance + "m")
                    .icon(BitmapDescriptorFactory.fromBitmap(smallMarker));

            recyclerMap.addMarker(markerOptions);



            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {

                    recyclerMap.animateCamera(CameraUpdateFactory.newLatLng(
                            new LatLng(oil_list.getWgs84Y() ,oil_list.getWgs84X() )  ),
                                    600,
                                    null
                            );


                }

            });
            //클릭하면 동작


            navi_button_kakao.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {

                    if(NaviClient.getInstance().isKakaoNaviInstalled(view.getContext())){
                        //카카오 navi API 코드
                        Location destination = Location.newBuilder(oil_list.get_oil_name(),oil_list.getWgs84X(), oil_list.getWgs84Y()).build();

                        //현재 위치는 고려할 필요가 없는가?? 목적지 주유소 이름 ,x,y좌표
                        NaviOptions options = NaviOptions.newBuilder().setCoordType(CoordType.WGS84).setVehicleType(VehicleType.FIRST)
                                .setRpOption(RpOption.FAST).build();

                        // 네비 경로 설정 , 카텍 좌표 , 1종 차량, 빠른길/최단거리 안내
                        // 1종 승용차/소형승합차/소형화물차

                        KakaoNaviParams params = KakaoNaviParams.newBuilder(destination)
                                .setNaviOptions(options)
                                .build();



                        KakaoNaviService.getInstance().navigate(view.getContext(),params);

                    }
                    else{

                        view.getContext().startActivity(
                                new Intent(
                                        Intent.ACTION_VIEW,
                                        Uri.parse(Constants.WEB_NAVI_INSTALL)
                                ).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        );

                    }



                }



            }); //카카오 navi 버튼을 눌렀을때


            intelButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {


                    Intent intent = new Intent(itemView.getContext(), IntelActivity.class);

                    intent.putExtra("wgsY",oil_list.getWgs84Y());
                    intent.putExtra("wgsX",oil_list.getWgs84X());


                    intent.putExtra("title",oil_list.get_oil_name());
                    //주유소 이름
                    intent.putExtra("gas_img",oil_list.get_image());
                    //주유소 imageView (정수임)

                    intent.putExtra("lotAddress",lotAddress);
                    intent.putExtra("stAddress",stAddress);
                    intent.putExtra("tel",tel);
                    intent.putExtra("oil_kind",sector);

                    intent.putExtra("carWash",carWash);
                    intent.putExtra("store",store);

                    itemView.getContext().startActivity(intent);

                }

            }); //intel 버튼을 눌렀을때


        }

        public String formatSeconds(int seconds) {

            if (seconds < 0) {
                throw new IllegalArgumentException("초는 음수일 수 없습니다.");
            }

            int minutes = seconds / 60;
            seconds = seconds % 60;

            if (minutes == 0) {
                return seconds + "초";
            } else if (seconds == 0) {
                return minutes + "분";
            } else {
                return minutes + "분 " + seconds + "초";
            }

        }


        public String changeKm(String distance){

            double doubleD = Double.parseDouble(distance)/1000;


            return String.format("%.1f",doubleD);

        }//m -> km 변경 메소드




    }



}
