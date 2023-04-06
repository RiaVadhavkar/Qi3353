package com.example.qi3353

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import org.json.JSONObject

class NotificationWorker(appContext: Context, workerParams: WorkerParameters): Worker(appContext, workerParams) {
    @SuppressLint("RestrictedApi")
    override fun doWork(): Result {

        var title = ""
        var message = ""
        var isChanged = ""
//        val json = JSONObject()
//        json.put("username", inputData.getString("username"))
//        json.put("event", inputData.getString("event"))
//        Log.d("PP05_Tag", "params:"+json.toString()+" url "+MainActivity.URL)
//        return uploadLog(json, MainActivity.URL)
        return Result.Success()
    }

//    fun uploadLog(json: JSONObject, url: String): Result {
//        Log.d("PP05_Tag", "uploadLog() "+url)
//        var call = TrackerRetrofitService.create(url).postLog(json)
//        call.execute()
//        return Result.success()
//    }



}