package ru.kcoder.currency.data.storage

import android.content.Context
import com.google.gson.Gson
import io.reactivex.Completable
import io.reactivex.Single
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import javax.inject.Inject

class PersistentStorageImpl @Inject constructor(
    private val context: Context,
    private val gson: Gson
) : PersistentStorage {

    override fun <T> writeToStorage(name: String, data: T): Completable {
        return Completable.fromAction {
            val json = gson.toJson(data)
            val file = File(context.cacheDir, name)
            writeTextToFile(file, json)
        }
    }

    override fun <T> readFromStorage(name: String, clazz: Class<T>): Single<T> {
        return Single.fromCallable {
            val file = File(context.cacheDir, name)
            val reader = BufferedReader(FileReader(file))
            gson.fromJson<T>(reader, clazz)
        }
    }

    private fun writeTextToFile(file: File, text: String) {
        val writer = FileWriter(file)
        writer.write(text)
        writer.flush()
        writer.close()
    }
}