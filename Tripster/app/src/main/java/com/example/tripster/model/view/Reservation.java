package com.example.tripster.model.view;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.example.tripster.model.enums.ReservationStatus;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class Reservation implements Parcelable{
    private Long id;
    private String name;
    private String photo;
    private String address;
    private int duration;
    private String timeStamp;
    private ReservationStatus status;
    private int numOfGuest;
    private int numOfCancelled;
    private String guest;
    private Long userID;
    private boolean reportable;

    private double price;

    protected Reservation(Parcel in) {
        id = in.readLong();
        name = in.readString();
        photo = in.readString();
        address = in.readString();
        duration = in.readInt();
        timeStamp = in.readString();
        status = ReservationStatus.valueOf(in.readString());
        numOfGuest = in.readInt();
        numOfCancelled = in.readInt();
        guest = in.readString();
        userID = in.readLong();
        price = in.readDouble();
        reportable = in.readBoolean();
    }

    public static final Parcelable.Creator<Reservation> CREATOR = new Parcelable.Creator<Reservation>() {
        @Override
        public Reservation createFromParcel(Parcel in) {
            return new Reservation(in);
        }

        @Override
        public Reservation[] newArray(int size) {
            return new Reservation[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeString(photo);
        dest.writeString(address);
        dest.writeInt(duration);
        dest.writeString(timeStamp);
        dest.writeString(String.valueOf(status));
        dest.writeInt(numOfGuest);
        dest.writeInt(numOfCancelled);
        dest.writeString(guest);
        dest.writeLong(userID);
        dest.writeDouble(price);
        dest.writeBoolean(reportable);
    }
}
