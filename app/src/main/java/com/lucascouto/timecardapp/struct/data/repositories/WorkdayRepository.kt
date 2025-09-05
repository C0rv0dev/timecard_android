package com.lucascouto.timecardapp.struct.data.repositories

import com.lucascouto.timecardapp.struct.data.daos.WorkdayDao
import com.lucascouto.timecardapp.struct.data.entities.WorkdayEntity

class WorkdayRepository(private val dao: WorkdayDao) {
    suspend fun fetch(): List<WorkdayEntity> = dao.fetch()
    suspend fun show(id: Long): WorkdayEntity? = dao.show(id)
    suspend fun find(date: String): WorkdayEntity? = dao.find(date)
    suspend fun create(workday: WorkdayEntity) = dao.create(workday)
    suspend fun update(workday: WorkdayEntity) = dao.update(workday)
    suspend fun delete(workday: WorkdayEntity) = dao.delete(workday)
}