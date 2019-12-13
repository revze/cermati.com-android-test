package id.revan.githubuserlist.ui.main

import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import id.revan.githubuserlist.R
import id.revan.githubuserlist.data.state.UserListState
import id.revan.githubuserlist.di.Injector
import id.revan.githubuserlist.helper.constants.StatusCode
import id.revan.githubuserlist.shared.extensions.hide
import id.revan.githubuserlist.shared.extensions.hideKeyboard
import id.revan.githubuserlist.shared.extensions.show
import id.revan.githubuserlist.shared.listener.EndlessScrollListener
import id.revan.githubuserlist.shared.view.LoaderItem
import id.revan.githubuserlist.shared.view.UserItem
import id.revan.githubuserlist.ui.base.BaseViewModelFactory
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_error.*
import kotlinx.android.synthetic.main.layout_loader.*
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    private val userAdapter = GroupAdapter<GroupieViewHolder>()
    private lateinit var viewModel: MainViewModel
    private val loaderItem = LoaderItem()

    @Inject
    lateinit var viewModelFactory: BaseViewModelFactory<MainViewModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Injector.getApp().inject(this)

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MainViewModel::class.java)
        viewModel.userListState.observe(this, userListObserver)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        edt_search.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                if (edt_search.text.toString().isBlank()) {
                    Toast.makeText(this, "User required", Toast.LENGTH_LONG).show()
                } else if (!viewModel.isLoading()) {
                    viewModel.refreshUsers(edt_search.text.toString())
                    hideKeyboard()
                }
            }

            true
        }

        val layoutManager = LinearLayoutManager(this)
        rv_users.layoutManager = layoutManager
        rv_users.adapter = userAdapter
        rv_users.addOnScrollListener(object : EndlessScrollListener(layoutManager) {
            override fun loadMoreItems() {
                if (!viewModel.isLoading()) {
                    viewModel.getNextUsers(edt_search.text.toString())
                }
            }
        })
    }

    private val userListObserver = Observer<UserListState> {
        if (it.isLoading) {
            if (viewModel.page == 1) userAdapter.clear()
            if (viewModel.page > 1) {
                userAdapter.add(loaderItem)
                return@Observer
            }
            layout_loader.show()
            layout_error.hide()
            rv_users.hide()
            return@Observer
        }

        if (it.errorCode != StatusCode.NO_ERROR) {
            val errorMessage =
                if (it.errorCode == StatusCode.NETWORK_ERROR) getString(R.string.no_internet_error_message) else getString(
                    R.string.general_error_message
                )

            if (viewModel.page > 1) {
                userAdapter.remove(loaderItem)
                Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
                return@Observer
            }
            layout_loader.hide()
            layout_error.show()
            tv_error_message.text = errorMessage
            rv_users.hide()
            return@Observer
        }

        it.users.map {
            userAdapter.add(UserItem(it))
        }
        if (viewModel.page == 2) rv_users.scrollToPosition(0)
        if (viewModel.page > 2) userAdapter.remove(loaderItem)
        layout_loader.hide()

        if (viewModel.page == 1 && it.users.isEmpty()) {
            layout_error.show()
            tv_error_message.text = getString(R.string.users_not_found_message)
            rv_users.hide()
        } else {
            layout_error.hide()
            rv_users.show()
        }
    }
}
