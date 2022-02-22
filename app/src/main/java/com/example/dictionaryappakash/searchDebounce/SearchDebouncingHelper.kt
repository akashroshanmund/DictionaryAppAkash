package com.example.dictionaryappakash.SearchHelper


import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

internal class SearchDebouncingHelper (
    lifecycle : Lifecycle,
    val onDebounceTextChange : (String?) -> Unit
) : SearchView.OnQueryTextListener, LifecycleObserver{
    val debouncingDelay : Long = 600
    val scope : CoroutineScope = CoroutineScope(Dispatchers.Main)

    var searchJob : Job? = null

    init {
        lifecycle.addObserver(this)
    }


    override fun onQueryTextSubmit(p0: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(p0: String?): Boolean {
        searchJob?.cancel()
        searchJob = scope.launch {
            p0?.let{
                delay(debouncingDelay)
                onDebounceTextChange(p0)
            }
        }

        return false
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    private fun destroy(){
        searchJob?.cancel()
    }

}