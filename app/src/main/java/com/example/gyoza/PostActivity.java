package com.example.gyoza;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.*;
import android.os.*;
import android.view.*;
import android.content.*;

import com.google.firebase.database.*;

public class PostActivity extends AppCompatActivity {
    //Get text data from text box
    private EditText editText;
    private Button button;
    private TextView textView;
    private Button backbutton;

    //このページで保持する取得したカテゴリ
    private String categori;
    //このページで保持する取得した駅名
    private String station;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.posting);

        editText = (EditText) findViewById(R.id.editText);
        button = (Button) findViewById(R.id.button);
        textView = (TextView) findViewById(R.id.textView);
        backbutton = (Button) findViewById(R.id.backtomain);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ここに押された時の処理を記述
                String post = editText.getText().toString();
                System.out.println("post取得 = " + post);

                //取得テスト。テキストボックスから取得した値をそのまま画面に返す
                if(!post.equals("")) {
                    textView.setText(post);
                    editText.setHint("投稿文");//ヒント文
                }
                //カテゴリ選択
                android.widget.Spinner cat_spinner_pt = (android.widget.Spinner) findViewById(com.example.gyoza.R.id.categoriselecter_post);
                cat_spinner_pt.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                        categori = (String) parent.getSelectedItem();
                        System.out.println(categori);
                    }

                    @Override
                    public void onNothingSelected(android.widget.AdapterView<?> parent) {

                    }
                });

                //insert to Firebase DB
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("test");

                //DBに入れる情報をMap型にする
                java.util.Map<String , String> postMessage = new java.util.HashMap<>();
                //現在時刻を取得する関数をいれる
                postMessage.put("atcreate","currentTime");
                postMessage.put("message",post);
                postMessage.put("user","user1");
                postMessage.put("categori",categori);
                postMessage.put("station","渋谷");

                myRef.push().setValue(postMessage);

                //popup success message
                //back to mainView.

            }

        });
        //いったんメインへの画面遷移ボタン
        backbutton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(getApplication(), MapsActivity.class);
                startActivity(intent);
            }

        });

    }

}
