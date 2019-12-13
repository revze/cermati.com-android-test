package id.revan.githubuserlist.data.repository

import id.revan.githubuserlist.data.model.User
import id.revan.githubuserlist.domain.Output

interface GithubRepository {
    suspend fun getUsers(query: String, page: Int, perPage: Int): Output<List<User>>
}