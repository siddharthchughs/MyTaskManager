package com.example.mytaskmanager.createtask

import android.widget.DatePicker
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.mytaskmanager.R
import com.example.mytaskmanager.TaskManagerRoute
import java.util.Calendar
import java.util.Date

@Composable
fun CreateTaskScreen(
    navigateToHome: NavHostController,
) {
    val createTaskViewModel: CreateTaskViewModel = hiltViewModel()
    val createTaskUIState =
        createTaskViewModel.createTaskUIState.collectAsStateWithLifecycle(CreateTaskUIState.Loading).value

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        modifier = Modifier
            .padding(8.dp)
            .fillMaxSize()
    ) {
        CreateTaskToolbar(
            navigationHostController = navigateToHome
        )
        CreateTaskScreenStructure(
            createTaskUIState = createTaskUIState,
            taskTitleText = createTaskViewModel.taskTitleText.value,
            onTitleChange = createTaskViewModel::onTaskTitleChange,
            descriptionText = createTaskViewModel.descriptionText.value,
            onDescriptionChange = createTaskViewModel::onDescriptionChange,
            dateText = createTaskViewModel.dateText.value,
            onDateTextChange = createTaskViewModel::onDateSelected,
            taskPriority = createTaskViewModel.selectedOption.value,
            onPrioritySelect = createTaskViewModel::onPrioritySelect,
            createTask = createTaskViewModel::createTask,
            navigateToHome = navigateToHome
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTaskToolbar(
    navigationHostController: NavHostController
) {
    TopAppBar(
        title = {
            Text(text = stringResource(R.string.create_task_title))
        },
        navigationIcon = {
            IconButton(
                onClick = {
                    navigationHostController.navigateUp()
                }
            ) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
            }
        },
    )
}

@Composable
fun CreateTaskScreenStructure(
    createTaskUIState: CreateTaskUIState,
    taskTitleText: String,
    onTitleChange: (String) -> Unit,
    descriptionText: String,
    onDescriptionChange: (String) -> Unit,
    dateText: String,
    onDateTextChange: (String) -> Unit,
    taskPriority: String,
    onPrioritySelect: (String) -> Unit,
    createTask: () -> Unit,
    navigateToHome: NavHostController,
) {
    when (createTaskUIState) {
        CreateTaskUIState.Loaded -> {
            CreateTaskForm(
                taskTitleText = taskTitleText,
                onTitleChange = onTitleChange,
                descriptionText = descriptionText,
                onDescriptionChange = onDescriptionChange,
                dateText = dateText,
                onDateTextChange = onDateTextChange,
                selectedOption = taskPriority,
                onPrioritySelect = onPrioritySelect,
                createTask = createTask,
                navigateToHome = navigateToHome
            )
        }

        CreateTaskUIState.Loading -> {
            Progressbar()
        }
    }
}

@Composable
fun CreateTaskForm(
    taskTitleText: String,
    onTitleChange: (String) -> Unit,
    descriptionText: String,
    onDescriptionChange: (String) -> Unit,
    dateText: String,
    onDateTextChange: (String) -> Unit,
    selectedOption: String,
    onPrioritySelect: (String) -> Unit,
    createTask: () -> Unit,
    navigateToHome: NavHostController
) {
    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .padding(start = 16.dp, top = 10.dp, end = 16.dp)
    ) {
        CreateTitleInputField(
            titleText = taskTitleText,
            onTitleChange = onTitleChange,
        )

        CreateDescriptionTextInput(
            descriptionText = descriptionText,
            onDescriptionChange = onDescriptionChange,
        )


        CreateTaskShowDateTextInput(
            dateText = dateText,
            onDateTextChange = onDateTextChange
        )

        Spacer(
            modifier = Modifier
                .height(16.dp)
        )

        SingleHeaderLabel(
            header = stringResource(R.string.select_priority_mode_label)
        )

        CreateTaskSelectPriorityMode(
            selectedOption = selectedOption,
            onPrioritySelect = onPrioritySelect
        )
        Spacer(
            modifier = Modifier
                .height(16.dp)
        )

        CreateNewTaskButton(
            title = taskTitleText,
            description = descriptionText,
            date = dateText,
            priority = selectedOption,
            createNewTask = createTask,
            navigateToHome = navigateToHome
        )
    }
}

@Composable
fun SingleHeaderLabel(header: String) {
    Column(
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(
            text = header,
            style = TextStyle(
                fontSize = 16.sp,
                fontStyle = MaterialTheme.typography.headlineMedium.fontStyle,
                fontWeight = FontWeight.W600
            )
        )
    }
    Spacer(
        modifier = Modifier
            .height(16.dp)
    )

}

@Composable
fun CreateTitleInputField(
    titleText: String,
    onTitleChange: (String) -> Unit,
) {

    val focusManager = LocalFocusManager.current

    CustomTextInputField(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 56.dp)
            .border(
                BorderStroke(1.dp, color = Color.LightGray),
                shape = RoundedCornerShape(10.dp)
            ),
        textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Start),
        value = titleText,
        onValueChange = {
            onTitleChange(it)
        },
        singleLine = true,
        placeholder = stringResource(R.string.create_task_title_placeholder),
        keyboardActions = KeyboardActions(
            onNext = {
                focusManager.moveFocus(FocusDirection.Next)
            }
        ),
        trailingIcon = {
            if (titleText.isNotEmpty()) {
                IconButton(onClick = {
                    onTitleChange("")
                }) {
                    Icon(
                        Icons.Filled.Clear, contentDescription = "Clear Text"
                    )
                }
            }
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Next
        ),
        visualTransformation = VisualTransformation.None
    )

    Spacer(
        modifier = Modifier
            .height(16.dp)
    )
}

@Composable
fun CreateDescriptionTextInput(
    descriptionText: String,
    onDescriptionChange: (String) -> Unit,
) {
    val focusManager = LocalFocusManager.current
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        CustomTextInputField(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .border(
                    BorderStroke(1.dp, color = Color.LightGray),
                    shape = RoundedCornerShape(10.dp)
                ),
            textStyle = TextStyle(
                textAlign = TextAlign.Start
            ),
            value = descriptionText,
            onValueChange = {
                onDescriptionChange(it)
            },
            singleLine = false,
            placeholder = stringResource(R.string.add_task_description_placeholder),
            keyboardActions = KeyboardActions(
                onNext = {
                    focusManager.moveFocus(FocusDirection.Next)
                },
            ),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            visualTransformation = VisualTransformation.None
        )
        Spacer(
            Modifier.height(height = 4.dp)
        )
        if (descriptionText.isNotEmpty()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onDescriptionChange("")
                    },
                horizontalArrangement = Arrangement.SpaceEvenly,
            ) {
                Text(
                    text = stringResource(R.string.clear_text),
                    modifier = Modifier
                        .padding(end = 2.dp)
                        .fillMaxWidth(),
                    style = TextStyle(
                        color = if (descriptionText.isNotEmpty()) Color.Blue else Color.Transparent,
                        textAlign = TextAlign.End
                    ),

                    )
            }
        } else {
            return
        }

    }
    Spacer(
        modifier = Modifier
            .height(20.dp)
    )

}

@Composable
fun CreateTaskShowDateTextInput(
    dateText: String,
    onDateTextChange: (String) -> Unit,
) {
    ShowDateTextSelected(
        dateText = dateText,
        onDateTextChange = onDateTextChange
    )
    Spacer(
        modifier = Modifier
            .height(16.dp)
    )

}


@Composable
fun CustomTextInputField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    singleLine: Boolean,
    placeholder: String,
    textStyle: TextStyle,
    trailingIcon: @Composable (() -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardActions: KeyboardActions,
    keyboardOptions: KeyboardOptions,
) {

    TextField(
        modifier = modifier,
        value = value,
        onValueChange = onValueChange,
        singleLine = singleLine,
        placeholder = {
            Text(text = placeholder)
        },
        textStyle = textStyle,
        trailingIcon = trailingIcon,
        visualTransformation = visualTransformation,
        keyboardActions = keyboardActions,
        keyboardOptions = keyboardOptions,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surface,
            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
            unfocusedIndicatorColor = MaterialTheme.colorScheme.surface,
            focusedIndicatorColor = MaterialTheme.colorScheme.surface
        )
    )
}


@Composable
fun ShowDateTextSelected(
    dateText: String,
    onDateTextChange: (String) -> Unit,
) {
    val mContext = LocalContext.current
    val mYear: Int
    val mMonth: Int
    val mDay: Int
    val mCalendar = Calendar.getInstance()
    mYear = mCalendar.get(Calendar.YEAR)
    mMonth = mCalendar.get(Calendar.MONTH)
    mDay = mCalendar.get(Calendar.DAY_OF_MONTH)
    mCalendar.time = Date()

    val selectedDateText = remember { mutableStateOf(dateText) }

    val mDatePickerDialog = android.app.DatePickerDialog(
        mContext,
        { _: DatePicker, mYear: Int, mMonth: Int, mDayOfMonth: Int ->
            selectedDateText.value = "$mDayOfMonth/${mMonth + 1}/$mYear"
        }, mYear, mMonth, mDay
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        onDateTextChange(selectedDateText.value)
        if (selectedDateText.value.isEmpty())
            Text(text = "Select Date", fontSize = 20.sp, textAlign = TextAlign.Center)
        else
            Text(
                text = "Selected date: ${selectedDateText.value}",
                fontSize = 20.sp,
                textAlign = TextAlign.Center
            )

        IconButton(
            onClick = {
                mDatePickerDialog.show()
            }
        ) {
            Icon(imageVector = Icons.Default.DateRange, contentDescription = "")
        }
    }
}

@Composable
fun CreateTaskSelectPriorityMode(
    selectedOption: String,
    onPrioritySelect: (String) -> Unit
) {
    val priorityOption = remember { mutableStateOf(selectedOption) }
    val radioOptions = listOf("Low", "Medium", "High")
    val mContext = LocalContext.current

    Column(Modifier.selectableGroup()) {
        radioOptions.forEach { text ->
            Row(
                Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .selectable(
                        selected = (priorityOption.value == text), // Use priorityOption.value here
                        onClick = {
                            Toast.makeText(
                                mContext.applicationContext,
                                priorityOption.value,
                                Toast.LENGTH_SHORT
                            ).show()
                            onPrioritySelect(text) // Uncomment this line
                            priorityOption.value = text
                        },
                        role = Role.RadioButton
                    )
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = (priorityOption.value == text),
                    onClick = {}
                )
                Text(
                    text = text,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
        }
    }
}

@Composable
fun CreateNewTaskButton(
    title: String,
    description: String,
    date: String,
    priority: String,
    createNewTask: () -> Unit,
    navigateToHome: NavHostController
) {
    Button(
        onClick = {
            createNewTask()
            navigateToHome.navigate(TaskManagerRoute.TaskHomeScreen.name)
        },
        modifier = Modifier
            .fillMaxWidth(),
        enabled = if (title.isEmpty() || description.isEmpty() || date.isEmpty() || priority.isEmpty()
        )
            false else true
    ) {
        Text(
            text = stringResource(R.string.button_save_label)
        )

    }
    Spacer(
        modifier = Modifier
            .height(16.dp)
    )
}

@Composable
fun Progressbar(modifier: Modifier = Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .fillMaxSize()
    ) {
        Spacer(
            modifier = modifier
                .weight(1f)
        )
        LinearProgressIndicator(
            modifier = modifier
                .fillMaxWidth()
                .heightIn(min = 4.dp)
                .background(MaterialTheme.colorScheme.primary)
        )
        Spacer(
            modifier = modifier
                .weight(1f)
        )

    }
}
