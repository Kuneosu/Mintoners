package com.kuneosu.mintoners.data.model


import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.Date

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun fromPlayerList(value: List<Player>?): String? {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun toPlayerList(value: String): List<Player>? {
        val listType = object : TypeToken<List<Player>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromGameList(value: List<Game>?): String? {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun toGameList(value: String): List<Game>? {
        val listType = object : TypeToken<List<Game>>() {}.type
        return Gson().fromJson(value, listType)
    }
}
