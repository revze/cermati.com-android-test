package id.revan.githubuserlist.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.revan.githubuserlist.data.repository.GithubRepository
import id.revan.githubuserlist.data.state.UserListState
import id.revan.githubuserlist.domain.Output
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel @Inject constructor(private val repository: GithubRepository) : ViewModel() {
    val userListState = MutableLiveData<UserListState>()
    var page = 1
        private set
    val perPage = 15
    var hasReachedMax = false

    fun refreshUsers(query: String) {
        hasReachedMax = false
        page = 1
        getNextUsers(query)
    }

    fun getNextUsers(query: String) {
        if (!hasReachedMax) {
            userListState.value = UserListState(isLoading = true)
            viewModelScope.launch {
                val result = repository.searchUser(query, page, perPage)

                when (result) {
                    is Output.Success -> {
                        if (result.output.isNotEmpty()) {
                            page++
                        } else {
                            hasReachedMax = true
                        }
                        userListState.postValue(UserListState(users = result.output))
                    }
                    is Output.Error -> userListState.postValue(
                        UserListState(
                            errorCode = result.code
                        )
                    )
                }
            }
        }
    }

    fun isLoading(): Boolean {
        val currentState = userListState.value

        if (currentState != null) {
            return currentState.isLoading
        }
        return false
    }
}