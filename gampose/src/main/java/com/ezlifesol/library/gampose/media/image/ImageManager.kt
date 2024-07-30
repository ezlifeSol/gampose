/**
 * MIT License
 *
 * Copyright 2024 ezlifeSol
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package com.ezlifesol.library.gampose.media.image

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.annotation.Keep
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import java.io.InputStream

/**
 * A singleton object to handle image caching and retrieval for game assets.
 *
 * This class provides a mechanism to cache images loaded from asset files,
 * helping to optimize performance by avoiding redundant image decoding and loading.
 *
 * In addition to single image caching, it also supports splitting images into
 * smaller parts based on specified columns and rows, and caching these split images.
 */
@Keep
object ImageManager {
    // A mutable map that stores cached images with their asset paths as keys.
    private val cache = mutableMapOf<String, ImageBitmap>()

    // A mutable map that stores cached split images with their asset paths and split info as keys.
    private val splitCache = mutableMapOf<String, List<ImageBitmap>>()

    /**
     * Retrieves an ImageBitmap from the cache or loads it from assets if not already cached.
     *
     * This function first checks if the image is available in the cache. If the image is found in
     * the cache, it is returned. If not, the image is loaded from the asset files, decoded into a
     * Bitmap, converted to an ImageBitmap, and stored in the cache for future access.
     *
     * @param context The context used to access the asset manager for retrieving the image.
     * @param assetPath The relative path to the asset image file within the assets folder.
     * @return The ImageBitmap corresponding to the specified asset path.
     */
    fun getBitmap(context: Context, assetPath: String): ImageBitmap {
        // Return the cached image if it exists; otherwise, load it from assets and cache it.
        return cache[assetPath] ?: run {
            val inputStream: InputStream = context.assets.open(assetPath)
            val bitmap = BitmapFactory.decodeStream(inputStream).asImageBitmap()
            cache[assetPath] = bitmap
            bitmap
        }
    }

    /**
     * Splits an image from assets into a grid of smaller images and caches the resulting images.
     *
     * This function divides the original image into the specified number of columns and rows,
     * then stores the resulting images in the cache for future access.
     *
     * @param context The context used to access the asset manager for retrieving the image.
     * @param assetPath The relative path to the asset image file within the assets folder.
     * @param col The number of columns to split the image into.
     * @param row The number of rows to split the image into.
     * @return A list of ImageBitmap objects representing the split images.
     * @throws IllegalArgumentException If col or row is less than or equal to 0.
     */
    fun splitSprite(context: Context, assetPath: String, col: Int, row: Int): List<ImageBitmap> {
        require(col > 0 && row > 0) { "Column and row numbers must be greater than 0" }

        // Create a unique key for the split images based on asset path, columns, and rows.
        val splitKey = "$assetPath-$col-$row"

        // Return the cached split images if they exist; otherwise, load and split the image, then cache the results.
        return splitCache[splitKey] ?: run {
            val assetManager = context.assets
            val inputStream = assetManager.open(assetPath)
            val originalBitmap = BitmapFactory.decodeStream(inputStream)
            inputStream.close()

            val spriteWidth = originalBitmap.width / col
            val spriteHeight = originalBitmap.height / row

            val sprites = mutableListOf<ImageBitmap>()

            for (i in 0 until row) {
                for (j in 0 until col) {
                    val sprite = Bitmap.createBitmap(
                        originalBitmap,
                        j * spriteWidth,
                        i * spriteHeight,
                        spriteWidth,
                        spriteHeight
                    )
                    sprites.add(sprite.asImageBitmap())
                }
            }

            splitCache[splitKey] = sprites
            sprites
        }
    }
}
