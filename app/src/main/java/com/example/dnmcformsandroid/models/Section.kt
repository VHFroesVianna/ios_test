package com.example.dnmcformsandroid.models

data class Section(
    val uuid: String,
    val title: String,
    val from: Int,
    val to: Int,
    val index: Int,
    val fields: List<Field>
)