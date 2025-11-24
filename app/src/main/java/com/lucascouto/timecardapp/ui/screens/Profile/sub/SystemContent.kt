package com.lucascouto.timecardapp.ui.screens.Profile.sub

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.lucascouto.timecardapp.struct.AppManager
import com.lucascouto.timecardapp.struct.data.storage.FileStoreManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun SystemContent(appManager: AppManager) {
    val context: Context = LocalContext.current
    val folderPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocumentTree()
    ) { uri ->
        if (uri != null) {
            context.contentResolver.takePersistableUriPermission(
                uri,
                Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
            )

            // save selected folder for future exports
            CoroutineScope(Dispatchers.IO).launch {
                appManager.dataStorageManager.saveExportPath(uri)
            }.invokeOnCompletion {
                // export data after saving the path
                exportData(context, uri)
            }
        }
    }

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = {
                CoroutineScope(Dispatchers.IO).launch {
                    val savedUri = appManager.dataStorageManager.getExportPath()
                    if (savedUri != null) {
                        // export data directly
                        exportData(context, savedUri)
                    } else {
                        // launch folder picker
                        folderPicker.launch(null)
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Export Data")
        }

        Button(
            onClick = { /* TODO **/ },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Import Data")
        }
    }
}

fun exportData(context: Context, folderUri: Uri) {
    CoroutineScope(Dispatchers.IO).launch {
        FileStoreManager.saveToFile(folderUri, context)
    }
}

