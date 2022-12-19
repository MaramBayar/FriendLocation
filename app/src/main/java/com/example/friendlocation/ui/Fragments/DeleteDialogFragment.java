package com.example.friendlocation.ui.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.friendlocation.JSONParser;
import com.example.friendlocation.Mylocation;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class DeleteDialogFragment extends AppCompatDialogFragment {

    ArrayAdapter ad;
    ArrayList<Mylocation> data;
    int index;

    public DeleteDialogFragment(ArrayList<Mylocation> data, int i, ArrayAdapter ad) {
        // Required empty public constructor
        this.data = data;
        this.index = i;
        this.ad = ad;
    }

    public Dialog onCreateDialog (Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Confirm Delete")
                .setMessage("Are you sure you want to delete this Location ? This process cannot be undone ! ")
                .setCancelable(true)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Delete d = new Delete(getActivity(),data.get(index));
                        d.execute();
                    }
                });
        return builder.create();
    }

    class Delete extends AsyncTask {

        Context con;
        Mylocation ml;

        public Delete(Context con, Mylocation ml) {
            this.con = con;
            this.ml = ml;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Object doInBackground(Object[] objects) {

            String ip = "192.168.1.18";
            String url = "http://"+ip+":8080/servicephp/delete.php";

            JSONParser parser = new JSONParser();
            HashMap<String, String> param = new HashMap<String, String>();
            param.put("nom", ml.getNom());
            param.put("numero", ml.getNumero());

            JSONObject response = parser.makeHttpRequest(url, "POST", param);

            try {
                int success = response.getInt("success");
                if (success == 0) {
                    String msg = response.getString("message");
                } else {
                    data.remove(ml);
                }
            } catch (JSONException e) {

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
    }
}