package com.fahimshariar.fahimshariar.model.smartplaylist;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;

import com.fahimshariar.fahimshariar.loader.medialoader.TopAndRecentlyPlayedTracksLoader;
import com.fahimshariar.fahimshariar.util.MusicUtil;
import com.fahimshariar.fahimshariar.util.PreferenceUtil;
import com.ldt.fahimshariar.R;
import com.fahimshariar.fahimshariar.loader.medialoader.TopAndRecentlyPlayedTracksLoader;
import com.fahimshariar.fahimshariar.model.Song;
import com.fahimshariar.fahimshariar.util.MusicUtil;
import com.fahimshariar.fahimshariar.util.PreferenceUtil;

import java.util.ArrayList;

/**
 * @author SC (soncaokim)
 */
public class NotRecentlyPlayedPlaylist extends AbsSmartPlaylist {

    public NotRecentlyPlayedPlaylist(@NonNull Context context) {
        super(context.getString(R.string.not_recently_played), R.drawable.ic_watch_later_white_24dp);
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
        return TopAndRecentlyPlayedTracksLoader.getNotRecentlyPlayedTracks(context);
    }

    @Override
    public void clear(@NonNull Context context) {
    }

    @Override
    public boolean isClearable() {
        return false;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    protected NotRecentlyPlayedPlaylist(Parcel in) {
        super(in);
    }

    public static final Parcelable.Creator<NotRecentlyPlayedPlaylist> CREATOR = new Parcelable.Creator<NotRecentlyPlayedPlaylist>() {
        public NotRecentlyPlayedPlaylist createFromParcel(Parcel source) {
            return new NotRecentlyPlayedPlaylist(source);
        }

        public NotRecentlyPlayedPlaylist[] newArray(int size) {
            return new NotRecentlyPlayedPlaylist[size];
        }
    };
}
