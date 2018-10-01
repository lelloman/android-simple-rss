package com.lelloman.kotlinnn

import com.lelloman.kotlinnn.dataset.DataSet1D
import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import java.util.*
import javax.imageio.ImageIO
import kotlin.math.roundToInt


fun getNoVcsDir() = File("src/test/novcs").apply {
    if (!exists()) {
        mkdir()
    }
}

// Images
fun createImage(width: Int, height: Int) = BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR).apply {
    val graphics = createGraphics()
    graphics.paint = Color.BLACK
    graphics.fillRect(0, 0, width, height)
}

fun createImage(size: Double) = createImage(size.toInt(), size.toInt())

fun BufferedImage.save(dirName: String, fileName: String) = getNoVcsDir().let { noVcs ->
    File(noVcs, dirName).let { dir ->
        if (!dir.exists()) dir.mkdir()
        File(dir, "$fileName.png").let { file ->
            if (file.exists()) file.delete()
            ImageIO.write(this, "png", file)
        }
    }
}

// Logic gates
fun Double.toBoolean() = roundToInt() == 1

fun Boolean.toDouble() = if (this) 1.0 else 0.0

fun xorSample(a: Double, b: Double) = a.toBoolean().xor(b.toBoolean()).toDouble()
fun orSample(a: Double, b: Double) = a.toBoolean().or(b.toBoolean()).toDouble()

fun logicGateDataSet(size: Int, f: (a: Double, b: Double) -> Double, random: Random = Random()) = DataSet1D.Builder(size)
    .add {
        val x = doubleArrayOf(random.nextBoolean().toDouble(), random.nextBoolean().toDouble())
        x to doubleArrayOf(f(x[0], x[1]))
    }
    .random(random)
    .build()

fun xorDataSet(size: Int, random: Random) = DataSet1D.Builder(size)
    .add {
        val x = doubleArrayOf(random.nextBoolean().toDouble(), random.nextBoolean().toDouble())
        x to doubleArrayOf(xorSample(x[0], x[1]))
    }
    .build()

object SpiralDataSet {

    private var random = Random()

    private val spiralSample = { index: Int ->
        val j = index % 3

        val rIndex = random.nextInt(101)
        val r = rIndex / 120.0
        val tStep = (4) / 100.0

        val t = j * 4 + rIndex * tStep + (random.nextGaussian() + 1.0) * 0.25
        val x = 0.5 + r * Math.sin(t) / 2.0
        val y = 0.5 + r * Math.cos(t) / 2.0

        doubleArrayOf(x, y) to DoubleArray(3, { (it == j).toDouble() })
    }

    fun make(size: Int, random: Random = Random()): DataSet1D {
        this.random = random
        return DataSet1D.Builder(size)
            .add(spiralSample)
            .random(random)
            .build()
    }
}