package com.example.weet.ui.screen.relationshipMap

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.weet.viewmodel.MainViewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.weet.ui.screen.main.TagSelector
import androidx.compose.foundation.layout.Column
import com.example.weet.data.local.entity.PersonEntity
import com.example.weet.data.local.entity.toFriend
import com.example.weet.data.local.entity.Friend

@Composable
fun RelationshipScreen(
    viewModel: MainViewModel = hiltViewModel(),
    onBack: () -> Unit
) {
    val allFriends: List<Friend> by viewModel.allFriends.collectAsState()

    val tagList = listOf("family", "friend", "business", "others")
    var selectedTag by remember { mutableStateOf(tagList.first()) }

    Column {
        TagSelector(
            tags = tagList,
            selectedTag = selectedTag,
            onTagSelected = { selectedTag = it }
        )

        RelationshipMap(
            allFriends = allFriends,
            selectedTag = selectedTag
        )
    }
}