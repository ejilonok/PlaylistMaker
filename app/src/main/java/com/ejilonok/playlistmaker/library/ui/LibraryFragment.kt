package com.ejilonok.playlistmaker.library.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ejilonok.playlistmaker.R
import com.ejilonok.playlistmaker.databinding.FragmentLibraryBinding
import com.ejilonok.playlistmaker.main.ui.common.BindingFragment
import com.google.android.material.tabs.TabLayoutMediator

class LibraryFragment : BindingFragment<FragmentLibraryBinding>() {
    private var tabMediator: TabLayoutMediator? = null

    companion object {
        val TAB_NAMES = arrayOf(R.string.favorite_tracks, R.string.playlists)
    }
    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentLibraryBinding {
        return FragmentLibraryBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewPager.adapter = FragmentAdapter(childFragmentManager, lifecycle)

        tabMediator = TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = getString(TAB_NAMES[position])
        }
        tabMediator?.attach()
    }

    override fun onDestroy() {
        tabMediator?.detach()
        super.onDestroy()
    }
}
