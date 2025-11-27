package com.example.aprendejapones.data.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Centralized Firebase configuration manager.
 * This singleton provides easy access to Firebase services and configuration.
 */
@Singleton
class FirebaseManager @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage
) {
    companion object {
        // Collection names
        const val USERS_COLLECTION = "users"
        const val POSTS_COLLECTION = "posts"
        const val COMMENTS_COLLECTION = "comments"
        const val LIKES_COLLECTION = "likes"
        
        // Storage paths
        const val PROFILE_PHOTOS_PATH = "profile_photos"
        const val POST_IMAGES_PATH = "post_images"
    }

    /**
     * Get current authenticated user ID
     */
    fun getCurrentUserId(): String? = auth.currentUser?.uid

    /**
     * Check if user is logged in
     */
    fun isUserLoggedIn(): Boolean = auth.currentUser != null

    /**
     * Get users collection reference
     */
    fun usersCollection() = firestore.collection(USERS_COLLECTION)

    /**
     * Get posts collection reference
     */
    fun postsCollection() = firestore.collection(POSTS_COLLECTION)

    /**
     * Get comments collection reference
     */
    fun commentsCollection() = firestore.collection(COMMENTS_COLLECTION)

    /**
     * Get likes collection reference
     */
    fun likesCollection() = firestore.collection(LIKES_COLLECTION)

    /**
     * Get profile photos storage reference
     */
    fun profilePhotosStorage() = storage.reference.child(PROFILE_PHOTOS_PATH)

    /**
     * Get post images storage reference
     */
    fun postImagesStorage() = storage.reference.child(POST_IMAGES_PATH)

    /**
     * Sign out current user
     */
    fun signOut() = auth.signOut()
}
