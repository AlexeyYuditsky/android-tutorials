package ua.cn.stu.hilt.app.screens.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ua.cn.stu.hilt.app.model.accounts.AccountsRepository
import ua.cn.stu.hilt.app.utils.log
import ua.cn.stu.hilt.app.utils.share
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    accountsRepository: AccountsRepository,
) : ViewModel() {

    private val _username = MutableLiveData<String>()
    val username = _username.share()

    private val _isSignIn = MutableLiveData<Boolean>()
    val isSignIn = _isSignIn.share()

    init {
        viewModelScope.launch {
            _isSignIn.value = accountsRepository.isSignedIn()

            // listening for the current account and send the username to be displayed in the toolbar
            accountsRepository.getAccount().collect { result ->
                _username.value = result.getValueOrNull()?.username?.let { "@$it" } ?: ""
            }
        }
    }

}