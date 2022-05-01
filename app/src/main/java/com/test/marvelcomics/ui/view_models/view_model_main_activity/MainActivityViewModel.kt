package com.test.marvelcomics.ui.view_models.view_model_main_activity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

class MainActivityViewModel(private val handle: SavedStateHandle) : ViewModel() {
    private val _state: MutableLiveData<UiState>
    val state: LiveData<UiState>

    init {
        val initialScreenState = handle.get(LAST_SCREEN_STATE) ?: DEFAULT_STATE
        _state = MutableLiveData(UiState(initialScreenState))
        state = _state
    }

    fun setState(state: StateScreen) {
        _state.postValue(UiState(state))
    }

    override fun onCleared() {
        handle[LAST_SCREEN_STATE] = _state.value?.stateScreen
        super.onCleared()
    }

}

data class UiState(
    val stateScreen: StateScreen = DEFAULT_STATE
)

val DEFAULT_STATE = StateScreen.SCREEN_LIST_COMICS_FRAGMENT
const val LAST_SCREEN_STATE = "last_screen_state"

enum class StateScreen {
    SCREEN_LIST_COMICS_FRAGMENT,
    SCREEN_COMIC_DETAIL_FRAGMENT,
    SCREEN_COMIC_DETAIL_LANDSCAPE_FRAGMENT
}