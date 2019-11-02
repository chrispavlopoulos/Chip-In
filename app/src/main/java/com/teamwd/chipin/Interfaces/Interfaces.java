package com.teamwd.chipin.Interfaces;

import com.teamwd.chipin.Models.ModelUser;

public class Interfaces {

    public interface DataProviderCallback{
        public void onCompleted();
        public void onError(String msg);
    }

    public interface UserCallback{
        public void onCompleted(ModelUser user);
        public void onError(String msg);
    }
}
