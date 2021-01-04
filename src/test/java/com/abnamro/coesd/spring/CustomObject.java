package com.abnamro.coesd.spring;

public class CustomObject {

    private String val;

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }

    public static CustomObject valueOf(String value){
        CustomObject result = new CustomObject();
        result.setVal(value);
        return result;
    }
}
