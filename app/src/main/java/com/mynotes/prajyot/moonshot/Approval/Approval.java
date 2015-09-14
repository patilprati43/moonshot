package com.mynotes.prajyot.moonshot.Approval;

import java.util.ArrayList;


public class Approval {
    ArrayList<ApprovalList> fence=new ArrayList<>();
    String result;

    public ArrayList<ApprovalList> getFence() {
        return fence;
    }

    public void setFence(ArrayList<ApprovalList> fence) {
        this.fence = fence;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
