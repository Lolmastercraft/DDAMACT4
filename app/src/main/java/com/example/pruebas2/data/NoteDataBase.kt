package com.example.pruebas2.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [Note::class],
    version = 3,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class NoteDataBase : RoomDatabase() {
    abstract fun noteDao(): NoteDao

    companion object {

        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {

                database.execSQL("ALTER TABLE notes ADD COLUMN images TEXT NOT NULL DEFAULT '[]'")
                database.execSQL("ALTER TABLE notes ADD COLUMN isCompleted INTEGER NOT NULL DEFAULT 0")
            }
        }

        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {

                database.execSQL("""
                    CREATE TABLE notes_new (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        title TEXT NOT NULL,
                        content TEXT NOT NULL,
                        date TEXT NOT NULL,
                        isCompleted INTEGER NOT NULL DEFAULT 0
                    )
                """)

                database.execSQL("""
                    INSERT INTO notes_new (id, title, content, date, isCompleted)
                    SELECT id, title, content, date, isCompleted FROM notes
                """)

                database.execSQL("DROP TABLE notes")
                database.execSQL("ALTER TABLE notes_new RENAME TO notes")
            }
        }
    }
}