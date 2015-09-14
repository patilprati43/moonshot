package com.mynotes.prajyot.moonshot.beans;

/**
 * Created by prajyot on 11/9/15.
 */
public class Values {
    String key;
    String value;
    public Values(String key,String value)
    {
        this.key=key;
        this.value=value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
