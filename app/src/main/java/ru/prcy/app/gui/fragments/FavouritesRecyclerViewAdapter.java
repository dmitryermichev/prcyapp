package ru.prcy.app.gui.fragments;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import ru.prcy.app.R;
import ru.prcy.app.db.DomainData;
import ru.prcy.app.db.FavouriteDomainData;
import ru.prcy.app.gui.fragments.FavouritesFragment.OnListFragmentInteractionListener;


import java.util.List;

public class FavouritesRecyclerViewAdapter extends RecyclerView.Adapter<FavouritesRecyclerViewAdapter.ViewHolder> {

    private List<FavouriteDomainData> mValues;
    private final OnListFragmentInteractionListener mListener;
    private Context context;
    private boolean allowDelete;

    public FavouritesRecyclerViewAdapter(Context context, List<FavouriteDomainData> items, boolean allowDelete, OnListFragmentInteractionListener listener) {
        this.context = context;
        this.allowDelete = allowDelete;
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.favourites_fragment_item, parent, false);
        return new ViewHolder(view);
    }

    public void setValues(List<FavouriteDomainData> domains) {
        this.mValues = domains;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        FavouriteDomainData currentDomain = mValues.get(position);
        holder.mItem = mValues.get(position);

        if(currentDomain.getFavicon() != null && !currentDomain.getFavicon().isEmpty()) {
            try {
                Glide.with(context).load(currentDomain.getFavicon())
                        .apply(new RequestOptions().placeholder(R.drawable.ic_favicon).error(R.drawable.ic_favicon))
                        .into(holder.mIconView);
            } catch (Exception e) {
                holder.mIconView.setImageResource(R.drawable.ic_favicon_grey);
            }
        }

        if(!allowDelete)
            holder.mView.findViewById(R.id.deleteLayout).setVisibility(View.GONE);
        else
            holder.mView.findViewById(R.id.deleteLayout).setVisibility(View.VISIBLE);

        holder.mNameView.setText(mValues.get(position).getDomain());

        holder.mView.findViewById(R.id.favLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onDomainSelected(holder.mItem);
                }
            }
        });
        holder.mView.findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onDomainDeleted(holder.mItem.getDomain());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView mIconView;
        public final TextView mNameView;
        public FavouriteDomainData mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIconView = (ImageView) view.findViewById(R.id.icon);
            mNameView = (TextView) view.findViewById(R.id.name);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mNameView.getText() + "'";
        }
    }
}
