package com.example.ifeed

import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class BackgroundTask {
    // Get available processors
    private val availableProcessors = Runtime.getRuntime().availableProcessors()

    // Create thread pool
    private val backgroundExecutor : ExecutorService =
        Executors.newFixedThreadPool(availableProcessors)

    // Callback interface for the task result
    interface TaskCallBack {
        fun onResult(result: String)
    }

    // Perform background task
    fun performBackgroundTask(callBack: TaskCallBack) {


        // Execute the task on the background executor
        backgroundExecutor.execute {

            try {

                // Simulate time-consuming task
                Thread.sleep(3000)

                // Callback with the result on the main thread
                callBack.onResult("Background task completed!")

            } finally {

                // Shutdown the executor in a finally block to ensure it's done
                backgroundExecutor.shutdown()
            }
        }
    }
}