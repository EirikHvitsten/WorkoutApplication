package com.example.workoutapplication

import android.graphics.drawable.Icon
import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlin.math.absoluteValue


@Composable
fun StatefulExercisesScreen(
    viewModel: WorkoutViewModel,
    state: Workout,
    onNavigate: () -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf("Select Exercise") }
    var currentDay: WorkoutDay by remember { mutableStateOf(WorkoutDay()) }


    StatelessExercisesScreen(
        onRemoveExercise = { workoutDay, exercise ->  viewModel.removeExercise(workoutDay, exercise) },
        showDialog = showDialog,
        onShowDialogChange = { showDialog = it },
        days = state.days,
        options = FakeRepository.exerciseNames,
        expanded = expanded,
        onExpandedChange = { expanded = it },
        onAddExercise = { day, option -> viewModel.addNewExercise( day, option ) },
        selectedOption = selectedOption,
        onSelectedOption = { selectedOption = it },
        currentDay = currentDay,
        onCurrentDay = { currentDay = it },
        onNavigate = onNavigate,
        onChangeSets = { workoutDay, exercise, value -> viewModel.changeExerciseSets(workoutDay, exercise, value) },
        onChangeRepRange = { workoutDay, exercise, value -> viewModel.changeExerciseReps(workoutDay, exercise, value) }
    )
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomEnd
    ) {
        Button(onClick = {
            state.days.forEach { day ->
                day.exercises.forEach { exercise ->
                    Log.d("Workout Info", "Exercise: ${exercise.name.value} Sets: ${exercise.sets.value} Rep Range: ${exercise.repRange.value}")
                }
            }
        }) {
            Text(text = stringResource(id = R.string.next))
        }
    }
}


@Composable
fun StatelessExercisesScreen(
    onRemoveExercise: (WorkoutDay, Exercise) -> Unit,
    showDialog: Boolean,
    onShowDialogChange: (Boolean) -> Unit,
    days: List<WorkoutDay>,
    options: List<String>,
    expanded: Boolean = false,
    onExpandedChange: (Boolean) -> Unit,
    onAddExercise: (WorkoutDay, String) -> Unit,
    selectedOption: String,
    onSelectedOption: (String) -> Unit,
    currentDay: WorkoutDay,
    onCurrentDay: (WorkoutDay) -> Unit,
    onNavigate: () -> Unit,
    onChangeSets: (WorkoutDay, Exercise, Int) -> Unit,
    onChangeRepRange: (WorkoutDay, Exercise, Pair<Int, Int>) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        days.forEach{ workoutDay ->
            item {
                Text(
                    text = workoutDay.name.value,
                    style = TextStyle(fontSize = 22.sp)
                )

                workoutDay.exercises.forEach{ exercise ->
                    Spacer(modifier = Modifier.height(4.dp))
                    ExerciseInfoTwo(
                        exercise = exercise,
                        onChangeExerciseSets = { exercise, value -> onChangeSets(workoutDay, exercise, value) },
                        onChangeRepRange = { exercise, value -> onChangeRepRange(workoutDay, exercise, value) },
                        onRemoveExercise = { onRemoveExercise(workoutDay, exercise) }
                    )

                }
                Button(onClick = {
                    onShowDialogChange(true)
                    onCurrentDay(workoutDay)
                }) {
                    Text(text = stringResource(id = R.string.add_exercise))
                }
            }
        }
    }
    if (showDialog) {
        AddExercise(
            onShowDialogChange = onShowDialogChange,
            selectedOption = selectedOption,
            onExpandedChange = onExpandedChange,
            expanded = expanded,
            options = options,
            onAddExercise = onAddExercise,
            currentDay = currentDay,
            onSelectedOption = onSelectedOption
        )
    }
}


@Composable
fun ExerciseInfoTwo(
    exercise: Exercise,
    setOptions: List<Int> = (1..10).toList(), // Add your desired options here
    onChangeExerciseSets: (Exercise, Int) -> Unit,
    onChangeRepRange: (Exercise, Pair<Int, Int>) -> Unit,
    onRemoveExercise: (Exercise) -> Unit
) {
    var selectedSet by remember { mutableStateOf(exercise.sets.value) }
    var selectedRepRange by remember { mutableStateOf(exercise.repRange.value) }
    var repRangeOptionsDynamicMutable by remember { mutableStateOf(Pair((1..30).toList(), (1..30).toList())) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .border(width = 1.dp, color = Color.Black)
            .padding(8.dp)
    ) {
        Column(modifier = Modifier.align(Alignment.CenterStart)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = exercise.name.value,
                    style = TextStyle(fontSize = 16.sp)
                )
                Button(onClick = { onRemoveExercise(exercise)}) {
                    Text("Remove")
                }
            }
            DropdownMenu(
                items = setOptions,
                selectedItem = exercise.sets.value,
                onItemSelected = {
                    selectedSet = it
                    onChangeExerciseSets(exercise, it)
                },
                label = "Sets "
            )
            Row(
                modifier = Modifier
            ) {
                DropdownMenu(
                    items = repRangeOptionsDynamicMutable.first,
                    selectedItem = exercise.repRange.value.first,
                    onItemSelected = {
                        selectedRepRange = Pair(it, maxOf(it, selectedRepRange.second))
                        onChangeRepRange(exercise, selectedRepRange)

                        repRangeOptionsDynamicMutable = Pair(
                            repRangeOptionsDynamicMutable.first,
                            (selectedRepRange.first..30).toList() // Changes the second list to start at the start of the Rep Range
                        )
                    },
                    label = "Rep Range "
                )
                DropdownMenu(
                    items = repRangeOptionsDynamicMutable.second,
                    selectedItem = exercise.repRange.value.second,
                    onItemSelected = {
                        selectedRepRange = Pair(selectedRepRange.first, it)
                        onChangeRepRange(exercise, selectedRepRange)
                    },
                    label = "  -  "
                )
            }
        }
    }
}


@Composable
fun <T> DropdownMenu(
    items: List<T>,
    selectedItem: T,
    onItemSelected: (T) -> Unit,
    label: String
) {
    var expanded by remember { mutableStateOf(false) }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label)
        Button(onClick = { expanded = !expanded }) {
            Text("$selectedItem")
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            items.forEach { item ->
                DropdownMenuItem(
                    text = { Text(text = item.toString()) },
                    onClick = {
                    onItemSelected(item)
                    expanded = false
                    }
                )
            }
        }
    }
}


@Composable
fun AddExercise(
    onShowDialogChange: (Boolean) -> Unit,
    selectedOption: String,
    onExpandedChange: (Boolean) -> Unit,
    expanded: Boolean,
    options: List<String>,
    onAddExercise: (WorkoutDay, String) -> Unit,
    currentDay: WorkoutDay,
    onSelectedOption: (String) -> Unit
) {

    var currOptions by remember { mutableStateOf(options.toMutableList()) }

    // Remove the options that are already in the current day
    currentDay.exercises.forEach { exercise ->
        currOptions.remove(exercise.name.value)
        Log.d("AddExercise", "Option removed: ${exercise.name.value}")
    }
    Log.d("AddExercise", "currOptions: $currOptions")

    AlertDialog(
        onDismissRequest = { onShowDialogChange(false) },
        title = { Text(text = stringResource(id = R.string.add_exercise)) },
        text = {
            Text(
                text = selectedOption,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = { onExpandedChange(true) })
                    .padding(16.dp)
            )
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { onExpandedChange(false) },
                modifier = Modifier.fillMaxWidth()
            ) {
                currOptions.forEach { option ->
                    val interactionSource = remember { MutableInteractionSource() }
                    DropdownMenuItem(
                        onClick = {
                            onSelectedOption(option)
                            onExpandedChange(false)
                        },
                        interactionSource = interactionSource,
                        text = { Text(text = option) }
                    )
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                onShowDialogChange(false)
                onAddExercise(currentDay, selectedOption)
                onSelectedOption("Select Exercise")
            }) {
                Text(text = stringResource(id = R.string.add))
            }
        }
    )
}



@Preview
@Composable
fun ExerciseInfoTwoPreview() {
    LazyColumn(modifier = Modifier.padding(8.dp), content = {
        item {
            ExerciseInfoTwo(
                exercise = Exercise(
                    name = remember { mutableStateOf("Bench Press") },
                    //sets = remember { mutableStateOf(3) },
                    //repRange = remember { mutableStateOf(5 to 10) }
                ),
                onChangeExerciseSets = { _, _ -> }, onChangeRepRange = { _, _ -> }, onRemoveExercise = {  })
        }
        item {
            ExerciseInfoTwo(
                exercise = Exercise(
                    name = remember { mutableStateOf("Deadlift") },
                    //sets = remember { mutableStateOf(3) },
                    //repRange = remember { mutableStateOf(3 to 5) }
                ),
                onChangeExerciseSets = { _, _ -> }, onChangeRepRange = { _, _ -> }, onRemoveExercise = {  })
        }
        item {
            ExerciseInfoTwo(
                exercise = Exercise(
                    name = remember { mutableStateOf("Pullup") },
                    //sets = remember { mutableStateOf(3) },
                    //repRange = remember { mutableStateOf(8 to 12) }
                ),
                onChangeExerciseSets = { _, _ -> }, onChangeRepRange = { _, _ -> }, onRemoveExercise = {  })
        }
    })
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Exercise(
    exercise: Exercise,
    workoutDay: WorkoutDay,
    onRemoveExercise: (WorkoutDay, Exercise) -> Unit,
    onChangeSets: (WorkoutDay, Exercise, Int) -> Unit
) {
    var showEdit by remember  { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
            .border(width = 1.dp, color = Color.Black)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, end = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Text(text = exercise.name.value)
            Button(onClick = {
                onRemoveExercise(workoutDay, exercise)
            }
            ) {
                Text(text = stringResource(id = R.string.remove))
            }
            Button(onClick = {
                showEdit = !showEdit
            }) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit",
                    modifier = Modifier.size(16.dp)
                )
            }
        }
        Row (
            modifier = Modifier,
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(
                modifier = Modifier.padding(8.dp),
                text = "Sets"
            )
            var setText by remember { mutableStateOf(exercise.sets.value.toString()) }
            if (showEdit) {
                TextField(
                    value = setText,
                    onValueChange = { newText ->
                        setText = newText
                        if (newText.toIntOrNull() != null) {
                            //exercise.sets.value = newText.toInt()
                            onChangeSets(workoutDay, exercise, newText.toInt())
                        }
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.width(60.dp)
                )
            } else {
                Text(text = exercise.sets.value.toString())
            }
        }
        Row (
            modifier = Modifier,
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(
                modifier = Modifier.padding(8.dp),
                text = "Rep Range"
            )
            var minRepRangeText by remember { mutableStateOf(exercise.repRange.value.first.toString()) }
            var maxRepRangeText by remember { mutableStateOf(exercise.repRange.value.second.toString()) }
            if (showEdit) {
                TextField(
                    value = minRepRangeText,
                    onValueChange = { newText ->
                        minRepRangeText = newText
                        if (newText.toIntOrNull() != null) {
                            exercise.repRange.value = exercise.repRange.value.copy(first = newText.toInt())
                        }
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.width(60.dp)
                )
                Text(text = " - ")
                TextField(
                    value = maxRepRangeText,
                    onValueChange = { newText ->
                        maxRepRangeText = newText
                        if (newText.toIntOrNull() != null) {
                            exercise.repRange.value = exercise.repRange.value.copy(second = newText.toInt())
                        }
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.width(60.dp)
                )
            } else {
                Text(text = minRepRangeText)
                Text(text = " - ")
                Text(text = maxRepRangeText)
            }
        }
    }
}


@Preview
@Composable
fun ExercisesScreenPrevieww() {
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