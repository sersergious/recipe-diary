package com.ingridientsinc.recipe.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey


@Entity(
    tableName = "recipe",
    foreignKeys = [
        ForeignKey(
            entity = Categories::class,
            parentColumns = ["id"],
            childColumns = ["category_id"],
            //onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Recipes(
    @PrimaryKey
    val id: Int = 0,
    val name: String,
    @ColumnInfo(name = "category_id")
    val categoryId: Int = 0
)
