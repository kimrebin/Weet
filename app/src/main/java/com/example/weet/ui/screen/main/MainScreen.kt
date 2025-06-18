package com.example.weet.ui.screen.main

import androidx.benchmark.perfetto.ExperimentalPerfettoTraceProcessorApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.weet.viewmodel.MainViewModel

@OptIn(ExperimentalPerfettoTraceProcessorApi::class)
@Composable
fun MainScreen(viewModel: MainViewModel = hiltViewModel(), onPersonClick: (personId: Int) -> Unit, onOpenChecklist:() -> Unit) {
    val personsByTag by viewModel.personByTag.collectAsState()
    val tags = personsByTag.keys.toList()
    var selectedTag by remember { mutableStateOf(tags.firstOrNull()) }
    val tagOptions = listOf("family", "friend", "business")
    val setSelectedTag = remember {mutableStateOf<String?>(null)}

    Column(modifier = Modifier.fillMaxSize()){
        TagSelector(
            tags = tags,
            selectedTag = selectedTag,
            onTagSelected = {selectedTag =it}
        )
        personsByTag[selectedTag]?.let {persons ->
            MindMapSection(
                persons = persons,
                onPersonClick = onPersonClick
                )
        }
        Spacer(modifier = Modifier.weight(1f))

        Row(
           modifier = Modifier
               .fillMaxWidth()
               .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = onOpenChecklist) {
                Text("Check list")
            }

            val firstPerson = personsByTag[selectedTag]?.firstOrNull()
            Button(
                onClick = {
                    firstPerson?.let { person ->
                        onPersonClick(person.id)
                    }
                },
                enabled = firstPerson != null
            ) {
                Text("Profile")
            }
        }
    }


}
@Composable
fun TagSelector(
    tags: List<String>,
    selectedTag: String?,
    onTagSelected: (String) -> Unit
) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(tags) { tag ->
            FilterChip(
                selected = tag == selectedTag,
                onClick = { onTagSelected(tag) },
                label = { Text(tag) }
            )
        }
    }
}

@OptIn(ExperimentalPerfettoTraceProcessorApi::class)

@Composable
fun MindMapSection(
    persons: List<com.example.weet.repository.Person>,
    onPersonClick: (Int) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        items(persons) { person ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
                    .clickable { onPersonClick(person.id) }, // ✅ 클릭 이벤트
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(8.dp)
                ) {
                    AsyncImage(
                        model = person.photoUrl,
                        contentDescription = null,
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(text = person.name, style = MaterialTheme.typography.titleMedium)
                        Text(text = person.tag)
                    }
                }
            }
        }
    }
}
