package com.example.workoutapplication

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf

class WorkoutDay(
    var name: MutableState<String> = mutableStateOf(""),
    var exercises: MutableList<Exercise> = mutableStateListOf<Exercise>()
) {

}