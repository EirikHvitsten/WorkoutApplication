package com.example.workoutapplication

import android.util.Range
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf

class Exercise(
    var name: MutableState<String> = mutableStateOf(""),
    var sets: MutableState<Int> = mutableIntStateOf(0),
    var repRange: MutableState<Pair<Int, Int>> = mutableStateOf(Pair(0,0)),
    var weight: MutableState<Int> = mutableIntStateOf(0),
    var rest: MutableState<Int> = mutableIntStateOf(0),
    var notes: MutableState<String> = mutableStateOf("")
) {
}