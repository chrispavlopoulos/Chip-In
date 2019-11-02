package com.teamwd.chipin.Interfaces;

import com.teamwd.chipin.Models.Donation;
import com.teamwd.chipin.Models.Event;
import com.teamwd.chipin.Models.ModelUser;
import com.teamwd.chipin.Models.Post;

import java.util.ArrayList;

public class Interfaces {

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

    public interface PostsCallback{
        public void onCompleted(ArrayList<Post> posts);
        public void onError(String msg);
    }

    public interface BadgeCallback{
        public void onCompleted(ArrayList<String> badges);
        public void onError(String msg);
    }

    public interface EventsCallback{
        public void onCompleted(ArrayList<Event> events);
        public void onError(String msg);
    }

    public interface ScoreCallback{
        public void onCompleted(long score);
        public void onError(String msg);
    }


}
