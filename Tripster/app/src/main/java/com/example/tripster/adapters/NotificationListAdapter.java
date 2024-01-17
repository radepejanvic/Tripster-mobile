package com.example.tripster.adapters;

import android.app.Dialog;
import android.content.Context;
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
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.tripster.R;
import com.example.tripster.client.ClientUtils;
import com.example.tripster.fragment.reports.ReportDialog;
import com.example.tripster.model.Status;
import com.example.tripster.model.enums.NotificationStatus;
import com.example.tripster.model.enums.ReportType;
import com.example.tripster.model.enums.UserType;
import com.example.tripster.model.view.Notification;
import com.example.tripster.model.view.Review;
import com.example.tripster.util.SharedPreferencesManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationListAdapter extends ArrayAdapter<Notification> {

    private ArrayList<Notification> notifications;

    private UserType userType;

    private String mode;

    private Long userId;

    public NotificationListAdapter(Context context, ArrayList<Notification> notifications){
        super(context, R.layout.notification_card, notifications);
        this.notifications = notifications;
        userType = SharedPreferencesManager.getUserInfo(getContext()).getUserType();
        userId =  SharedPreferencesManager.getUserInfo(getContext()).getUserID();
    }

    @Override
    public int getCount() {
        return notifications.size();
    }

    @Nullable
    @Override
    public Notification getItem(int position) {
        return notifications.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Notification notification = getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.notification_card,
                    parent, false);
        }

        ConstraintLayout card = convertView.findViewById(R.id.notification_card);
        TextView title = convertView.findViewById(R.id.notification_title);
        TextView timeStamp = convertView.findViewById(R.id.notification_timestamp);
        TextView text = convertView.findViewById(R.id.notification_text);
        TextView markAsRead = convertView.findViewById(R.id.mark_as_read);
        customizeMarkAsReadVisibility(markAsRead, notification);

        if (notification != null) {
            title.setText(notification.getTitle());
            timeStamp.setText(notification.getTimeStamp());
            text.setText(notification.getText());
            // TODO: Customize text so the words before ':' be bolded
        }

        markAsRead.setOnClickListener(v -> {
            Call<String> call = ClientUtils.notificationService.markAsRead(new Status(notification.getId(), "READ"));

            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if(response.code() == 200) {

                        notifications.remove(position);
                        notifyDataSetChanged();

                        Log.d("PATCH Request", "Mark as read " + response.body());
                    } else {
                        Log.e("PATCH Request", "Error marking notification as read " + response.code());
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    notifications.remove(position);
                    notifyDataSetChanged();
                    t.printStackTrace();
                }
            });
        });

        return convertView;
    }

    private void customizeMarkAsReadVisibility(TextView markAsRead, Notification notification) {
        switch(notification.getStatus()) {
            case NEW: markAsRead.setVisibility(View.VISIBLE); break;
            default: markAsRead.setVisibility(View.GONE);
        }
    }

}
