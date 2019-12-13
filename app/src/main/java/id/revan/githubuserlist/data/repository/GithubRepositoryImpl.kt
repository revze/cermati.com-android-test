package id.revan.githubuserlist.data.repository

import id.revan.githubuserlist.data.model.User
import id.revan.githubuserlist.data.services.ApiServices
import id.revan.githubuserlist.domain.Output
import id.revan.githubuserlist.helper.constants.StatusCode
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GithubRepositoryImpl @Inject constructor(private val apiServices: ApiServices) :
    GithubRepository {
    override suspend fun searchUser(query: String, page: Int, perPage: Int): Output<List<User>> {
        return try {
            val result = apiServices.searchUser(query, page, perPage)
            Output.Success(result.items)
        } catch (e: HttpException) {
            Output.Error(StatusCode.GENERAL_ERROR)
        } catch (e: IOException) {
            Output.Error(StatusCode.NETWORK_ERROR)
        } catch (e: Exception) {
            Output.Error(StatusCode.GENERAL_ERROR)
        }
    }
}