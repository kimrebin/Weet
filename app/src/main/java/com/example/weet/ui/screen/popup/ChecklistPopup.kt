package com.example.weet.ui.screen.popup

import android.app.TimePickerDialog
import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weet.data.local.entity.ChecklistResultEntity
import com.example.weet.data.local.entity.PersonEntity
import com.example.weet.viewmodel.ChecklistViewModel
import java.util.*

@Composable
fun SchedulePopup(
    people: List<PersonEntity>,
    personId: Int?,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    var selectedPerson by remember { mutableStateOf<PersonEntity?>(null) }
    var showChecklist by remember { mutableStateOf(false) }
    var popupTime by remember { mutableStateOf("시간 미설정") }

    LaunchedEffect(personId) {
        if (personId != null) {
            selectedPerson = people.find { it.id == personId }
            showChecklist = true
        }
    }

    if (!showChecklist) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text("인물을 선택해주세요") },
            text = {
                Column {
                    people.forEach { person ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .selectable(
                                    selected = selectedPerson == person,
                                    onClick = { selectedPerson = person }
                                )
                                .padding(8.dp)
                        ) {
                            RadioButton(
                                selected = selectedPerson == person,
                                onClick = { selectedPerson = person }
                            )
                            Text(text = person.name, modifier = Modifier.padding(start = 8.dp))
                        }
                    }
                }
            },
            confirmButton = {
                Button(onClick = {
                    if (selectedPerson != null) showChecklist = true
                }) {
                    Text("확인")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = onDismiss) {
                    Text("취소")
                }
            }
        )
    } else {
        selectedPerson?.let { person ->
            val tagWeight = when (person.tag.lowercase()) {
                "family" -> 1.2
                "friend" -> 1.0
                "business" -> 0.8
                "other" -> 0.5
                else -> 1.0
            }
            ChecklistPopup(
                personId = person.id,
                personTagWeight = tagWeight,
                popupTime = popupTime,
                onPopupTimeChange = { popupTime = it },
                context = context,
                onDismiss = onDismiss
            )
        }
    }
}

@Composable
fun ChecklistPopup(
    personId: Int,
    personTagWeight: Double,
    popupTime: String,
    onPopupTimeChange: (String) -> Unit,
    context: Context,
    viewModel: ChecklistViewModel = viewModel(),
    onDismiss: () -> Unit
) {
    var frequency by remember { mutableStateOf<Float?>(null) }
    var emotion by remember { mutableStateOf<Float?>(null) }
    var distance by remember { mutableStateOf<Float?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("체크리스트 작성") },
        text = {
            Column {
                Text("얼마나 자주 연락하나요?", fontWeight = FontWeight.Bold)
                RadioOption("자주 (1주 이내)", 1.0f, frequency) { frequency = it }
                RadioOption("가끔 (1주~1달)", 0.5f, frequency) { frequency = it }
                RadioOption("거의 없음 (1달 이상)", 0.0f, frequency) { frequency = it }

                Text("이 사람과의 감정은 요즘 어떤가요?", fontWeight = FontWeight.Bold)
                RadioOption("주로 긍정적이에요", 1.0f, emotion) { emotion = it }
                RadioOption("보통이에요", 0.5f, emotion) { emotion = it }
                RadioOption("자주 불편하거나 부정적이에요", 0.0f, emotion) { emotion = it }

                Text("이 사람은 당신에게 얼마나 가까운 존재인가요?", fontWeight = FontWeight.Bold)
                RadioOption("매우 가까운 사람 (마음 터놓는 사이)", 1.0f, distance) { distance = it }
                RadioOption("보통 (가끔 의지하거나 대화함)", 0.5f, distance) { distance = it }
                RadioOption("심리적으로 거리가 있는 편", 0.0f, distance) { distance = it }

                Spacer(modifier = Modifier.height(8.dp))

                Text("팝업 시간 설정: $popupTime")
                Button(onClick = {
                    val calendar = Calendar.getInstance()
                    TimePickerDialog(
                        context,
                        { _, hour, minute ->
                            onPopupTimeChange(String.format("%02d:%02d", hour, minute))
                        },
                        calendar.get(Calendar.HOUR_OF_DAY),
                        calendar.get(Calendar.MINUTE),
                        true
                    ).show()
                }) {
                    Text("시간 설정")
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                if (frequency != null && emotion != null && distance != null) {
                    val rqs = ChecklistViewModel.calculateRQS(
                        frequency!!, emotion!!, distance!!, personTagWeight
                    )
                    viewModel.saveChecklist(
                        ChecklistResultEntity(
                            personId = personId,
                            frequencyScore = frequency!!,
                            emotionScore = emotion!!,
                            distanceScore = distance!!,
                            tagWeight = personTagWeight,
                            rqsScore = rqs
                        ),
                        tagWeight = personTagWeight
                    )
                    onDismiss()
                }
            }) {
                Text("저장")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text("취소")
            }
        }
    )
}

@Composable
fun RadioOption(
    text: String,
    value: Float,
    selectedValue: Float?,
    onSelect: (Float) -> Unit
) {
    Row(
        Modifier
            .fillMaxWidth()
            .selectable(
                selected = selectedValue == value,
                onClick = { onSelect(value) }
            )
            .padding(4.dp)
    ) {
        RadioButton(
            selected = selectedValue == value,
            onClick = { onSelect(value) }
        )
        Text(text, Modifier.padding(start = 8.dp))
    }
}
