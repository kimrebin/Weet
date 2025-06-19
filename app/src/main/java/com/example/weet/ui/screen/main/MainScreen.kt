package com.example.weet.ui.screen.main

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.core.app.Person
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.weet.data.local.entity.PersonEntity
import com.example.weet.viewmodel.MainViewModel
import com.example.weet.repository.PersonRepository

@Composable
fun MainScreen(
    viewModel: MainViewModel = hiltViewModel(),
    onPersonClick: (personId: Int) -> Unit,
    onAddPerson: () -> Unit, // 새로운 사람 추가용 화면으로 이동
) {
    val personsByTag by viewModel.personByTag.collectAsState(initial = emptyMap())
    val tags = personsByTag.keys.ifEmpty { listOf("family", "friend", "business") }.toList()
    var selectedTag by remember { mutableStateOf(tags.first()) }
    val persons = personsByTag[selectedTag].orEmpty()

    Column(modifier = Modifier.fillMaxSize()) {
        TagSelector(
            tags = tags,
            selectedTag = selectedTag,
            onTagSelected = { selectedTag = it }
        )

        Box(modifier = Modifier.weight(1f)) {
            MindMapSection(
                persons = persons,
                onPersonClick = onPersonClick
            )
        }
        Spacer(modifier = Modifier.weight(1f))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(onClick = onAddPerson) {
                Text(text = "+ ADD")
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

@Composable
fun MindMapSection(
    persons: List<PersonEntity>,
    onPersonClick: (Int) -> Unit
) {
    if (persons.isEmpty()) {
        // 사용자 노드만 중앙에 표시
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 64.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = null, // 기본 이미지 대체 가능
                contentDescription = "Current User",
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
            )
        }
    } else {
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
                        .clickable { onPersonClick(person.id) },
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
}
