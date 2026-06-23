package com.modernapp.app.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Brightness4
import androidx.compose.material.icons.filled.Brightness7
import androidx.compose.material.icons.filled.BrightnessAuto
import androidx.compose.material.icons.filled.DataArray
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Language
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.modernapp.app.R
import com.modernapp.app.utils.Constants
import com.modernapp.app.viewmodel.SettingsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onNavigateBack: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    var showDeleteDialog by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.successMessage) {
        uiState.successMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearMessages()
        }
    }

    LaunchedEffect(uiState.error) {
        uiState.error?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearMessages()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.settings)) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            // Section: Theme
            Text(
                text = stringResource(R.string.theme),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 4.dp)
            )

            listOf(
                Triple(Constants.THEME_SYSTEM, R.string.theme_system, Icons.Default.BrightnessAuto),
                Triple(Constants.THEME_LIGHT, R.string.theme_light, Icons.Default.Brightness7),
                Triple(Constants.THEME_DARK, R.string.theme_dark, Icons.Default.Brightness4)
            ).forEach { (mode, labelRes, icon) ->
                ListItem(
                    headlineContent = { Text(stringResource(labelRes)) },
                    leadingContent = {
                        Icon(imageVector = icon, contentDescription = null)
                    },
                    trailingContent = {
                        RadioButton(
                            selected = uiState.themeMode == mode,
                            onClick = { viewModel.setThemeMode(mode) }
                        )
                    }
                )
            }

            Divider(modifier = Modifier.padding(vertical = 8.dp))

            // Section: Language
            Text(
                text = stringResource(R.string.language),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp, bottom = 4.dp)
            )

            listOf(
                Pair("vi", R.string.lang_vietnamese),
                Pair("en", R.string.lang_english)
            ).forEach { (code, nameRes) ->
                ListItem(
                    headlineContent = { Text(stringResource(nameRes)) },
                    leadingContent = {
                        Icon(imageVector = Icons.Default.Language, contentDescription = null)
                    },
                    trailingContent = {
                        RadioButton(
                            selected = uiState.language == code,
                            onClick = { viewModel.setLanguage(code) }
                        )
                    }
                )
            }

            Divider(modifier = Modifier.padding(vertical = 8.dp))

            // Section: Data
            Text(
                text = stringResource(R.string.data_management),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp, bottom = 4.dp)
            )

            ListItem(
                headlineContent = { Text(stringResource(R.string.delete_all_data)) },
                supportingContent = { Text(stringResource(R.string.delete_all_data_desc)) },
                leadingContent = {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.error
                    )
                },
                modifier = Modifier.padding()
            )
            TextButton(
                onClick = { showDeleteDialog = true },
                modifier = Modifier.padding(start = 56.dp)
            ) {
                Text(
                    text = stringResource(R.string.delete_all_data),
                    color = MaterialTheme.colorScheme.error
                )
            }

            ListItem(
                headlineContent = { Text(stringResource(R.string.backup_data)) },
                supportingContent = { Text(stringResource(R.string.backup_data_desc)) },
                leadingContent = {
                    Icon(imageVector = Icons.Default.DataArray, contentDescription = null)
                }
            )
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text(stringResource(R.string.confirm_delete)) },
            text = { Text(stringResource(R.string.confirm_delete_desc)) },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.deleteAllData()
                        showDeleteDialog = false
                    }
                ) {
                    Text(
                        text = stringResource(R.string.delete),
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text(stringResource(R.string.cancel))
                }
            }
        )
    }
}
