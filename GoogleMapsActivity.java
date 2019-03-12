package com.example.googledistance01;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

public class GoogleMapsActivity extends FragmentActivity
        implements OnMapReadyCallback , GoogleMap.OnMapLongClickListener{

    private GoogleMap mMap;

    private double latitude=37.5670;
    private double longitude=126.9807;
    private String otitle="Seoul";

    private int numberOfPoint=0;
    private LatLng first;
    private LatLng second;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapLongClickListener(this); //이번트 등록
        //처음을 서울로 설정한다.
        LatLng seoul = new LatLng(latitude, longitude);  //서울 위도경도 설정
        showMap(seoul);
    }
    private void showMap(LatLng city) {
        String mytitle=String.format("%s [%f, %f]",otitle,city.latitude,city.longitude);
        mMap.addMarker(new MarkerOptions().position(city).title(mytitle.trim()));
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(city, 5);
        mMap.animateCamera(update);
        Toast.makeText(this, mytitle, Toast.LENGTH_LONG).show();
    }
    @Override
    public void onMapLongClick(LatLng latLng) {
        otitle="";
        if(numberOfPoint==0){         //첫번째 위치 0
            mMap.clear();        //맵에서 마커제거
            first=latLng;         //첫번째 위치
            numberOfPoint++;    //1
        }else {                         // numberOfPoint=1이면 두번째 위치
            numberOfPoint=0;   //첫번째로 0
            second=latLng;      //두번째 위치
            //거리를 구한다.
            double dist=HaversineDistance.distance(
                    first.latitude, first.longitude, second.latitude, second.longitude);
            String msg=""+dist+"Km : ";   //km로 표시
            //두위치 사이에 직선
            Polyline line = mMap.addPolyline(new PolylineOptions()
                    .add(first, second)       //두번째 위치를 선택할때 첫번째, 두번째 연결
                    .width(25)
                    .color(Color.BLUE)
                    .geodesic(true));
            //첫번째 위치를 중심으로 두번째 위치까지의 거리를 반지름으로 원 그리기
            Circle circle = mMap.addCircle(new CircleOptions()
                    .center(first)
                    .radius(dist * 1000) //km로 지도에 표시
                    .strokeColor(Color.RED)
                    .fillColor(0x3aff0000)    //투명도 3af, ff면 100%투명
            );
            otitle="\n"+msg;  //거리 포함
        }
        showMap(latLng);      //롱클릭을 할때마다 마커보이기
    }
}
