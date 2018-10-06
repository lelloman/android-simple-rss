package com.lelloman.launcher.packages

import android.content.Context
import com.lelloman.launcher.persistence.model.PackageLaunch
import io.reactivex.Single
import java.io.BufferedReader
import java.io.File

class PackageLaunchesExporter(private val context: Context) {

    fun readExportFile(bufferedReader: BufferedReader): Single<List<PackageLaunch>> = Single.fromCallable {
        val launches = mutableListOf<PackageLaunch>()
        val header = bufferedReader.readLine()
        if (header != CSV_HEADER) {
            throw InvalidExportFileException("Invalid header line \"$header\"")
        }

        var line = bufferedReader.readLine()
        while (!line.isNullOrBlank()) {
            val split = line.split(",")
            if (split.size != 3) {
                throw InvalidExportFileException("Invalid line with ${split.size} elements \"$line\"")
            }
            val packageName = split[0]
            val activityName = split[1]
            val timestampString = split[2]
            val timestamp: Long
            try {
                timestamp = java.lang.Long.parseLong(timestampString)
            } catch (exception: Exception) {
                throw InvalidExportFileException("Invalid timestamp value \"$timestampString\"", exception)
            }
            launches.add(PackageLaunch(
                id = 0L,
                timestampUtc = timestamp,
                packageName = packageName,
                activityName = activityName
            ))
            line = bufferedReader.readLine()
        }
        launches
    }

    fun createExportFile(fileName: String, launches: List<PackageLaunch>): Single<File> = Single.fromCallable {
        val exportFile = File(context.cacheDir, fileName)

        if (exportFile.exists()) {
            exportFile.delete()
        }

        val writer = exportFile.writer()
        writer.append("$CSV_HEADER\n")
        launches.forEach {
            writer.append("${it.packageName},${it.activityName},${it.timestampUtc}\n")
        }
        writer.flush()
        writer.close()
        exportFile
    }

    private companion object {
        const val CSV_HEADER = "packageName,activityName,timestampUtc"
    }
}