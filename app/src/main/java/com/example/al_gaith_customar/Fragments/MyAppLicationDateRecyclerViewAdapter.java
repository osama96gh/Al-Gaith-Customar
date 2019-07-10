package com.example.al_gaith_customar.Fragments;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.al_gaith_customar.Data.ApplicationDate;
import com.example.al_gaith_customar.Fragments.AppLicationDateFragment.OnListFragmentInteractionListener;
import com.example.al_gaith_customar.Fragments.dummy.DummyContent.DummyItem;
import com.example.al_gaith_customar.R;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyAppLicationDateRecyclerViewAdapter extends RecyclerView.Adapter<MyAppLicationDateRecyclerViewAdapter.ViewHolder> {

    private final List<ApplicationDate> mValues;
    private final OnListFragmentInteractionListener mListener;

    public MyAppLicationDateRecyclerViewAdapter(ArrayList<ApplicationDate> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_applicationdate, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(mValues.get(position).app_name);
        holder.mContentView.setText(mValues.get(position).review_date);
        holder.haveReplyLL.setVisibility(mValues.get(position).isDateHaveReply() ? View.VISIBLE : View.GONE);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public final LinearLayout haveReplyLL;
        public ApplicationDate mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.item_number);
            mContentView = (TextView) view.findViewById(R.id.content);
            haveReplyLL = view.findViewById(R.id.date_have_reply_ll);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
