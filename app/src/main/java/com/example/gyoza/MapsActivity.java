package com.example.gyoza;


import java.util.ArrayList;


import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


import com.google.firebase.database.*;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private DatabaseReference mFirebaseDatabaseReference;

    private ArrayList data = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



        /*Firebaseへのデータ登録*/

//        User user = new User("山田太郎",30);
//        // インスタンスの取得
//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//
//        // ファイルパスを指定してリファレンスを取得
//        DatabaseReference refName = database.getReference("info/user");
//        // データを登録
//        refName.setValue(user);

        /*Firebaseへのデータ登録ここまで*/

        /*Firebaseからのデータ取得*/

        // インスタンスの取得
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("info/user");

// Attach a listener to read the data at our posts reference
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                System.out.println("取得したよ"+user.name);

                //ListViewへの表示
                data.add("コメント１");
                data.add(user.name);
                data.add(user.age);
                data.add("コメント4");
                data.add("コメント5");
                data.add("コメント6");
                data.add("コメント7");

                System.out.println("取得したの"+ data.get(1));
                MakeList(data);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }


        });



    }

    public void MakeList(ArrayList x) {
        android.widget.ArrayAdapter adapter = new android.widget.ArrayAdapter(this, android.R.layout.simple_list_item_1, x);

        android.widget.ListView listView = (android.widget.ListView) findViewById(com.example.gyoza.R.id.comments);
        listView.setAdapter(adapter);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng tokyo = new LatLng(35, 139);
        mMap.addMarker(new MarkerOptions().position(tokyo).title("Marker in Tokyo"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(tokyo));
    }
}
