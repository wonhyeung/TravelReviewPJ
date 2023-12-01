package com.won.travelreviewpj.travel

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.won.travelreviewpj.common.SERVICE_KEY
import com.won.travelreviewpj.common.TARGET_URL
import com.won.travelreviewpj.travel.wishlist.TravelWishlist
import com.won.travelreviewpj.travel.wishlist.db.TravelWishlistDao
import com.won.travelreviewpj.travel.wishlist.db.TravelWishlistRoomDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class TravelRepository(application: Application) {
    val travelData: MutableLiveData<TravelEntity?> = MutableLiveData()
    var searchResults = MutableLiveData<TravelWishlist>()
    private var travelWishlistDao: TravelWishlistDao
    private val coroutineScope = CoroutineScope(Dispatchers.IO)
    val allTravelWishlists: LiveData<List<TravelWishlist>>?

    init {
        val db: TravelWishlistRoomDatabase = TravelWishlistRoomDatabase.getDatabase(application)
        travelWishlistDao = db.travelWishlistDao()
        allTravelWishlists = travelWishlistDao.getAllTravelWishlist()
    }

    companion object {

        private var _travelService: TravelService? = null
        private val travelService get() = _travelService!!

        private val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(100, TimeUnit.SECONDS)
            .readTimeout(100, TimeUnit.SECONDS)
            .writeTimeout(100, TimeUnit.SECONDS)
            .build()

        fun getRetrofitTravelService(): TravelService {
            if (_travelService != null) {
                return travelService
            } else {
                _travelService = Retrofit.Builder()
                    .baseUrl(TARGET_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .build()
                    .create(TravelService::class.java)
            }
            return travelService
        }
    }

    suspend fun getRetrofit(): Response<Travel> {
        val contentId = (125406..125506).random()
        Log.e("contentId", "$contentId")
        return getRetrofitTravelService().getTravelData(
            serviceKey = SERVICE_KEY,
            contentId = contentId,
        )
    }

    fun getTravelData() = CoroutineScope(Dispatchers.IO).launch {
        val response = getRetrofit()
        response.let {
            if (response.isSuccessful) {
                val travelList =
                    response.body()?.response?.body?.items?.travelEntities.orEmpty().firstOrNull()
                travelData.postValue(travelList)
            }
        }

    }

    fun insertTravelWishlist(newTravelWishList: TravelWishlist) {
        coroutineScope.launch {
            travelWishlistDao.insertTravelWishlist(newTravelWishList)
        }
    }

    fun deleteTravelWishlist(id: Long) {
        coroutineScope.launch {
            travelWishlistDao.deleteTravelWishlist(id)
        }
    }

    fun findTravelWishlist(id: Long) {
        CoroutineScope(Dispatchers.Main).launch {
            searchResults.value = coroutineScope.async(Dispatchers.IO) {
                return@async travelWishlistDao.findTravelWishlist(id)
            }.await()
        }
    }
    /*



    var searchResults = MutableLiveData<TravelWishlist>()
    private var travelWishlistDao: TravelWishlistDao
    private val coroutineScope = CoroutineScope(Dispatchers.IO)
    val allTravelWishlists: LiveData<List<TravelWishlist>>?

    init {
        val db: TravelWishlistRoomDatabase = TravelWishlistRoomDatabase.getDatabase(application)
        travelWishlistDao = db.travelWishlistDao()
        allTravelWishlists = travelWishlistDao.getAllTravelWishlist()
    }

    fun insertTravelWishlist(newTravelWishList: TravelWishlist) {
        coroutineScope.launch {
            travelWishlistDao.insertTravelWishlist(newTravelWishList)
        }
    }

    fun deleteTravelWishlist(id: Long) {
        coroutineScope.launch {
            travelWishlistDao.deleteTravelWishlist(id)
        }
    }

    fun findTravelWishlist(id: Long) {
        CoroutineScope(Dispatchers.Main).launch {
            searchResults.value = coroutineScope.async(Dispatchers.IO) {
                return@async travelWishlistDao.findTravelWishlist(id)
            }.await()
        }
    }
     */
}