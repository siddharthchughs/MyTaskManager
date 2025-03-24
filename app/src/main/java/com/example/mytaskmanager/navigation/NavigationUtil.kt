package com.example.mytaskmanager

enum class TaskManagerRoute {
    SplashScreen,
    CreateTaskScreen,
    TaskHomeScreen,
    DetailedTaskScreen,
    DeletedTaskScreen,
    SettingScreen;

    companion object {
        fun fromRoute(route: String?): TaskManagerRoute =
            when (route?.substringBefore("/")) {
            SplashScreen.name -> SplashScreen
            CreateTaskScreen.name -> CreateTaskScreen
            TaskHomeScreen.name -> TaskHomeScreen
            DetailedTaskScreen.name -> DetailedTaskScreen
            DeletedTaskScreen.name -> DeletedTaskScreen
            SettingScreen.name -> SettingScreen
            null-> TaskHomeScreen
            else->throw IllegalStateException("Unknown State")
        }
    }
}