package org.techtown.find_gas_station;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;


public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.ViewHolder> {

    private List<oil_list> oil_lists;
    private GoogleMap recyclerMap;


    public MyRecyclerAdapter(List<oil_list> Oil_lists,GoogleMap map){
        oil_lists = Oil_lists;
        recyclerMap = map;
    }

    @NonNull
    @Override
    public MyRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recyclerview,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyRecyclerAdapter.ViewHolder holder, int position) {
        holder.onBind(oil_lists.get(position));
    }

    public void setOil_lists(List<oil_list> list){
        this.oil_lists = list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return oil_lists.size();
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
        Button intelButton;


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

        public void onBind(oil_list oil_list) {

            name.setText(oil_list.get_oil_name());
            price.setText(oil_list.getPrice()+"원");
            distance.setText(changeKm(oil_list.getDistance())+"km");
            oil_kind.setText(oil_list.getOil_kind());
            oil_image.setImageResource(oil_list.get_image());

            carWash = oil_list.getCarWash();
            store = oil_list.getConStore();

            lotAddress = oil_list.getLotNumberAdd();
            stAddress = oil_list.getRoadAdd();
            tel = oil_list.getTel();
            sector = oil_list.getSector();

            if(carWash.equals("Y")){
                carWashImg.setImageResource(R.drawable.car_wash);
            }

            if(store.equals("Y")){
                convenStore.setImageResource(R.drawable.conven_store);
            }




            //구글맵에 주유소 이미지 표시
            LatLng pos = new LatLng(oil_list.getWgs84Y(),oil_list.getWgs84X());

            MarkerOptions markerOptions = new MarkerOptions();

            BitmapDrawable bitmapdraw = (BitmapDrawable) oil_image.getResources().getDrawable(oil_list.get_image());
            Bitmap b = bitmapdraw.getBitmap();
            Bitmap smallMarker = Bitmap.createScaledBitmap(b,80,80,false);

            markerOptions.position(pos).title(oil_list.get_oil_name()).snippet("현 위치로부터 거리" + oil_list.getDistance())
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
                    recyclerMap.setOnMarkerClickListener((GoogleMap.OnMarkerClickListener) itemView.getContext());

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


                    Intent intent = new Intent(itemView.getContext(),IntelActivity.class);

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

        public String changeKm(String distance){

            double doubleD = Double.parseDouble(distance)/1000;


            return String.format("%.1f",doubleD);

        }//m -> km 변경 메소드



    }



}
