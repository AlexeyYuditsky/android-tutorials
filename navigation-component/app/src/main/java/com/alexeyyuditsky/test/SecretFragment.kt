package com.alexeyyuditsky.test

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.alexeyyuditsky.test.databinding.FragmentSecretBinding

class SecretFragment : Fragment(R.layout.fragment_secret) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        FragmentSecretBinding.bind(view).apply {
            goBackButton.setOnClickListener {
                findNavController().popBackStack()
            }
            closeBoxButton.setOnClickListener {
                findNavController().popBackStack(R.id.rootFragment, false)
            }
        }
    }

}