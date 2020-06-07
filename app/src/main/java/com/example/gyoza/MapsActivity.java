package com.example.gyoza;


import java.util.ArrayList;
import java.util.*;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.widget.*;
import android.view.*;
import android.content.*;


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
    private Message message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        com.google.android.gms.maps.SupportMapFragment mapFragment = (com.google.android.gms.maps.SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        /*Firebaseからのデータ取得*/

        /*カテゴリ選択されたときの挙動*/

        //取得したカテゴリを引数に、データ取得メソッドを呼ぶ。全ての場合は全件取得。

        Spinner cat_spinner = (android.widget.Spinner) findViewById(com.example.gyoza.R.id.categoriselecter);
        cat_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String categori = (String) adapterView.getSelectedItem();
                System.out.println("ドロップリスト取得"+categori);
                String catAns = "全て";

                if (categori.equals(catAns)) {
                    System.out.println("called all");
                    getMessage();
                }else{
                    System.out.println("called cate");
                   getMessage(categori);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //何も選択されなかった時の処理
            }
        });

        /*Firebaseからのデータ取得ここまで*/

        /*画面遷移ボタンの挙動生成*/

        Button sendButton = (Button) findViewById(R.id.send_button);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), PostActivity.class);
                startActivity(intent);
            }
        });

        /*画面遷移挙動ここまで*/


    }

    //情報取得、全件取得
    public void getMessage(){
        /*パスを指定して全件取得*/
        // インスタンスの取得
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        //パスを指定
        DatabaseReference ref = database.getReference("test");

        //Attach a listener to read the data at our posts reference
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GenericTypeIndicator<Map<String,Message>> indicator = new com.google.firebase.database.GenericTypeIndicator<java.util.Map<String,Message>>() {
                };
                //メッセージの初期化
                data = new ArrayList<>();

                Map<String , Message> value = dataSnapshot.getValue(indicator);
                for(Message message : value.values()){
                    String putmessage = message.getMessage();
                    data.add(putmessage);
                    System.out.println(putmessage);
                }
                //リスト作成メソッド
                MakeList(data);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }


        });
        /*全件取得ここまで*/
    }

    //情報取得メソッドカテゴリ指定時,選択されたカテゴリを引数にこのメソッドを呼ぶ。

    public void getMessage(String categori){
        /*条件指定で取得*/
        // インスタンスの取得
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        //path指定
        DatabaseReference dbref = database.getReference("test");

        //引数のカテゴリを指定して取得
        dbref.orderByChild("categori").equalTo(categori).addValueEventListener(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(@androidx.annotation.NonNull com.google.firebase.database.DataSnapshot dataSnapshot) {
                GenericTypeIndicator<Map<String,Message>> indicator = new com.google.firebase.database.GenericTypeIndicator<java.util.Map<String,Message>>() {
                };
                //メッセージを初期化する
                data = new ArrayList<>();

                Map<String , Message> value;
                value = dataSnapshot.getValue(indicator);

                for(Message message : value.values()){
                    String putmessage = message.getMessage();
                    data.add(putmessage);
                    System.out.println(putmessage);
                }
                //リスト作成メソッド
                MakeList(data);
            }

            @Override
            public void onCancelled(@androidx.annotation.NonNull com.google.firebase.database.DatabaseError databaseError) {

            }
        });
        /*条件指定取得ここまで*/

    }


    //リスト作成メソッド
    public void MakeList(ArrayList x) {
        android.widget.ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, x);

        ListView listView = (android.widget.ListView) findViewById(R.id.comments);
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
