package com.example.mytaskmanager.home

import android.annotation.SuppressLint
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.mytaskmanager.R
import com.example.mytaskmanager.TaskManagerRoute

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun TaskScreen(
    navigationHostController: NavHostController,
    navigationToDetail: NavHostController,
) {
    val homeViewModel: HomeViewModel = hiltViewModel()
    val homeUIState =
        homeViewModel.taskUIState.collectAsStateWithLifecycle(TaskUIState.Loading).value

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                navigationHostController.navigate(TaskManagerRoute.CreateTaskScreen.name)
            }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.create_new_task_floating_button)
                )
            }
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(all = 8.dp)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.primary),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            TaskToolbar(
                navigationHostController = navigationHostController,
                clearCompletedTasks = homeViewModel::clearAllCompletedTasks,
            )

            TaskScreenStructure(
                taskUIState = homeUIState,
                navigationToDetail = navigationToDetail,
                taskStatus = homeViewModel.statusSelected.value,
                setTaskStatus = homeViewModel::setTaskStatus,
            )
        }

    }
}

@Composable
fun TaskScreenStructure(
    taskUIState: TaskUIState,
    navigationToDetail: NavHostController,
    taskStatus: String,
    setTaskStatus: (String) -> Unit,
) {
    Row(horizontalArrangement = Arrangement.SpaceEvenly) {
        SingleChoiceSegmentedButton(
            modifier = Modifier
                .weight(2f)
                .padding(start = 8.dp, end = 8.dp),
            statusLabelSelected = taskStatus,
            setTaskStatus = setTaskStatus
        )

    }


    when (taskUIState) {
        is TaskUIState.EmptyState -> {
            HomeTerminalError(
                errorMessage = stringResource(R.string.empty_state_message)
            )
        }

        TaskUIState.Loading -> {
            ShimmerEffect(
                modifier = Modifier
                    .fillMaxSize(),
                widthOfShadowBrush = 100,
                angleOfAxisY = 270f,
                durationMillis = 1000
            )
        }

        is TaskUIState.TaskLoaded -> {
            Column(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.SpaceEvenly
            ) {

                Text(
                    modifier = Modifier
                        .padding(16.dp)
                        .wrapContentWidth(),
                    text = if (taskStatus == "Completed")
                        "Number of Completed Task: ${taskUIState.taskLists.size}"
                    else
                        "Number of Pending Task: ${taskUIState.taskLists.size}",
                    style = TextStyle(
                        color = MaterialTheme.colorScheme.surface,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.W500
                    )
                )

                TaskList(
                    taskList = taskUIState.taskLists,
                    navigationToDetail = navigationToDetail,
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskToolbar(
    navigationHostController: NavHostController,
    clearCompletedTasks: () -> Unit
) {
    val isMenuDisplayed = remember {
        mutableStateOf(false)
    }

    TopAppBar(
        title = {
            Text(
                text = stringResource(R.string.toolbar_title),
                style = TextStyle(
                    color = MaterialTheme.colorScheme.secondary,
                    fontSize = MaterialTheme.typography.titleLarge.fontSize,
                    fontWeight = FontWeight.W500
                ),
                modifier = Modifier
                    .padding(start = 16.dp)
            )
        },
        modifier = Modifier
            .background(color = Color.Blue),
        actions = {
            IconButton(onClick = {
                isMenuDisplayed.value = !isMenuDisplayed.value
            }) {
                Image(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = stringResource(R.string.menu_more),
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
                )
            }
            IconButton(onClick = {
                clearCompletedTasks()
            }) {
                Image(
                    imageVector = Icons.Default.Clear,
                    contentDescription = stringResource(R.string.clear_completed_task),
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
                )
            }

            DropdownMenu(
                expanded = isMenuDisplayed.value,
                onDismissRequest = { isMenuDisplayed.value = !isMenuDisplayed.value }
            ) {
                DropdownMenuItem(
                    text = { Text(text = stringResource(R.string.menu_item_setting)) },
                    onClick = {
                        navigationHostController.navigate(route = TaskManagerRoute.SettingScreen.name)
                    }
                )
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SingleChoiceSegmentedButton(
    modifier: Modifier = Modifier,
    statusLabelSelected: String,
    setTaskStatus: (String) -> Unit
) {
    var selectedIndex by remember { mutableIntStateOf(0) }
    val options = listOf("Completed", "Pending")
    val selectedStatus = remember { mutableStateOf(statusLabelSelected) }
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        SingleChoiceSegmentedButtonRow {
            options.forEachIndexed { index, label ->
                SegmentedButton(
                    shape = SegmentedButtonDefaults.itemShape(
                        index = index,
                        count = options.size
                    ),
                    onClick = {
                        selectedIndex = index
                        selectedStatus.value = options[index]

                    },
                    selected = index == selectedIndex,
                    label = {
                        setTaskStatus(selectedStatus.value)
                        Text(label)
                    }
                )
            }
        }
    }
}

@Composable
fun TaskList(
    taskList: List<Tasks>,
    navigationToDetail: NavHostController
) {
    Spacer(
        Modifier.widthIn(20.dp)
    )
    LazyColumn {
        items(items = taskList, key = {
            it.id
        }) {
            TaskItem(
                taskState = it,
                navigationToDetail = navigationToDetail
            )
        }
    }
    Spacer(
        Modifier.widthIn(20.dp)
    )

}

@Composable
fun TaskItem(
    taskState: Tasks,
    navigationToDetail: NavHostController
) {
    ElevatedCard(
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
            contentColor = Color.Blue,
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        ),
        modifier = Modifier
            .padding(horizontal = 4.dp)
            .fillMaxWidth()
            .clickable {
                navigationToDetail.navigate(
                    TaskManagerRoute.DetailedTaskScreen.name +
                            "/${taskState.id}" +
                            "/${taskState.title}" + "/${taskState.description}"
                            + "/${taskState.date}" + "/${taskState.taskPriority}"
                            + "/${taskState.taskStatus}"
                )
            },

        ) {
        Column(
            verticalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .padding(start = 8.dp)
                .fillMaxWidth()
                .height(100.dp)
        ) {
            taskState.apply {
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier
                        .padding(end = 16.dp)
                        .fillMaxWidth()
                ) {
                    SinglePriorityText(
                        taskState = taskState,
                        priority = taskPriority
                    )
                }
                SingleHeadText(title = title)
                SingleDueDateText(taskState = this)
            }
        }
    }
    Spacer(
        Modifier.height(8.dp)
    )

}

@Composable
fun SingleHeadText(title: String) {
    Text(
        modifier = Modifier
            .padding(horizontal = 8.dp),
        text = title,
        style = TextStyle(
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.W200,
            fontSize = 20.sp,
        )
    )
}

@Composable
fun SinglePriorityText(taskState: Tasks, priority: String) {

    if (taskState.taskStatus == "Pending")
        Text(
            text = priority,
            modifier = Modifier
                .background(color = Color.White)
                .border(
                    BorderStroke(1.dp, MaterialTheme.colorScheme.secondary),
                    shape = RoundedCornerShape(20.dp)
                )
                .clip(shape = RoundedCornerShape(8.dp))
                .padding(start = 8.dp, top = 4.dp, end = 8.dp, bottom = 4.dp),
            style = TextStyle(
                color = MaterialTheme.colorScheme.primary,
                fontSize = MaterialTheme.typography.labelMedium.fontSize,
                fontWeight = FontWeight.W300
            )
        )
    else
        return
}


@Composable
fun SingleDueDateText(taskState: Tasks) {
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Text(
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .weight(1F),
            text = taskState.date,
            style = TextStyle(
                color = MaterialTheme.colorScheme.primary,
                fontSize = MaterialTheme.typography.labelMedium.fontSize,
                fontWeight = FontWeight.W300
            )
        )
        Text(
            modifier = Modifier
                .padding(end = 16.dp)
                .weight(1F),
            text = taskState.taskStatus,
            style = TextStyle(
                color = MaterialTheme.colorScheme.primary,
                fontSize = MaterialTheme.typography.titleMedium.fontSize,
                fontWeight = FontWeight.W500
            ),
            textAlign = TextAlign.End
        )
    }
}

@Composable
fun HomeTerminalError(
    errorMessage: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 8.dp)
    ) {
        Text(
            text = errorMessage,
            style = TextStyle(
                color = MaterialTheme.colorScheme.surface,
                fontSize = MaterialTheme.typography.displaySmall.fontSize,
                fontWeight = FontWeight.W300
            ),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun ShimmerEffect(
    modifier: Modifier,
    widthOfShadowBrush: Int = 500,
    angleOfAxisY: Float = 270f,
    durationMillis: Int = 1000,
) {
    val shimmerColors = listOf(
        Color.White.copy(alpha = 0.3f),
        Color.White.copy(alpha = 0.5f),
        Color.White.copy(alpha = 1.0f),
        Color.White.copy(alpha = 0.5f),
        Color.White.copy(alpha = 0.3f),
    )

    val transition = rememberInfiniteTransition(label = "")

    val translateAnimation = transition.animateFloat(
        initialValue = 0f,
        targetValue = (durationMillis + widthOfShadowBrush).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = durationMillis,
                easing = LinearEasing,
            ),
            repeatMode = RepeatMode.Restart,
        ),
        label = "Shimmer loading animation",
    )

    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset(x = translateAnimation.value - widthOfShadowBrush, y = 0.0f),
        end = Offset(x = translateAnimation.value, y = angleOfAxisY),
    )

    Box(
        modifier = modifier
    ) {
        Spacer(
            modifier = Modifier
                .matchParentSize()
                .background(brush)
        )
    }
}
