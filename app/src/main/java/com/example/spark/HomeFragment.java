package com.example.spark;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.IOException;
import java.lang.invoke.MethodHandle;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttp;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    final String TAG = "DEMO";
    IHome mListener;
    OkHttpClient client = new OkHttpClient();
    Handler mHandler;
    TextView textViewFact;



    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Initialize class fields
        textViewFact = view.findViewById(R.id.textViewFact);



        // Initialize thread handler
        mHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {

                // Get data from API call thread
                Fact[] facts = (Fact[]) msg.getData().getSerializable("FACT_ARR");
                textViewFact.setText(facts[0].fact);

                return false;
            }
        });



        // Next Button action
        view.findViewById(R.id.buttonNext).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFact();
            }
        });



        // Inflate the layout for this fragment
        return view;
    }


    /**
     * Send an API call to get facts
     *
     */
    public void getFact() {

        Request request = new Request.Builder()
                .url("https://api.api-ninjas.com/v1/facts?limit=1")
                .header("x-api-key", "uXRJBgWCwFWzYDUoPTbsFQ==daB2J0DTnpMpobmR")
                .build();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                // Invokes if there is no internet connection
                Log.d(TAG, "onFailure: No Internet");
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

                // Parse JSON data
                Gson gson = new Gson();
                Fact[] facts = gson.fromJson(response.body().charStream(), Fact[].class);

                Log.d(TAG, "onResponse: " + facts[0].fact);

                Message factMsg = new Message();
                Bundle factBundle = new Bundle();

                factBundle.putSerializable("FACT_ARR", facts);

                factMsg.setData(factBundle);
                mHandler.sendMessage(factMsg);

            }
        });



    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof IHome) {
            mListener = (IHome) context;
        } else {
            throw new RuntimeException("MainActivity must implement IHome");
        }
    }

    public interface IHome  {
        void goback();
    }


}