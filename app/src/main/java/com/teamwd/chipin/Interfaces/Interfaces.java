package com.teamwd.chipin.Interfaces;

public class Interfaces {

    public interface DataProviderCallback{
        public void onCompleted();
        public void onError(String msg);
    }
}
