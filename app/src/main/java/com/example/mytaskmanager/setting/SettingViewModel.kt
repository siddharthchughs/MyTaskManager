package com.example.mytaskmanager.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mytaskmanager.BuildConfig
import com.example.mytaskmanager.home.TasksLocalDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface SettingUIState {
    sealed interface SettingItems
    data object CrashItem : SettingItems
    data object GenerateDummyData : SettingItems
    data object ClearData : SettingItems
    data object NotRequired : SettingUIState
    data class Loaded(
        val list: List<SettingItems>,
    ) : SettingUIState
}

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val tasksLocalDataSource: TasksLocalDataSource,
) : ViewModel() {

    val settingUIState: Flow<SettingUIState> = flowOf(
        transform()
    )

    private fun transform(): SettingUIState {
        val settingItems: MutableList<SettingUIState.SettingItems> = mutableListOf(
        )
        if (BuildConfig.DEBUG) {
            settingItems.add(
                SettingUIState.CrashItem
            )
            settingItems.add(
                SettingUIState.GenerateDummyData
            )
            settingItems.add(
                SettingUIState.ClearData
            )
        }
        return SettingUIState.Loaded(
            list = settingItems,
        )
    }

    fun onCrashClicked() {
        throw Exception()
    }

    fun onGenerateDummyClicked() {
        viewModelScope.launch {
            tasksLocalDataSource.generateDummyTasks(flowOf(emptyList()))
        }
    }

    fun clearDummyData() {
        viewModelScope.launch {
            tasksLocalDataSource.deleteAllTasks()
        }
    }
}