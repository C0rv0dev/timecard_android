package com.lucascouto.timecardapp.struct.data.storage

import android.content.Context
import android.net.Uri
import android.provider.DocumentsContract
import android.util.Log
import androidx.documentfile.provider.DocumentFile
import com.lucascouto.timecardapp.struct.data.DatabaseProvider
import com.lucascouto.timecardapp.struct.data.entities.WorkdayEntity
import com.lucascouto.timecardapp.struct.data.singletons.ToastController
import com.lucascouto.timecardapp.struct.data.singletons.ToastType
import com.lucascouto.timecardapp.struct.data.utils.TimeUtils
import java.time.LocalDate
import java.util.Date
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject

class FileStoreManager {
    companion object {
        suspend fun saveToFile(path: Uri, context: Context) {
            try {
                val resolver = context.contentResolver
                val folder =
                    DocumentFile.fromTreeUri(context, path) ?: throw Exception("Invalid folder URI")

                val json = exportJson()

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

                importJson(content)
                ToastController.show("Data imported successfully", ToastType.SUCCESS)

            } catch (e: Exception) {
                Log.e("FileStoreManager", "Error loading from file", e)
                ToastController.show("Error loading from file: ${e.message}", ToastType.ERROR)
            }
        }

        private suspend fun exportJson(): ArrayList<JSONObject?>? {
            // export all workdays data to a file
            val workdays = DatabaseProvider.workdayRepository.fetch()

            // if no workdays are found, show a message and return
            if (workdays.isEmpty()) {
                return null
            }

            val jsonList = ArrayList<JSONObject?>()

            for (workday in workdays) {
                val obj = JSONObject()

                obj.put("date", workday.date)
                obj.put("shiftType", workday.shiftType)
                obj.put("shiftStartHour", workday.shiftStartHour)
                obj.put("shiftEndHour", workday.shiftEndHour)
                obj.put("lunchStartHour", workday.lunchStartHour)
                obj.put("lunchDurationMinutes", workday.lunchDurationMinutes)
                obj.put("defaultHourlyPayAtTime", workday.defaultHourlyPayAtTime)
                obj.put("overtimeRateMultiAtTime", workday.overtimeRateMultiAtTime)
                obj.put("lateNightRateMultiAtTime", workday.lateNightRateMultiAtTime)
                obj.put("baseShiftDurationHoursAtTime", workday.baseShiftDurationHoursAtTime)
                obj.put("lateNightStartTimeAtTime", workday.lateNightStartTimeAtTime)
                obj.put("lateNightEndTimeAtTime", workday.lateNightEndTimeAtTime)

                jsonList.add(obj)
            }

            return jsonList
        }

        private suspend fun importJson(content: String) {
            val jsonArray = JSONArray(content)
            val existingDates = DatabaseProvider.workdayRepository.fetch().map { it.date }.toSet()

            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)

                val workday = WorkdayEntity(
                    date = obj.getString("date"),
                    shiftType = obj.getInt("shiftType"),
                    shiftStartHour = obj.getString("shiftStartHour"),
                    shiftEndHour = obj.getString("shiftEndHour"),
                    lunchStartHour = obj.getString("lunchStartHour"),
                    lunchDurationMinutes = obj.getInt("lunchDurationMinutes"),
                    defaultHourlyPayAtTime = obj.getInt("defaultHourlyPayAtTime"),
                    overtimeRateMultiAtTime = obj.getInt("overtimeRateMultiAtTime"),
                    lateNightRateMultiAtTime = obj.getInt("lateNightRateMultiAtTime"),
                    baseShiftDurationHoursAtTime = obj.getInt("baseShiftDurationHoursAtTime"),
                    lateNightStartTimeAtTime = obj.getString("lateNightStartTimeAtTime"),
                    lateNightEndTimeAtTime = obj.getString("lateNightEndTimeAtTime"),
                    shiftDuration = TimeUtils.calculateDuration(
                        start = obj.getString("shiftStartHour"),
                        end = obj.getString("shiftEndHour"),
                        lunchDuration = obj.getInt("lunchDurationMinutes")
                    )
                )

                // Avoid duplicates based on date
                if (workday.date !in existingDates) {
                    DatabaseProvider.workdayRepository.create(workday)
                }
            }
        }
    }
}
