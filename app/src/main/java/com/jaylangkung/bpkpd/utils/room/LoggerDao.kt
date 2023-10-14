package com.jaylangkung.bpkpd.utils.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface LoggerDao {
    @Query("SELECT * FROM Logger ORDER BY id DESC")
    fun getAllLog(): List<Logger>

    @Insert
    fun insert(log: Logger)
}