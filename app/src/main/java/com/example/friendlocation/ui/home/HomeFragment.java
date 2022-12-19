package com.example.friendlocation.ui.home;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.friendlocation.Mylocation;
import com.example.friendlocation.R;
import com.example.friendlocation.databinding.FragmentHomeBinding;
import com.example.friendlocation.JSONParser;
import com.example.friendlocation.ui.Fragments.BottomSheetFragment;
import com.example.friendlocation.ui.Fragments.LocationFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    ArrayAdapter ad;

    ArrayList<Mylocation> data = new ArrayList<Mylocation>();
    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        ad = new ArrayAdapter(getActivity(), android.R.layout.simple_dropdown_item_1line, data);
        binding.listM.setAdapter(ad);

        binding.btnD.setOnClickListener(v -> {
            Telechargement t = new Telechargement(getActivity());
            t.execute();

        });

        binding.listM.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                BottomSheetFragment bottomSheetFragment = new BottomSheetFragment(data,i,ad);
                bottomSheetFragment.show(getParentFragmentManager(), bottomSheetFragment.getTag());
                return false;
            }
        });

        binding.listM.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
              /*  LocationFragment LocFragment = new LocationFragment();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment1, R.id.map);
                fragmentTransaction.commit(); */

                Intent intent = new Intent(getActivity(), LocationFragment.class);
                startActivity(intent);
            }
        });
        return root;
    }

    class Telechargement extends AsyncTask {

        Context con;
        AlertDialog alert;

        public Telechargement(Context con) {
            this.con = con;
        }

        @Override
        protected void onPreExecute() {
            //uithread

            AlertDialog.Builder builder = new AlertDialog.Builder(con);
            builder.setTitle("telechargement");
            builder.setMessage("veuillez patientez");
            alert = builder.create();
            alert.show();

        }

        @Override
        protected Object doInBackground(Object[] objects) {
            //second thread
            String ip = "192.168.1.18";
            String url = "http://" + ip + ":8080/servicephp/getAll.php";

            JSONParser parser = new JSONParser();
            JSONObject response = parser.makeHttpRequest(url, "GET", null);

            try {
                int success = response.getInt("success");
                if (success == 0) {
                    String msg = response.getString("message");
                } else {
                    data.clear();
                    JSONArray tableau = response.getJSONArray("Ami");
                    for (int i = 0; i < tableau.length(); i++) {
                        JSONObject ligne = tableau.getJSONObject(i);
                        String nom = ligne.getString("nom");
                        String numero = ligne.getString("numero");
                        String longitude = ligne.getString("longitude");
                        String latitude = ligne.getString("latitude");
                        data.add(new Mylocation(nom, numero, longitude, latitude));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            ad.notifyDataSetChanged();
            alert.dismiss();

        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}