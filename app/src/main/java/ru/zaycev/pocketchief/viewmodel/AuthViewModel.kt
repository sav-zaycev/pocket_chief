package ru.zaycev.pocketchief.viewmodel

import android.app.Application
import android.content.Context
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Patterns
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.zaycev.pocketchief.R
import ru.zaycev.pocketchief.data.VerificationRequirement
import ru.zaycev.pocketchief.repository.FirebaseRepository
import ru.zaycev.pocketchief.repository.FirebaseRepositoryImpl

class AuthViewModel(private val apl: Application) : AndroidViewModel(apl) {
    private val firebaseRepository: FirebaseRepository = FirebaseRepositoryImpl()

    private var email: String = ""
    private var password: String = ""

    private val _requirements: MutableLiveData<List<VerificationRequirement>> = MutableLiveData()
    val requirements: LiveData<List<VerificationRequirement>> = _requirements

    private val _iconEmailValidation: MutableLiveData<Drawable> = MutableLiveData()
    val iconEmailValidation: LiveData<Drawable> = _iconEmailValidation

    private val _errorText: MutableLiveData<Int> = MutableLiveData()
    val errorText: LiveData<Int> = _errorText

    private val _authSuccess: MutableLiveData<Boolean> = MutableLiveData()
    val authSuccess: MutableLiveData<Boolean> = _authSuccess

    private val requirementPasswordList: List<VerificationRequirement> = listOf(
        VerificationRequirement("длина от 8 до 32 символов", "^[\\w\$&.-/*]{8,32}\$"),
        VerificationRequirement("только буквы лат.алфавита (a-z) и арабские цифры (0-9)", "[A-Za-z0-9]"),
        VerificationRequirement("не менее 1 заглавной буквы", "[A-Z]"),
        VerificationRequirement("не менее 1 спец.символа ($&.-/*)", "[$&.-/*]")
    )

    init {
        _requirements.value = requirementPasswordList
    }

    fun checkUserSignIn() {
        if (firebaseRepository.getCurrentUser() != null) authSuccess.value = true
    }

    fun inputEmail(text: String) {
        email = text.trim()

        _iconEmailValidation.value = if (email.isEmpty()) {
            ResourcesCompat.getDrawable(apl.resources, R.drawable.bg_transparent, null)
        } else if (isEmailValid()) {
            ResourcesCompat.getDrawable(apl.resources, R.drawable.ic_check, null)
        } else {
            ResourcesCompat.getDrawable(apl.resources, R.drawable.ic_warning, null)
        }
    }

    fun inputPassword(text: String) {
        password = text.trim()
        updateStateRequirements(password)
    }

    fun registrationUser() {
        if (isNetworkAvailable()) {
            if (isEmailValid() && isPasswordValid()) {
                firebaseRepository.registrationUser(email, password) { authResult: Boolean ->
                    authSuccess.value = authResult
                    if (!authResult) _errorText.value = R.string.error_duplicate_user
                }
            } else {
                _errorText.value = R.string.error_invalid_data
            }
        } else {
            _errorText.value = R.string.error_network_unavailable
        }
    }

    fun signInUser() {
        if (isNetworkAvailable()) {
            if (isEmailValid() && password.isNotEmpty()) {
                firebaseRepository.signInUser(email, password) { authResult: Boolean ->
                    authSuccess.value = authResult
                    if (!authResult) _errorText.value = R.string.error_incorrect_data
                }
            } else {
                _errorText.value = R.string.error_invalid_data
            }
        } else {
            _errorText.value = R.string.error_network_unavailable
        }
    }

    private fun isPasswordValid(): Boolean {
        for (index in requirementPasswordList.indices) {
            if (!requirementPasswordList[index].state) return false
        }
        return true
    }

    private fun updateStateRequirements(password: String) {
        for (index in requirementPasswordList.indices) {
            requirementPasswordList[index].checkCondition(password)
        }
        _requirements.value = requirementPasswordList
    }

    private fun isEmailValid(): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager: ConnectivityManager = apl.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork) ?: return false
        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            else -> false
        }
    }
}