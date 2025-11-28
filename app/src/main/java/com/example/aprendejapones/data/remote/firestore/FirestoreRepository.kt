package com.example.aprendejapones.data.remote.firestore

import com.example.aprendejapones.domain.model.Publicaciones
import com.example.aprendejapones.domain.model.Comentarios
import com.example.aprendejapones.domain.model.UsuariosFirestore
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await

class FirestoreRepository(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) {

    private fun postsCollection() = firestore.collection("posts")
    private fun usersCollection() = firestore.collection("users")

    // Toggle like: crea o borra el documento /posts/{postId}/likes/{uid} y usa transaction para contador
    // Retorna true si ahora está likeado, false si se removió.
    suspend fun toggleLike(postId: String, uid: String): Boolean {
        val likeRef = postsCollection().document(postId).collection("likes").document(uid)
        val postRef = postsCollection().document(postId)

        return try {
            firestore.runTransaction { tx ->
                val likeSnapshot = tx.get(likeRef)
                if (likeSnapshot.exists()) {
                    // eliminar like
                    tx.delete(likeRef)
                    val postSnapshot = tx.get(postRef)
                    val current = postSnapshot.getLong("contador_likes") ?: 0L
                    tx.update(postRef, "contador_likes", (current - 1).coerceAtLeast(0))
                    false
                } else {
                    // crear like con ID = uid
                    val like = mapOf(
                        "id_usuario" to uid,
                        "fecha_reaccion" to System.currentTimeMillis()
                    )
                    tx.set(likeRef, like)
                    val postSnapshot = tx.get(postRef)
                    val current = postSnapshot.getLong("contador_likes") ?: 0L
                    tx.update(postRef, "contador_likes", current + 1)
                    true
                }
            }.await()
        } catch (e: Exception) {
            throw e
        }
    }

    // Añadir comentario y actualizar contador en la misma transaction
    suspend fun addComment(postId: String, comentario: Comentarios): String {
        val commentsCol = postsCollection().document(postId).collection("comments")
        val postRef = postsCollection().document(postId)

        return try {
            val newDocRef = commentsCol.document()
            firestore.runTransaction { tx ->
                tx.set(newDocRef, comentario)
                val postSnapshot = tx.get(postRef)
                val current = postSnapshot.getLong("contador_comentarios") ?: 0L
                tx.update(postRef, "contador_comentarios", current + 1)
                newDocRef.id
            }.await()
        } catch (e: Exception) {
            throw e
        }
    }

    // Obtener publicaciones (orden descendente por fecha)
    suspend fun getPosts(): List<Publicaciones> {
        val snapshot = postsCollection().orderBy("fecha_publicacion", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .get().await()
        return snapshot.documents.mapNotNull { doc ->
            doc.toObject(Publicaciones::class.java)?.copy(id_publicacion = doc.id)
        }
    }

    // Cargar comentarios de una publicación
    suspend fun getComments(postId: String): List<Comentarios> {
        val snapshot = postsCollection().document(postId).collection("comments")
            .orderBy("fecha_publicacion", com.google.firebase.firestore.Query.Direction.ASCENDING).get().await()
        return snapshot.documents.mapNotNull { doc ->
            doc.toObject(Comentarios::class.java)?.copy(id_comentario = doc.id, id_publicacion = postId)
        }
    }

    // Crear publicación (cliente debe asegurarse id_autor == auth.uid)
    suspend fun createPost(post: Publicaciones): String {
        val docRef = postsCollection().document()
        docRef.set(post).await()
        return docRef.id
    }

    // Comprobar si user ha likeado un post (existe doc)
    suspend fun hasUserLikedPost(postId: String, uid: String): Boolean {
        val doc = postsCollection().document(postId).collection("likes").document(uid).get().await()
        return doc.exists()
    }

    // Obtener perfil público del usuario
    suspend fun getUser(uid: String): UsuariosFirestore? {
        val doc = usersCollection().document(uid).get().await()
        return if (doc.exists()) {
            doc.toObject(UsuariosFirestore::class.java)?.copy(id_usuario = doc.id)
        } else null
    }

    // Crear/actualizar perfil parcial del usuario (solo campos permitidos)
    suspend fun updateUserProfilePartial(uid: String, data: Map<String, Any?>) {
        usersCollection().document(uid).set(data, SetOptions.merge()).await()
    }

    // Operaciones atomicas: coins delta, add purchase, set daily mission
    suspend fun incrementCoins(uid: String, delta: Long) {
        usersCollection().document(uid).update("coins", com.google.firebase.firestore.FieldValue.increment(delta)).await()
    }

    suspend fun addPurchase(uid: String, itemId: String) {
        usersCollection().document(uid).update("purchases", com.google.firebase.firestore.FieldValue.arrayUnion(itemId)).await()
    }

    suspend fun setDailyMissionDone(uid: String, timestampMillis: Long) {
        usersCollection().document(uid).update(mapOf(
            "dailyMissionDoneAt" to timestampMillis,
            "updatedAt" to System.currentTimeMillis()
        )).await()
    }
}
