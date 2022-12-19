package com.example.friendlocation.ui.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import com.example.friendlocation.JSONParser;
import com.example.friendlocation.Mylocation;
import com.example.friendlocation.R;
import com.example.friendlocation.databinding.FragmentUpdateDialogBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class UpdateDialogFragment extends AppCompatDialogFragment {

    ArrayAdapter ad;
    ArrayList<Mylocation> data;
    int index;

    FragmentUpdateDialogBinding binding;

    EditText ed_nom, ed_numero, ed_lon, ed_lat;
    String nom, numero, longitude, latitude;

    public UpdateDialogFragment(ArrayList<Mylocation> data, int i, ArrayAdapter ad) {
        this.data = data;
        this.index = i;
        this.ad = ad;
    }

    public Dialog onCreateDialog (Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_update_dialog, null);

        ed_nom = view.findViewById(R.id.up_nom);
        ed_numero = view.findViewById(R.id.up_numero);
        ed_lon = view.findViewById(R.id.up_lon);
        ed_lat = view.findViewById(R.id.up_lat);

        ed_nom.setText(data.get(index).getNom());
        ed_numero.setText(data.get(index).getNumero());
        ed_lon.setText(data.get(index).getLongitude());
        ed_lat.setText(data.get(index).getLatitude());

        builder.setView(view)
                .setTitle("Update Position")
                .setCancelable(true)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        nom = ed_nom.getText().toString();
                        numero = ed_numero.getText().toString();
                        longitude = ed_lon.getText().toString();
                        latitude = ed_lat.getText().toString();
                        UpdateDialogFragment.Update up = new UpdateDialogFragment.Update(getActivity());
                        up.execute();
                    }
                });

        return builder.create();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentUpdateDialogBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    class Update extends AsyncTask {

        Context con;

        public Update(Context con) {
            this.con = con;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Object doInBackground(Object[] objects) {

            String ip = "192.168.1.18";
            String url = "http://" + ip + ":8080/servicephp/update.php";

            JSONParser parser = new JSONParser();
            HashMap<String, String> param = new HashMap<String, String>();

            param.put("old_nom", data.get(index).getNom());
            param.put("old_numero", data.get(index).getNumero());

            param.put("nom", ed_nom.getText().toString());
            param.put("numero", ed_numero.getText().toString());
            param.put("longitude", ed_lon.getText().toString());
            param.put("latitude", ed_lat.getText().toString());

            JSONObject response = parser.makeHttpRequest(url, "POST", param);

            try {
                int success = response.getInt("success");
                if (success == 0) {
                    String msg = response.getString("message");
                } else {

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            ad.notifyDataSetChanged();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}