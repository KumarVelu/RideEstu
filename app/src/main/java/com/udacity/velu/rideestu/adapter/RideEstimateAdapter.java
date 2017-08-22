package com.udacity.velu.rideestu.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.udacity.velu.rideestu.R;

/**
 * Created by Velu on 21/08/17.
 */

public class RideEstimateAdapter extends RecyclerView.Adapter<RideEstimateAdapter.RideEstimateViewHolder> {

    private static final String TAG = RideEstimateAdapter.class.getSimpleName();
    private Cursor mCursor;
    private Context mContext;
    private LayoutInflater mInflater;

    public RideEstimateAdapter(Context context, Cursor mCursor) {
        this.mCursor = mCursor;
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public RideEstimateViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rowItem = mInflater.inflate(R.layout.row_item_estimation, parent, false);
        return new RideEstimateViewHolder(rowItem);
    }

    @Override
    public void onBindViewHolder(RideEstimateViewHolder holder, int position) {
        mCursor.moveToPosition(position);
        Log.i(TAG, "onBindViewHolder: " + mCursor.getString(0));
        holder.displayName.setText(mCursor.getString(0));
        holder.rideCost.setText(mCursor.getString(1));
    }

    @Override
    public int getItemCount() {
        return mCursor == null ? 0 : mCursor.getCount();
    }

    public void swapData(Cursor cursor){
        mCursor = cursor;
        notifyDataSetChanged();
    }

    class RideEstimateViewHolder extends RecyclerView.ViewHolder{

        TextView displayName;
        TextView rideCost;

        public RideEstimateViewHolder(View itemView) {
            super(itemView);
            displayName = (TextView) itemView.findViewById(R.id.display_name);
            rideCost = (TextView) itemView.findViewById(R.id.cost);
        }
    }

}
