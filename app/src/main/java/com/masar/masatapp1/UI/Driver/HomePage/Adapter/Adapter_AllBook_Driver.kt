package com.masar.masatapp1.UI.Driver.HomePage.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.masar.masatapp1.Data.BookInfo_Data
import com.masar.masatapp1.R
import com.masar.masatapp1.UI.Student.stateData.SateData.bookInfo
import com.masar.masatapp1.databinding.ItembookDriverBinding

class Adapter_AllBook_Driver(var list_Book: List<BookInfo_Data>, var context: Context) :
    RecyclerView.Adapter<Adapter_AllBook_Driver.Adapter_Holder>() {

    inner class Adapter_Holder(var binding: ItembookDriverBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Adapter_Holder {
       return Adapter_Holder(ItembookDriverBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return list_Book.size
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: Adapter_Holder, position: Int) {
        var book = list_Book[position]
        holder.binding.apply {
            counter.text = "${book.counter}"
            subscribe.text = "${book.subscribe}"
            goBack.text = "${book.go_back}"

            if (book.stateBook==1){
                stateOrder.setTextColor(Color.parseColor("#A9A9A9"))

                stateOrder.text = "في الانتظار"
            }
            else if (book.stateBook==0){
                stateOrder.text = "طلب مرفوض"
            }
            else if (book.stateBook==2){
                stateOrder.text = "يعمل حاليا"
                stateOrder.setTextColor(Color.parseColor("#FF7A00"))

            }
            else if (book.stateBook==3){
                stateOrder.text = "تم تنفيذ الرحلة"
            }
        }
        holder.itemView.setOnClickListener {
            bookInfo = book
            var state = 0
            if (book.stateBook==2){
                state=2
            }
            else if (book.stateBook==3){
                state =3
            }
            else if (book.stateBook==0){
                state =0
            }
            else if (book.stateBook==1){
                state = 1
            }
            var bundle = bundleOf("stateOrder" to state)
            it.findNavController().navigate(R.id.action_homeDriverFragment_to_showBookInDriverFragment,bundle)
        }
    }


}
