package com.awesome.scottquach.proximitypush_upcounter.database

import android.content.Context
import android.os.AsyncTask
import com.awesome.scottquach.proximitypush_upcounter.BaseApplication
import io.reactivex.Single
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

/**
 * Created by Scott Quach on 11/22/2017.
 * handles interactions concerning writing and reading from the sql database
 */

class DatabaseManager(context: Context) {

    private var listener: DatabaseCallback? = null

    init {
        if (context is DatabaseCallback) {
            listener = context as DatabaseCallback
            Timber.d("is of DatabaseCallback")
        } else {
            Timber.d("is not of DatabaseCallback")
        }
    }

    interface DatabaseCallback {
        fun onSessionDataLoaded(data: Array<SessionEntity>?)
    }

    fun loadSessions() {
        LoadSessions().execute()
    }

    fun insertSession(numPushups: Int, isGoalReached: Boolean, date: String) {
        val entity = SessionEntity()
        entity.numberOfPushups = numPushups
        entity.isGoalReached = isGoalReached
        entity.date = date
        InsertSession().execute(entity)
    }

    fun resetSessionData(data: Array<SessionEntity>) {
        ResetSessionData().execute(data)
    }

    fun deleteSession(entity: SessionEntity) {
       DeleteSession().execute(entity)
    }

    inner class LoadSessions : AsyncTask<Void, Void, Array<SessionEntity>>() {
        override fun doInBackground(vararg p0: Void?): Array<SessionEntity> {
            return BaseApplication.getInstance().database.sessionDOA().querySessions()
        }

        override fun onPostExecute(result: Array<SessionEntity>?) {
            listener?.onSessionDataLoaded(result)
        }
    }

    inner class InsertSession : AsyncTask<SessionEntity, Void, Void>() {
        override fun doInBackground(vararg p0: SessionEntity): Void? {
            BaseApplication.getInstance().database.sessionDOA().insertSession(p0[0])
            return null
        }
    }

    inner class ResetSessionData : AsyncTask<Array<SessionEntity>,Void, Array<SessionEntity>>() {
        override fun doInBackground(vararg data: Array<SessionEntity>): Array<SessionEntity> {
            BaseApplication.getInstance().database.sessionDOA().resetTable(data[0])
            return BaseApplication.getInstance().database.sessionDOA().querySessions()
        }

        override fun onPostExecute(result: Array<SessionEntity>) {
            listener?.onSessionDataLoaded(result)
        }
    }

    inner class DeleteSession : AsyncTask<SessionEntity, Void, Void> () {
        override fun doInBackground(vararg p0: SessionEntity): Void? {
            BaseApplication.getInstance().database.sessionDOA().deleteSession(p0[0])
            return null
        }

    }
}
