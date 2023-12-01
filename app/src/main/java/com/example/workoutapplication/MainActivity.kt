package com.example.workoutapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import androidx.room.Room
import com.example.workoutapplication.ui.theme.WorkoutApplicationTheme

class MainActivity : ComponentActivity() {

    val db = Room.databaseBuilder(
        applicationContext,
        WorkoutDatabase::class.java, "workout-database"
    ).build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WorkoutApplicationTheme {
                // A surface container using the 'background' color from the theme
                val navController = rememberNavController()
                NavHost(navController, startDestination = "create_workout") {
                    navigation(
                        startDestination = "new_workout",
                        route = "create_workout"
                    ) {
                        composable("new_workout") {entry ->
                            val viewModel = entry.sharedViewModel<WorkoutViewModel>(navController = navController)
                            val state by viewModel.currentWorkout.collectAsStateWithLifecycle()

                            StatefulWorkoutScreen(
                                viewModel = viewModel,
                                state = state,
                                onNavigate = { navController.navigate("select_days") }
                            )
                        }
                        composable("select_days") { entry ->
                            val viewModel = entry.sharedViewModel<WorkoutViewModel>(navController = navController)
                            val state by viewModel.currentWorkout.collectAsStateWithLifecycle()

                            StatefulSelectDaysScreen(
                                viewModel = viewModel,
                                onNavigate = { navController.navigate("exercises") }
                            )
                        }
                        composable("exercises") {entry ->
                            val viewModel = entry.sharedViewModel<WorkoutViewModel>(navController = navController)
                            val state by viewModel.currentWorkout.collectAsStateWithLifecycle()

                            StatefulExercisesScreen(
                                viewModel = viewModel,
                                state = state,
                                onNavigate = { navController.navigate("customize_exercises") }
                            )
                        }
                        composable("customize_exercises") {entry ->
                            val viewModel = entry.sharedViewModel<WorkoutViewModel>(navController = navController)
                            val state by viewModel.currentWorkout.collectAsStateWithLifecycle()

                            CustomizeExercisesScreen(
                                viewModel = viewModel,
                                state = state,
                                onNavigate = { navController.navigate("exercises") }
                            )
                        }
                    }
                }
            }
        }
    }
}


/*
@Preview
@Composable
fun StatelessWorkoutScreenPreview() {
    StatelessWorkoutScreen(
        name = "Workout name",
        onNameChange = {},
        onNavigate = {},
        showDialog = false,
        onShowDialogChange = {},
        dialogText = "Change Workout Name",
        onDialogTextChange = {}
    )
}

@Preview
@Composable
fun SelectDaysScreenPreview() {
    StatelessSelectDaysScreen(
        onNavigate = {},
        selectedDays = mutableListOf(false, false, false, true, false, false, false),
        daysOfWeek = listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"),
        onSetNameChange = {}
    )
}*/


@Preview
@Composable
fun ExercisesScreenPreview() {
    val temporaryViewModel = WorkoutViewModel()
    val days = listOf("Monday", "Tuesday", "Saturday")
    val amountOfExercises = 3

    days.forEach() { day ->
        val tempWorkday = WorkoutDay()
        tempWorkday.name.value = day

        for (i in 0..amountOfExercises) {
            val randomNumber = (0 until FakeRepository.exerciseNames.size).random()
            val tempExercise = Exercise(name = mutableStateOf(FakeRepository.exerciseNames[randomNumber]))
            tempWorkday.exercises.add(tempExercise)
        }

        temporaryViewModel.currentWorkout.value.days.add(tempWorkday)
    }
    val state by temporaryViewModel.currentWorkout.collectAsStateWithLifecycle()
    //ExercisesScreen(viewModel = temporaryViewModel, state = state)
    StatelessExercisesScreen(
        onRemoveExercise = { _, _ ->  },
        showDialog = false,
        onShowDialogChange = {},
        days = state.days,
        options = FakeRepository.exerciseNames,
        onExpandedChange = {},
        onAddExercise = { _, _ -> },
        selectedOption = "Select Exercise",
        onSelectedOption = {},
        currentDay = WorkoutDay(),
        onCurrentDay = {},
        onNavigate = {},
        onChangeSets = { _, _, _ -> },
        onChangeRepRange = { _, _, _ -> }
    )
}


@Composable
inline fun <reified T : ViewModel> NavBackStackEntry.sharedViewModel(navController: NavController): T {
    val navGraphRoute = destination.parent?.route ?: return viewModel()
    val parentEntry = remember(key1 = this) {
        navController.getBackStackEntry(navGraphRoute)
    }
    return viewModel(parentEntry)
}