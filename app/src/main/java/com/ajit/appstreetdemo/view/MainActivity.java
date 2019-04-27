package com.ajit.appstreetdemo.view;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ajit.appstreetdemo.Constants;
import com.ajit.appstreetdemo.R;
import com.ajit.appstreetdemo.adapter.SearchAdapter;
import com.ajit.appstreetdemo.data.DataEmitter;
import com.ajit.appstreetdemo.data.ImagesRequest;
import com.ajit.appstreetdemo.data.WebModel;
import com.ajit.appstreetdemo.data.models.Flicker;
import com.ajit.appstreetdemo.data.models.FlickerPhotosPhoto;
import com.ajit.appstreetdemo.util.MediaSharedElementCallback;
import com.ajit.appstreetdemo.util.TransitionCallback;
import com.ajit.appstreetdemo.util.Utility;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.app.SharedElementCallback;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.ajit.appstreetdemo.Constants.GRID_DEFAULT_SPAN_COUNT;

public class MainActivity extends AppCompatActivity implements DataEmitter {

    @BindView(R.id.toolbar_search_input)
    EditText toolbarSearchInput;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.search_recyclerview)
    RecyclerView searchRecyclerView;
    @BindView(R.id.search_emptyview)
    TextView searchEmptyview;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    private GridLayoutManager gridLayoutManager;
    private WebModel webModel;
    private SearchAdapter adapter;

    private ImagesRequest imagesRequest;
    RecyclerViewScrollListener recyclerViewScrollListener;
    Utility utility;

    Activity activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        activity = new Activity();
        webModel = new WebModel(this);
        utility = Utility.getInstance();
        init();
    }

    private void init() {

        // RecyclerView
        gridLayoutManager = new GridLayoutManager(this, GRID_DEFAULT_SPAN_COUNT);
        searchRecyclerView.setLayoutManager(gridLayoutManager);

        adapter = new SearchAdapter(this, this::openFullScreen);
        searchRecyclerView.setAdapter(adapter);
        adapter.setWidthOfImage(GRID_DEFAULT_SPAN_COUNT);
        // prevent recyclerView from not starting at top
        searchRecyclerView.setFocusable(false);
        searchEmptyview.setVisibility(View.GONE);
        showData();
        setupScrollListener(gridLayoutManager);
        setupTransition();
    }

    private void showData() {
        toolbarSearchInput.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                imagesRequest = new ImagesRequest();
                imagesRequest.setSearchText(toolbarSearchInput.getText().toString());
                imagesRequest.setPage(1);
                imagesRequest.setPerPage(30);
                imagesRequest.setLastId(0);
                adapter.clear();
                webModel.imageList(imagesRequest);
                progressBar.setVisibility(View.VISIBLE);
                if (recyclerViewScrollListener != null) {
                    recyclerViewScrollListener.resetState();
                }
            }
            return false;
        });

    }


    private void openFullScreen(List<FlickerPhotosPhoto> photos, int position, View view) {
        FullScreenActivity.start(this, photos, position,view);
    }

    private void setupScrollListener(GridLayoutManager gridLayoutManager) {
        searchRecyclerView.clearOnScrollListeners();
        recyclerViewScrollListener = new RecyclerViewScrollListener(gridLayoutManager, Constants.RECYCLER_VIEW_SCROLL_LISTENER_VISIBLE_THRESHOLD) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Paginate Vertically
                imagesRequest.setPage(imagesRequest.getPage() + 1);
                webModel.imageList(imagesRequest);
                if (Utility.isConnected()) {
                    progressBar.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onScrollStateIdle(RecyclerView recyclerView) {
                // DO NOTHING
            }
        };
        searchRecyclerView.addOnScrollListener(recyclerViewScrollListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_searching, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int spanCount = GRID_DEFAULT_SPAN_COUNT;// default value of spancount
        try {
            spanCount = Integer.parseInt(item.getTitle().toString());
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        updateGridLayout(spanCount);
        return false;
    }

    private void updateGridLayout(int spanCount) {
        gridLayoutManager.setSpanCount(spanCount);
        adapter.setWidthOfImage(spanCount);
    }

    @OnClick(R.id.toolbar_search_clear)
    public void onViewClicked() {
        toolbarSearchInput.setText(null);
        adapter.clear();
        showView(false);
        webModel.closeRequest();
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void setData(Flicker flicker) {
        this.runOnUiThread(() -> {
            if (flicker.getStat().equals(Constants.API_STATUS_OK)) {
                List<FlickerPhotosPhoto> itemList = flicker.getPhotos().getPhoto();
                if (itemList.size() > 0) {
                    adapter.setItems(itemList);
                    showView(true);
                    progressBar.setVisibility(View.GONE);
                } else {
                    showView(false);
                }
            } else {
                showView(false);
            }
        });
    }

    @Override
    public void setLastId(int lastId) {
        imagesRequest.setLastId(lastId);
    }

    @Override
    public void error() {
        showView(false);
        progressBar.setVisibility(View.GONE);
    }

    public void showView(boolean hasData) {
        searchEmptyview.setVisibility(hasData ? View.GONE : View.VISIBLE);
        searchRecyclerView.setVisibility(hasData ? View.VISIBLE : View.GONE);
    }

    private void setupTransition() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            TransitionInflater inflater = TransitionInflater.from(this);
            Transition exitTransition = inflater.inflateTransition(R.transition.gallery_exit);
            exitTransition.addListener(new TransitionCallback() {
                @Override
                public void onTransitionStart(Transition transition) {
                    super.onTransitionStart(transition);
                }
            });
            getWindow().setExitTransition(exitTransition);
            Transition reenterTransition = inflater.inflateTransition(R.transition.gallery_reenter);
            reenterTransition.addListener(new TransitionCallback() {
                @Override
                public void onTransitionEnd(Transition transition) {
                    super.onTransitionEnd(transition);
                }
            });
            getWindow().setReenterTransition(reenterTransition);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_CODE_START_FULLSCREEN_ACTIVITY) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                int position = data.getIntExtra(Constants.KEY_SELECTED_POSITION, 0);
                gridLayoutManager.scrollToPosition(position);
                final MediaSharedElementCallback sharedElementCallback = new MediaSharedElementCallback();
               setExitSharedElementCallback(sharedElementCallback);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    // Listener to reset shared element exit transition callbacks.
                   getWindow().getSharedElementExitTransition().addListener(new TransitionCallback() {
                        @Override
                        public void onTransitionEnd(Transition transition) {
                            removeCallback();
                        }

                        @Override
                        public void onTransitionCancel(Transition transition) {
                            removeCallback();
                        }

                        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                        private void removeCallback() {
                            if (activity != null) {
                               getWindow().getSharedElementExitTransition().removeListener(this);
                               setExitSharedElementCallback((SharedElementCallback) null);
                            }
                        }
                    });
                }

                //noinspection ConstantConditions
               supportPostponeEnterTransition();
                searchRecyclerView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        searchRecyclerView.getViewTreeObserver().removeOnPreDrawListener(this);

                        RecyclerView.ViewHolder holder = searchRecyclerView.findViewHolderForAdapterPosition(position);
                        if (holder instanceof SearchAdapter.Holder) {
                            SearchAdapter.Holder mediaViewHolder = (SearchAdapter.Holder) holder;
                            sharedElementCallback.setSharedElementViews(mediaViewHolder.imageView);
                        }

                       supportStartPostponedEnterTransition();

                        return true;
                    }
                });
            }
        }
    }
}
