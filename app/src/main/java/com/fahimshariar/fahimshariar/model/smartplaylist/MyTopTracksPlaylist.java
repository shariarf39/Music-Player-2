package com.fahimshariar.fahimshariar.model.smartplaylist;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;


import com.fahimshariar.fahimshariar.loader.medialoader.TopAndRecentlyPlayedTracksLoader;
import com.fahimshariar.fahimshariar.provider.SongPlayCountStore;
import com.ldt.fahimshariar.R;
import com.fahimshariar.fahimshariar.loader.medialoader.TopAndRecentlyPlayedTracksLoader;
import com.fahimshariar.fahimshariar.model.Song;
import com.fahimshariar.fahimshariar.provider.SongPlayCountStore;

import java.util.ArrayList;

/**
 * @author Karim Abou Zeid (kabouzeid)
 */
public class MyTopTracksPlaylist extends AbsSmartPlaylist {

    public MyTopTracksPlaylist(@NonNull Context context) {
        super(context.getString(R.string.playlist_top_tracks), R.drawable.ic_trending_up_white_24dp);
    }

    @NonNull
    @Override
    public ArrayList<Song> getSongs(@NonNull Context context) {
        return TopAndRecentlyPlayedTracksLoader.getTopTracks(context);
    }

    @Override
    public void clear(@NonNull Context context) {
        SongPlayCountStore.getInstance(context).clear();
    }


    @Override
    public int describeContents() {
        return 0;
    }

    protected MyTopTracksPlaylist(Parcel in) {
        super(in);
    }

    public static final Parcelable.Creator<MyTopTracksPlaylist> CREATOR = new Parcelable.Creator<MyTopTracksPlaylist>() {
        public MyTopTracksPlaylist createFromParcel(Parcel source) {
            return new MyTopTracksPlaylist(source);
        }

        public MyTopTracksPlaylist[] newArray(int size) {
            return new MyTopTracksPlaylist[size];
        }
    };
}
