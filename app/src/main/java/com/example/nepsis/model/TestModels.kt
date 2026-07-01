package com.example.nepsis.model

import com.google.gson.annotations.SerializedName

data class QuestionModel(
    @SerializedName("id") val id: Int,
    @SerializedName("question") val question: String,
    @SerializedName("options") val options: List<OptionModel>
)

data class OptionModel(
    @SerializedName("text") val text: String,
    @SerializedName("category") val category: String
)