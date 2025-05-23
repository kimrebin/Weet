package com.example.weet.data.remote

import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

object FirebaseManager {
    private val db = FirebaseFirestore.getInstance()

    fun getPersonsCollection() = db.collection("persons")
}
