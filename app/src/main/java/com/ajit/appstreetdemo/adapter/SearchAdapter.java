package com.ajit.appstreetdemo.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ajit.appstreetdemo.R;
import com.ajit.appstreetdemo.data.models.FlickerPhotosPhoto;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.Holder> {

    public interface Listener {
        void onItemClicked(FlickerPhotosPhoto setting);
    }

    private final Listener listener;

    List<FlickerPhotosPhoto> photoList;

    public SearchAdapter(Listener listener) {
        this.listener = listener;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int i) {
        return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.image_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(Holder holder, int i) {
        FlickerPhotosPhoto imageItem = photoList.get(i);
        holder.imageView.setOnClickListener(v -> listener.onItemClicked(imageItem));

    }

    @Override
    public int getItemCount() {
        if (photoList != null) {
            return photoList.size();
        }
        return 0;
    }

    public void setItems(List<FlickerPhotosPhoto> items) {
        photoList = items;
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
