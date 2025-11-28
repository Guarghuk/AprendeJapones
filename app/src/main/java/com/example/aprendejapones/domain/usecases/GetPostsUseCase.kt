package com.example.aprendejapones.domain.use_case

import com.example.aprendejapones.domain.repository.PostRepository
import javax.inject.Inject

class GetPostsUseCase @Inject constructor(
    private val repo: PostRepository
) {
    operator fun invoke() = repo.getPosts()
}