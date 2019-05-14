package com.example.al_gaith_customar.Fragment;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.al_gaith_customar.Data.Application;
import com.example.al_gaith_customar.Fragment.ApplicationFragment.OnListFragmentInteractionListener;
import com.example.al_gaith_customar.R;

import java.util.ArrayList;

/**
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyApplicationRecyclerViewAdapter extends RecyclerView.Adapter<MyApplicationRecyclerViewAdapter.ViewHolder> {

    private final ArrayList<Application> mValues;
    private final OnListFragmentInteractionListener mListener;

    public MyApplicationRecyclerViewAdapter(ArrayList<Application> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_application, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mDateView.setText("" + mValues.get(position).created_at);
        holder.mContentView.setText("" + mValues.get(position).app_name);
        holder.mStatusView.setText(mValues.get(position).status);
        holder.mHaveReplyView.setVisibility(mValues.get(position).isAppHaveReply() ? View.VISIBLE : View.GONE);

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
        public final TextView mDateView;
        public final TextView mContentView;
        public final TextView mStatusView;
        public final LinearLayout mHaveReplyView;
        public Application mItem;


        public ViewHolder(View view) {
            super(view);
            mView = view;
            mDateView = (TextView) view.findViewById(R.id.date);
            mContentView = (TextView) view.findViewById(R.id.content);
            mStatusView = view.findViewById(R.id.status);
            mHaveReplyView = view.findViewById(R.id.app_have_reply_ll);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "' ";
        }
    }
}
