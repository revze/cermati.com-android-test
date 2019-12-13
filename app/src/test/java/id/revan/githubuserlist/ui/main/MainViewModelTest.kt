package id.revan.githubuserlist.ui.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import id.revan.githubuserlist.data.model.User
import id.revan.githubuserlist.data.repository.GithubRepository
import id.revan.githubuserlist.data.state.UserListState
import id.revan.githubuserlist.domain.Output
import id.revan.githubuserlist.helper.constants.StatusCode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.*
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito.*

@ExperimentalCoroutinesApi
class MainViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var SUT: MainViewModel
    private val KEYWORD = "revan"
    private val testDispatcher = TestCoroutineDispatcher()

    @Mock
    private lateinit var repository: GithubRepository

    @Mock
    private lateinit var userListStateObserver: Observer<UserListState>

    @Captor
    private lateinit var argumentCaptor: ArgumentCaptor<UserListState>

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        Dispatchers.setMain(testDispatcher)
        SUT = MainViewModel(repository)
        SUT.userListState.observeForever(userListStateObserver)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun getNextUsers_listNotEmpty_success() = runBlockingTest {
        // Arrange
        val users = mutableListOf(User(username = "revze", avatarUrl = ""))
        `when`(repository.searchUser(KEYWORD, SUT.page, SUT.perPage)).thenReturn(
            Output.Success(
                users
            )
        )

        // Act
        SUT.getNextUsers(KEYWORD)

        // Assert
        val expectedLoadingState = UserListState(isLoading = true)
        val expectedSuccessState = UserListState(users = users)
        argumentCaptor.run {
            verify(userListStateObserver, times(2)).onChanged(capture())
            val (loadingState, successState) = allValues
            assertEquals(expectedLoadingState, loadingState)
            assertEquals(expectedSuccessState, successState)
            assertEquals(2, SUT.page)
            assertEquals(false, SUT.hasReachedMax)
        }
    }

    @Test
    fun getNextUsers_listEmpty_success() = runBlockingTest {
        // Arrange
        val users = mutableListOf<User>()
        `when`(repository.searchUser(KEYWORD, SUT.page, SUT.perPage)).thenReturn(
            Output.Success(
                users
            )
        )

        // Act
        SUT.getNextUsers(KEYWORD)

        // Assert
        val expectedLoadingState = UserListState(isLoading = true)
        val expectedSuccessState = UserListState(users = users)
        argumentCaptor.run {
            verify(userListStateObserver, times(2)).onChanged(capture())
            val (loadingState, successState) = allValues
            assertEquals(expectedLoadingState, loadingState)
            assertEquals(expectedSuccessState, successState)
            assertEquals(1, SUT.page)
            assertEquals(true, SUT.hasReachedMax)
        }
    }

    @Test
    fun getNextUsers_networkError_error() = runBlockingTest {
        // Arrange
        `when`(repository.searchUser(KEYWORD, SUT.page, SUT.perPage)).thenReturn(
            Output.Error(
                StatusCode.NETWORK_ERROR
            )
        )

        // Act
        SUT.getNextUsers(KEYWORD)

        // Assert
        val expectedLoadingState = UserListState(isLoading = true)
        val expectedErrorState = UserListState(errorCode = StatusCode.NETWORK_ERROR)
        argumentCaptor.run {
            verify(userListStateObserver, times(2)).onChanged(capture())
            val (loadingState, errorState) = allValues
            assertEquals(expectedLoadingState, loadingState)
            assertEquals(expectedErrorState, errorState)
        }
    }

    @Test
    fun getNextUsers_generalError_error() = runBlockingTest {
        // Arrange
        `when`(repository.searchUser(KEYWORD, SUT.page, SUT.perPage)).thenReturn(
            Output.Error(
                StatusCode.GENERAL_ERROR
            )
        )

        // Act
        SUT.getNextUsers(KEYWORD)

        // Assert
        val expectedLoadingState = UserListState(isLoading = true)
        val expectedErrorState = UserListState(errorCode = StatusCode.GENERAL_ERROR)
        argumentCaptor.run {
            verify(userListStateObserver, times(2)).onChanged(capture())
            val (loadingState, errorState) = allValues
            assertEquals(expectedLoadingState, loadingState)
            assertEquals(expectedErrorState, errorState)
        }
    }

    @Test
    fun getNextUsers_hasReachedMax_nothing() = runBlockingTest {
        // Arrange
        SUT.hasReachedMax = true

        // Act
        SUT.getNextUsers(KEYWORD)

        // Assert
        verify(repository, never()).searchUser(anyString(), anyInt(), anyInt())
    }
}