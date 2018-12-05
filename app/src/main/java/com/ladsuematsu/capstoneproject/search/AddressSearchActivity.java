package com.ladsuematsu.capstoneproject.search;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;

import com.ladsuematsu.capstoneproject.R;

public class AddressSearchActivity extends AppCompatActivity {

    private static final String TAG = AddressSearchActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_search);

        processQuery(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        processQuery(intent);
    }

    private void processQuery(Intent intent) {
        String action = intent.getAction();
        if (!TextUtils.isEmpty(action) && action.equals(Intent.ACTION_SEARCH)) {
            if (intent.hasExtra(SearchManager.QUERY)) {
                String searchQuery = intent.getStringExtra(SearchManager.QUERY);

                Log.v(TAG, "Search query: " + searchQuery);
            }
        } else {
            super.onNewIntent(intent);
        }
    }
}
