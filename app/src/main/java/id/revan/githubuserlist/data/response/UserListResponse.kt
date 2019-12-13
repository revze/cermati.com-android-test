package id.revan.githubuserlist.data.response

import com.google.gson.annotations.SerializedName
import id.revan.githubuserlist.data.model.User

data class UserListResponse (
    @SerializedName("total_count")
    val count: Int,

    @SerializedName("items")
    val items: List<User>,

    @SerializedName("message")
    val message: String
)