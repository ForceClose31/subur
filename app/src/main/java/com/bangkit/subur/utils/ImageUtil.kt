package com.bangkit.subur.utils

import android.graphics.Bitmap
import java.io.File
import java.io.FileOutputStream

object ImageUtil {
    fun convertBitmapToFile(bitmap: Bitmap): File {
        val file = File.createTempFile("image", ".jpg")
        val outStream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream)
        outStream.flush()
        outStream.close()
        return file
    }
}
