package com.test.marvelcomics.ui.view_models.view_model_list_comics

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.insertSeparators
import androidx.paging.map
import com.test.marvelcomics.data.MarvelComicsRepository
import com.test.marvelcomics.domain.entity.database.ComicWithWritersAndPainters
import com.test.marvelcomics.util.UtilData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.sql.Timestamp
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit
import java.util.*

@OptIn(ExperimentalCoroutinesApi::class)
class ListComicsViewModel(
    private val comicsRepository: MarvelComicsRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private fun Int.formatDayOrMonth() = "%02d".format(this)

    private val UiModel.ComicItem.zoneDataSaleDay: ZonedDateTime?
        get() {
            return UtilData.createZoneDataForItemOfList(this.comic.comic.saleDay)
        }

    val stateRangeData: StateFlow<UiState>

    val pagingDataFlow: Flow<PagingData<UiModel>>

    val accept: (UiAction) -> Unit

    init {
        val initialDataRange: String =
            savedStateHandle.get(LAST_DATA_RANGE_SET) ?: getInitialiseDataRange()
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
                emit(UiAction.ShowComics(dataRange = initialDataRange))
            }

        pagingDataFlow = shower
            .flatMapLatest { uiActionShowComics ->
                getMarvelComicsByRange(dataRange = uiActionShowComics.dataRange)
            }
            .cachedIn(viewModelScope)

        stateRangeData = shower.map { uiActionShowComics ->
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
                            "${after.zoneDataSaleDay?.dayOfMonth?.formatDayOrMonth()}." +
                                    "${after.zoneDataSaleDay?.monthValue?.formatDayOrMonth()}." +
                                    "${after.zoneDataSaleDay?.year}"
                        )
                    }
                    val beforeDay = before.zoneDataSaleDay
                    val afterDay = after.zoneDataSaleDay
                    val diffDay =
                        ChronoUnit.DAYS.between(beforeDay, afterDay)
                    if (diffDay > 0) {
                        UiModel.SeparatorItem(
                            "${after.zoneDataSaleDay?.dayOfMonth?.formatDayOrMonth()}." +
                                    "${after.zoneDataSaleDay?.monthValue?.formatDayOrMonth()}." +
                                    "${after.zoneDataSaleDay?.year}"
                        )
                    } else {
                        null
                    }
                }
            }

    override fun onCleared() {
        savedStateHandle[LAST_DATA_RANGE_SET] = stateRangeData.value.dataRange
        super.onCleared()
    }
}

data class UiState(
    val dataRange: String = getInitialiseDataRange()
)

sealed class UiAction {
    data class ShowComics(val dataRange: String) : UiAction()
}

sealed class UiModel {
    data class ComicItem(val comic: ComicWithWritersAndPainters) : UiModel()
    data class SeparatorItem(val description: String) : UiModel()
}

private const val LAST_DATA_RANGE_SET: String = "last_data_range_set"

fun getInitialiseDataRange(): String {
    val firstDayOfMonth = Date(getTimeStampFirstDayOfMonth().time)
    val nowDate = Date()
    return UtilData.createStringDataRange(firstDayOfMonth, nowDate)
}

fun getTimeStampFirstDayOfMonth(): Timestamp {
    val calendar: Calendar = Calendar.getInstance()
    calendar.time = Date()
    calendar.set(
        Calendar.DAY_OF_MONTH,
        calendar.getActualMinimum(Calendar.DAY_OF_MONTH)
    )
    calendar.set(Calendar.HOUR_OF_DAY, 0)
    calendar.set(Calendar.MINUTE, 0)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)
    return Timestamp(calendar.timeInMillis)
}