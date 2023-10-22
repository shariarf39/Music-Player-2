package com.ldt.fahimshariar.ui.maintab.subpages;

import androidx.appcompat.app.AppCompatActivity;

import com.ldt.fahimshariar.R;
import com.ldt.fahimshariar.helper.menu.SongMenuHelper;
import com.ldt.fahimshariar.ui.maintab.library.song.SongChildAdapter;
import com.ldt.fahimshariar.ui.bottomsheet.OptionBottomSheet;

public class ViewArtistSongAdapter extends SongChildAdapter {
    private static final String TAG = "SongInArtistPagerAdapter";

    @Override
    public int getItemViewType(int position) {
        if(position==0) return R.layout.item_sort_song_child;
        return R.layout.item_song_bigger;
    }

    @Override
    protected void onMenuItemClick(int positionInData) {
        OptionBottomSheet
                .newInstance(SongMenuHelper.SONG_ARTIST_OPTION,getData().get(positionInData))
                .show(((AppCompatActivity)getContext()).getSupportFragmentManager(), "song_popup_menu");
    }
}
