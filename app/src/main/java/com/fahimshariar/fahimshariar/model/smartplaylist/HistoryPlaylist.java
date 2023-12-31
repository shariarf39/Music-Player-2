package com.fahimshariar.fahimshariar.model.smartplaylist;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;


import com.fahimshariar.fahimshariar.loader.medialoader.TopAndRecentlyPlayedTracksLoader;
import com.fahimshariar.fahimshariar.provider.HistoryStore;
import com.fahimshariar.fahimshariar.util.MusicUtil;
import com.fahimshariar.fahimshariar.util.PreferenceUtil;
import com.ldt.fahimshariar.R;
import com.fahimshariar.fahimshariar.loader.medialoader.TopAndRecentlyPlayedTracksLoader;
import com.fahimshariar.fahimshariar.model.Song;
import com.fahimshariar.fahimshariar.provider.HistoryStore;
import com.fahimshariar.fahimshariar.util.MusicUtil;
import com.fahimshariar.fahimshariar.util.PreferenceUtil;

import java.util.ArrayList;

/**
 * @author Karim Abou Zeid (kabouzeid)
 */
public class HistoryPlaylist extends AbsSmartPlaylist {

    public HistoryPlaylist(@NonNull Context context) {
        super(context.getString(R.string.history), R.drawable.ic_access_time_white_24dp);
    }

    @NonNull
    @Override
    public String getInfoString(@NonNull Context context) {
        String cutoff = PreferenceUtil.getInstance(context).getRecentlyPlayedCutoffText(context);

        return MusicUtil.buildInfoString(
            cutoff,
            super.getInfoString(context)
        );
    }

    @NonNull
    @Override
    public ArrayList<Song> getSongs(@NonNull Context context) {
        return TopAndRecentlyPlayedTracksLoader.getRecentlyPlayedTracks(context);
    }

    @Override
    public void clear(@NonNull Context context) {
        HistoryStore.getInstance(context).clear();
    }


    @Override
    public int describeContents() {
        return 0;
    }

    protected HistoryPlaylist(Parcel in) {
        super(in);
    }

    public static final Parcelable.Creator<HistoryPlaylist> CREATOR = new Parcelable.Creator<HistoryPlaylist>() {
        public HistoryPlaylist createFromParcel(Parcel source) {
            return new HistoryPlaylist(source);
        }

        public HistoryPlaylist[] newArray(int size) {
            return new HistoryPlaylist[size];
        }
    };
}
