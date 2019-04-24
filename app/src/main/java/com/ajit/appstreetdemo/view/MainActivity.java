package com.ajit.appstreetdemo.view;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.ajit.appstreetdemo.Constants;
import com.ajit.appstreetdemo.R;
import com.ajit.appstreetdemo.adapter.SearchAdapter;
import com.ajit.appstreetdemo.data.ImageItem;
import com.ajit.appstreetdemo.data.WebModel;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.toolbar_search_input)
    EditText toolbarSearchInput;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.search_recyclerview)
    RecyclerView searchRecyclerView;
    @BindView(R.id.search_emptyview)
    TextView searchEmptyview;

    private GridLayoutManager gridLayoutManager;
    private WebModel webModel;
    private SearchAdapter adapter;
    private static final int GRID_DEFAULT_SPAN_COUNT = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        webModel = new WebModel();
        init();
    }

    private void init() {

        // RecyclerView
        gridLayoutManager = new GridLayoutManager(this, GRID_DEFAULT_SPAN_COUNT);
        searchRecyclerView.setLayoutManager(gridLayoutManager);

        adapter = new SearchAdapter(this::openFullScreen);
        searchRecyclerView.setAdapter(adapter);
        // prevent recyclerView from not starting at top
        searchRecyclerView.setFocusable(false);

        showData();
        setupScrollListener(gridLayoutManager);
    }

    private void showData() {
        toolbarSearchInput.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                List<ImageItem> itemList = webModel.getImageList(toolbarSearchInput.getText().toString());
                if (itemList.size() > 0) {
                    adapter.setItems(itemList);
                } else {
                    searchEmptyview.setVisibility(View.VISIBLE);
                    searchRecyclerView.setVisibility(View.GONE);
                }
            }
            return false;
        });

    }

    private void openFullScreen(ImageItem imageItem) {

    }

    private void setupScrollListener(GridLayoutManager gridLayoutManager) {
        searchRecyclerView.clearOnScrollListeners();
        searchRecyclerView.addOnScrollListener(new RecyclerViewScrollListener(gridLayoutManager, Constants.RECYCLER_VIEW_SCROLL_LISTENER_VISIBLE_THRESHOLD) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Paginate Vertically

            }

            @Override
            public void onScrollStateIdle(RecyclerView recyclerView) {
                // DO NOTHING
            }
        });
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
        adapter.notifyDataSetChanged();
    }

    @OnClick(R.id.toolbar_search_clear)
    public void onViewClicked() {
        toolbarSearchInput.setText(null);
    }
}
