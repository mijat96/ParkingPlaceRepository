package com.rmj.parking_place.actvities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.rmj.parking_place.R;
import com.rmj.parking_place.actvities.login.ui.LoginActivity;
import com.rmj.parking_place.dto.RegistrationDTO;
import com.rmj.parking_place.service.ParkingPlaceServiceUtils;
import com.rmj.parking_place.utils.JsonLoader;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrationActivity extends CheckWifiActivity /*AppCompatActivity*/ {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        final EditText usernameEditText = findViewById(R.id.username_reg);
        final EditText passwordEditText = findViewById(R.id.password_reg);
        final EditText repeatPasswordEditText = findViewById(R.id.repeat_password);
        final EditText carRegistrationNumberEditText = findViewById(R.id.car_registration_number);
        final Button registerButton = findViewById(R.id.btn_register);
        final ProgressBar loadingProgressBar = findViewById(R.id.loading_reg);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingProgressBar.setVisibility(View.VISIBLE);

                String username = usernameEditText.getText().toString();
                RegistrationDTO registrationDTO = new RegistrationDTO(username,
                                                                    passwordEditText.getText().toString(),
                                                                    repeatPasswordEditText.getText().toString(),
                                                                    carRegistrationNumberEditText.getText().toString());
                Call<ResponseBody> call = ParkingPlaceServiceUtils.authenticationService.register(registrationDTO);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        loadingProgressBar.setVisibility(View.GONE);
                        if (response.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "User registered successfully:  "
                                    + username, Toast.LENGTH_LONG).show();

                            Intent nextIntent = new Intent(RegistrationActivity.this, LoginActivity.class);
                            startActivity(nextIntent);
                        }
                        else {
                            String message = null;
                            try {
                                message = response.errorBody().string();
                                message = message.replace("\"", "");
                            } catch (IOException e) {
                                e.printStackTrace();
                                message = "Error";
                            }

                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        t.printStackTrace();
                    }
                });
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }}
