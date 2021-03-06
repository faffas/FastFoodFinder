package com.iceteaviet.fastfoodfinder.data.remote.store

import androidx.annotation.NonNull
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.iceteaviet.fastfoodfinder.data.domain.store.StoreDataSource
import com.iceteaviet.fastfoodfinder.data.remote.store.model.Comment
import com.iceteaviet.fastfoodfinder.data.remote.store.model.Store
import com.iceteaviet.fastfoodfinder.utils.exception.NotSupportedException
import com.iceteaviet.fastfoodfinder.utils.getStoreType
import com.iceteaviet.fastfoodfinder.utils.w
import io.reactivex.Single
import java.util.*

/**
 * Created by tom on 7/17/18.
 */
class FirebaseStoreRepository(private val databaseRef: DatabaseReference) : StoreDataSource {

    override fun setStores(storeList: List<Store>) {
        databaseRef.child(CHILD_STORES_LOCATION).setValue(storeList)
    }

    override fun getAllStores(): Single<MutableList<Store>> {
        return Single.create { emitter ->
            databaseRef.child(CHILD_STORES_LOCATION).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(@NonNull dataSnapshot: DataSnapshot) {
                    emitter.onSuccess(parseStoresDataFromFirebase(dataSnapshot))
                }

                override fun onCancelled(@NonNull databaseError: DatabaseError) {
                    w(TAG, "The read failed: " + databaseError.message)
                    emitter.onError(databaseError.toException())
                }
            })
        }
    }

    override fun getStoreInBounds(minLat: Double, minLng: Double, maxLat: Double, maxLng: Double): Single<MutableList<Store>> {
        return Single.error(NotSupportedException())
    }

    override fun findStores(queryString: String): Single<MutableList<Store>> {
        return Single.error(NotSupportedException())
    }

    override fun findStoresByCustomAddress(customQuerySearch: List<String>): Single<MutableList<Store>> {
        return Single.error(NotSupportedException())
    }

    override fun findStoresBy(key: String, value: Int): Single<MutableList<Store>> {
        return Single.error(NotSupportedException())
    }

    override fun findStoresBy(key: String, values: List<Int>): Single<MutableList<Store>> {
        return Single.error(NotSupportedException())
    }

    override fun findStoresByType(type: Int): Single<MutableList<Store>> {
        return Single.error(NotSupportedException())
    }

    override fun findStoresById(id: Int): Single<MutableList<Store>> {
        return Single.error(NotSupportedException())
    }

    override fun findStoresByIds(ids: List<Int>): Single<MutableList<Store>> {
        return Single.error(NotSupportedException())
    }

    override fun deleteAllStores() {
        databaseRef.child(CHILD_STORES_LOCATION).removeValue()
    }

    override fun getComments(storeId: String): Single<MutableList<Comment>> {
        return Single.create { emitter ->
            databaseRef.child(CHILD_COMMENT_LIST).child(storeId).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(databaseError: DatabaseError) {
                    w(TAG, "The read failed: " + databaseError.message)
                    emitter.onError(databaseError.toException())
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    emitter.onSuccess(parseCommentsDataFromFirebase(snapshot))
                }
            })
        }
    }

    override fun insertOrUpdateComment(storeId: String, comment: Comment) {
        databaseRef.child(CHILD_COMMENT_LIST).child(storeId).push().setValue(comment)
    }


    private fun parseStoresDataFromFirebase(dataSnapshot: DataSnapshot): MutableList<Store> {
        val storeList = ArrayList<Store>()
        for (child in dataSnapshot.children) {
            for (storeLocation in child.child(CHILD_MARKERS_ADD).children) {
                val store = storeLocation.getValue(Store::class.java)
                store!!.type = getStoreType(child.key!!)
                storeList.add(store)
            }
        }

        return storeList
    }

    private fun parseCommentsDataFromFirebase(dataSnapshot: DataSnapshot): MutableList<Comment> {
        val commentList = ArrayList<Comment>()
        for (child in dataSnapshot.children) {
            val comment = child.getValue(Comment::class.java)
            comment!!.id = child.key
            commentList.add(comment)
        }
        return commentList
    }

    companion object {
        private val TAG = FirebaseStoreRepository::class.java.simpleName
        private const val CHILD_STORES_LOCATION = "stores_location"
        private const val CHILD_MARKERS_ADD = "markers_add"
        private const val CHILD_COMMENT_LIST = "comment_list"
    }
}
