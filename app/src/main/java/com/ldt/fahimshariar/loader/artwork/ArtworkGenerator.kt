package com.ldt.fahimshariar.loader.artwork

import android.graphics.Bitmap
import com.ldt.fahimshariar.model.Media

interface ArtworkGenerator<T> where T : Media{
    fun get(media: T): Bitmap
}