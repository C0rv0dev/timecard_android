package com.lucascouto.timecardapp.ui.screens.Profile.sub

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.lucascouto.timecardapp.R
import com.lucascouto.timecardapp.struct.AppManager
import com.lucascouto.timecardapp.struct.data.logic.WorkdaysDataLogic
import com.lucascouto.timecardapp.struct.data.singletons.ToastController
import com.lucascouto.timecardapp.struct.data.singletons.ToastType
import com.lucascouto.timecardapp.struct.data.storage.FileStoreManager
import com.lucascouto.timecardapp.ui.components.ActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun SystemContent(appManager: AppManager) {
    val context: Context = LocalContext.current

    val filePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
    ) { uri: Uri? ->
        if (uri != null) {
            importData(context, uri)
        }
    }

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
        modifier = Modifier
            .padding(16.dp)
            .fillMaxHeight(),
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        Column {
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
                onClick = { filePicker.launch(arrayOf("application/json")) },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text("Import Data")
            }
        }

        ActionButton(
            buttonContent = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Image(
                        painter = painterResource(R.drawable.ic_trash),
                        contentDescription = "Delete Icon",
                        modifier = Modifier.padding(end = 8.dp)
                    )

                    Text("Delete All Data")
                }
            },
            dialogContent = {
                Text("Are you sure you want to delete all data? This action cannot be undone.")
            },
            onConfirm = { deleteAllData() },
            enabled = true,
            buttonStyle = ButtonColors(
                containerColor = Color.Red,
                contentColor = Color.White,
                disabledContainerColor = Color.LightGray,
                disabledContentColor = Color.Gray
            )
        )
    }
}

fun exportData(context: Context, folderUri: Uri) {
    CoroutineScope(Dispatchers.IO).launch {
        FileStoreManager.saveToFile(folderUri, context)
    }
}

fun importData(context: Context, folderUri: Uri) {
    CoroutineScope(Dispatchers.IO).launch {
        FileStoreManager.loadFromFile(folderUri, context)
    }
}

fun deleteAllData() {
    CoroutineScope(Dispatchers.IO).launch {
        WorkdaysDataLogic.deleteAllData()
        ToastController.show("All data deleted successfully", ToastType.SUCCESS)
    }
}
