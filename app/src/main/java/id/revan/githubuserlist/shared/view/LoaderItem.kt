package id.revan.githubuserlist.shared.view

import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import id.revan.githubuserlist.R

class LoaderItem : Item() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {}

    override fun getLayout() = R.layout.item_row_paging_loader
}