package com.ldt.fahimshariar.ui.maintab.library.viewholder

import com.ldt.fahimshariar.helper.songpreview.PreviewSong

interface BindTheme {
    fun bindTheme()
}

interface BindPreviewState {
    fun bindPreviewState(previewSong: PreviewSong?)
}

interface BindPlayingState {
    fun bindPlayingState()
}