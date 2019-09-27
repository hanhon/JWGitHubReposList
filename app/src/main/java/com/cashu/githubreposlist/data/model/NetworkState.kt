package com.cashu.githubreposlist.data.model

sealed class NetworkState {
    object Success : NetworkState()
    class Failure(val message: String) : NetworkState()
    object Loading : NetworkState()
}
