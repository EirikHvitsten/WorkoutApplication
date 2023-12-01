package com.example.workoutapplication

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class WorkoutViewModel: ViewModel() {
    private var _currentWorkout = MutableStateFlow( Workout() )
    val currentWorkout: StateFlow<Workout> = _currentWorkout.asStateFlow()

    private var _workoutDay = MutableStateFlow( WorkoutDay() )
    val workoutDay: StateFlow<WorkoutDay> = _workoutDay.asStateFlow()

    fun setWorkoutName(name: String) {
        _currentWorkout.value.name.value = name
    }

    fun addDay( name: String) {
        val newDay = WorkoutDay()
        newDay.name.value = name
        _currentWorkout.value.days.add( newDay )
    }

    fun addNewWorkoutDay(name: String) {
        val newDay = WorkoutDay()
        newDay.name.value = name
        _currentWorkout.value.days.add( newDay )
    }

    fun addNewExercise(currentWorkoutDay: WorkoutDay, name: String) {
        val newExercise = Exercise(name = mutableStateOf(name))
        currentWorkoutDay.exercises.add(newExercise)
    }

    fun removeExercise(currentWorkoutDay: WorkoutDay, exercise: Exercise) {
        currentWorkoutDay.exercises.remove(exercise)
    }

    fun changeExerciseSets(currentWorkoutDay: WorkoutDay, exercise: Exercise, newSetValue: Int) {
        currentWorkoutDay.exercises.find { it.name.value == exercise.name.value }?.let { it.sets.value = newSetValue }
    }

    fun changeExerciseReps(currentWorkoutDay: WorkoutDay, exercise: Exercise, newRepValue: Pair<Int, Int>) {
        currentWorkoutDay.exercises.find { it.name.value == exercise.name.value }?.let { it.repRange.value = newRepValue }
    }

    private var _testState = MutableStateFlow( mutableListOf<String>())
    val testState = _testState.asStateFlow()

    override fun onCleared() {
        super.onCleared()
        println("ViewModel cleared")
    }
}