package com.test.marvelcomics.ui.view_models.view_model_list_comics

import android.annotation.SuppressLint
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
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalCoroutinesApi::class)
class ListComicsViewModel(
    private val comicsRepository: MarvelComicsRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private fun Int.formatDayOrMonth() = "%02d".format(this)

    private val UiModel.ComicItem.roundedSaleDay: ZonedDateTime?
        get() {
            val pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ")
            return ZonedDateTime.parse(this.comic.comic.saleDay, pattern)
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
                            "${after.roundedSaleDay?.dayOfMonth?.formatDayOrMonth()}." +
                                    "${after.roundedSaleDay?.monthValue?.formatDayOrMonth()}." +
                                    "${after.roundedSaleDay?.year}"
                        )
                    }
                    val beforeDay = before.roundedSaleDay
                    val afterDay = after.roundedSaleDay
                    val diffDay =
                        ChronoUnit.DAYS.between(beforeDay, afterDay)
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

@SuppressLint("SimpleDateFormat")
fun getInitialiseDataRange(): String {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd")
    val firstDayOfMonth = Date(getTimeStampFirstDayOfMonth().time)
    val nowDate = Date()
    val firstDayOfMonthStr = dateFormat.format(firstDayOfMonth)
    val nowDateStr = dateFormat.format(nowDate)
    return "$firstDayOfMonthStr,$nowDateStr"
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