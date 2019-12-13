package id.revan.githubuserlist.shared.view

import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import id.revan.githubuserlist.R
import id.revan.githubuserlist.data.model.User
import id.revan.githubuserlist.shared.extensions.GlideApp
import kotlinx.android.synthetic.main.item_row_user.view.*

class UserItem(private val user: User) : Item() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        val context = viewHolder.itemView.context
        viewHolder.itemView.tv_username.text = user.username
        GlideApp.with(context).load(user.avatarUrl).centerCrop().into(viewHolder.itemView.iv_avatar)
    }

    override fun getLayout() = R.layout.item_row_user
}