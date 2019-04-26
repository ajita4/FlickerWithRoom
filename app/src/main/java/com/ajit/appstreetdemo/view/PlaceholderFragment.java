package com.ajit.appstreetdemo.view;

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
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    public static String TRANSITION_VIEW_LOCAL_ID;
    public static final String IMAGE_KEY = "image";
    @BindView(R.id.fullscreen_image)
    ImageView fullscreenImage;

    public PlaceholderFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static PlaceholderFragment newInstance(int imageId) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle args = new Bundle();
        args.putInt(IMAGE_KEY, imageId);
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
                loadItem(photosPhoto);
            }
        }.execute();

    }

    private void loadItem(FlickerPhotosPhoto photosPhoto) {
        if (photosPhoto.getLocalId() == Integer.parseInt(TRANSITION_VIEW_LOCAL_ID)) {
            ViewCompat.setTransitionName(fullscreenImage, TRANSITION_VIEW_LOCAL_ID);
            addTransitionListener(photosPhoto);
        }
        loadFullSizeImage(photosPhoto);
    }

    private void loadThumbnail(FlickerPhotosPhoto photosPhoto) {
        if (getActivity() == null) {
            return;
        }
        Glide.with(getActivity())
                .load(Utility.getImageUrlFromIds(photosPhoto, ImageSize.IMAGE_SIZE_SMALL_SQUARE))
                .thumbnail(0.5f)
                .into(fullscreenImage);

    }

    /**
     * Load the item's full-size image into our {@link ImageView}.
     *
     * @param photosPhoto
     */
    private void loadFullSizeImage(FlickerPhotosPhoto photosPhoto) {
        if (getActivity() == null) {
            return;
        }
        Glide.with(getActivity())
                .load(Utility.getImageUrlFromIds(photosPhoto, ImageSize.IMAGE_SIZE_LARGE))
                .into(fullscreenImage);
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private boolean addTransitionListener(FlickerPhotosPhoto photosPhoto) {
        Transition transition = getActivity().getWindow().getSharedElementEnterTransition();

        if (transition != null) {
            // There is an entering shared element transition so add a listener to it
            transition.addListener(new Transition.TransitionListener() {
                @Override
                public void onTransitionEnd(Transition transition) {
                    loadThumbnail(photosPhoto);
                    transition.removeListener(this);
                }

                @Override
                public void onTransitionStart(Transition transition) {
                    // No-op
                }

                @Override
                public void onTransitionCancel(Transition transition) {
                    // Make sure we remove ourselves as a listener
                    transition.removeListener(this);
                }

                @Override
                public void onTransitionPause(Transition transition) {
                    // No-op
                }

                @Override
                public void onTransitionResume(Transition transition) {
                    // No-op
                }
            });
            return true;
        }

        // If we reach here then we have not added a listener
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}