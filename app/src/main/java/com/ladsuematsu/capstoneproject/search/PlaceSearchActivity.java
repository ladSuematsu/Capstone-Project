package com.ladsuematsu.capstoneproject.search;

import android.app.SearchManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

import com.ladsuematsu.capstoneproject.R;

public class PlaceSearchActivity extends AppCompatActivity {

    private static final String TAG = PlaceSearchActivity.class.getSimpleName();

    private SearchView.OnQueryTextListener searchListener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String s) {
            return false;
        }

        @Override
        public boolean onQueryTextChange(String s) {
            return false;
        }
    };

    private String searchQuery;
    private SearchView menuSearchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_search);

        processQuery(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        if (!processQuery(intent)) {
            super.onNewIntent(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.place_search_menu, menu);
        MenuItem menuItem =  menu.findItem(R.id.app_bar_search);
        menuItem.expandActionView();
        menuSearchView = (SearchView) menuItem.getActionView();

    menuSearchView.setOnQueryTextListener(searchListener);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menuSearchView.setIconifiedByDefault(false);

        if (!TextUtils.isEmpty(searchQuery)) {
            menuSearchView.setQuery(searchQuery, true);
        }

        return super.onPrepareOptionsMenu(menu);
    }

    private boolean processQuery(Intent intent) {
        String action = intent.getAction();
        if (!TextUtils.isEmpty(action) && action.equals(Intent.ACTION_SEARCH)) {
            if (intent.hasExtra(SearchManager.QUERY)) {
                searchQuery = intent.getStringExtra(SearchManager.QUERY);

                Log.v(TAG, "Search query: " + searchQuery);

                return true;
            }
        }

        return false;
    }
}
