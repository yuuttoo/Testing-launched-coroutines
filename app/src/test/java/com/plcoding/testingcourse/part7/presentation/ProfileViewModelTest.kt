package com.plcoding.testingcourse.part7.presentation

import androidx.lifecycle.SavedStateHandle
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import com.plcoding.testingcourse.part7.data.UserRepositoryFake
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ProfileViewModelTest {

    private lateinit var viewModel: ProfileViewModel
    private lateinit var repository: UserRepositoryFake

    @BeforeEach
    fun setUp() {
        //switch to test thread
        val testDispatcher = StandardTestDispatcher()
        Dispatchers.setMain(testDispatcher)

        repository = UserRepositoryFake()
        viewModel = ProfileViewModel(
            repository = repository,
            savedStateHandle = SavedStateHandle(
                initialState = mapOf(
                    "userId" to repository.profileToReturn.user.id
                )
            )
        )
    }

    @AfterEach
    fun tearDown() {
        //reset thread back after testing
        Dispatchers.resetMain()
    }

    @Test
    fun `Test loading profile success`() = runTest {
        viewModel.loadProfile()//here's a function takes time but not suspending,
        // need to wait until finishes runTest above and add advanceUntilIdle,
        // wait until no tasks remaining
        advanceUntilIdle()

        assertThat(viewModel.state.value.profile).isEqualTo(repository.profileToReturn)
        assertThat(viewModel.state.value.isLoading).isFalse()
    }

}