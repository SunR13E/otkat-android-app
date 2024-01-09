package com.otkat_android_app.ui.presentation.activity

import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.navigation.createGraph
import androidx.navigation.findNavController
import androidx.navigation.fragment.fragment
import com.otkat_android_app.R
import com.otkat_android_app.ui.presentation.fragment.MainFragment

class MainActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        initNavController()
    }

    private fun initNavController() {
        val navController = findNavController(R.id.fragment_container)

        navController.graph = navController.createGraph(
            startDestination = MainFragment.NAV_GRAPH_NAME
        ) {
            fragment<MainFragment>(MainFragment.NAV_GRAPH_NAME) {
                label = MainFragment.NAV_GRAPH_LABEL
            }
        }
    }
}