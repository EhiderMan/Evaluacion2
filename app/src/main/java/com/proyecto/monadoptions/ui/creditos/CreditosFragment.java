package com.proyecto.monadoptions.ui.creditos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import com.proyecto.monadoptions.R;

public class CreditosFragment extends Fragment {

    private CreditosViewModel creditosViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_creditos, container, false);
        final TextView textView = root.findViewById(R.id.text_creditos);

        return root;
    }
}