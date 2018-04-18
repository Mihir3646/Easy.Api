package com.hlabexamples.easyapi.kotlin.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity

import com.hlabexmaples.easyapi.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        replace(R.id.container, MethodSelectionFragment())
    }

    /**
     * Replaces the Fragment into layout container.
     *
     * @param container    Resource id of the layout in which Fragment will be added
     * @param nextFragment New Fragment to be loaded into container
     */
    private fun replace(container: Int, nextFragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(container, nextFragment, nextFragment.javaClass.simpleName).commit()
    }

}
