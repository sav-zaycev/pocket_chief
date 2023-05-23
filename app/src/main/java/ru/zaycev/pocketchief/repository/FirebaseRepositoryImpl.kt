package ru.zaycev.pocketchief.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class FirebaseRepositoryImpl : FirebaseRepository {
    override val authentication: FirebaseAuth = FirebaseAuth.getInstance()
    override val database: DatabaseReference = Firebase.database.reference

    private val user: FirebaseUser? by lazy {
        authentication.currentUser
    }

    override fun getCurrentUser(): FirebaseUser? {
        return user
    }

    override fun signInUser(email: String, password: String, actionSuccess: (Boolean) -> Unit) {
        authentication.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task: Task<AuthResult> ->
                if (task.isSuccessful) {
                    actionSuccess.invoke(true)
                } else {
                    actionSuccess.invoke(false)
                }
            }

    }

    override fun registrationUser(email: String, password: String, actionSuccess: (Boolean) -> Unit) {
        authentication.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task: Task<AuthResult> ->
                if (task.isSuccessful) {
                    addUserToDataBase(email)
                    actionSuccess.invoke(true)
                } else {
                    actionSuccess.invoke(false)
                }
            }
    }

    override fun addUserToDataBase(email: String) {
        database
            .child("users")
            .child(user!!.uid)
            .child("login (email)").setValue(email)
    }
}