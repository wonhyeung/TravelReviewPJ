package com.won.travelreviewpj.travel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.won.travelreviewpj.common.RetrofitManager.Companion.getRetrofitTravelService
import com.won.travelreviewpj.common.SERVICE_KEY
import com.won.travelreviewpj.travel.wishlist.TravelWishlist
import com.won.travelreviewpj.travel.wishlist.db.TravelWishlistDao
import com.won.travelreviewpj.travel.wishlist.db.TravelWishlistRoomDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import retrofit2.Response

class TravelRepository(application: Application) {

    var searchResults = MutableLiveData<TravelWishlist>()
    private var travelWishlistDao: TravelWishlistDao
    private val coroutineScope = CoroutineScope(Dispatchers.IO)
    val allTravelWishlists: Flow<List<TravelWishlist>>?

    private val contentIds = mutableListOf(125406, 125407, 125409, 125411, 125412, 125413, 125414, 125415, 125417, 125408, 125435, 125452, 125498, 125416, 125457, 125423, 125465, 125455, 125502, 125504).shuffled()

    init {
        val db: TravelWishlistRoomDatabase = TravelWishlistRoomDatabase.getDatabase(application)
        travelWishlistDao = db.travelWishlistDao()
        allTravelWishlists = travelWishlistDao.getAllTravelWishlist()
    }

    private suspend fun getRetrofit(): Response<Travel> {
        return getRetrofitTravelService().getTravelData(
            serviceKey = SERVICE_KEY,
            contentId = contentIds.first()
        )
    }

    suspend fun getTravelDataResult(): Flow<TravelEntity?> {
        return flow {
            var response = getRetrofit()
            if (response.body()?.response?.body?.items?.travelEntities?.first()?.numOfRows == 0) {
                response = getRetrofitTravelService().getTravelData(
                    serviceKey = SERVICE_KEY,
                    contentId = contentIds.shuffled().first(),
                )
            }
            val travelList =
                response.body()?.response?.body?.items?.travelEntities?.first()
            emit(travelList)
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
            travelWishlistDao.findTravelWishlist(id).collect { travelWishlist ->
                searchResults.value = travelWishlist
            }
        }
    }
}