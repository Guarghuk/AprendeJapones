package com.example.aprendejapones.domain.repository

import com.example.aprendejapones.domain.model.FirestorePost
import com.example.aprendejapones.domain.model.Response
import kotlinx.coroutines.flow.Flow

typealias Posts = List<FirestorePost>

interface PostRepository {
    fun getPosts(): Flow<Response<Posts>>
    fun getPostsByUserId(userId: String): Flow<Response<Posts>>
}