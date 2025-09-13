package com.example.pruebas2.data

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class Converters {
    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
    private val gson = Gson()


    @TypeConverter
    fun fromData(value: String?): LocalDateTime? {
        return value?.let { LocalDateTime.parse(it, formatter) }
    }

    @TypeConverter
    fun dataToString(date: LocalDateTime?): String? {
        return date?.format(formatter)
    }

     //imágenes
    @TypeConverter
    fun fromImageList(value: List<String>): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toImageList(value: String): List<String> {
        return try {
            val listType = object : TypeToken<List<String>>() {}.type
            gson.fromJson(value, listType) ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }
}