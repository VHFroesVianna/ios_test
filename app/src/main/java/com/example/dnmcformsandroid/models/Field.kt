package com.example.dnmcformsandroid.models

import com.google.gson.annotations.SerializedName

enum class FieldType(val type: String) {
    @SerializedName("text") TEXT("text"),
    @SerializedName("number") NUMBER("number"),
    @SerializedName("dropdown") DROPDOWN("dropdown"),
    @SerializedName("description") DESCRIPTION("description")
}

data class Field(
    val uuid: String,
    val name: String,
    val label: String,
    val type: String,
    val options: List<String>? = null,
    val required: Boolean = false
)