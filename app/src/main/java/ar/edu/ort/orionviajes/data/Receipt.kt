package ar.edu.ort.orionviajes.data

import android.os.Parcel
import android.os.Parcelable

data class Receipt(
    val total: Float
): Parcelable {
    constructor(parcel: Parcel) : this(parcel.readFloat()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeFloat(total)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Receipt> {
        override fun createFromParcel(parcel: Parcel): Receipt {
            return Receipt(parcel)
        }

        override fun newArray(size: Int): Array<Receipt?> {
            return arrayOfNulls(size)
        }
    }
}
