package com.mundcode.muntam.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

abstract class BaseViewModel<T> : ViewModel() {

    protected val _state = MutableStateFlow(createInitialState())
    val state: StateFlow<T> = _state

    protected val mutex = Mutex()

    abstract fun createInitialState(): T

    open fun updateState(newState: () -> T) = viewModelScope.launch {
        mutex.withLock {
            _state.value = newState()
        }
    }
}
