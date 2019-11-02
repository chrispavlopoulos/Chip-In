package com.teamwd.chipin.Interfaces;

import com.teamwd.chipin.Models.Donation;
import com.teamwd.chipin.Models.ModelUser;

import java.util.ArrayList;

public class Interfaces {

    public interface Callback{
        void onSuccess();
        void onError();
    }

    public interface DataProviderCallback{
        public void onCompleted();
        public void onError(String msg);
    }

    public interface UserCallback{
        public void onCompleted(ModelUser user);
        public void onError(String msg);
    }

    public interface DonationsCallback{
        public void onCompleted(ArrayList<Donation> donations);
        public void onError(String msg);
    }
}
