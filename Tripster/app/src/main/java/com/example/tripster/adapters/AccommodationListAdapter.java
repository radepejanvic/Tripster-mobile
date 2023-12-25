package com.example.tripster.adapters;




import static android.app.ProgressDialog.show;

import static androidx.navigation.ViewKt.findNavController;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.tripster.R;
import com.example.tripster.client.AccommodationService;
import com.example.tripster.client.ClientUtils;
import com.example.tripster.model.Accommodation;
import com.example.tripster.model.enums.AccommodationStatus;
import com.example.tripster.model.enums.UserType;
import com.example.tripster.model.view.Product;
import com.example.tripster.util.SharedPreferencesManager;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/*
 * Adapteri unutar Android-a sluze da prikazu unapred nedefinisanu kolicinu podataka
 * pristigle sa interneta ili ucitane iz baze ili filesystem-a uredjaja.
 * Da bi napravili adapter treba da napraivmo klasu, koja nasledjuje neki od postojecih adaptera.
 * Za potrebe ovih vezbi koristicemo ArrayAdapter koji kao izvor podataka iskoristi listu ili niz.
 * Nasledjivanjem bilo kog adaptera, dobicemo
 * nekolko metoda koje moramo da referinisemo da bi adapter ispravno radio.
 * */
public class AccommodationListAdapter extends ArrayAdapter<Product> {
    private ArrayList<Product> aProducts;

    public AccommodationListAdapter(Context context, ArrayList<Product> products){
        super(context, R.layout.product_card, products);
        aProducts = products;

    }
    /*
     * Ova metoda vraca ukupan broj elemenata u listi koje treba prikazati
     * */
    @Override
    public int getCount() {
        return aProducts.size();
    }

    /*
     * Ova metoda vraca pojedinacan element na osnovu pozicije
     * */
    @Nullable
    @Override
    public Product getItem(int position) {
        return aProducts.get(position);
    }

    /*
     * Ova metoda vraca jedinstveni identifikator, za adaptere koji prikazuju
     * listu ili niz, pozicija je dovoljno dobra. Naravno mozemo iskoristiti i
     * jedinstveni identifikator objekta, ako on postoji.
     * */
    @Override
    public long getItemId(int position) {
        return position;
    }

    /*
     * Ova metoda popunjava pojedinacan element ListView-a podacima.
     * Ako adapter cuva listu od n elemenata, adapter ce u petlji ici
     * onoliko puta koliko getCount() vrati. Prilikom svake iteracije
     * uzece java objekat sa odredjene poziciuje (model) koji cuva podatke,
     * i layout koji treba da prikaze te podatke (view) npr R.layout.product_card.
     * Kada adapter ima model i view, prosto ce uzeti podatke iz modela,
     * popuniti view podacima i poslati listview da prikaze, i nastavice
     * sledecu iteraciju.
     * */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Product product = getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.product_card,
                    parent, false);
        }
        LinearLayout productCard = convertView.findViewById(R.id.linear);
        ImageView imageView = convertView.findViewById(R.id.imageView2);
        TextView productTitle = convertView.findViewById(R.id.residenceName2);
        TextView productDescription = convertView.findViewById(R.id.distance2);
        TextView accepted = convertView.findViewById(R.id.accapted);
        TextView decline = convertView.findViewById(R.id.decline);
        TextView see = convertView.findViewById(R.id.textBooking2);

        if (SharedPreferencesManager.getUserInfo(getContext()).getUserType().equals(UserType.ADMIN)){
            see.setVisibility(View.GONE);
        } else if (SharedPreferencesManager.getUserInfo(getContext()).getUserType().equals(UserType.HOST)) {
            accepted.setVisibility(View.GONE);
            decline.setVisibility(View.GONE);
        }
        if(product != null){
            productTitle.setText(product.getTitle());
            productDescription.setText(product.getDescription());
            String base64Image = product.getImage();
            byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
            Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            imageView.setImageBitmap(decodedBitmap);
            productCard.setOnClickListener(v -> {
                Log.i("ShopApp", "Clicked: " + product.getTitle() + ", id: " +
                        product.getId().toString());
                Toast.makeText(getContext(), "Clicked: " + product.getTitle()  +
                        ", id: " + product.getId().toString(), Toast.LENGTH_SHORT).show();
            });
        }

        see.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("id", product.getId().toString());
                findNavController(v).navigate(R.id.action_navigation_accommodations_host_to_navigation_accommodation_form,bundle);

            }
        });

        accepted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Accommodation accommodation = new Accommodation();
                accommodation.setId(product.getId());
                accommodation.setStatus(AccommodationStatus.ACTIVE);
               Call<Accommodation> call =  ClientUtils.accommodationService.patchStatus(accommodation);

               call.enqueue(new Callback<Accommodation>() {
                   @Override
                   public void onResponse(Call<Accommodation> call, Response<Accommodation> response) {
                       aProducts.remove(position);


                   }

                   @Override
                   public void onFailure(Call<Accommodation> call, Throwable t) {

                   }
               });
            }
        });

        decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Accommodation accommodation = new Accommodation();
                accommodation.setId(product.getId());
                accommodation.setStatus(AccommodationStatus.SUSPENDED);
                ClientUtils.accommodationService.patchStatus(accommodation);
            }
        });
        return convertView;
    }
}
