package com.teamwd.chipin.Fragments;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.teamwd.chipin.Interfaces.Interfaces;
import com.teamwd.chipin.Models.Donation;
import com.teamwd.chipin.Models.Event;
import com.teamwd.chipin.R;
import com.teamwd.chipin.Utils.UserDataProvider;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class HomeFragment extends ChipFragment{

    private RecyclerView mainRecyclerView;
    private RecyclerView eventRecyclerView;
    private UserDataProvider userDataProvider;
    private ArrayList<Event> eventsList = new ArrayList<>();
    private ArrayList<Donation> donationsList = new ArrayList<>();
    private ArrayList<Event> bestFiveEvents = new ArrayList<>();
    private Context context;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.context = getContext();

        root = inflater.inflate(R.layout.fragment_home, container, false);
        mainRecyclerView = root.findViewById(R.id.recycler_home_view);
        eventRecyclerView = root.findViewById(R.id.recycler_home_featured);
        userDataProvider = UserDataProvider.getInstance(getContext());
        setUpRecyclerViews();
        return root;
    }

    public void setUpRecyclerViews(){
        userDataProvider.getAllDonations(new Interfaces.DonationsListCallback() {
            @Override
            public void onCompleted(ArrayList<Donation> donations) {
                donationsList = donations;
                mainRecyclerView.setLayoutManager(new LinearLayoutManager(context));
                mainRecyclerView.setAdapter(new RecyclerAdapter());
            }

            @Override
            public void onError(String msg) {
                onError("Error loading donations.");
            }
        });
        userDataProvider.getAllEvents(new Interfaces.EventsCallback() {
            @Override
            public void onCompleted(ArrayList<Event> events) {
                eventsList = events;
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
                    eventRecyclerView.setAdapter(new RecyclerAdapter2());
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
            holder.title.setText(donation.getCharityName());
            holder.time.setText(donation.getCharityName());
            holder.comment.setText(donation.getCharityName());
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

        ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.donation_name);
            time = itemView.findViewById(R.id.donation_time_stamp);
            comment = itemView.findViewById(R.id.donation_comment);
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
            }

            @Override
            public void onClick(View view) {
                Event event = getItem(getAdapterPosition());
                //showDetailView(result);
            }
        }

        // convenience method for getting data at click position
        Event getItem(int id) {
            return bestFiveEvents.get(id);
        }
    }
}