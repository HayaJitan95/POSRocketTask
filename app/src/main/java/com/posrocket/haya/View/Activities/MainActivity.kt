package com.posrocket.haya.View.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.posrocket.haya.R
import com.posrocket.haya.View.Adapters.CustomerListAdapter
import com.posrocket.haya.ViewModel.ListViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    lateinit var viewModel: ListViewModel
    private val customerAdapter = CustomerListAdapter(arrayListOf())
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = ViewModelProviders.of(this).get(ListViewModel::class.java)
        viewModel.refresh(this)


        customersRV.apply {
            layoutManager = GridLayoutManager(context, 3)
            adapter = customerAdapter
        }
        swipeRefreshLayout.setOnRefreshListener {
            swipeRefreshLayout.isRefreshing = false
            viewModel.refresh(this)
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                customerAdapter.filter.filter(newText)
                return false

            }
        })
        observeViewModel()
    }

    private fun observeViewModel() {
        // calls value from view model
        viewModel.customers.observe(this, Observer { customers ->
            customers?.let {
                customersRV.visibility = View.VISIBLE
                customerAdapter.updateCustomers(it)
            }
        })
        viewModel.customerLoadError.observe(this, Observer { isError ->
            isError?.let { txt_error.visibility = if (it) View.VISIBLE else View.GONE }
        })

        viewModel.loading.observe(this, Observer { isLoading ->
            isLoading?.let {
                loading_progress.visibility = if (it) View.VISIBLE else View.GONE
                if (it) {
                    txt_error.visibility = View.GONE
                    customersRV.visibility = View.GONE
                }
            }
        })
    }

    fun addNewCustomer(view: View) {
        val intent = Intent(
            this, AddCustomerActivity::class.java
        )
        startActivity(intent)
    }
}
