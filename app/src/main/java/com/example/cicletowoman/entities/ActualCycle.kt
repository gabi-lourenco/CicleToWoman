package com.example.cicletowoman.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ActualCycle(
    @PrimaryKey val uid: String,
    @ColumnInfo(name = "start_date") val startDate: String?,
    @ColumnInfo(name = "end_date") val endDate: String?,
    @ColumnInfo(name = "neutral_start_first_fertility") val neutralStartFirst: String?,
    @ColumnInfo(name = "neutral_end_first_fertility") val neutralEndFirst: String?,
    @ColumnInfo(name = "start_fertility") val startFertility: String?,
    @ColumnInfo(name = "end_fertility") val endFertility: String?,
    @ColumnInfo(name = "neutral_start_last_fertility") val neutralStartLast: String?,
    @ColumnInfo(name = "neutral_end_last_fertility") val neutralEndLast: String?,
    @ColumnInfo(name = "startDateInMillis") val startDateInMillis: Long?,
    @ColumnInfo(name = "endDateInMillis") val endDateInMillis: Long?,
    @ColumnInfo(name = "actual") val actual: Boolean?,

)