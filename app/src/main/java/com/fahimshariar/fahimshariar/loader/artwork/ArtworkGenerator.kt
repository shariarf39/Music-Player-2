package com.fahimshariar.fahimshariar.loader.artwork

import android.graphics.Bitmap
import com.fahimshariar.fahimshariar.model.Media

interface ArtworkGenerator<T> where T : _root_ide_package_.com.fahimshariar.fahimshariar.model.Media {
    fun get(media: T): Bitmap
}