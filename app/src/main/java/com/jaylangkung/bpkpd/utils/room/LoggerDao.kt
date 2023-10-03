package com.jaylangkung.bpkpd.utils.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.jaylangkung.bpkpd.utils.room.Logger

@Dao
interface LoggerDao {
    @Query("SELECT * FROM Logger ORDER BY id DESC")
    fun getAllLog(): List<Logger>

    @Insert
    fun insert(log: Logger)
}