package com.example.mytaskmanager.setting

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.sharp.Delete
import androidx.compose.material.icons.sharp.List
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.mytaskmanager.R

@Composable
fun SettingScreen(
    navigationUp: () -> Unit
) {

    val settingViewmodel: SettingsViewModel = hiltViewModel()
    val settingUiState =
        settingViewmodel.settingUIState.collectAsStateWithLifecycle(initialValue = SettingUIState.NotRequired)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        modifier = Modifier
            .fillMaxSize()
    ) {
        SettingToolbar(
            navigationUp = navigationUp
        )
        SettingScreenStructure(
            settingUIState = settingUiState.value,
            onCrashClicked = settingViewmodel::onCrashClicked,
            onGenerateDummyClicked = settingViewmodel::onGenerateDummyClicked,
            clearDummyData = settingViewmodel::clearDummyData
        )
    }
}

@Composable
fun SettingScreenStructure(
    settingUIState: SettingUIState,
    onCrashClicked: () -> Unit,
    onGenerateDummyClicked: () -> Unit,
    clearDummyData: () -> Unit
) {
    when (settingUIState) {
        is SettingUIState.Loaded -> {
            SettingItems(
                settingUIState = settingUIState.list,
                onCrashClicked = onCrashClicked,
                onGenerateDummyClicked = onGenerateDummyClicked,
                clearDummyData = clearDummyData
            )
        }

        SettingUIState.NotRequired -> {}
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingToolbar(
    navigationUp: () -> Unit
) {
    TopAppBar(modifier = Modifier.fillMaxWidth(), title = {
        androidx.compose.material3.Text(
            text = stringResource(R.string.label_settings)
        )
    },
        navigationIcon = {
            IconButton(onClick = {
                navigationUp()
            }
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = stringResource(R.string.back)
                )

            }
        },
        actions = {
        }
    )
}

@Composable
fun SettingItems(
    settingUIState: List<SettingUIState.SettingItems>,
    onCrashClicked: () -> Unit,
    onGenerateDummyClicked: () -> Unit,
    clearDummyData: () -> Unit
) {
    LazyColumn {
        items(settingUIState) { it ->
            when (it) {
                SettingUIState.CrashItem -> {
                    SettingItemLayout(
                        label = stringResource(R.string.crash_label),
                        icon = Icons.Default.Warning,
                        onClick = onCrashClicked
                    )
                }

                is SettingUIState.GenerateDummyData -> {
                    SettingItemLayout(
                        label = stringResource(R.string.create_dummy_data),
                        icon = Icons.Sharp.List,
                        onClick = onGenerateDummyClicked
                    )
                }

                SettingUIState.ClearData -> {
                    SettingItemLayout(
                        label = stringResource(R.string.delete_dummy_data),
                        icon = Icons.Sharp.Delete,
                        onClick = clearDummyData
                    )
                }
            }
        }
    }
}

@Composable
fun SettingItemLayout(label: String, icon: ImageVector, onClick: () -> Unit) {
    ElevatedCard(
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
        ),
        modifier = Modifier
            .padding(horizontal = 4.dp)
            .fillMaxWidth()
            .wrapContentHeight()
        ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(60.dp)
                .clickable { onClick() }
        ) {
            SingleItemIcon(
                icon = icon
            )
            SingleItemText(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp),
                label = label,
            )
        }
    }
}

@Composable
fun SingleItemText(
    modifier: Modifier,
    label: String
) {
    Text(
        modifier = modifier
            .padding(start = 8.dp),
        text = label,
        style = TextStyle(
            color = MaterialTheme.colorScheme.primary,
            fontSize = MaterialTheme.typography.titleLarge.fontSize
        )
    )
}

@Composable
fun SingleItemIcon(
    icon: ImageVector
) {
    Icon(
        modifier = Modifier
            .size(48.dp)
            .padding(start = 8.dp),
        imageVector = icon,
        contentDescription = stringResource(R.string.crash_label)
    )
}