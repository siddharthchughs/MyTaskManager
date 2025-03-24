package com.example.mytaskmanager.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.mytaskmanager.R
import com.example.mytaskmanager.TaskManagerRoute

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskDetailScreen(
    id: Int?,
    title: String?,
    description: String?,
    dueDate: String?,
    priority: String?,
    taskStatus: String?,
    navigateUp: () -> Unit,
    navigateToHome: NavHostController
) {
    val detailedTaskViewModel: DetailedTaskViewModel = hiltViewModel()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        TaskDetailToolbar(
            navigateUp = navigateUp
        )
        TaskDetailScreenStructure(
            id = id,
            title = title,
            description = description,
            dueDate = dueDate,
            priority = priority,
            taskStatus = taskStatus,
            updateTask = detailedTaskViewModel::updateTaskStatus,
             navigateToHome = navigateToHome
        )
    }

}

@Composable
fun TaskDetailScreenStructure(
    id: Int?,
    title: String?,
    description: String?,
    dueDate: String?,
    priority: String?,
    taskStatus: String?,
    updateTask: (Int) -> Unit,
    navigateToHome: NavHostController,
) {
    Divider(
        modifier = Modifier
            .fillMaxWidth()
            .height(2.dp),
        thickness = 2.dp,
        color = when (taskStatus) {
            "Completed" -> Color.Green
            else -> {
                Color.Red
            }
        }
    )

    TaskDetailForm(
        id = id,
        title = title,
        description = description,
        dueDate = dueDate,
        priority = priority,
        taskStatus = taskStatus,
        updateTask = updateTask,
        navigateToHome = navigateToHome
    )
}

@ExperimentalMaterial3Api
@Composable
fun TaskDetailToolbar(
    navigateUp: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = "Detail",
                style = TextStyle(
                    fontSize = MaterialTheme.typography.titleLarge.fontSize,
                    fontWeight = FontWeight.W400
                )
            )
        },
        navigationIcon = {
            IconButton(
                onClick = navigateUp
            ) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
            }
        },
        actions = {}
    )
}

@Composable
fun TaskDetailForm(
    id: Int?,
    title: String?,
    description: String?,
    dueDate: String?,
    priority: String?,
    taskStatus: String?,
    updateTask: (Int) -> Unit,
    navigateToHome: NavHostController,
) {
    Column(
        modifier = Modifier
            .padding(start = 16.dp, top = 8.dp, end = 16.dp)
    ) {
        SingleTitleText(title = title)
        SingleMultilineText(description = description)
        DueDateText(dueDate = dueDate)
        if (taskStatus == "Pending") {
            TasDetailPriorityLabel(priority = priority)
        }
        TasDetailStatusLabel(taskStatus = taskStatus)
        Spacer(
            Modifier.height(
                height = 20.dp
            )
        )
        Spacer(
            Modifier.heightIn(min = 20.dp)
        )

        if (taskStatus == "Pending") {
            CompletedLayout(
                id = id,
                updateTask = updateTask,
                navigateToHome = navigateToHome
            )
        } else
            return
    }
}

@Composable
fun CompletedLayout(
    id: Int?,
    updateTask: (Int) -> Unit,
    navigateToHome: NavHostController
) {
    CompletedLabel(
        title = stringResource(R.string.label_mark_as_complete)
    )
    Spacer(
        Modifier.heightIn(min = 20.dp)
    )
    Button(
        onClick = {
            id?.let { updateTask(it) }
            navigateToHome.navigate(TaskManagerRoute.TaskHomeScreen.name)
        },
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Text(text = stringResource(R.string.label_complete))

    }

}

@Composable
fun CompletedLabel(title: String) {
    Text(
        modifier = Modifier
            .padding(horizontal = 8.dp),
        text = title,
        style = TextStyle(
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.W400,
            fontSize = MaterialTheme.typography.titleLarge.fontSize,
        )
    )
}

@Composable
fun TasDetailPriorityLabel(
    priority: String?
) {
    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        SingleHeadingText(header = "Priority")
        Spacer(
            Modifier.height(
                height = 8.dp
            )
        )

        Text(
            text = "${priority}",
            style = TextStyle(
                color = MaterialTheme.colorScheme.error,
                fontSize = 14.sp,
                fontStyle = MaterialTheme.typography.titleSmall.fontStyle,
                fontWeight = FontWeight.W400
            )
        )

    }
}

@Composable
fun TasDetailStatusLabel(
    taskStatus: String?
) {
    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        SingleHeadingText(header = stringResource(R.string.header_task_status))
        Spacer(
            Modifier.height(
                height = 8.dp
            )
        )

        Text(
            text = "${taskStatus}",
            style = TextStyle(
                color = if (taskStatus == "Completed") MaterialTheme.colorScheme.primary else Color.Red,
                fontSize = 14.sp,
                fontStyle = MaterialTheme.typography.titleSmall.fontStyle,
                fontWeight = FontWeight.W400
            )
        )

    }
}

@Composable
fun SingleTitleText(title: String?) {
    Text(
        modifier = Modifier
            .padding(start = 8.dp, top = 10.dp)
            .fillMaxWidth(),
        text = "${title}",
        style = TextStyle(
            fontSize = 30.sp,
            fontStyle = MaterialTheme.typography.headlineMedium.fontStyle,
            fontWeight = FontWeight.W600
        )
    )
    Spacer(
        modifier = Modifier
            .height(20.dp)
    )
}

@Composable
fun SingleHeadingText(header: String) {
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
}

@Composable
fun SingleMultilineText(
    description: String?
) {
    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .padding(start = 8.dp)
            .fillMaxWidth()
    ) {
        SingleHeadingText(header = "Description")
        Spacer(
            Modifier.height(
                height = 8.dp
            )
        )
        Text(
            text = "${description}",
            style = TextStyle(
                fontSize = 24.sp,
                fontStyle = MaterialTheme.typography.headlineMedium.fontStyle,
                fontWeight = FontWeight.W200
            )
        )
    }
}

@Composable
fun DueDateText(
    dueDate: String?
) {
    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        SingleHeadingText(header = "Due Date")
        Spacer(
            Modifier.height(
                height = 8.dp
            )
        )

        Text(
            text = "${dueDate}",
            style = TextStyle(
                fontSize = 24.sp,
                fontStyle = MaterialTheme.typography.titleMedium.fontStyle,
                fontWeight = FontWeight.W200
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    Column {
//        TaskDetailScreen()
    }
}