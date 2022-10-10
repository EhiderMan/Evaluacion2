package com.proyecto.monadoptions.ui.home;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.proyecto.monadoptions.R;
import com.proyecto.monadoptions.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        final TextView textView = root.findViewById(R.id.text_video);

        VideoView video = (VideoView) root.findViewById(R.id.videoView);
        video.setMediaController(new MediaController(getContext()));
        Uri videoView = Uri.parse("android.resource://" + getContext().getPackageName() + "/" + R.raw.narracion);
        video.setVideoURI(videoView);
        return (root);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}