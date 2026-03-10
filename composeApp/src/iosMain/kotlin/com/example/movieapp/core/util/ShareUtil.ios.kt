package com.example.movieapp.core.util

import platform.UIKit.*
import platform.Foundation.*

actual object ShareUtil {
    actual fun shareText(text: String, title: String) {
        val window = UIApplication.sharedApplication.keyWindow
        val rootViewController = window?.rootViewController

        if (rootViewController != null) {
            val activityViewController = UIActivityViewController(
                activityItems = listOf(text),
                applicationActivities = null
            )
            
            // Hỗ trợ iPad (tránh crash do popover)
            activityViewController.popoverPresentationController?.apply {
                sourceView = rootViewController.view
                sourceRect = rootViewController.view.bounds
            }

            rootViewController.presentViewController(
                viewControllerToPresent = activityViewController,
                animated = true,
                completion = null
            )
        }
    }
}
