package com.ajit.appstreetdemo.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
import com.ajit.appstreetdemo.util.Utility;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityOptionsCompat;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
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

    ActivityOptionsCompat activityOptions;

    private void openFullScreen(List<FlickerPhotosPhoto> photos, int position, View view) {
        PlaceholderFragment.TRANSITION_VIEW_LOCAL_ID = String.valueOf(photos.get(position).getLocalId());
        activityOptions = ActivityOptionsCompat
                .makeSceneTransitionAnimation(this,
                        view.findViewById(R.id.imageView),
                        PlaceholderFragment.TRANSITION_VIEW_LOCAL_ID);

        FullScreenActivity.start(this, photos, position, activityOptions);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_CODE_START_FULLSCREEN_ACTIVITY) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                int position = data.getIntExtra(Constants.KEY_SELECTED_POSITION, 0);
                gridLayoutManager.scrollToPositionWithOffset(position, 0);
            }
        }
    }
}
