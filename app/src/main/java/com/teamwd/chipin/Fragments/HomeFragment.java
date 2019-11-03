package com.teamwd.chipin.Fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.teamwd.chipin.Activities.ActivityDatabaseTest;
import com.teamwd.chipin.Activities.ActivityDonate;
import com.teamwd.chipin.Interfaces.Interfaces;
import com.teamwd.chipin.Models.Donation;
import com.teamwd.chipin.Models.Event;
import com.teamwd.chipin.R;
import com.teamwd.chipin.Utils.UserDataProvider;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class HomeFragment extends ChipFragment{

    private RecyclerView mainRecyclerView;
    private RecyclerView eventRecyclerView;
    private UserDataProvider userDataProvider;
    private ArrayList<Event> eventsList = new ArrayList<>();
    private ArrayList<Donation> donationsList = new ArrayList<>();
    private ArrayList<Event> bestFiveEvents = new ArrayList<>();
    private Context context;
    private FloatingActionButton floatingActionButton;
    private RelativeLayout progressLayout;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerAdapter adapter;
    private RecyclerAdapter2 adapter2;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.context = getContext();

        root = inflater.inflate(R.layout.fragment_home, container, false);
        mainRecyclerView = root.findViewById(R.id.recycler_home_view);
        eventRecyclerView = root.findViewById(R.id.recycler_home_featured);
        floatingActionButton = root.findViewById(R.id.fab);
        progressLayout = root.findViewById(R.id.progress_layout);
        swipeRefreshLayout = root.findViewById(R.id.home_swipe);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                updateViews();
            }
        });
        userDataProvider = UserDataProvider.getInstance(getContext());
        setUpRecyclerViews();
        setUpFab();
        return root;
    }

    private void setUpFab() {
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ActivityDonate.class);
                startActivity(intent);
            }
        });
    }

    public void updateViews() {
        userDataProvider.getAllDonations(new Interfaces.DonationsListCallback() {
            @Override
            public void onCompleted(ArrayList<Donation> donations) {
                donationsList = donations;
                adapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onError(String msg) {
                progressLayout.setVisibility(View.GONE);
                onError("Error loading donations.");
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    public void setUpRecyclerViews(){
        userDataProvider.getAllDonations(new Interfaces.DonationsListCallback() {
            @Override
            public void onCompleted(ArrayList<Donation> donations) {
                donationsList = donations;
                Collections.sort(donationsList, new Comparator<Donation>() {
                    @Override
                    public int compare(Donation donation, Donation t1) {
                        return Long.compare(t1.getTimeInMillis(),donation.getTimeInMillis());
                    }
                });


                mainRecyclerView.setLayoutManager(new LinearLayoutManager(context));
                adapter = new RecyclerAdapter();
                mainRecyclerView.setAdapter(adapter);
                progressLayout.setVisibility(View.GONE);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(String msg) {
                progressLayout.setVisibility(View.GONE);
                onError("Error loading donations.");
            }
        });
        userDataProvider.getAllEvents(new Interfaces.EventsCallback() {
            @Override
            public void onCompleted(ArrayList<Event> events) {
                eventsList = events;
                bestFiveEvents = new ArrayList<>();
                if (eventsList.size() > 5) {
                    while (bestFiveEvents.size() < 5) {
                        int rand = ThreadLocalRandom.current().nextInt(0, eventsList.size());
                        if (!bestFiveEvents.contains(eventsList.get(rand))) {
                            bestFiveEvents.add(eventsList.get(rand));
                        }
                    }
                } else {
                    if (eventsList.isEmpty()) {
                        return;
                    } else {
                        bestFiveEvents.addAll(eventsList);
                    }
                }
                if (!eventsList.isEmpty()) {
                    eventRecyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
                    SnapHelper snapHelper = new PagerSnapHelper();
                    snapHelper.attachToRecyclerView(eventRecyclerView);
                    eventRecyclerView.setOnFlingListener(null);
                    adapter2 = new RecyclerAdapter2();
                    eventRecyclerView.setAdapter(adapter2);
                    adapter2.notifyDataSetChanged();
                    eventRecyclerView.addItemDecoration(new LinePagerIndicatorDecoration());
                }
            }

            @Override
            public void onError(String msg) {
                onError("Error loading events.");
            }
        });
    }

    public class LinePagerIndicatorDecoration extends RecyclerView.ItemDecoration {

        private int colorActive = 0xFFFFFFFF;
        private int colorInactive = 0x66FFFFFF;

        private final float DP = context.getResources().getSystem().getDisplayMetrics().density;

        /**
         * Height of the space the indicator takes up at the bottom of the view.
         */
        private final int mIndicatorHeight = (int) (DP * 16);

        /**
         * Indicator stroke width.
         */
        private final float mIndicatorStrokeWidth = DP * 2;

        /**
         * Indicator width.
         */
        private final float mIndicatorItemLength = DP * 16;
        /**
         * Padding between indicators.
         */
        private final float mIndicatorItemPadding = DP * 4;

        /**
         * Some more natural animation interpolation
         */
        private final Interpolator mInterpolator = new AccelerateDecelerateInterpolator();

        private final Paint mPaint = new Paint();

        public LinePagerIndicatorDecoration() {
            mPaint.setStrokeCap(Paint.Cap.ROUND);
            mPaint.setStrokeWidth(mIndicatorStrokeWidth);
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setAntiAlias(true);
        }

        @Override
        public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
            super.onDrawOver(c, parent, state);

            int itemCount = parent.getAdapter().getItemCount();

            // center horizontally, calculate width and subtract half from center
            float totalLength = mIndicatorItemLength * itemCount;
            float paddingBetweenItems = Math.max(0, itemCount - 1) * mIndicatorItemPadding;
            float indicatorTotalWidth = totalLength + paddingBetweenItems;
            float indicatorStartX = (parent.getWidth() - indicatorTotalWidth) / 2F;

            // center vertically in the allotted space
            float indicatorPosY = parent.getHeight() - mIndicatorHeight / 2F;

            drawInactiveIndicators(c, indicatorStartX, indicatorPosY, itemCount);


            // find active page (which should be highlighted)
            LinearLayoutManager layoutManager = (LinearLayoutManager) parent.getLayoutManager();
            int activePosition = layoutManager.findFirstVisibleItemPosition();
            if (activePosition == RecyclerView.NO_POSITION) {
                return;
            }

            // find offset of active page (if the user is scrolling)
            final View activeChild = layoutManager.findViewByPosition(activePosition);
            int left = activeChild.getLeft();
            int width = activeChild.getWidth();

            // on swipe the active item will be positioned from [-width, 0]
            // interpolate offset for smooth animation
            float progress = mInterpolator.getInterpolation(left * -1 / (float) width);

            drawHighlights(c, indicatorStartX, indicatorPosY, activePosition, progress, itemCount);
        }

        private void drawInactiveIndicators(Canvas c, float indicatorStartX, float indicatorPosY, int itemCount) {
            mPaint.setColor(colorInactive);

            // width of item indicator including padding
            final float itemWidth = mIndicatorItemLength + mIndicatorItemPadding;

            float start = indicatorStartX;
            for (int i = 0; i < itemCount; i++) {
                // draw the line for every item
                c.drawLine(start, indicatorPosY, start + mIndicatorItemLength, indicatorPosY, mPaint);
                start += itemWidth;
            }
        }

        private void drawHighlights(Canvas c, float indicatorStartX, float indicatorPosY,
                                    int highlightPosition, float progress, int itemCount) {
            mPaint.setColor(colorActive);

            // width of item indicator including padding
            final float itemWidth = mIndicatorItemLength + mIndicatorItemPadding;

            if (progress == 0F) {
                // no swipe, draw a normal indicator
                float highlightStart = indicatorStartX + itemWidth * highlightPosition;
                c.drawLine(highlightStart, indicatorPosY,
                        highlightStart + mIndicatorItemLength, indicatorPosY, mPaint);
            } else {
                float highlightStart = indicatorStartX + itemWidth * highlightPosition;
                // calculate partial highlight
                float partialLength = mIndicatorItemLength * progress;

                // draw the cut off highlight
                c.drawLine(highlightStart + partialLength, indicatorPosY,
                        highlightStart + mIndicatorItemLength, indicatorPosY, mPaint);

                // draw the highlight overlapping to the next item as well
                if (highlightPosition < itemCount - 1) {
                    highlightStart += itemWidth;
                    c.drawLine(highlightStart, indicatorPosY,
                            highlightStart + partialLength, indicatorPosY, mPaint);
                }
            }
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            outRect.bottom = mIndicatorHeight;
        }
    }

    class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

        // data is passed into the constructor
        RecyclerAdapter() {

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
            final Donation donation = donationsList.get(position);
            holder.title.setText(donation.getDonationTitle());
            Calendar cal = Calendar.getInstance();
            DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss aaa");
            cal.setTimeInMillis(donation.getTimeInMillis());
            holder.time.setText(df.format(cal.getTime()));
            holder.comment.setText(donation.getUserComment());
            NumberFormat formatter = NumberFormat.getCurrencyInstance();
            holder.donation.setText(formatter.format(donation.getAmount()));

            holder.shareImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String donationText = holder.title.getText() + "\n";
                    donationText += holder.time.getText()+ "\n\n";
                    donationText += holder.comment.getText()+ "\n\n";
                    donationText += holder.donation.getText();

                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, donationText);
                    sendIntent.setType("text/plain");
                    startActivity(Intent.createChooser(sendIntent, "Share"));
                }
            });
        }

        // total number of rows
        @Override
        public int getItemCount() {
            return donationsList.size();
        }


        // stores and recycles views as they are scrolled off screen
        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            TextView title;
            TextView time;
            TextView comment;
            TextView donation;
            View shareImage;

        ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.donation_name);
            time = itemView.findViewById(R.id.donation_time_stamp);
            comment = itemView.findViewById(R.id.donation_comment);
            donation = itemView.findViewById(R.id.donation_amount);
            shareImage = itemView.findViewById(R.id.wrapper_share_img);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Donation donation = getItem(getAdapterPosition());
            //showDetailView(result);
        }
    }

    // convenience method for getting data at click position
    Donation getItem(int id) {
        return donationsList.get(id);
    }
}
    class RecyclerAdapter2 extends RecyclerView.Adapter<RecyclerAdapter2.ViewHolder2> {

        // data is passed into the constructor
        RecyclerAdapter2() {

        }

        // inflates the row layout from xml when needed
        @Override
        public ViewHolder2 onCreateViewHolder(ViewGroup parent, final int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.view_home_featured_item, parent, false);
            return new ViewHolder2(view);
        }

        // binds the data to the TextView in each row
        @Override
        public void onBindViewHolder(final ViewHolder2 holder, int position) {
            final Event event = bestFiveEvents.get(position);
            holder.eventTitle.setText(event.getEvenTitle());
            holder.eventDescription.setText(event.getEventDetails());
            ArrayList<Long> values = new ArrayList<>();
            long l = event.getEndTime() - System.currentTimeMillis();
            long days = TimeUnit.MILLISECONDS.toDays(l);
            long years = days / 365;
            days %= 365;
            long months = days / 30;
            days %= 30;
            long weeks = days / 7;
            days %= 7;
            long hours = TimeUnit.MILLISECONDS.toHours(l) - TimeUnit.DAYS.toHours(TimeUnit.MILLISECONDS.toDays(l));
            long minutes = TimeUnit.MILLISECONDS.toMinutes(l) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(l));
            ArrayList<Object> stringValues = new ArrayList<>();
            stringValues.add(years + "Y");
            values.add(years);
            stringValues.add(months + "M");
            values.add(months);
            stringValues.add(weeks + "w");
            values.add(weeks);
            stringValues.add(days + "d");
            values.add(days);
            stringValues.add(hours + "h");
            values.add(hours);
            stringValues.add(minutes + "m");
            values.add(minutes);
            String time = "";
            for (int i = 0; i < values.size(); i++) {
                if (values.get(i) != 0) {
                    time += stringValues.get(i) + " ";
                }
            }
            holder.countdown.setText(time.trim());
        }

        // total number of rows
        @Override
        public int getItemCount() {
            return bestFiveEvents.size();
        }

        public class ViewHolder2 extends RecyclerView.ViewHolder implements View.OnClickListener {
            ImageView companyImg;
            TextView countdown;
            TextView eventTitle;
            TextView eventDescription;

            ViewHolder2(View itemView) {
                super(itemView);
                companyImg = itemView.findViewById(R.id.company_feature_img);
                countdown = itemView.findViewById(R.id.event_countdown);
                eventTitle = itemView.findViewById(R.id.featured_event_title);
                eventDescription = itemView.findViewById(R.id.featured_event_description);
                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ActivityDonate.class);
                intent.putExtra("event", getItem(getAdapterPosition()));
                startActivity(intent);
            }
        }

        // convenience method for getting data at click position
        Event getItem(int id) {
            return bestFiveEvents.get(id);
        }
    }
}