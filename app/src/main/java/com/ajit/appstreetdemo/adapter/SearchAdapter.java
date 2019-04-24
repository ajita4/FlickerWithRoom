package com.ajit.appstreetdemo.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ajit.appstreetdemo.R;
import com.ajit.appstreetdemo.data.ImageItem;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.Holder> {

    public interface Listener {
        void onItemClicked(ImageItem setting);
    }

    private final Listener listener;

    private ImageItem imageItem;
    List<ImageItem> imageItemList;

    public SearchAdapter(Listener listener) {
        this.listener = listener;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int i) {
        return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.image_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(Holder holder, int i) {
        ImageItem imageItem = imageItemList.get(i);
        holder.imageView.setOnClickListener(v -> listener.onItemClicked(imageItem));

    }

    @Override
    public int getItemCount() {
        if (imageItemList != null) {
            return imageItemList.size();
        }
        return 0;
    }

    public void setItems(List<ImageItem> items) {
        imageItemList = items;
    }

    public void setImageItem(ImageItem setting) {
        imageItem = setting;
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
