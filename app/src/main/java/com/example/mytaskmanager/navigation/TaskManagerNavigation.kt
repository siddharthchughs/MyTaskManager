package com.example.mytaskmanager.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.mytaskmanager.TaskManagerRoute
import com.example.mytaskmanager.createtask.CreateTaskScreen
import com.example.mytaskmanager.deletetask.DeletedTaskScreen
import com.example.mytaskmanager.detail.TaskDetailScreen
import com.example.mytaskmanager.home.TaskScreen
import com.example.mytaskmanager.setting.SettingScreen

@Composable
fun NavigationManager() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = TaskManagerRoute.TaskHomeScreen.name
    ) {
        composable(TaskManagerRoute.TaskHomeScreen.name,
            enterTransition = {
                return@composable fadeIn(tween(1000))
            }, exitTransition = {
                return@composable slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Start, tween(700)
                )
            }
            ) {
            TaskScreen(
                navigationHostController = navController,
                navigationToDetail = navController,
            )
        }

        composable(TaskManagerRoute.CreateTaskScreen.name,
            enterTransition = {
                return@composable slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Start, tween(700)
                )
            },
            popExitTransition = {
                return@composable slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.End, tween(700)
                )
            }
            ) {
            CreateTaskScreen(
                navigateToHome = navController,
            )
        }

        composable(
            route = TaskManagerRoute.DetailedTaskScreen.name +
                    "/{id}" + "/{title}" +
                    "/{description}" + "/{dueDate}" +
                    "/{priority}" + "/{taskStatus}",

            arguments = listOf(
                navArgument("id") { type = NavType.IntType },
                navArgument("title") { type = NavType.StringType },
                navArgument("description") { type = NavType.StringType },
                navArgument("dueDate") { type = NavType.StringType },
                navArgument("priority") { type = NavType.StringType },
                navArgument("taskStatus") { type = NavType.StringType },
            ),
            enterTransition = {
                return@composable slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Start, tween(700)
                )
            },
            popExitTransition = {
                return@composable slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.End, tween(700)
                )
            }
        ) { taskInfo ->
            TaskDetailScreen(
                id = taskInfo.arguments?.getInt("id"),
                title = taskInfo.arguments?.getString("title"),
                description = taskInfo.arguments?.getString("description"),
                dueDate = taskInfo.arguments?.getString("dueDate"),
                priority = taskInfo.arguments?.getString("priority"),
                taskStatus = taskInfo.arguments?.getString("taskStatus"),
                navigateUp = { navController.navigateUp() },
                navigateToHome = navController
            )
        }

        composable(TaskManagerRoute.DeletedTaskScreen.name) {
            DeletedTaskScreen(
                // navController = navController
            )
        }

        composable(TaskManagerRoute.SettingScreen.name) {
            SettingScreen(
                navigationUp = { navController.navigateUp() }
            )
        }
    }
}