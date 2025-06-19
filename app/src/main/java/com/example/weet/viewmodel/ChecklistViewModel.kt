package com.example.weet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weet.data.local.entity.ChecklistResultEntity
import com.example.weet.repository.ChecklistRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.roundToInt

@HiltViewModel
class ChecklistViewModel @Inject constructor(
    private val repository: ChecklistRepository
) : ViewModel() {

    private val _latestResult = MutableStateFlow<ChecklistResultEntity?>(null)
    val latestResult: StateFlow<ChecklistResultEntity?> = _latestResult

    fun loadLatestChecklist(personId: Int) {
        viewModelScope.launch {
            repository.getLatestChecklist(personId).collect {
                _latestResult.value = it
            }
        }
    }

    fun saveChecklist(result: ChecklistResultEntity, tagWeight: Double) {
        viewModelScope.launch {
            repository.insertChecklistAndUpdateScore(result, tagWeight)

            val rqs = calculateRQS(
                frequency = result.frequencyScore,
                emotion = result.emotionScore,
                distance = result.distanceScore,
                tagWeight = tagWeight
            )

            // 점수를 정수로 변환 (예: 78.5 → 79)
            repository.updatePersonScore(result.personId, rqs.roundToInt())

            _latestResult.value = result
        }
    }

    companion object {
        fun calculateRQS(
            frequency: Float,
            emotion: Float,
            distance: Float,
            tagWeight: Double
        ): Double {
            val w1 = 0.4f
            val w2 = 0.3f
            val w3 = 0.2f
            val w4 = 0.1f
            return w1 * frequency + w2 * emotion + w3 * distance + w4 * tagWeight
        }
    }
}
