package com.teamwd.chipin.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.teamwd.chipin.Interfaces.Interfaces;
import com.teamwd.chipin.Models.Donation;
import com.teamwd.chipin.Models.ModelUser;
import com.teamwd.chipin.R;
import com.teamwd.chipin.Utils.UserDataProvider;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;

import static com.teamwd.chipin.Utils.SharedPrefsUtil.PREF_USER_EMAIL;
import static com.teamwd.chipin.Utils.SharedPrefsUtil.getSharedPrefs;

public class UserFragment extends ChipFragment{

    private TextView noDonationsView;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private UserDataProvider dataProvider;
    private ImageView addImg;
    private ImageView profileImg;
    private LinearLayout layoutImg;
    private boolean isProfileSet = false;
    private ModelUser user;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.context = getContext();

        root = inflater.inflate(R.layout.fragment_user, container, false);
        noDonationsView = root.findViewById(R.id.no_donations_text);
        recyclerView = root.findViewById(R.id.rv);
        swipeRefreshLayout = root.findViewById(R.id.user_swipe);
        addImg = root.findViewById(R.id.add_img);
        profileImg = root.findViewById(R.id.profile_img);
        layoutImg = root.findViewById(R.id.img_layout);

        handleImgs();

        SharedPreferences sharedPrefs = getSharedPrefs(context);
        final String email = sharedPrefs.getString(PREF_USER_EMAIL, "");
        dataProvider = UserDataProvider.getInstance(root.getContext());
        dataProvider.getDonations(email, new Interfaces.DonationsCallback() {
            @Override
            public void onCompleted(ArrayList<Donation> donations) {
                setAdapter(donations);
                if (donations.isEmpty()) {
                    noDonationsView.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                } else {
                    noDonationsView.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onError(String msg) {

            }
        });
        dataProvider.getUser(email, new Interfaces.UserCallback() {
            @Override
            public void onCompleted(ModelUser user) {
                buildViews(user);
            }

            @Override
            public void onError(String msg) {

            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                dataProvider.getDonations(email, new Interfaces.DonationsCallback() {
                    @Override
                    public void onCompleted(ArrayList<Donation> donations) {
                        setAdapter(donations);
                        if (donations.isEmpty()) {
                            noDonationsView.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                        } else {
                            noDonationsView.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                        }
                        swipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onError(String msg) {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
                dataProvider.getUser(email, new Interfaces.UserCallback() {
                    @Override
                    public void onCompleted(ModelUser user) {
                        buildViews(user);
                        swipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onError(String msg) {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
            }
        });

        return root;
    }

    public static final int GALLERY_REQUEST_CODE = 1;
    private void handleImgs() {

        layoutImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(isProfileSet)
                    return;

                pickFromGallery();
/*                Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
                getIntent.setType("image/*");

                Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                pickIntent.setType("image/*");

                Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});

                startActivityForResult(chooserIntent, PICK_IMAGE);*/
            }
        });
    }

    private void pickFromGallery(){
        //Create an Intent with action as ACTION_PICK
        Intent intent=new Intent(Intent.ACTION_PICK);
        // Sets the type as image/*. This ensures only components of type image are selected
        intent.setType("image/*");
        //We pass an extra array with the accepted mime types. This will ensure only components with these MIME types as targeted.
        String[] mimeTypes = {"image/jpeg", "image/png"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES,mimeTypes);
        // Launching the Intent
        startActivityForResult(intent,GALLERY_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        // Result code is RESULT_OK only if the user selects an Image
        if (resultCode == Activity.RESULT_OK)
            switch (requestCode) {
                case GALLERY_REQUEST_CODE:

                    Uri selectedImage = data.getData();
                    String[] filePathColumn = { MediaStore.Images.Media.DATA };

                    Cursor cursor = root.getContext().getContentResolver().query(selectedImage,
                            filePathColumn, null, null, null);
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String picturePath = cursor.getString(columnIndex);
                    cursor.close();

                    Bitmap bitmap = BitmapFactory.decodeFile(picturePath);
                    profileImg.setImageBitmap(bitmap);

                    break;
            }


    }

    private void buildViews(ModelUser user) {
        this.user = user;
        TextView name= root.findViewById(R.id.user_name);

        name.setText(user.getFirstName().substring(0, 1).toUpperCase() + user.getFirstName().substring(1) + " " + user.getLastName().substring(0, 1).toUpperCase() + user.getLastName().substring(1));

        TextView xpTV= root.findViewById(R.id.xpTV);
        xpTV.setText(user.getScore()+" points");


    }

    private void setAdapter(ArrayList<Donation> donations) {

        Collections.sort(donations, new Comparator<Donation>() {
            @Override
            public int compare(Donation donation, Donation t1) {
                return Long.compare(donation.getTimeInMillis(), t1.getTimeInMillis());
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(root.getContext()));
        UserDonationsAdapter adapter = new UserDonationsAdapter(donations);
        recyclerView.setAdapter(adapter);

    }


    public class UserDonationsAdapter extends RecyclerView.Adapter<UserDonationsAdapter.ViewHolder> {

        ArrayList<Donation> donations;

        UserDonationsAdapter(ArrayList<Donation> donations) {
            this.donations = donations;
        }

        // inflates the row layout from xml when needed
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.view_item_donation, parent, false);
            return new ViewHolder(view);
        }

        // binds the data to the TextView in each row
        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            final Donation donation = donations.get(position);
            holder.title.setText(donation.getDonationTitle());
            Calendar cal = Calendar.getInstance();
            DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss aaa");
            cal.setTimeInMillis(donation.getTimeInMillis());
            holder.time.setText(df.format(cal.getTime()));
            holder.comment.setText(donation.getUserComment());
            NumberFormat formatter = NumberFormat.getCurrencyInstance();
            holder.donation.setText(formatter.format(donation.getAmount()));
        }

        // total number of rows
        @Override
        public int getItemCount() {
            return donations.size();
        }


        // stores and recycles views as they are scrolled off screen
        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            TextView title;
            TextView time;
            TextView comment;
            TextView donation;

            ViewHolder(View itemView) {
                super(itemView);
                title = itemView.findViewById(R.id.donation_name);
                time = itemView.findViewById(R.id.donation_time_stamp);
                comment = itemView.findViewById(R.id.donation_comment);
                donation = itemView.findViewById(R.id.donation_amount);
                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {
            }
        }
    }
}
