package com.iceteaviet.fastfoodfinder.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.iceteaviet.fastfoodfinder.App
import com.iceteaviet.fastfoodfinder.R
import com.iceteaviet.fastfoodfinder.data.remote.store.model.Store
import com.iceteaviet.fastfoodfinder.data.remote.user.model.User
import com.iceteaviet.fastfoodfinder.ui.base.BaseActivity
import com.iceteaviet.fastfoodfinder.ui.login.LoginActivity
import com.iceteaviet.fastfoodfinder.ui.main.MainActivity
import com.iceteaviet.fastfoodfinder.utils.filterInvalidData
import com.iceteaviet.fastfoodfinder.utils.isValidUserUid
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class SplashActivity : BaseActivity() {

    companion object {
        const val SPLASH_DELAY_TIME = 500;
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val start = System.currentTimeMillis()

        dataManager = App.getDataManager()

        if (dataManager.getPreferencesHelper().getAppLaunchFirstTime()!! || dataManager.getPreferencesHelper().getNumberOfStores() == 0) {
            // Download data from Firebase and store in Realm
            dataManager.loadStoresFromServer(this)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : SingleObserver<List<Store>> {
                        override fun onSubscribe(d: Disposable) {

                        }

                        override fun onSuccess(storeList: List<Store>) {
                            dataManager.getPreferencesHelper().setAppLaunchFirstTime(false)
                            dataManager.getPreferencesHelper().setNumberOfStores(storeList.size)
                            dataManager.getLocalStoreDataSource().setStores(filterInvalidData(storeList.toMutableList()))

                            Toast.makeText(this@SplashActivity, R.string.update_database_successfull, Toast.LENGTH_SHORT).show()
                            startMyActivity(LoginActivity::class.java)
                        }

                        override fun onError(e: Throwable) {
                            dataManager.signOut()
                            e.printStackTrace()
                            startMyActivity(LoginActivity::class.java)
                        }
                    })
        } else {
            if (dataManager.isSignedIn()) {
                val uid = dataManager.getCurrentUserUid()
                if (isValidUserUid(uid)) {
                    // User still signed in
                    dataManager.getRemoteUserDataSource().getUser(uid)
                            .subscribeOn(Schedulers.io())
                            .observeOn(Schedulers.io())
                            .subscribe(object : SingleObserver<User> {
                                override fun onSubscribe(d: Disposable) {

                                }

                                override fun onSuccess(user: User) {
                                    dataManager.setCurrentUser(user)
                                }

                                override fun onError(e: Throwable) {
                                    e.printStackTrace()
                                }
                            })
                }

                val remainTime = SPLASH_DELAY_TIME - (System.currentTimeMillis() - start)
                if (remainTime > 0) {
                    Handler(Looper.getMainLooper())
                            .postDelayed({
                                startMyActivity(MainActivity::class.java)
                            }, remainTime)
                } else {
                    startMyActivity(MainActivity::class.java)
                }
            } else {
                startMyActivity(LoginActivity::class.java)
            }
        }
    }

    override val layoutId: Int
        get() = R.layout.activity_splash

    private fun startMyActivity(activity: Class<*>) {
        val intent = Intent(this@SplashActivity, activity)
        startActivity(intent)
        finish()
    }
}
