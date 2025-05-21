package com.anand.catchandroidassessment.repository

interface DataRepository {
    suspend fun fetchJson(): String
}