package com.lucascouto.timecardapp.struct.data.storage

import android.content.Context
import android.net.Uri
import android.provider.DocumentsContract
import android.util.Log
import androidx.documentfile.provider.DocumentFile
import com.lucascouto.timecardapp.struct.data.logic.WorkdaysDataLogic
import com.lucascouto.timecardapp.struct.data.singletons.ToastController
import com.lucascouto.timecardapp.struct.data.singletons.ToastType
import java.time.LocalDate
import java.util.Date
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FileStoreManager {
    companion object {
        suspend fun saveToFile(path: Uri, context: Context) {
            try {
                val resolver = context.contentResolver
                val folder =
                    DocumentFile.fromTreeUri(context, path) ?: throw Exception("Invalid folder URI")

                val json = WorkdaysDataLogic.exportJson()

                if (json == null) {
                    ToastController.show("No data to export", ToastType.WARNING)
                    return
                }

                val date =
                    "${LocalDate.now().year}-${LocalDate.now().monthValue}-${LocalDate.now().dayOfMonth}_${Date().time}"
                val fileName = "workdays_export_$date.json"

                // delete duplicate file if exists
                folder.findFile(fileName)?.delete()

                // create new file
                val file = folder.createFile("application/json", fileName)
                    ?: throw Exception("Failed to create file")

                withContext(Dispatchers.IO) {
                    resolver.openOutputStream(file.uri)?.use { stream ->
                        stream.write(json.toString().toByteArray())
                    } ?: throw Exception("Failed to open output stream")
                }

                ToastController.show("Data exported successfully", ToastType.SUCCESS)
            } catch (e: Exception) {
                Log.e("FileStoreManager", "Error saving to file", e)
                ToastController.show("Error saving to file", ToastType.ERROR)
            }
        }

        suspend fun loadFromFile(uri: Uri, context: Context) {
            try {
                val content = withContext(Dispatchers.IO) {

                    val resolver = context.contentResolver

                    val isTree = DocumentsContract.isTreeUri(uri)

                    // When the user selects a FOLDER
                    if (isTree) {
                        val folder = DocumentFile.fromTreeUri(context, uri)
                            ?: throw Exception("Unable to access folder")

                        val file = folder.listFiles()
                            .find { it.name?.endsWith(".json", ignoreCase = true) == true }
                            ?: throw Exception("No JSON file found in folder")

                        resolver.openInputStream(file.uri)?.bufferedReader().use { it?.readText() }
                            ?: throw Exception("Failed to read file")
                    }

                    // When the user selects a FILE
                    else {
                        val file = DocumentFile.fromSingleUri(context, uri)
                            ?: throw Exception("Unable to access file")

                        if (file.name?.endsWith(".json", true) != true)
                            throw Exception("Selected file is not JSON")

                        resolver.openInputStream(file.uri)?.bufferedReader().use { it?.readText() }
                            ?: throw Exception("Failed to read file")
                    }
                }

                WorkdaysDataLogic.importJson(content)
                ToastController.show("Data imported successfully", ToastType.SUCCESS)

            } catch (e: Exception) {
                Log.e("FileStoreManager", "Error loading from file", e)
                ToastController.show("Error loading from file: ${e.message}", ToastType.ERROR)
            }
        }
    }
}
