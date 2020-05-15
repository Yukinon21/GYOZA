package com.example.gyoza;
//DBとのやりとり確認用にいったん作った、本番だとここが投稿内容とかになる
public class User {
    public String name;
    public Integer age;
    public String introduction;

    // 空のコンストラクタの宣言が必須
    public User() {
    }

    public User(String _name, Integer _age) {
        name = _name;
        age = _age;
    }
    public String getName(){
        return name;
    }
    public Integer getAge(){
        return age;
    }
    public String getIntroduction(){return introduction;}
}