package com.posrocket.haya.ViewModel

import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.net.ConnectivityManager
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.posrocket.haya.DBHelper
import com.posrocket.haya.Model.Customer
import com.posrocket.haya.Model.CustomerResponse
import com.posrocket.haya.Model.Service
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import java.lang.reflect.Type


class ListViewModel : ViewModel() {
    private val service = Service()
    private val disposable =
        CompositeDisposable()
    val customers = MutableLiveData<List<Customer>>()
    val customerLoadError = MutableLiveData<Boolean>()
    val loading = MutableLiveData<Boolean>()

    lateinit var myDb: DBHelper


    fun refresh(context: Context) {
        myDb = DBHelper(context)
        if (isNetworkAvailable(context)) {
            fetchCustomers()
        } else {
            fetchCountriesOffline()
        }
    }

    private fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(CONNECTIVITY_SERVICE) as? ConnectivityManager
        val networkInfo = connectivityManager?.activeNetworkInfo
        // In here we return true if network is not null and Network is connected
        return networkInfo != null && networkInfo.isConnected
    }

    val gson = Gson()
    private fun fetchCountriesOffline() {
        loading.value = true
        val res = myDb.getAllData(myDb.TABLE_CUSTOMERS)
        if (res!!.count == 0) {
            // show message
            Log.e("Error", "Nothing found")
            return
        }
        lateinit var finalOutputString: ArrayList<Customer>

        with(res) {
            while (moveToNext()) {
                val type: Type = object : TypeToken<ArrayList<Customer?>?>() {}.getType()
                finalOutputString = gson.fromJson(res.getString(res.getColumnIndex("DATA")), type)
            }
        }
        customers.value = finalOutputString
        customerLoadError.value = false
        loading.value = false
    }

    private fun fetchCustomers() {
        loading.value = true
        myDb.deleteData(myDb.TABLE_CUSTOMERS)
        disposable.add(
            service.getCustomers()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<CustomerResponse>() {
                    override fun onSuccess(value: CustomerResponse?) {

                        myDb.insertData(
                            value?.customerlist, myDb.TABLE_CUSTOMERS
                        )

                        customers.value = value?.customerlist
                        customerLoadError.value = false
                        loading.value = false
                    }

                    override fun onError(e: Throwable?) {
                        Log.e("Error", e?.message.toString())
                        customerLoadError.value = true
                        loading.value = false
                    }

                })
        )

    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}