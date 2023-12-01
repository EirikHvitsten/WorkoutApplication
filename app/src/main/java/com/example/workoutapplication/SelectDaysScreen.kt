package com.example.workoutapplication

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

@Composable
fun StatefulSelectDaysScreen(
    viewModel: WorkoutViewModel,

    onNavigate: () -> Unit
) {
    val selectedDays = remember { mutableStateListOf(false, false, false, false, false, false, false) }
    val daysOfWeek = remember { listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday") }

    StatelessSelectDaysScreen(
        onNavigate = onNavigate,
        selectedDays = selectedDays,
        daysOfWeek = daysOfWeek,
        onSetNameChange = { setName -> viewModel.addNewWorkoutDay(setName) }
    )
}


@Composable
fun StatelessSelectDaysScreen(
    onNavigate: () -> Unit,
    selectedDays: MutableList<Boolean>,
    daysOfWeek: List<String>,
    onSetNameChange: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        daysOfWeek.forEachIndexed { index, day ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ){
                Text(text = day, modifier = Modifier.weight(1f))
                Switch(
                    checked = selectedDays[index],
                    onCheckedChange = { isChecked ->
                        selectedDays[index] = isChecked
                    }
                )
            }
        }

        Button(
            onClick = {
                selectedDays.forEachIndexed { index, day ->
                    if (day) {
                        onSetNameChange(daysOfWeek[index])
                    }
                }
                onNavigate()
            },
            enabled = selectedDays.contains(true),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(id = R.string.next))
        }
    }
}