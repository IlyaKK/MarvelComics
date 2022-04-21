package com.test.marvelcomics.ui.view_models.view_model_list_comics

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.test.marvelcomics.data.MarvelComicsRepository
import com.test.marvelcomics.domain.entity.database.ComicWithWritersAndPainters
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class ListComicsViewModel(
    private val comicsRepository: MarvelComicsRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val state: StateFlow<UiState>

    val pagingDataFlow: Flow<PagingData<ComicWithWritersAndPainters>>

    val accept: (UiAction) -> Unit

    init {
        val initialDataRange: String = savedStateHandle.get(LAST_DATA_RANGE_SET) ?: DEFAULT_QUERY
        val actionStateFlow = MutableSharedFlow<UiAction>()
        val shower = actionStateFlow
            .filterIsInstance<UiAction.ShowComics>()
            .distinctUntilChanged()
            .shareIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
                replay = 1
            )
            .onStart {
                emit(
                    UiAction.ShowComics(dataRange = initialDataRange)
                )
            }

        pagingDataFlow = shower
            .flatMapLatest { uiActionShowComics ->
                getMarvelComicsByRange(dataRange = uiActionShowComics.dataRange)
            }
            .cachedIn(viewModelScope)

        state = shower.map { uiActionShowComics ->
            UiState(dataRange = uiActionShowComics.dataRange)
        }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
                initialValue = UiState()
            )

        accept = { action ->
            viewModelScope.launch {
                actionStateFlow.emit(action)
            }
        }
    }

    private fun getMarvelComicsByRange(dataRange: String): Flow<PagingData<ComicWithWritersAndPainters>> =
        comicsRepository.getComicsData(dataRange)

    override fun onCleared() {
        savedStateHandle[LAST_DATA_RANGE_SET] = state.value.dataRange
        super.onCleared()
    }
}

data class UiState(
    val dataRange: String = DEFAULT_QUERY
)

sealed class UiAction {
    data class ShowComics(val dataRange: String) : UiAction()
}

private const val LAST_DATA_RANGE_SET: String = "last_data_range_set"
private const val DEFAULT_QUERY: String = "1949-01-01,2022-04-05"