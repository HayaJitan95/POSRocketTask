package com.posrocket.haya.View.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.core.content.ContextCompat.getDrawable
import androidx.recyclerview.widget.RecyclerView
import com.posrocket.haya.Model.Customer
import com.posrocket.haya.R
import kotlinx.android.synthetic.main.item_customer_list.view.*
import java.util.*
import kotlin.collections.ArrayList

class CustomerListAdapter(var customers: ArrayList<Customer>) :
    RecyclerView.Adapter<CustomerListAdapter.CustomerViewHolder>(), Filterable {

    var customerList: ArrayList<Customer> = customers
    var originalList: ArrayList<Customer> = customers

    fun updateCustomers(newCustomers: List<Customer>) {
        customerList.clear()
        customerList.addAll(newCustomers)
        notifyDataSetChanged()
    }

    class CustomerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val context = view.context
        private val customerName = view.txt_name
        private val mobile = view.txt_mobile
        private val balance = view.txt_balance
        private val tags = view.txt_tags
        private val img_gender = view.img_gender


        fun bind(customer: Customer) {
            customerName.text = customer.name + " " + customer.lastName.orEmpty()
            balance.text = customer.balance

            for (i in customer.tags!!) {
                tags.text = i.name
            }

            if (customer.gender.equals("MALE"))
                img_gender.background = getDrawable(context, R.drawable.ic_face_male)
            else if (customer.gender.equals("FEMALE"))
                img_gender.background = getDrawable(context, R.drawable.ic_face_female)
            else img_gender.background = getDrawable(context, R.drawable.ic_face_gray)

            for (i in customer.mobile!!) {
                if (i.isPrimary!!) mobile.text = i.number
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        CustomerViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_customer_list, parent, false)
        )

    override fun getItemCount() = customerList.size

    override fun onBindViewHolder(holder: CustomerViewHolder, position: Int) {
        holder.bind(customerList[position])
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            val filterResults = FilterResults()
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val searchableList: ArrayList<Customer> = arrayListOf()

                if (constraint.isNullOrBlank()) {
                    searchableList.addAll(originalList)
                } else {
                    val filterPattern =
                        constraint.toString().toLowerCase(Locale.getDefault()).trim { it <= ' ' }

                    for (item in originalList) {
                        if (item.toString().toLowerCase(Locale.getDefault())
                                .contains(filterPattern)
                        ) {
                            searchableList.add(item)
                        }
                    }

                }
                return filterResults.also {
                    it.values = searchableList

                }
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                customerList = results?.values as ArrayList<Customer>
                notifyDataSetChanged()
            }
        }
    }
}