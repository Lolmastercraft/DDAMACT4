package com.example.pruebas2.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [Note::class],
    version = 2,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class NoteDataBase : RoomDatabase() {
    abstract fun noteDao(): NoteDao

    companion object {

        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Agregar nuevas columnas
                database.execSQL("ALTER TABLE notes ADD COLUMN images TEXT NOT NULL DEFAULT '[]'")
                database.execSQL("ALTER TABLE notes ADD COLUMN isCompleted INTEGER NOT NULL DEFAULT 0")
            }
        }
    }
}