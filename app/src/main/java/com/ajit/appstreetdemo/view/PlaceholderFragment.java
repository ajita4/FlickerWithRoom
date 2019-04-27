package com.ajit.appstreetdemo.view;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.transition.Transition;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ajit.appstreetdemo.R;
import com.ajit.appstreetdemo.data.database.FlickerRoomDatabase;
import com.ajit.appstreetdemo.data.database.PhotoDao;
import com.ajit.appstreetdemo.data.models.FlickerPhotosPhoto;
import com.ajit.appstreetdemo.util.ImageSize;
import com.ajit.appstreetdemo.util.Utility;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment {
    private static final String PAGER_POSITION = "pager_position";
    private static final String GRID_ITEM_POSITION = "grid_item_position";
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String IMAGE_KEY = "image";
    @BindView(R.id.imageView)
    ImageView fullscreenImage;
    private int gridItemSelectedPosition;

    public PlaceholderFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static PlaceholderFragment newInstance(int imageId, int position, int gridItemSelectedPosition) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle args = new Bundle();
        args.putInt(IMAGE_KEY, imageId);
        args.putInt(PAGER_POSITION, position);
        args.putInt(GRID_ITEM_POSITION, gridItemSelectedPosition);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_full_screen, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        int imageID = getArguments().getInt(IMAGE_KEY);
        gridItemSelectedPosition = getArguments().getInt(GRID_ITEM_POSITION);
        FlickerRoomDatabase flickerRoomDatabase = FlickerRoomDatabase.getDatabase(getActivity().getApplicationContext());
        PhotoDao photoDao = flickerRoomDatabase.flickerDao();
        new AsyncTask<Void, Void, Void>() {
            FlickerPhotosPhoto photosPhoto;

            @Override
            protected Void doInBackground(Void... voids) {
                photosPhoto = photoDao.getSignlePhotosPhoto(imageID);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                loadFullSizeImage(photosPhoto);
            }
        }.execute();

    }


    private void loadFullSizeImage(FlickerPhotosPhoto photosPhoto) {
        if (getActivity() == null) {
            return;
        }
        String imageTransitionName = String.valueOf(photosPhoto.getLocalId());
        ViewCompat.setTransitionName(fullscreenImage, imageTransitionName);

        RequestOptions options = new RequestOptions()
                .skipMemoryCache(true)
                .fitCenter();

        Glide.with(getActivity())
                .load(Utility.getImageUrlFromIds(photosPhoto, ImageSize.IMAGE_SIZE_LARGE))
                .apply(options)
                .listener(new ImageLoadingCallback(getArguments().getInt(PAGER_POSITION)))
                .into(fullscreenImage);

    }

    private class ImageLoadingCallback implements RequestListener<Drawable> {

        final int mPosition;

        ImageLoadingCallback(int position) {
            mPosition = position;
        }

        @Override
        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
            if (mPosition == gridItemSelectedPosition) {
                startPostponedEnterTransition();
            }
            return false;
        }

        @Override
        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
            if (mPosition == gridItemSelectedPosition) {
                startPostponedEnterTransition();
            }
            return false;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}