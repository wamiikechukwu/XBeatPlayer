package com.techweblearn.musicbeat.Adapters;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.signature.MediaStoreSignature;
import com.techweblearn.musicbeat.Glide.audiocover.AlbumCover.AlbumFileCover;
import com.techweblearn.musicbeat.Glide.audiocover.AudioCover.AudioFileCover;
import com.techweblearn.musicbeat.Glide.audiocover.GlideApp;
import com.techweblearn.musicbeat.R;
import com.techweblearn.musicbeat.Utils.Util;
import com.techweblearn.musicbeat.View.CustomFragmentStatePagerAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Kunal on 19-12-2017.
 */

public class SongCoverPagerQueueAdapter extends CustomFragmentStatePagerAdapter {


    public static final String TAG = SongCoverPagerQueueAdapter.class.getSimpleName();
    private List<MediaSessionCompat.QueueItem> songArrayList;
    public SongCoverPagerQueueAdapter(FragmentManager fm, List<MediaSessionCompat.QueueItem>songArrayList) {
        super(fm);
        this.songArrayList=songArrayList;
    }

    @Override
    public Fragment getItem(int position) {
        return SongCoverFragment.newInstance(songArrayList.get(position));
    }

    @Override
    public int getCount() {
        if(songArrayList!=null)
            return songArrayList.size();
        return 0;
    }


    @NonNull
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        return super.instantiateItem(container, position);
    }

    public static class SongCoverFragment extends Fragment
    {

        private static final String SONG_ARG = "song";
        private MediaSessionCompat.QueueItem song;
        @BindView(R.id.song_image)ImageView imageView;
        Unbinder unbinder;

        public static SongCoverFragment newInstance(final MediaSessionCompat.QueueItem song) {
            SongCoverFragment frag = new SongCoverFragment();
            final Bundle args = new Bundle();
            args.putParcelable(SONG_ARG, song);
            frag.setArguments(args);
            return frag;
        }

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            song = getArguments().getParcelable(SONG_ARG);
        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view=inflater.inflate(R.layout.player_album_art,container,false);
            unbinder= ButterKnife.bind(this,view);
            return view;
        }

        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            loadAlbumCover();
        }

        private void loadAlbumCover()
        {
            Drawable drawable= Util.getSongDrawable(getContext());
            GlideApp.with(getContext())
                    .load(new AudioFileCover(String.valueOf(song.getDescription().getIconUri())))
                    .centerCrop()
                    .override(600,600)
                    .error(drawable)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .placeholder(imageView.getDrawable())
                    .signature(new MediaStoreSignature("", song.getDescription().hashCode(), 0))
                    .into(imageView);
        }

        @Override
        public void onDestroyView() {
            super.onDestroyView();
            unbinder.unbind();
        }
    }
}
