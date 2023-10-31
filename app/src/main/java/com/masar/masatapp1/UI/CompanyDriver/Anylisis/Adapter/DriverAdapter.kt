package com.masar.masatapp1.UI.CompanyDriver.Anylisis.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.masar.masatapp1.Data.DriverInfo
import com.masar.masatapp1.R
import com.masar.masatapp1.server.driverInfo
import com.masar.masatapp1.databinding.ItemDriverBinding
import com.squareup.picasso.Picasso

class DriverAdapter(var listDataDriver : List<DriverInfo>) : RecyclerView.Adapter<DriverAdapter.DriverHolder>() {

    inner class DriverHolder(var binding : ItemDriverBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DriverHolder {
        return DriverHolder(ItemDriverBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return listDataDriver.size
    }

    override fun onBindViewHolder(holder: DriverHolder, position: Int) {
        var driver = listDataDriver[position]
        holder.binding.nameDriver.setText("${driver.name}")
        if (driver.stateAccount==true){
            holder.binding.stateDriver.setText("يعمل")
        }
        else if (driver.stateAccount==false){
            holder.binding.stateDriver.setText("محظور")
        }
        Picasso.get().load(driver.imageDriver).into(holder.binding.imageDriver)
        holder.binding.typeVechile.setText("${driver.typeOfVechile}")

        holder.itemView.setOnClickListener {
            driverInfo = driver
            var idDriver = bundleOf("idDriver" to driver.idDriver)

            it.findNavController().navigate(R.id.action_showAllDriverFragment_to_showOneDriverFragment,idDriver)
        }
    }
}