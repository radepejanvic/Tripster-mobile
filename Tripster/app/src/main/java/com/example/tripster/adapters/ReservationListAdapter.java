package com.example.tripster.adapters;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.example.tripster.FragmentTransition;
import com.example.tripster.R;
import com.example.tripster.client.ClientUtils;
import com.example.tripster.fragment.reports.ReportDialog;
import com.example.tripster.fragment.reservations.ReservationListFragment;
import com.example.tripster.model.enums.ReportType;
import com.example.tripster.model.enums.ReservationStatus;
import com.example.tripster.model.enums.UserType;
import com.example.tripster.model.view.Reservation;
import com.example.tripster.util.SharedPreferencesManager;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReservationListAdapter extends ArrayAdapter<Reservation> {

        private ArrayList<Reservation> reviews;

        private UserType userType;

        private Long userId;

        private ReservationListFragment reservationListFragment;

        public ReservationListAdapter(Context context, ArrayList<Reservation> reviews, ReservationListFragment reservationListFragment){
                super(context, R.layout.review_card, reviews);
                this.reviews = reviews;
                this.reservationListFragment = reservationListFragment;
                userType = SharedPreferencesManager.getUserInfo(getContext()).getUserType();
                userId =  SharedPreferencesManager.getUserInfo(getContext()).getUserID();
        }
        @Override
        public int getCount() {
                return reviews.size();
        }

        @Nullable
        @Override
        public Reservation getItem(int position) {
                return reviews.get(position);
        }

        @Override
        public long getItemId(int position) {
                return position;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                Reservation reservation = getItem(position);

                if(convertView == null){
                        convertView = LayoutInflater.from(getContext()).inflate(R.layout.reservation,
                                parent, false);
                }

                CardView cardView = convertView.findViewById(R.id.reservationCard);
                TextView title = convertView.findViewById(R.id.residenceName);
                ImageView imageView = convertView.findViewById(R.id.imageView2);
                TextView address = convertView.findViewById(R.id.address);
                TextView guest = convertView.findViewById(R.id.guestEmail);
                TextView numOfCancelled = convertView.findViewById(R.id.numOfCancelled);
                TextView status = convertView.findViewById(R.id.status);
                TextView price = convertView.findViewById(R.id.price);
                TextView numOfGuest = convertView.findViewById(R.id.numOfGuest);
                TextView duration = convertView.findViewById(R.id.duration);

                TextView accept = convertView.findViewById(R.id.acceptReservation);
                TextView reject = convertView.findViewById(R.id.rejectReservation);
                TextView userButton = convertView.findViewById(R.id.userReservationButton);
                ImageView report = convertView.findViewById(R.id.report_guest);

                if(reservation != null){

                        title.setText(reservation.getName());
                        address.setText(reservation.getAddress());
                        if (userType.equals(UserType.HOST)){
                                guest.setText("Guest: "+reservation.getGuest());
                                numOfCancelled.setText("Number of cancelled: "+reservation.getNumOfCancelled());
                        }
                        status.setText(String.valueOf(reservation.getStatus()));
                        price.setText(String.valueOf(reservation.getPrice()));
                        numOfGuest.setText(String.valueOf(reservation.getDuration())+" nights, "
                                +String.valueOf(reservation.getNumOfGuest())+" guests");
                        duration.setText(reservation.getTimeStamp());
                        String base64Image = reservation.getPhoto();
                        byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
                        Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                        imageView.setImageBitmap(decodedBitmap);
                        customizeImageViewVisibility(accept,reject,userButton, report,cardView,reservation);

                }

                reject.setOnClickListener(v -> {

                        Call<String> call =  ClientUtils.reservationService.reject(reservation.getId());
                        FragmentTransition.to(ReservationListFragment.newInstance(new ArrayList<>()), reservationListFragment.getActivity(), false, R.id.scroll_reviews_list);

                        notifyDataSetChanged();
                        call.enqueue(new Callback<String>() {
                                @Override
                                public void onResponse(Call<String> call, Response<String> response) {

                                }

                                @Override
                                public void onFailure(Call<String> call, Throwable t) {
                                        Log.d("REZ","greska");
                                }
                        });

                });

                accept.setOnClickListener(v -> {

                        Call<String> call =  ClientUtils.reservationService.accept(reservation.getId());
                        FragmentTransition.to(ReservationListFragment.newInstance(new ArrayList<>()), reservationListFragment.getActivity(), false, R.id.scroll_reviews_list);

                        call.enqueue(new Callback<String>() {
                                @Override
                                public void onResponse(Call<String> call, Response<String> response) {

                                }

                                @Override
                                public void onFailure(Call<String> call, Throwable t) {
                                        Log.d("REZ","greska");
                                }
                        });

                });
                userButton.setOnClickListener(v -> {
                        Call<String> call;

                        if (reservation.getStatus().equals(ReservationStatus.REJECTED)){
                                call =  ClientUtils.reservationService.delete(reservation.getId());
                                FragmentTransition.to(ReservationListFragment.newInstance(new ArrayList<>()), reservationListFragment.getActivity(), false, R.id.scroll_reviews_list);
                        }else {
                                call =  ClientUtils.reservationService.cancel(reservation.getId());
                                FragmentTransition.to(ReservationListFragment.newInstance(new ArrayList<>()), reservationListFragment.getActivity(), false, R.id.scroll_reviews_list);

                        }


                        call.enqueue(new Callback<String>() {
                                @Override
                                public void onResponse(Call<String> call, Response<String> response) {

                                }

                                @Override
                                public void onFailure(Call<String> call, Throwable t) {
                                        Log.d("REZ","greska");
                                }
                        });

                });

                report.setOnClickListener(v -> {
                        Dialog reportDialog =new ReportDialog(getContext(), userId, reservation.getUserID(), ReportType.USER);
                        reportDialog.setCancelable(true);
                        reportDialog.setCanceledOnTouchOutside(true);
                        reportDialog.show();
                });


                return convertView;
        }

        private void customizeImageViewVisibility(TextView accept, TextView reject,TextView userButton, ImageView report, CardView cardView,Reservation reservation) {
                accept.setVisibility(View.VISIBLE);
                reject.setVisibility(View.VISIBLE);
                userButton.setVisibility(View.VISIBLE);
                report.setVisibility(View.VISIBLE);
                cardView.setAlpha(1F);


                if(userType.equals(UserType.HOST)){
                        userButton.setVisibility(View.GONE);

                        if (!reservation.getStatus().equals(ReservationStatus.PENDING)){
                                accept.setVisibility(View.GONE);
                                reject.setVisibility(View.GONE);

                        }
                        if (reservation.getStatus().equals(ReservationStatus.CANCELLED)){
                                cardView.setAlpha(0.5F);
                                report.setVisibility(View.GONE);
                        }
                        if (!reservation.isReportable()) {
                                report.setVisibility(View.GONE);
                        }

                } else {

                        if (!userType.equals(UserType.GUEST) || !reservation.isReportable()) {
                                report.setVisibility(View.GONE);
                        }

                        accept.setVisibility(View.GONE);
                        reject.setVisibility(View.GONE);
                        switch(reservation.getStatus()) {
                                case CANCELLED:
                                        cardView.setAlpha(0.5F);
                                        userButton.setVisibility(View.GONE);
                                        break;
                                case REJECTED:
                                        userButton.setText("Delete");
                                        userButton.setTextColor(Color.RED);
                                        break;
                                case ACCEPTED:
                                        userButton.setText("Cancel");
                                        break;
                                case PENDING:
                                        userButton.setVisibility(View.GONE);
                                        break;
                        }
                }
        }
}
