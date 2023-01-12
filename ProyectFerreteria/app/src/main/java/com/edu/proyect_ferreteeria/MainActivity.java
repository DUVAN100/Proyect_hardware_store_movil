package com.edu.proyect_ferreteeria;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements Response.Listener<JSONObject>, Response.ErrorListener {
    EditText idProducto, nombreProducto, StockProducto, PrecioCompra, PrecioVenta;
    CheckBox ActivoProducto;
    RequestQueue rq;
    JsonRequest jrq;
    String id, nombre, stock, preciocompra, preciocventa, url;
    byte sw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        idProducto = findViewById(R.id.IdProducto);
        nombreProducto = findViewById(R.id.NombreProducto);
        StockProducto = findViewById(R.id.StockProducto);
        PrecioCompra = findViewById(R.id.PrecioCompra);
        PrecioVenta = findViewById(R.id.PrecioVenta);
        ActivoProducto = findViewById(R.id.ActivoProducto);
        rq = Volley.newRequestQueue(this);
        sw = 0;
    }
    public void Limpiar_campos(View view) {
        idProducto.setText("");
        nombreProducto.setText("");
        StockProducto.setText("");
        PrecioCompra.setText("");
        PrecioVenta.setText("");
    }

    public void Guardar(View view) {
        id = idProducto.getText().toString();
        nombre = nombreProducto.getText().toString();
        stock = StockProducto.getText().toString();
        preciocompra = PrecioCompra.getText().toString();
        preciocventa = PrecioVenta.getText().toString();
        if (id.isEmpty() || nombre.isEmpty() || stock.isEmpty() || preciocompra.isEmpty() || preciocventa.isEmpty()) {
            Toast.makeText(this, "Todos los datos son regueridos", Toast.LENGTH_SHORT).show();
            idProducto.requestFocus();
            nombreProducto.requestFocus();
            StockProducto.requestFocus();
            PrecioCompra.requestFocus();
            PrecioVenta.requestFocus();
        } else {
            if (sw == 0) {
                Toast.makeText(this, "Registro", Toast.LENGTH_SHORT).show();
                url = "http://172.18.70.125:80/NewWebService/guardar.php";
            } else {
                Toast.makeText(this, "Actualizo", Toast.LENGTH_SHORT).show();
                url = "http://172.18.70.125:80/NewWebService/actualiza.php";
                sw = 0;
            }
            StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Limpiar_campos(view);
                            Toast.makeText(getApplicationContext(), "Registro de Producto realizado!", Toast.LENGTH_LONG).show();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), "Registro de producto incorrecto!", Toast.LENGTH_LONG).show();
                        }
                    }
            ) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("id", idProducto.getText().toString().trim());
                    params.put("nombre", nombreProducto.getText().toString().trim());
                    params.put("stock", StockProducto.getText().toString().trim());
                    params.put("precio_compra", PrecioCompra.getText().toString().trim());
                    params.put("precio_venta", PrecioVenta.getText().toString().trim());
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(postRequest);
        }
    }
    public void Consultar(View view){
        id = idProducto.getText().toString();
        if (id.isEmpty()) {
            Toast.makeText(this, "id es requerido para la busqueda", Toast.LENGTH_SHORT).show();
            idProducto.requestFocus();
        } else {
            url = "http://172.18.70.125:80/NewWebService/consulta.php?id=" +id;
            jrq = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
            rq.add(jrq);
        }
    }
    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onResponse(JSONObject response) {
        sw=1;
        Toast.makeText(this, "Usuario registrado", Toast.LENGTH_SHORT).show();
        JSONArray jsonArray = response.optJSONArray("datos");
        JSONObject jsonObject = null;
        try {
            jsonObject=jsonArray.getJSONObject(0); //Posicion 0 del arreglo...
            idProducto.setText(jsonObject.optString("id"));
            nombreProducto.setText(jsonObject.optString("nombre"));
            StockProducto.setText(jsonObject.optString("stock"));
            PrecioCompra.setText(jsonObject.optString("precio_compra"));
            PrecioVenta.setText(jsonObject.optString("precio_venta"));
            if (jsonObject.optString("activo").equals("si"))
                ActivoProducto.setChecked(true);
            else
                ActivoProducto.setChecked(false);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}












