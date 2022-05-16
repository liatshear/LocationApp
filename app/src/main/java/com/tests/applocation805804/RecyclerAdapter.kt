package com.tests.applocation805804

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.w3c.dom.Text

class RecyclerAdapter(
    numbers: MutableList<String>,
    Cnames: MutableList<String>,
    Locationtxt: String?,
    mainActivity: ShareLocation
):
RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {


    private var contactNames = Cnames
    private var digits = numbers
    private var Locationtxt = Locationtxt
    private var mainActivity = mainActivity

    // create new views


    // binds the list items to a view
    override fun onBindViewHolder(holder: RecyclerAdapter.ViewHolder, position: Int) {

        holder.itemName.text = contactNames[position]
        holder.itemNumber.text = digits[position]

    }

    override fun onCreateViewHolder(holder: ViewGroup, p1: Int): RecyclerAdapter.ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val viewH = LayoutInflater.from(holder.context)
            .inflate(R.layout.card_view_design, holder, false)
        return ViewHolder(viewH)
    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return contactNames.size
    }

    // Holds the views for adding it to image and text
    inner class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        var itemName: TextView
        var itemNumber: TextView

        init {
            itemName = itemView.findViewById(R.id.itemName)
            itemNumber = itemView.findViewById(R.id.itemNumber)
            itemName.setOnClickListener {
                //val intent = Intent(Intent.ACTION_SEND)
                val url = "https://api.whatsapp.com/send?phone=" + itemNumber.text + "&text=" + Locationtxt
                val uri = Uri.parse(url)
                val intent = Intent(Intent.ACTION_VIEW, uri)
               /* intent.type = "text/plain"
                intent.setPackage("com.whatsapp")
                intent.putExtra(Intent.EXTRA_TEXT, Locationtxt)*/
                mainActivity.startActivity(intent)
            }
        }

    }
}
