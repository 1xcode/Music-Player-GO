package com.iven.musicplayergo.music

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import com.iven.musicplayergo.musicRepo
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class MusicViewModel : ViewModel(), CoroutineScope {

    private val job = Job()

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main + handler

    private val handler = CoroutineExceptionHandler { _, exception ->
        Log.d("TAG", "$exception handled !")
    }

    fun loadMusic(context: Context) {
        launch(Dispatchers.Main) {
            withContext(Dispatchers.Main) {
                musicRepo.loadMusic(context)
            }
        }
    }
}
