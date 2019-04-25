package com.ajit.appstreetdemo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ajit.appstreetdemo.Constants;
import com.ajit.appstreetdemo.R;
import com.ajit.appstreetdemo.data.models.FlickerPhotosPhoto;
import com.ajit.appstreetdemo.util.ImageSize;
import com.ajit.appstreetdemo.util.Utility;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.Holder> {

    public interface Listener {
        void onItemClicked(FlickerPhotosPhoto photo);
    }

    private final Listener listener;
    Utility utility;
    List<FlickerPhotosPhoto> photoList = new ArrayList<>();
    Context context;
    int itemSize;
    int spanCount = Constants.GRID_DEFAULT_SPAN_COUNT;

    public SearchAdapter(Context context, Listener listener) {
        this.listener = listener;
        utility = Utility.getInstance();
        this.context = context;
        setWidthOfImage(spanCount);
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int i) {
        return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.image_list_item, parent, false));
    }

    public void setWidthOfImage(int spanCount) {
        int deviceWidthInPx = utility.deviceWidthInPx(context);
        itemSize = deviceWidthInPx / spanCount;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(Holder holder, int i) {
        FlickerPhotosPhoto imageItem = photoList.get(i);
        holder.itemParent.getLayoutParams().width = itemSize;
        holder.itemParent.getLayoutParams().height = itemSize;
        Glide.with(context)
                .load(Utility.getImageUrlFromIds(imageItem, ImageSize.IMAGE_SIZE_SMALL_SQUARE))
                .thumbnail(0.2f)
                .override(itemSize, itemSize)
                .centerCrop()
                .into(holder.imageView);

        holder.imageView.setOnClickListener(v -> listener.onItemClicked(imageItem));

    }

    @Override
    public int getItemCount() {
        if (photoList != null) {
            return photoList.size();
        }
        return 0;
    }

    public void setItems(List<FlickerPhotosPhoto> newData) {
        this.photoList.addAll(newData);
        if (photoList.size() != newData.size()) {
            notifyItemRangeInserted(photoList.size() - newData.size(), newData.size());
        } else {
            notifyDataSetChanged();
        }
    }

    public void clear() {
        photoList.clear();
        notifyDataSetChanged();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //// ViewHolder
    ////////////////////////////////////////////////////////////////////////////////////////////////
    static class Holder extends RecyclerView.ViewHolder {

        @BindView(R.id.imageView)
        ImageView imageView;
        @BindView(R.id.item_parent)
        LinearLayout itemParent;

        Holder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
