package com.fahimshariar.fahimshariar.ui.maintab.subpages;

import com.fahimshariar.fahimshariar.addon.lastfm.rest.model.LastFmArtist;
import com.fahimshariar.fahimshariar.addon.lastfm.rest.model.LastFmArtist;

import java.util.ArrayList;

public interface ResultCallback {
    void onSuccess(LastFmArtist lastFmArtist);
    void onFailure(Exception e);
    void onSuccess(ArrayList<String> mResult);
}