package id.revan.githubuserlist.data.state

import id.revan.githubuserlist.data.model.User
import id.revan.githubuserlist.helper.constants.StatusCode

data class UserListState(
    var isLoading: Boolean = false,
    var errorCode: Int = StatusCode.NO_ERROR,
    var users: List<User> = mutableListOf()
)