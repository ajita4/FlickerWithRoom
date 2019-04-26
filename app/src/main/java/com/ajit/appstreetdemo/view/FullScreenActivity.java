package com.ajit.appstreetdemo.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ajit.appstreetdemo.Constants;
import com.ajit.appstreetdemo.R;
import com.ajit.appstreetdemo.data.database.FlickerRoomDatabase;
import com.ajit.appstreetdemo.data.database.PhotoDao;
import com.ajit.appstreetdemo.data.models.FlickerPhotosPhoto;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;

public class FullScreenActivity extends AppCompatActivity {

    @BindView(R.id.container)
    ViewPager container;
    @BindView(R.id.main_content)
    CoordinatorLayout mainContent;

    private SectionsPagerAdapter mSectionsPagerAdapter;
    PhotoDao photoDao;

    public static void start(Activity activity, List<FlickerPhotosPhoto> photos, int position, ActivityOptionsCompat activityOptionsCompat) {
        Intent intent = new Intent(activity.getApplicationContext(), FullScreenActivity.class);
        intent.putParcelableArrayListExtra(Constants.KEY_PHOTOS_LIST, (ArrayList<? extends Parcelable>) photos);
        intent.putExtra(Constants.KEY_SELECTED_POSITION, position);
        ActivityCompat.startActivityForResult(activity, intent, Constants.REQUEST_CODE_START_FULLSCREEN_ACTIVITY,activityOptionsCompat.toBundle());
    }

    /**
     * The {@link ViewPager} that will host the section contents.
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen);
        ButterKnife.bind(this);
        FlickerRoomDatabase flickerRoomDatabase = FlickerRoomDatabase.getDatabase(this);
        photoDao = flickerRoomDatabase.flickerDao();
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        container.setAdapter(mSectionsPagerAdapter);
        updatePagerData(getIntent().getIntExtra(Constants.KEY_SELECTED_POSITION, 0), getIntent().getParcelableArrayListExtra(Constants.KEY_PHOTOS_LIST));
    }

    /**
     * Used to Initialize pager data
     *
     * @param flickerPhotosPhotoList Items which contains images to show
     * @param position       Initial position of tab
     */

    private void updatePagerData(int position, List<FlickerPhotosPhoto> flickerPhotosPhotoList) {

        for (int i = 0; i < flickerPhotosPhotoList.size(); i++) {
            FlickerPhotosPhoto contentFolderListItem = flickerPhotosPhotoList.get(i);
            mSectionsPagerAdapter.addFrag(PlaceholderFragment.newInstance(contentFolderListItem.getLocalId()));
        }
        mSectionsPagerAdapter.notifyDataSetChanged();

        // Set selected tab if adapter is getting initialized for first time
        container.setCurrentItem(position , true);
        container.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                getIntent().putExtra(Constants.KEY_SELECTED_POSITION, position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentStatePagerAdapter {
        private final List<Fragment> fragmentList = new ArrayList<>();

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        public void addFrag(Fragment fragment) {
            fragmentList.add(fragment);
        }

        @Override
        public int getItemPosition(@NonNull Object object) {
            return POSITION_NONE;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            PlaceholderFragment seeMoreItemFragment = (PlaceholderFragment) super.instantiateItem(container, position);
            //Update the references to the Fragments we have on the view pager
            fragmentList.set(position, seeMoreItemFragment);
            return seeMoreItemFragment;
        }

        public void clearData() {
            fragmentList.clear();
            notifyDataSetChanged();
        }
    }
}
