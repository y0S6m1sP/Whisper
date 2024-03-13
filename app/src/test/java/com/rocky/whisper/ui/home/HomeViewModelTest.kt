package com.rocky.whisper.ui.home

import com.rocky.shared_test.MainCoroutineRule
import com.rocky.shared_test.data.repository.FakeMessageRepository
import com.rocky.shared_test.data.repository.FakeUserRepository
import com.rocky.whisper.data.User
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
@ExperimentalCoroutinesApi
class HomeViewModelTest {

    private lateinit var homeViewModel: HomeViewModel

    private lateinit var messageRepository: FakeMessageRepository
    private lateinit var userRepository: FakeUserRepository

    private var testDispatcher = UnconfinedTestDispatcher()

    @ExperimentalCoroutinesApi
    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setup() {
        messageRepository = FakeMessageRepository()
        userRepository = FakeUserRepository()

        homeViewModel = HomeViewModel(
            messageRepository = messageRepository,
            userRepository = userRepository,
            dispatcher = testDispatcher
        )
    }


    @Test
    fun observeUser_updatesUiStateWithUser() = runTest {
        val user = User("fakeId", "fakeName", "fakeAvatar")
        userRepository.setUser(user)
        homeViewModel.observeUser()

        advanceUntilIdle()

        assertEquals(user, homeViewModel.uiState.value.user)
    }

    @Test
    fun observeUser_noUserUpdatesUiStateWithNull() = runTest {
        homeViewModel.observeUser()

        advanceUntilIdle()

        assertNull(homeViewModel.uiState.value.user)
    }

    @Test
    fun createRoom_createNewRoom() {
        homeViewModel.createRoom("fakeId")
        assertEquals(1, messageRepository.recentChatList.size)
    }

    @Test
    fun observeChatRoom_updateUiState() = runTest {
        val chatroomId = "fakeId"
        messageRepository.createRoom(chatroomId)
        homeViewModel.observeChatroom()

        advanceUntilIdle()

        assertEquals(chatroomId, homeViewModel.uiState.value.recentChatList[0].id)
    }

    @Test
    fun observeMessageCount_updateUiState() = runTest {
        messageRepository.setMessageCount(10)
        homeViewModel.observeMessageCount()

        advanceUntilIdle()

        assertEquals(10, homeViewModel.uiState.value.messageCount)
    }

    @Test
    fun showWhisperDialog_updateUiState() {
        homeViewModel.showWhisperDialog()
        assertEquals(true, homeViewModel.uiState.value.isShowWhisperDialog)
    }

    @Test
    fun hideWhisperDialog_updateUiState() {
        homeViewModel.hideWhisperDialog()
        assertEquals(false, homeViewModel.uiState.value.isShowWhisperDialog)
    }


}