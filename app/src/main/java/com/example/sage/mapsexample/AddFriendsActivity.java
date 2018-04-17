package com.example.sage.mapsexample;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AddFriendsActivity extends AppCompatActivity {
    private static final String TAG = "AddFriendsActivity";

    //View Variable
    public Button SendReqBT;
    public EditText phoneNumberET;

    private FirebaseFirestore firestoreDB;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    private String recieverPhone;

    public RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friends);

        firestoreDB = FirebaseFirestore.getInstance();
        phoneNumberET = findViewById(R.id.phone_number);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        requestQueue = Volley.newRequestQueue(this);

        SendReqBT = findViewById(R.id.send_request);
        SendReqBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recieverPhone = phoneNumberET.getText().toString();
                String url = "https://conerive-fcm.herokuapp.com/sendrequest";
                StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Intent intent = new Intent(getApplicationContext(), FriendsActivity.class);
                                startActivity(intent);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d("Error.Response", error.toString());
                            }
                        }
                ) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();
                        params.put("senderId", currentUser.getUid());
                        params.put("phone", recieverPhone);
                        return params;
                    }
                };
                requestQueue.add(postRequest);
            }
        });
    }

}