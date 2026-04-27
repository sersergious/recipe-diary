package com.ingridientsinc.recipe.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categories")
data class Categories(
    @PrimaryKey
    val id: Int = 0,
    val name: String)

