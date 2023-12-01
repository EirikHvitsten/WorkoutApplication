package com.example.workoutapplication

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Workout(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var name: MutableState<String> = mutableStateOf(""),
    var days: MutableList<WorkoutDay> = mutableListOf<WorkoutDay>()
) {
}