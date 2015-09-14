package com.mynotes.prajyot.moonshot.Login;

import java.util.ArrayList;


public class LoginObject {
    ArrayList<ResultLogin> fence=new ArrayList<>();
    String result;

    public ArrayList<ResultLogin> getFence() {
        return fence;
    }

    public void setFence(ArrayList<ResultLogin> fence) {
        this.fence = fence;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
