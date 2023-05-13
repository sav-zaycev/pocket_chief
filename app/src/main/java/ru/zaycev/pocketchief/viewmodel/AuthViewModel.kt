package ru.zaycev.pocketchief.viewmodel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Patterns
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import ru.zaycev.pocketchief.data.VerificationRequirement
import ru.zaycev.pocketchief.repository.FirebaseRepository
import ru.zaycev.pocketchief.repository.FirebaseRepositoryImpl

class AuthViewModel(application: Application) : AndroidViewModel(application) {
    private val firebaseRepository: FirebaseRepository = FirebaseRepositoryImpl()

    val requirements: MutableLiveData<List<VerificationRequirement>> = MutableLiveData()

    init {
        requirements.value = requirementPasswordList
    }

    fun updateStateRequirements(password: String) {
        for (index in requirementPasswordList.indices) {
            requirementPasswordList[index].checkCondition(password)
        }
        requirements.value = requirementPasswordList
    }

    fun isEmailValid(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun isPasswordValid(): Boolean {
        for (index in requirementPasswordList.indices) {
            if (!requirementPasswordList[index].state) return false
        }
        return true
    }

    fun inNetworkAvailable(): Boolean {
        val connectivityManager: ConnectivityManager = getApplication<Application>()
            .applicationContext
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork) ?: return false
        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            else -> false
        }
    }

    fun registrationUser(email: String, password: String, lambda: (Boolean) -> Unit) {
        firebaseRepository.registrationUser(email, password, lambda)
    }

    companion object {
        // Список, содержащий требования к текстовому полю (в данном случае, к паролю)
        private val requirementPasswordList: List<VerificationRequirement> = listOf(
            VerificationRequirement("длина не менее 8 символов", "^[\\w\$&.-/*]{8,}\$"),
            VerificationRequirement("только буквы лат.алфавита (a-z) и арабские цифры (0-9)", "[A-Za-z0-9]"),
            VerificationRequirement("не менее 1 заглавной буквы", "[A-Z]"),
            VerificationRequirement("не менее 1 спец.символа ($&.-/*)", "[$&.-/*]")
        )
    }
}