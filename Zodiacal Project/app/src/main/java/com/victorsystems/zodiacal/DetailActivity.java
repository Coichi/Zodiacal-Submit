package com.victorsystems.zodiacal;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;

import com.victorsystems.zodiacal.fragments.DetailFragment;

public class DetailActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        if (savedInstanceState == null) {
            Bundle arguments = new Bundle();
            arguments.putParcelable(DetailFragment.DETAIL_URI, getIntent().getData());

            DetailFragment fragment = new DetailFragment();
            fragment.setArguments(arguments);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.signos_detail_container, fragment)
                    .commit();
        }
    }
}
