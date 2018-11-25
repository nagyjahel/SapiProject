package com.example.nagyjahel.sapiads.Main;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.nagyjahel.sapiads.Main.AdListFragment.OnListFragmentInteractionListener;
import com.example.nagyjahel.sapiads.Main.DummyData.DummyContent.DummyItem;
import com.example.nagyjahel.sapiads.R;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class AdRecyclerViewAdapter extends RecyclerView.Adapter<AdRecyclerViewAdapter.ViewHolder> {

    private final List<DummyItem> mValues;
    private final OnListFragmentInteractionListener mListener;

    public AdRecyclerViewAdapter(List<DummyItem> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(mValues.get(position).id);
        holder.mContentView.setText(mValues.get(position).content);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
<<<<<<< HEAD
            public void onClick(View view) {
                AdDetailFragment detailFragment = new AdDetailFragment();
                Bundle fragmentArgs = new Bundle();
                fragmentArgs.putInt("adId",currentAd.getmId());
                detailFragment.setArguments(fragmentArgs);
                FragmentTransaction ft = mFragmentActivity.getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.ad_list_fragment, detailFragment);
                ft.commit();
=======
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
>>>>>>> 7e00523b6ba2aba9bbe7f18942de49544600c8dc
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
        public DummyItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.item_number);
            mContentView = (TextView) view.findViewById(R.id.content);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
