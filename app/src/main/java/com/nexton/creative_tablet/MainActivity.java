package com.nexton.creative_tablet;

import static net.daum.mf.map.api.MapPoint.mapPointWithGeoCoord;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import net.daum.mf.map.api.MapView;
import net.daum.mf.map.api.CalloutBalloonAdapter;
import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;

import java.security.MessageDigest;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        getAppKeyHash();
        ViewGroup mapViewContainer = findViewById(R.id.map_view);
        mapViewContainer.removeAllViews();
        MapView mapView = new MapView(MainActivity.this);
        mapViewContainer.addView(mapView);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference statusref = database.getReference("status");
        String itemname = "정성훈 / 19세 / 남";
        MapPoint mappoint = MapPoint.mapPointWithGeoCoord(37.3825244,126.6711298);
        statusref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot datas : snapshot.getChildren()){
                    if(datas.getKey().equals("isfcmgo")){
                        if(datas.getValue(String.class).equals("1")){
                             mapView.setMapCenterPointAndZoomLevel(mappoint,3,true);
                             MapPOIItem marker = new MapPOIItem();
                             marker.setMarkerType(MapPOIItem.MarkerType.CustomImage);
                             marker.setSelectedMarkerType(MapPOIItem.MarkerType.CustomImage);
                             marker.setCustomImageAutoscale(false);
                             marker.setItemName(itemname);
                             marker.setTag(0);
                             marker.setCustomImageResourceId(R.drawable.marker);
                             marker.setCustomSelectedImageResourceId(R.drawable.marker_clicked);
                             marker.setMapPoint(mappoint);
                             mapView.addPOIItem(marker);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    private void getAppKeyHash() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                Log.e("Hash key", something);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            Log.e("name not found", e.toString());
        }
    }


}