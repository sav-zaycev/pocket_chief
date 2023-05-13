package ru.zaycev.pocketchief.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference

interface FirebaseRepository {
    val authentication: FirebaseAuth
    val database: DatabaseReference

    fun registrationUser(email:String, password: String, lambda: (Boolean) -> Unit)
    fun addUserToDataBase(email: String)
}