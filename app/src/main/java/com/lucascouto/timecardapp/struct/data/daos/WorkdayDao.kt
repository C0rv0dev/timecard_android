package com.lucascouto.timecardapp.struct.data.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.lucascouto.timecardapp.struct.data.entities.WorkdayEntity

@Dao
interface WorkdayDao {
    @Query("SELECT * FROM workdays")
    fun fetch(): List<WorkdayEntity>

    @Query("SELECT * FROM workdays WHERE date LIKE :yearMonth || '%'")
    fun fetchByMonth(yearMonth: String): List<WorkdayEntity>

    @Query("SELECT * FROM workdays WHERE date = :date")
    fun find(date: String): WorkdayEntity?

    @Insert
    abstract suspend fun create(workday: WorkdayEntity)

    @Update
    abstract suspend fun update(workday: WorkdayEntity)

    @Delete
    abstract suspend fun delete(workday: WorkdayEntity)
}
