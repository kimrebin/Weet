package com.example.weet.data.remote
import android.net.Uri
import androidx.compose.animation.core.snap
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.tasks.await

import javax.inject.Inject
import kotlin.js.ExperimentalJsFileName

object FirebaseManager {
    private val firestore: FirebaseFirestore by lazy {FirebaseFirestore.getInstance()}
    private val storage: FirebaseStorage by lazy {FirebaseStorage.getInstance()}

    suspend fun savePerson(personId: String, person: Person) {
        firestore.collection("persons")
            .document(personId)
            .set(person)
            .await()
    }

    suspend fun getPersonByTag(tag: String): List<Person>{
        val snapshot = firestore.collection("persons")
            .whereEqualTo("tag", tag)
            .get()
            .await()
        return snapshot.documents.mapNotNull { document: DocumentSnapshot -> document.toObject(Person::class.java) }
    }

    suspend fun getPerson(personId: String): Person? {
        val doc = firestore.collection("persons")
            .document(personId)
            .get()
            .await()
        return doc.toObject(Person::class.java)
    }

    suspend fun deletePerson(personId: String) {
        firestore.collection("persons")
            .document(personId)
            .delete()
            .await()
    }

    suspend fun uploadProfileImage(fileUri: Uri, fileName: String): String? {
        val ref = storage.reference.child("profile_images/$fileName")
        ref.putFile(fileUri).await()
        return ref.downloadUrl.await().toString()
    }

    suspend fun saveSettings(userId: String, notificationInterval: Int, popupTime: String) {
        val settings = mapOf(
            "notificationInterval" to notificationInterval,
            "popupTime" to popupTime
        )
        firestore.collection("settings")
            .document(userId)
            .set(settings)
            .await()
    }

    suspend fun getSettings(userId: String): Map<String, Any>? {
        val snapshot = firestore.collection("settings")
            .document(userId)
            .get()
            .await()
        return if (snapshot.exists()) snapshot.data else null
    }


}
//data에 담아야 할 객체 : tag, name, score, image