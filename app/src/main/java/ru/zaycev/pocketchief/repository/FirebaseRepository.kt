package ru.zaycev.pocketchief.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference

interface FirebaseRepository {
    val authentication: FirebaseAuth
    val database: DatabaseReference

    fun getCurrentUser(): FirebaseUser?
    fun signInUser(email: String, password: String, actionSuccess: (Boolean) -> Unit)
    fun registrationUser(email: String, password: String, actionSuccess: (Boolean) -> Unit)
    fun addUserToDataBase(email: String)
}