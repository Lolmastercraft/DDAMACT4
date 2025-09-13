package com.example.pruebas2.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters

@Database(
    entities = [Note::class],
    version = 1,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class NoteDataBase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
}