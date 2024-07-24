package com.ezlifesol.library.gampose.media.image

import android.content.Context
import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import java.io.InputStream

/**
 * A singleton object to handle image caching and retrieval for game assets.
 *
 * This class provides a mechanism to cache images loaded from asset files,
 * helping to optimize performance by avoiding redundant image decoding and loading.
 */
object GameImage {
    // A mutable map that stores cached images with their asset paths as keys.
    private val cache = mutableMapOf<String, ImageBitmap>()

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
}
