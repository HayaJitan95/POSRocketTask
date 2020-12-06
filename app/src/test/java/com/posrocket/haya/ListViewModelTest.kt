package com.posrocket.haya

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.posrocket.haya.Model.Customer
import com.posrocket.haya.Model.CustomerResponse
import com.posrocket.haya.Model.PhoneNumbers
import com.posrocket.haya.Model.Service
import com.posrocket.haya.ViewModel.ListViewModel
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.disposables.Disposable
import io.reactivex.internal.schedulers.ExecutorScheduler
import io.reactivex.plugins.RxJavaPlugins
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations
import java.util.concurrent.Executor
import java.util.concurrent.TimeUnit

class ListViewModelTest {
    @get:Rule
    var rule = InstantTaskExecutorRule()

    @Mock
    lateinit var customerService: Service

    @InjectMocks
    var listViewModel = ListViewModel()

    private var testSingle: Single<CustomerResponse>? = null

    val context = mock(Context::class.java)

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
    }


    @Test
    fun getCustomersSuccess() {

        val customer =
            Customer(
                "name",
                "lastname",
                "balance",
                "gender",
                arrayListOf(PhoneNumbers("", true, false, "")),
                arrayListOf()
            )

        val country = CustomerResponse(arrayListOf(customer))

        testSingle = Single.just(country)

        Mockito.`when`(customerService.getCustomers()).thenReturn(testSingle)

        listViewModel.refresh(context)

        Assert.assertEquals(764, listViewModel.customers.value?.size)
        Assert.assertEquals(false, listViewModel.customerLoadError.value)
        Assert.assertEquals(false, listViewModel.loading.value)
    }

    @Test
    fun getCustomersFail() {
        testSingle = Single.error(Throwable())

        Mockito.`when`(customerService.getCustomers()).thenReturn(testSingle)

        listViewModel.refresh(context)

        Assert.assertEquals(true, listViewModel.customerLoadError.value)
        Assert.assertEquals(false, listViewModel.loading.value)
    }

    @Before
    fun setUpSchdeulers() {
        val immediate = object : Scheduler() {
            override fun scheduleDirect(run: Runnable?, delay: Long, unit: TimeUnit?): Disposable {
                return super.scheduleDirect(run, 0, unit)
            }

            override fun createWorker(): Worker {
                return ExecutorScheduler.ExecutorWorker(Executor { it.run() })
            }
        }
        RxJavaPlugins.setInitIoSchedulerHandler { scheduler -> immediate }
        RxJavaPlugins.setInitComputationSchedulerHandler { scheduler -> immediate }
        RxJavaPlugins.setInitNewThreadSchedulerHandler { scheduler -> immediate }
        RxJavaPlugins.setInitSingleSchedulerHandler { scheduler -> immediate }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { scheduler -> immediate }
    }
}