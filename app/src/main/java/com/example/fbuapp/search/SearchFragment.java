package com.example.fbuapp.search;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fbuapp.R;
import com.example.fbuapp.home.RestaurantsAdapter;
import com.example.fbuapp.models.Restaurant;

import java.util.ArrayList;
import java.util.List;


public class SearchFragment extends Fragment {

    private EditText etEnterLocation;
    private Button btnClear;
    private ImageView ivSearchIcon;
    private RecyclerView rvRestaurants;
    private List<Restaurant> restaurants;
    private RestaurantsAdapter adapter;
    String query;
    private TextView tvEmptyScreenNote;

    private SearchViewModel mViewModel;
    private LinearLayoutManager linearLayoutManager;

    public static final String TAG = "SearchFragment";


    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProviders().of(this).get(SearchViewModel.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

       etEnterLocation = view.findViewById(R.id.etEnterLocation);
       btnClear = view.findViewById(R.id.btnClear);
       btnClear.setBackgroundResource(R.drawable.ic_baseline_clear_24);
       ivSearchIcon = view.findViewById(R.id.ivSearchIcon);
       tvEmptyScreenNote = view.findViewById(R.id.tvEmptyScreenNote);
       tvEmptyScreenNote.setText("Please enter a location to find restaurants for you!");

       rvRestaurants = view.findViewById(R.id.rvRestaurants);
       linearLayoutManager = new LinearLayoutManager(getContext());
       rvRestaurants.setLayoutManager(linearLayoutManager);
       restaurants = new ArrayList<>();
       adapter = new RestaurantsAdapter(getContext(), restaurants);
       rvRestaurants.setAdapter(adapter);



        etEnterLocation.setOnKeyListener(new EditText.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {

                if (keyCode == EditorInfo.IME_ACTION_SEARCH || keyCode == EditorInfo.IME_ACTION_DONE || keyEvent.getAction() == KeyEvent.ACTION_DOWN &&
                        keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    query = etEnterLocation.getText().toString();
                    InputMethodManager inputManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(etEnterLocation.getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    mViewModel.getRestaurantsAtLocation(query).observe(getViewLifecycleOwner(), new Observer<List<Restaurant>>() {
                        @Override
                        public void onChanged(List<Restaurant> restaurants) {
                            // update UI
                            if (!restaurants.isEmpty()) {
                                tvEmptyScreenNote.setText("");
                                adapter.setRestaurants(restaurants);
                            } else {
                                tvEmptyScreenNote.setText("There are no restaurants fitting your food restrictions at this location.");
                            }

                        }
                    });
                }
                return false;
            }
        });


       btnClear.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               etEnterLocation.setText("");
           }
       });

       //CharSequence query = searchLocation.getQuery();
        Log.i(TAG, "Location queried is: " + query);





    }

}