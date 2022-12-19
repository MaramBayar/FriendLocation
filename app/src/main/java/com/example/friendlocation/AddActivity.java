package com.example.friendlocation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class AddActivity extends AppCompatActivity {

    EditText ed_nom, ed_numero, ed_lon, ed_lat;
    Button btnCancel, btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        ed_nom = findViewById(R.id.nom);
        ed_numero = findViewById(R.id.numero);
        ed_lon = findViewById(R.id.lon);
        ed_lat = findViewById(R.id.lat);

        btnCancel = findViewById(R.id.btnNoAdd);
        btnAdd = findViewById(R.id.btnAdd);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AddActivity.this, MainActivity.class);
                AddActivity.this.finish();
                startActivity(i);
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddActivity.Add add = new AddActivity.Add(AddActivity.this);
                add.execute();
            }
        });
    }

    class Add extends AsyncTask {

        Context con;

        public Add(Context con) {
            this.con = con;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Object doInBackground(Object[] objects) {

            String ip = "192.168.1.18";
            String url = "http://" + ip + ":8080/servicephp/add.php";

            JSONParser parser = new JSONParser();
            HashMap<String, String> param = new HashMap<String, String>();
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
                    Intent i = new Intent(AddActivity.this, MainActivity.class);
                    AddActivity.this.finish();
                    startActivity(i);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
        }
    }
}