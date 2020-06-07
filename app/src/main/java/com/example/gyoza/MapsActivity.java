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

    //ページが保持する、表示しているデータリスト
    private ArrayList data = new ArrayList<>();
    //ページが保持する、DBから取得した生データ
    private Map<String , Message> value;
    //ページが保持する、選択されている駅名
    private String station;
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

        //取得した駅名を引数に、データ取得メソッドを呼ぶ。
        Spinner sta_spinner = (android.widget.Spinner) findViewById(com.example.gyoza.R.id.stationselecter);
        sta_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                station = (String) adapterView.getSelectedItem();
                System.out.println("ドロップリスト駅名取得" + station);

                getMessagebySta(station);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //何も選択されなかった時の処理
            }
        });
        /*カテゴリ選択されたときの挙動*/

        //取得したカテゴリを引数に、今保持している情報を絞り込む。全ての場合は再度駅名取得メソッドを呼ぶ。

        Spinner cat_spinner = (android.widget.Spinner) findViewById(com.example.gyoza.R.id.categoriselecter);
        cat_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String categori = (String) adapterView.getSelectedItem();
                System.out.println("ドロップリストカテゴリ取得"+categori);
                String catAns = "全て";

                //条件分岐
                if (categori.equals(catAns)) {
                    System.out.println("called all");
                    getMessagebySta(station);
                }else{
                    System.out.println("called cate");
                   getMessagebyCat(categori);
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

    //条件指定なし全件取得
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

    //カテゴリ指定時,選択されたカテゴリを引数にこのメソッドを呼ぶ。

    public void getMessagebyCat(String categori){

        //メッセージを初期化する
        data = new ArrayList<>();

        //すでに取得している情報からさらに情報を絞り込む
        //選択されたカテゴリに一致する場合、messageを表示する

        for(Message message : value.values()){
            if (message.getCategori().equals(categori)){
                String putmessage = message.getMessage();
                data.add(putmessage);
                System.out.println(putmessage);
            }else{
            }
        }
        //リスト作成メソッド
        MakeList(data);
    }





    //駅名の条件指定取得
    public void getMessagebySta(String station){
        // インスタンスの取得
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        //path指定
        DatabaseReference dbref = database.getReference("test");

        //引数のカテゴリを指定して取得
        dbref.orderByChild("station").equalTo(station).addValueEventListener(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(@androidx.annotation.NonNull com.google.firebase.database.DataSnapshot dataSnapshot) {
                GenericTypeIndicator<Map<String,Message>> indicator = new com.google.firebase.database.GenericTypeIndicator<java.util.Map<String,Message>>() {
                };
                //メッセージリストを初期化して入れ直す
                data = new ArrayList<>();
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
        MarkerOptions options = new com.google.android.gms.maps.model.MarkerOptions();
        LatLng tokyo = new LatLng(35, 139);
        mMap.addMarker(options.position(tokyo).title("タイトル").snippet("詳細情報"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(tokyo));
    }
}
