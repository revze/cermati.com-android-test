package id.revan.githubuserlist.data.repository

import id.revan.githubuserlist.data.model.User
import id.revan.githubuserlist.data.response.UserListResponse
import id.revan.githubuserlist.data.services.ApiServices
import id.revan.githubuserlist.domain.Output
import id.revan.githubuserlist.helper.constants.StatusCode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import okhttp3.ResponseBody
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

@ExperimentalCoroutinesApi
class GithubRepositoryImplTest {

    private lateinit var SUT: GithubRepositoryImpl
    private val testDispatcher = TestCoroutineDispatcher()
    private val KEYWORD = "revan"
    private val PAGE = 1
    private val PER_PAGE = 15

    @Mock
    private lateinit var apiServices: ApiServices


    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        Dispatchers.setMain(testDispatcher)
        SUT = GithubRepositoryImpl(apiServices)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun searchUser_success_listReturned() = runBlockingTest {
        // Arrange
        `when`(apiServices.searchUser(KEYWORD, PAGE, PER_PAGE)).thenReturn(
            UserListResponse(
                0,
                mutableListOf(),
                ""
            )
        )

        // Act
        val result = SUT.searchUser(KEYWORD, PAGE, PER_PAGE)

        // Assert
        val expected = Output.Success<List<User>>(mutableListOf())
        assertEquals(expected, result)

        return@runBlockingTest
    }

    @Test
    fun searchUser_networkError_errorReturned() = runBlockingTest {
        // Arrange
        `when`(apiServices.searchUser(KEYWORD, PAGE, PER_PAGE)).thenAnswer { throw IOException() }

        // Act
        val result = SUT.searchUser(KEYWORD, PAGE, PER_PAGE)

        // Assert
        val expected = Output.Error<List<User>>(StatusCode.NETWORK_ERROR)
        assertEquals(expected, result)

        return@runBlockingTest
    }

    @Test
    fun searchUser_httpError_errorReturned() = runBlockingTest {
        // Arrange
        `when`(apiServices.searchUser(KEYWORD, PAGE, PER_PAGE)).thenAnswer {
            throw HttpException(
                Response.error<UserListResponse>(422, ResponseBody.create(null, ""))
            )
        }

        // Act
        val result = SUT.searchUser(KEYWORD, PAGE, PER_PAGE)

        // Assert
        val expected = Output.Error<List<User>>(StatusCode.GENERAL_ERROR)
        assertEquals(expected, result)

        return@runBlockingTest
    }

    @Test
    fun searchUser_unknownError_errorReturned() = runBlockingTest {
        // Arrange
        `when`(apiServices.searchUser(KEYWORD, PAGE, PER_PAGE)).thenAnswer {
            throw Exception()
        }

        // Act
        val result = SUT.searchUser(KEYWORD, PAGE, PER_PAGE)

        // Assert
        val exptected = Output.Error<List<User>>(StatusCode.GENERAL_ERROR)
        assertEquals(exptected, result)

        return@runBlockingTest
    }
}