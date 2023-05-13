package ru.zaycev.pocketchief.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class FirebaseRepositoryImpl : FirebaseRepository {
    override val authentication: FirebaseAuth = FirebaseAuth.getInstance()
    override val database: DatabaseReference = Firebase.database.reference


    override fun registrationUser(email: String, password: String, lambda: (Boolean) -> Unit) {
        authentication.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task: Task<AuthResult> ->
                if (task.isSuccessful) {
                    addUserToDataBase(email)
                    lambda.invoke(true)
                } else {
                    lambda.invoke(false)
                }
            }
    }

    override fun addUserToDataBase(email: String) {
        database.child("users").child("user").child("email").setValue(email)
    }
}