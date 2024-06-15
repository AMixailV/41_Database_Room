package ru.mixail_akulov.a41_database_room.model.boxes.room.entities

import android.graphics.Color
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.mixail_akulov.a41_database_room.model.boxes.entities.Box

@Entity(
    tableName = "boxes"
)
data class BoxDbEntity(
    @PrimaryKey @ColumnInfo(name = "id") val id: Long,
    @ColumnInfo(name = "color_name") val colorName: String,
    @ColumnInfo(name = "color_value") val colorValue: String
) {
    fun toBox(): Box = Box(
        id = id,
        colorName = colorName,
        colorValue = Color.parseColor(colorValue)
    )
}
