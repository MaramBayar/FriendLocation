package com.example.friendlocation.ui.Fragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.example.friendlocation.JSONParser;
import com.example.friendlocation.Mylocation;
import com.example.friendlocation.R;
import com.example.friendlocation.databinding.BottomSheetBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class BottomSheetFragment extends BottomSheetDialogFragment {

    private BottomSheetBinding binding;

    ArrayAdapter ad;
    ArrayList<Mylocation> data;
    int index;

    public BottomSheetFragment(ArrayList<Mylocation> data, int i, ArrayAdapter ad) {
        // Required empty public constructor
        this.data = data;
        this.index = i;
        this.ad = ad;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = BottomSheetBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.btnEdit.setOnClickListener(v -> {
            UpdateDialogFragment updateDialogFragment = new UpdateDialogFragment(data,index,ad);
            updateDialogFragment.show(getParentFragmentManager(), updateDialogFragment.getTag());
            this.onDestroyView();
        });

        binding.btnDelete.setOnClickListener(v -> {
            DeleteDialogFragment deleteDialogFragment = new DeleteDialogFragment(data,index,ad);
            deleteDialogFragment.show(getParentFragmentManager(), deleteDialogFragment.getTag());
            this.onDestroyView();
        });

        // Inflate the layout for this fragment
        return root;
    }

    @Override
    public int getTheme() {
        return R.style.BottomSheetDialogTheme;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}