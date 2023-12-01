package com.example.workoutapplication

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

@Composable
fun StatefulWorkoutScreen(
    viewModel: WorkoutViewModel,
    state: Workout,
    onNavigate: () -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }
    var dialogText by remember { mutableStateOf("") }

    StatelessWorkoutScreen(
        name = state.name.value,
        onNameChange = { newName -> viewModel.setWorkoutName(newName) },
        showDialog = showDialog,
        onShowDialogChange = { showDialog = it },
        dialogText = dialogText,
        onDialogTextChange = { dialogText = it },
        onNavigate = onNavigate,
    )
}


@Composable
fun StatelessWorkoutScreen(
    name: String,
    onNameChange: (String) -> Unit,
    showDialog: Boolean,
    onShowDialogChange: (Boolean) -> Unit,
    dialogText: String,
    onDialogTextChange: (String) -> Unit,
    onNavigate: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ){
        Text(text = stringResource(id = R.string.workout_name))

        if (name.isEmpty()) {
            NameInput(
                dialogText = dialogText,
                onNameChange = onNameChange,
                onDialogTextChange = onDialogTextChange
            )
        } else {
            WorkoutNameDisplay(
                name = name,
                onNavigate = onNavigate,
                onShowDialogChange = onShowDialogChange
            )
        }
        if (showDialog) {
            ChangeNameDialog(
                onNameChange = onNameChange,
                onShowDialogChange = onShowDialogChange,
                dialogText = dialogText,
                onDialogTextChange = onDialogTextChange
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangeNameDialog(
    onNameChange: (String) -> Unit,
    onShowDialogChange: (Boolean) -> Unit,
    dialogText: String,
    onDialogTextChange: (String) -> Unit
) {
    AlertDialog(
        onDismissRequest = { onShowDialogChange(false) },
        title = { Text(text = stringResource(id = R.string.change_workout_name)) },
        text = {
            TextField(
                value = dialogText,
                onValueChange = onDialogTextChange,
                label = { Text( text = stringResource(id = R.string.input_workout_name_here)) }
            )
        },
        confirmButton = {
            Button(onClick = {
                onShowDialogChange(false)
                onNameChange(dialogText)
            }) {
                Text(text = stringResource(id = R.string.change))
            }
        })
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NameInput(
    dialogText: String,
    onNameChange: (String) -> Unit,
    onDialogTextChange: (String) -> Unit
) {
    TextField(
        value = dialogText,
        onValueChange = onDialogTextChange,
        label = { Text(text = stringResource(id = R.string.workout_name)) }
    )
    Button(onClick = { onNameChange(dialogText) }) {
        Text(text = stringResource(id = R.string.set_name))
    }
}


@Composable
fun WorkoutNameDisplay(
    name: String,
    onNavigate: () -> Unit,
    onShowDialogChange: (Boolean) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ){
            Text(text = name)
            Button(onClick = { onShowDialogChange(true) }) {
                Text(text = stringResource(id = R.string.change_name))
            }
        }
        Button(onClick = onNavigate) {
            Text(text = stringResource(id = R.string.next))
        }
    }
}