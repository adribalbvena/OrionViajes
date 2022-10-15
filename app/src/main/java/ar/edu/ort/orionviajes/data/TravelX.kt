package ar.edu.ort.orionviajes.data

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class TravelX(
    @field:SerializedName("id")
    val id: String,
    @field:SerializedName("title")
    val title: String,
    @field:SerializedName("budget")
    val budget: Float,
    @field:SerializedName("startDate")
    val startDate: String,
    @field:SerializedName("endDate")
    val endDate: String,
    //val expenses: List<Any>,

) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readFloat(),
        parcel.readString().toString(),
        parcel.readString().toString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(title)
        parcel.writeFloat(budget)
        parcel.writeString(startDate)
        parcel.writeString(endDate)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TravelX> {
        override fun createFromParcel(parcel: Parcel): TravelX {
            return TravelX(parcel)
        }

        override fun newArray(size: Int): Array<TravelX?> {
            return arrayOfNulls(size)
        }
    }
}