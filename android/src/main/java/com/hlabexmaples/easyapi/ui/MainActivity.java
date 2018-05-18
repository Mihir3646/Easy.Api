package com.hlabexmaples.easyapi.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.hlabexmaples.easyapi.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        replace(R.id.container, new MethodSelectionFragment());

    }

    /**
     * Replaces the Fragment into layout container.
     *
     * @param container    Resource id of the layout in which Fragment will be added
     * @param nextFragment New Fragment to be loaded into container
     */
    protected void replace(final int container, final Fragment nextFragment) {
        getSupportFragmentManager().beginTransaction().replace(container, nextFragment, nextFragment.getClass().getSimpleName()).commit();
    }

}
