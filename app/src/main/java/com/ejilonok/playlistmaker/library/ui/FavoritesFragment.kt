package com.ejilonok.playlistmaker.library.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ejilonok.playlistmaker.databinding.FragmentFavoritesBinding
import com.ejilonok.playlistmaker.library.presentation.FavoritesState
import com.ejilonok.playlistmaker.library.presentation.FavoritesViewModel
import com.ejilonok.playlistmaker.main.ui.common.BindingFragment
import com.ejilonok.playlistmaker.main.ui.common.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoritesFragment : BindingFragment<FragmentFavoritesBinding>() {

    companion object {
        fun newInstance() = FavoritesFragment()
    }

    private val viewModel: FavoritesViewModel by viewModel()
    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentFavoritesBinding {
        return FragmentFavoritesBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getStateLiveData().observe(viewLifecycleOwner) { state ->
            when (state) {
                is FavoritesState.Empty -> renderEmptyState()
                else -> renderEmptyState()
            }
        }
    }

    private fun renderEmptyState() {
        binding.libraryEmpty.show()
    }
}
