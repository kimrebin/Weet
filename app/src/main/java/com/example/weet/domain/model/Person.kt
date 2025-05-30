package com.example.weet.domain.model

data class Person(
    val id: Int,
    val name: String,
    val photoUrl: String?,
    val tag: String,
    val score: Int,
    val tagWeight: Float
)

