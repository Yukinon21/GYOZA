package com.example.gyoza;

public class User {
    public String name;
    public Integer age;

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
}