package com.test.marvelcomics.ui.view_models.view_model_list_comics

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.test.marvelcomics.data.MarvelComicsRepository
import com.test.marvelcomics.domain.entity.database.ComicWithWritersAndPainters
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalCoroutinesApi::class)
class ListComicsViewModel(
    private val comicsRepository: MarvelComicsRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private fun Int.formatDayOrMonth() = "%02d".format(this)

    private val UiModel.ComicItem.roundedSaleDay: ZonedDateTime?
        @RequiresApi(Build.VERSION_CODES.O)
        get() {
            val pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ")
            return ZonedDateTime.parse(this.comic.comic.saleDay, pattern)
        }

    private val state: StateFlow<UiState>

    val pagingDataFlow: Flow<PagingData<UiModel>>

    val accept: (UiAction) -> Unit

    init {
        val initialDataRange: String =
            savedStateHandle.get(LAST_DATA_RANGE_SET) ?: DEFAULT_DATA_RANGE
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

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getMarvelComicsByRange(dataRange: String): Flow<PagingData<UiModel>> =
        comicsRepository.getComicsData(dataRange)
            .map { pagingData -> pagingData.map { UiModel.ComicItem(it) } }
            .map {
                it.insertSeparators { before, after ->
                    if (after == null) {
                        return@insertSeparators null
                    }

                    if (before == null) {
                        return@insertSeparators UiModel.SeparatorItem(
                            "${after.roundedSaleDay?.dayOfMonth?.formatDayOrMonth()}." +
                                    "${after.roundedSaleDay?.monthValue?.formatDayOrMonth()}." +
                                    "${after.roundedSaleDay?.year}"
                        )
                    }
                    val beforeDay = before.roundedSaleDay
                    val afterDay = after.roundedSaleDay
                    val diffDay =
                        ChronoUnit.DAYS.between(afterDay, beforeDay)
                    if (diffDay > 0) {
                        UiModel.SeparatorItem(
                            "${after.roundedSaleDay?.dayOfMonth?.formatDayOrMonth()}." +
                                    "${after.roundedSaleDay?.monthValue?.formatDayOrMonth()}." +
                                    "${after.roundedSaleDay?.year}"
                        )
                    } else {
                        null
                    }
                }
            }

    override fun onCleared() {
        savedStateHandle[LAST_DATA_RANGE_SET] = state.value.dataRange
        super.onCleared()
    }
}

data class UiState(
    val dataRange: String = DEFAULT_DATA_RANGE
)

sealed class UiAction {
    data class ShowComics(val dataRange: String) : UiAction()
}

sealed class UiModel {
    data class ComicItem(val comic: ComicWithWritersAndPainters) : UiModel()
    data class SeparatorItem(val description: String) : UiModel()
}

private const val LAST_DATA_RANGE_SET: String = "last_data_range_set"
private const val DEFAULT_DATA_RANGE: String = "1949-01-01,2022-04-05"