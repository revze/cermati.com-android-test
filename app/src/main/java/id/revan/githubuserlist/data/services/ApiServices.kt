package id.revan.githubuserlist.data.services

import id.revan.githubuserlist.data.response.UserListResponse
import id.revan.githubuserlist.helper.constants.Endpoint
import id.revan.githubuserlist.helper.constants.Request
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiServices {
    @GET(Endpoint.USER_LIST)
    suspend fun searchUser(
        @Query(Request.QUERY) query: String,
        @Query(Request.PAGE) page: Int,
        @Query(Request.PER_PAGE) perPage: Int
    ): UserListResponse
}