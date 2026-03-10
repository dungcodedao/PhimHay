package com.example.movieapp.data.util

import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import platform.Network.nw_path_get_status
import platform.Network.nw_path_monitor_create
import platform.Network.nw_path_monitor_set_queue
import platform.Network.nw_path_monitor_set_update_handler
import platform.Network.nw_path_monitor_start
import platform.Network.nw_path_status_satisfied
import platform.Network.nw_path_status_unsatisfied
import platform.Network.nw_path_monitor_cancel
import platform.darwin.dispatch_get_main_queue

/**
 * Triển khai NetworkConnectivityObserver trên iOS sử dụng NWPathMonitor.
 * Cung cấp trạng thái mạng thời gian thực cho ứng dụng.
 */
actual class NetworkConnectivityObserver actual constructor() {
    actual fun observe(): Flow<ConnectivityStatus> {
        return callbackFlow {
            val monitor = nw_path_monitor_create()
            
            nw_path_monitor_set_update_handler(monitor) { path ->
                val status = when (nw_path_get_status(path)) {
                    nw_path_status_satisfied -> ConnectivityStatus.Available
                    nw_path_status_unsatisfied -> ConnectivityStatus.Unavailable
                    else -> ConnectivityStatus.Unavailable
                }
                trySend(status)
            }
            
            nw_path_monitor_set_queue(monitor, dispatch_get_main_queue())
            nw_path_monitor_start(monitor)
            
            awaitClose {
                nw_path_monitor_cancel(monitor)
            }
        }.distinctUntilChanged()
    }
}
