package com.alexeyyuditsky.test

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.annotation.UiThread
import com.alexeyyuditsky.test.databinding.ActivityMainBinding
import com.alexeyyuditsky.test.presenter.LoginPresenter
import com.alexeyyuditsky.test.presenter.LoginView
import com.alexeyyuditsky.test.model.AuthRepository

class MainActivity : AppCompatActivity(), LoginView {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val loginPresenter = LoginPresenter.Base(AuthRepository.Base())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginPresenter.attachView(this@MainActivity)
        setContentView(binding.root)
        binding.click.setOnClickListener { onButtonClick() }
    }

    override fun showSuccess() {
        Toast.makeText(this, "Success login", Toast.LENGTH_SHORT).show()
    }

    override fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun showError(message: Int) {
        Toast.makeText(this, getString(message), Toast.LENGTH_SHORT).show()
    }

    override fun viewsState(state: Boolean) {
        binding.login.isEnabled = state
        binding.password.isEnabled = state
        binding.click.isEnabled = state
    }

    private fun onButtonClick() {
        loginPresenter.login(
            email = binding.login.text.toString(),
            password = binding.password.text.toString()
        )
    }

}