package com.test.marvelcomics.domain.entity.api

import com.google.gson.annotations.SerializedName

data class Comics(
    @SerializedName("results")
    val comicsList: List<Comic>
)
