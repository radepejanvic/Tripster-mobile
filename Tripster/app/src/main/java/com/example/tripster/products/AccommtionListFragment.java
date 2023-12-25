package com.example.tripster.products;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.ListFragment;

import com.example.tripster.adapters.AccommodationListAdapter;
import com.example.tripster.client.ClientUtils;
import com.example.tripster.databinding.FragmentAccommodationListBinding;
import com.example.tripster.model.Accommodation;
import com.example.tripster.model.view.Product;
import com.example.tripster.util.SharedPreferencesManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AccommtionListFragment extends ListFragment {
    private AccommodationListAdapter adapter;
    private static final String ARG_PARAM = "param";
    private ArrayList<Product> mProducts;
    private FragmentAccommodationListBinding binding;

    public static AccommtionListFragment newInstance(ArrayList<Product> products){
        AccommtionListFragment fragment = new AccommtionListFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_PARAM, products);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i("ShopApp", "onCreateView Products List Fragment");
        binding = FragmentAccommodationListBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        getAccommodation();
        return root;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("ShopApp", "onCreate Products List Fragment");
        if (getArguments() != null) {
            mProducts = getArguments().getParcelableArrayList(ARG_PARAM);
            adapter = new AccommodationListAdapter(getActivity(), mProducts);
            setListAdapter(adapter);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onListItemClick(@NonNull ListView l, @NonNull View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        // Handle the click on item at 'position'
    }

    private void getAccommodation(){
        Call<List<Accommodation>> call = ClientUtils.accommodationService.getForGuest(SharedPreferencesManager.getUserInfo(getContext()).getPersonID());

        call.enqueue(new Callback<List<Accommodation>>() {
            @Override
            public void onResponse(Call<List<Accommodation>> call, Response<List<Accommodation>> response) {
                List<Product> products = new ArrayList<>();
                for (Accommodation accommodation: response.body()) {
                    Product product = new Product();
                    product.setTitle(accommodation.getName());
                    product.setDescription(accommodation.getShortDescription());
                    product.setId(accommodation.getId());
                    product.setImage(accommodation.getPhoto());
                    products.add(product);

                }
                setAccommodation(products);
            }

            @Override
            public void onFailure(Call<List<Accommodation>> call, Throwable t) {

            }
        });
    }

    private void setAccommodation(List<Product> list){
        this.adapter.addAll(list);
    }

}
