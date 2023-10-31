package com.masar.masatapp1.UI.Student.MainPage_Student.Book_Student.Adapter_Reservation


import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.recyclerview.widget.RecyclerView
import com.masar.masatapp1.Data.DriverInfo
import com.masar.masatapp1.R
import com.masar.masatapp1.databinding.ItemSearchCarBinding
import com.masar.masatapp1.UI.Student.stateData.SateData
import com.masar.masatapp1.UI.Student.stateData.SateData.counter
import com.masar.masatapp1.UI.Student.stateData.SateData.selectedPosition
import com.squareup.picasso.Picasso

class Driver_Results_Adapter(var list_DeliveryInfo: List<DriverInfo>, var context: Context) :
    RecyclerView.Adapter<Driver_Results_Adapter.CarSeachHolder>() {


    inner class CarSeachHolder(var binding: ItemSearchCarBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarSeachHolder {
        return CarSeachHolder(ItemSearchCarBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return list_DeliveryInfo.size
    }

    override fun onBindViewHolder(holder: CarSeachHolder, position: Int) {
        val deliveryInfo = list_DeliveryInfo[position]

        holder.binding.apply {
            quntity.text = "${counter}"
            nameDeliveryCar.text = deliveryInfo.name
            counterSeetApprove.text = "${deliveryInfo.approveSeats}"
            nameComany.text = "${deliveryInfo.nameCompany}"
            Picasso.get().load(deliveryInfo.imageDriver).into(imageDelivery)
            // Set the initial background color and text based on the selected position
            val backgroundColor = if (position == selectedPosition) R.color.orange else R.color.gray
            val buttonText = if (position == selectedPosition) "تم" else "اخترني"
            chooseDeliveryButton.setBackgroundColor(ContextCompat.getColor(context, backgroundColor))
            chooseDeliveryButton.text = buttonText

            // Set up click listener for the button
            chooseDeliveryButton.setOnClickListener {
                counter = 1
                quntity.text = "${counter}"
                SateData.idDelivery = deliveryInfo.idDriver
                SateData.idComany = deliveryInfo.idCompany
                SateData.nameDelivery = deliveryInfo.name
                SateData.nameComany = deliveryInfo.nameCompany
                val previousSelectedPosition = selectedPosition
                selectedPosition = if (selectedPosition == position) -1 else position

                // Update the button's background color and text
                val newBackgroundColor = if (position == selectedPosition) R.color.orange else R.color.gray
                val newButtonText = if (position == selectedPosition) "تم" else "اخترني"

                chooseDeliveryButton.setBackgroundColor(ContextCompat.getColor(context, newBackgroundColor))
                chooseDeliveryButton.text = newButtonText

                // Notify items that their state has changed
                notifyItemChanged(previousSelectedPosition)
                notifyItemChanged(selectedPosition)
            }

            increase.setOnClickListener {
                if (counter == deliveryInfo.approveSeats) {
                    Toast.makeText(context.applicationContext, "هذا أقصى عدد يمكن حجزه", Toast.LENGTH_LONG).show()
                } else {
                    quntity.text = "${++counter}"
                }
            }

            decrese.setOnClickListener {
                if (counter == 1) {
                    // Handle the case when the counter is 1 (if needed)
                } else {
                    quntity.text = "${--counter}"
                }
            }

            // Update visibility based on the selected position
            ConstrainQuantity.isGone = position != selectedPosition


            infoDeliveryButton.setOnClickListener {
                Dialog_Info_Deriver(context,deliveryInfo).show()

            }
        }
    }
}
