package com.alexeyyuditsky.room.screens.splash

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.alexeyyuditsky.room.R
import com.alexeyyuditsky.room.Repositories
import com.alexeyyuditsky.room.databinding.FragmentSplashBinding
import com.alexeyyuditsky.room.utils.observeEvent
import com.alexeyyuditsky.room.utils.viewModelCreator

class SplashFragment : Fragment(R.layout.fragment_splash) {

    private lateinit var binding: FragmentSplashBinding

    private val viewModel by viewModelCreator { SplashViewModel(Repositories.accountsRepository) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSplashBinding.bind(view)

        renderAnimations()

        viewModel.launchMainScreenEvent.observeEvent(viewLifecycleOwner) { launchMainScreen(it) }
    }

    private fun launchMainScreen(isSignedIn: Boolean) {
        findNavController().navigate(
            if (isSignedIn) R.id.action_splashFragment_to_tabsFragment
            else R.id.action_splashFragment_to_signInFragment
        )
    }

    private fun renderAnimations() {
        binding.progressBar.alpha = 0f
        binding.progressBar.animate()
            .alpha(1f)
            .setDuration(1000)
            .start()

        binding.pleaseWaitTextView.alpha = 0f
        binding.pleaseWaitTextView.animate()
            .alpha(1f)
            .setDuration(1000)
            .start()
    }

}