package com.neu.snowhouse.ui.register;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.neu.snowhouse.SessionManagement;
import com.neu.snowhouse.MainActivity;
import com.neu.snowhouse.R;
import com.neu.snowhouse.api.API;
import com.neu.snowhouse.api.RetrofitClient;
import com.neu.snowhouse.model.request.UserRegisterRequestModel;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterFragment extends Fragment {
    EditText userName;
    EditText password;
    EditText password2;
    API api = RetrofitClient.getInstance().getAPI();
    Call<ResponseBody> uploadUser;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        assert getActivity() != null;
        userName = getActivity().findViewById(R.id.register_userName);
        password = getActivity().findViewById(R.id.register_password);
        password2 = getActivity().findViewById(R.id.register_password2);
        TextView navButton = getActivity().findViewById(R.id.nav_login);
        navButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.register_login);
            }
        });

        Button registerButton = getActivity().findViewById(R.id.button_register);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // only used in dev mode
//                SessionManagement.addUserName(getActivity(), "tma");
//                Intent intent = new Intent(getContext(), MainActivity.class);
//                startActivity(intent);
                // uncomment the following code when dev is done
                if (userName.getText().toString().trim().equals("") || password.getText().toString().trim().equals("")) {
                    Toast.makeText(getContext(), "UserName & Password can't be empty", Toast.LENGTH_SHORT).show();
                } else if (!password.getText().toString().equals(password2.getText().toString())) {
                    Toast.makeText(getContext(), "Two passwords must be same", Toast.LENGTH_SHORT).show();
                } else {
                    UserRegisterRequestModel user = new UserRegisterRequestModel();
                    user.setUserName(userName.getText().toString());
                    user.setPassword(password.getText().toString());
                    uploadUser = api.registerUser(user);
                    // get the result
                    uploadUser.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                try {
                                    Toast.makeText(getContext(), response.body().string(), Toast.LENGTH_LONG).show();
                                    SessionManagement.addUserName(getActivity(), userName.getText().toString());
                                    Intent intent = new Intent(getContext(), MainActivity.class);
                                    startActivity(intent);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                            if (!response.isSuccessful() && response.errorBody() != null) {
                                try {
                                    Toast.makeText(getContext(), response.errorBody().string(), Toast.LENGTH_LONG).show();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Toast.makeText(getContext(), "Request failed", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
}