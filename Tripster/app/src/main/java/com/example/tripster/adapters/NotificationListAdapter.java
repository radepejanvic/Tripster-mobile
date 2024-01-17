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
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.tripster.R;
import com.example.tripster.client.ClientUtils;
import com.example.tripster.fragment.reports.ReportDialog;
import com.example.tripster.model.Status;
import com.example.tripster.model.enums.ReportType;
import com.example.tripster.model.enums.UserType;
import com.example.tripster.model.view.Notification;
import com.example.tripster.model.view.Review;
import com.example.tripster.util.SharedPreferencesManager;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationListAdapter extends ArrayAdapter<Notification> {

    private ArrayList<Notification> notifications;

    private UserType userType;

    private String mode;

    private Long userId;

    public NotificationListAdapter(Context context, ArrayList<Notification> notifications){
        super(context, R.layout.review_card, notifications);
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
        customizeMarkAsReadVisibility(text, notification);

        if (notification != null) {
            title.setText(notification.getTitle());
            timeStamp.setText(notification.getTimeStamp());
            text.setText(notification.getText());
            // TODO: Customize text so the words before ':' be bolded
        }

        markAsRead.setOnClickListener(v -> {
            // TODO: Add mark as read call
        });

        return convertView;
    }

    private void customizeMarkAsReadVisibility(TextView text, Notification notification) {
        switch(notification.getStatus()) {
            case NEW: text.setVisibility(View.VISIBLE); break;
            default: text.setVisibility(View.GONE);
        }
    }

}
