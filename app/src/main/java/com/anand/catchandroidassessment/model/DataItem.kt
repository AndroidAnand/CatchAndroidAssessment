package com.anand.catchandroidassessment.model

import kotlinx.serialization.Serializable

@Serializable
data class DataItem(
    val id: Int,
    val title: String,
    val subtitle: String,
    val content: String
)

