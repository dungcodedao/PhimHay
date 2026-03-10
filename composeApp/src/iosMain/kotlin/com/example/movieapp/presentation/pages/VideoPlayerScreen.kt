package com.example.movieapp.presentation.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.OpenInNew
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.interop.UIKitView
import androidx.compose.ui.unit.dp
import platform.Foundation.NSURL
import platform.Foundation.NSURLRequest
import platform.WebKit.*
import platform.UIKit.*
import platform.CoreGraphics.CGRectZero

@OptIn(ExperimentalMaterial3Api::class)
@Composable
actual fun VideoPlayerScreen(
    url: String,
    title: String,
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(title, maxLines = 1, style = MaterialTheme.typography.titleMedium) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        val nsUrl = NSURL.URLWithString(url)
                        if (nsUrl != null) {
                            UIApplication.sharedApplication.openURL(nsUrl)
                        }
                    }) {
                        Icon(Icons.Rounded.OpenInNew, contentDescription = "Open in Safari", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Black.copy(alpha = 0.7f),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color.Black),
            contentAlignment = Alignment.Center
        ) {
            IOSVideoWebView(url)
        }
    }
}

@Composable
fun IOSVideoWebView(url: String) {
    val adBlockingScript = """
        (function() {
            function removeAds() {
                var adSelectors = [
                    '[class*="vpn"]', '[id*="vpn"]', '[class*="promo"]', '[id*="promo"]',
                    '[class*="popup-ad"]', '[id*="popup-ad"]', '[class*="adsbygoogle"]',
                    'iframe[src*="doubleclick"]', 'iframe[src*="googlesyndication"]',
                    'iframe[src*="pop"]', 'a[href*="shopee"]', 'a[href*="lazada"]',
                    '#overlay', '.overlay', '.popunder', '.pop-under'
                ];
                adSelectors.forEach(function(sel) {
                    try {
                        document.querySelectorAll(sel).forEach(function(el) {
                            if (!el.querySelector('video')) { el.remove(); }
                        });
                    } catch(e) {}
                });
                window.open = function() { return null; };
            }
            removeAds();
            setInterval(removeAds, 2000);
        })();
    """.trimIndent()

    val webView = remember {
        WKWebView(frame = CGRectZero.getPointer().rawValue.let { platform.CoreGraphics.CGRectZero }, configuration = WKWebViewConfiguration().apply {
            allowsInlineMediaPlayback = true
            mediaTypesRequiringUserActionForPlayback = WKAudiovisualMediaTypeNone
        }).apply {
            val script = WKUserScript(
                source = adBlockingScript,
                injectionTime = WKUserScriptInjectionTime.WKUserScriptInjectionTimeAtDocumentEnd,
                forMainFrameOnly = true
            )
            configuration.userContentController.addUserScript(script)
        }
    }

    UIKitView(
        factory = {
            webView.apply {
                val request = NSURLRequest.requestWithURL(NSURL.URLWithString(url)!!)
                loadRequest(request)
            }
        },
        modifier = Modifier.fillMaxSize()
    )
}
