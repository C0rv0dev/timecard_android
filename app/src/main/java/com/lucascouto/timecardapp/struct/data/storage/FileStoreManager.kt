package com.lucascouto.timecardapp.struct.data.storage

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.documentfile.provider.DocumentFile
import com.lucascouto.timecardapp.struct.data.logic.WorkdaysDataLogic
import com.lucascouto.timecardapp.struct.data.singletons.ToastController
import com.lucascouto.timecardapp.struct.data.singletons.ToastType
import java.time.LocalDate
import java.util.Date

class FileStoreManager {
    companion object {
        suspend fun saveToFile(path: Uri, context: Context) {
            try {

                val resolver = context.contentResolver
                val folder =
                    DocumentFile.fromTreeUri(context, path) ?: throw Exception("Invalid folder URI")

                val date =
                    "${LocalDate.now().year}-${LocalDate.now().monthValue}-${LocalDate.now().dayOfMonth}_${Date().time}"
                val fileName = "workdays_export_$date.json"

                // delete duplicate file if exists
                folder.findFile(fileName)?.delete()

                // create new file
                val file = folder.createFile("application/json", fileName)
                    ?: throw Exception("Failed to create file")

                val json = WorkdaysDataLogic.exportJson()

                resolver.openOutputStream(file.uri)?.use { stream ->
                    stream.write(json.toString().toByteArray())
                }
            } catch (e: Exception) {
                Log.e("FileStoreManager", "Error saving to file: ${e.message}")
                ToastController.show("Error saving to file", ToastType.ERROR)
            }
        }
    }
}
