package com.example.movieapp.presentation.pages

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.ActivityInfo
import android.net.Uri
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.FrameLayout
import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.rounded.OpenInNew
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VideoPlayerScreen(
    url: String,
    title: String,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current

    // Tự động xoay ngang khi xem phim
    DisposableEffect(Unit) {
        val activity = context as? ComponentActivity
        val originalOrientation = activity?.requestedOrientation
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        onDispose {
            activity?.requestedOrientation = originalOrientation ?: ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(title, maxLines = 1) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                        context.startActivity(intent)
                    }) {
                        Icon(Icons.Rounded.OpenInNew, contentDescription = "Open in Browser", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Black.copy(0.7f),
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
            VideoWebView(url)
        }
    }
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun VideoWebView(url: String) {
    val context = LocalContext.current
    val activity = context as? Activity

    // Lưu reference đến WebView để cleanup
    val webViewRef = remember { mutableStateOf<WebView?>(null) }

    // Cleanup khi rời khỏi màn hình
    DisposableEffect(Unit) {
        onDispose {
            // Thoát fullscreen nếu đang fullscreen khi back
            activity?.let { act ->
                val decorView = act.window.decorView as? FrameLayout
                decorView?.findViewWithTag<View>("fullscreen_custom_view")?.let { oldView ->
                    decorView.removeView(oldView)
                    restoreSystemUI(act)
                }
            }
            webViewRef.value?.destroy()
        }
    }

    AndroidView(
        factory = { ctx ->
            WebView(ctx).apply {
                webViewRef.value = this

                settings.apply {
                    javaScriptEnabled = true
                    domStorageEnabled = true
                    mediaPlaybackRequiresUserGesture = false
                    allowFileAccess = true
                    cacheMode = WebSettings.LOAD_DEFAULT
                    mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                    useWideViewPort = true
                    loadWithOverviewMode = true
                    javaScriptCanOpenWindowsAutomatically = false
                    setSupportMultipleWindows(false)
                    userAgentString = "Mozilla/5.0 (Linux; Android 13; Pixel 7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/121.0.0.0 Mobile Safari/537.36"
                }

                webViewClient = object : WebViewClient() {
                    override fun onPageFinished(view: WebView?, url: String?) {
                        super.onPageFinished(view, url)
                        val removeAdsJs = """
                            (function() {
                                function removeAds() {
                                    var adSelectors = [
                                        '[class*="vpn"]', '[id*="vpn"]',
                                        '[class*="promo"]', '[id*="promo"]',
                                        '[class*="popup-ad"]', '[id*="popup-ad"]',
                                        '[class*="adsbygoogle"]',
                                        'iframe[src*="doubleclick"]',
                                        'iframe[src*="googlesyndication"]',
                                        'iframe[src*="ads"]'
                                    ];
                                    adSelectors.forEach(function(sel) {
                                        try {
                                            document.querySelectorAll(sel).forEach(function(el) {
                                                if (!el.querySelector('video')) el.remove();
                                            });
                                        } catch(e) {}
                                    });
                                    var adKeywords = ['vpn', 'free trial', 'download app', 'install app', 'limited time offer'];
                                    document.querySelectorAll('div, section, aside').forEach(function(el) {
                                        try {
                                            var style = window.getComputedStyle(el);
                                            var pos = style.position;
                                            var zIndex = parseInt(style.zIndex) || 0;
                                            if ((pos === 'fixed' || pos === 'absolute') && zIndex > 100 && !el.querySelector('video')) {
                                                var text = (el.innerText || '').toLowerCase();
                                                var hasAdContent = adKeywords.some(function(kw) { return text.includes(kw); });
                                                if (hasAdContent) el.style.display = 'none';
                                            }
                                        } catch(e) {}
                                    });
                                }
                                removeAds();
                                var observer = new MutationObserver(function() { removeAds(); });
                                observer.observe(document.body, { childList: true, subtree: true });
                                setTimeout(removeAds, 1500);
                                setTimeout(removeAds, 4000);
                            })();
                        """.trimIndent()
                        view?.evaluateJavascript(removeAdsJs, null)
                    }

                    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                        val uri = request?.url ?: return false
                        val scheme = uri.scheme ?: ""
                        val host = uri.host ?: ""

                        if (scheme != "http" && scheme != "https") return true

                        val blockedKeywords = listOf(
                            "trip.com", "booking.com", "agoda", "airbnb",
                            "play.google.com", "apps.apple.com",
                            "shopee", "lazada", "tiki", "tokopedia",
                            "doubleclick", "googleadservices", "googlesyndication",
                            "trafficjunky", "propellerads", "popads"
                        )
                        if (blockedKeywords.any { host.contains(it) || uri.toString().contains(it) }) return true

                        val allowedKeywords = listOf(
                            "vidsrc.to", "vidsrc.me", "vidsrc.net", "vidsrc.xyz",
                            "vidcloud", "rabbitstream", "megacloud", "embedder",
                            "2embed", "multiembed", "autoembed",
                            "vidplay", "filemoon", "doodstream",
                            "youtube.com", "youtube-nocookie.com",
                            "googlevideo.com", "google.com", "gstatic.com",
                            "jwplatform.com", "jwpcdn.com", "cloudflare"
                        )
                        return !allowedKeywords.any { host.contains(it) }
                    }
                }

                webChromeClient = object : WebChromeClient() {
                    override fun onCreateWindow(
                        view: WebView?, isDialog: Boolean,
                        isUserGesture: Boolean, resultMsg: android.os.Message?
                    ): Boolean = false  // Chặn popup

                    override fun onShowCustomView(view: View?, callback: CustomViewCallback?) {
                        if (view == null || activity == null) return

                        // Gắn custom view trực tiếp vào DecorView của Activity
                        val decorView = activity.window.decorView as FrameLayout
                        view.tag = "fullscreen_custom_view"
                        view.layoutParams = FrameLayout.LayoutParams(
                            FrameLayout.LayoutParams.MATCH_PARENT,
                            FrameLayout.LayoutParams.MATCH_PARENT
                        )
                        decorView.addView(view)

                        // Ẩn system bars (status bar + nav bar) → thực sự fullscreen
                        hideSystemUI(activity)
                    }

                    override fun onHideCustomView() {
                        if (activity == null) return

                        // Xóa custom view khỏi DecorView
                        val decorView = activity.window.decorView as? FrameLayout
                        val customView = decorView?.findViewWithTag<View>("fullscreen_custom_view")
                        if (customView != null) {
                            decorView.removeView(customView)
                        }

                        // Khôi phục system bars
                        restoreSystemUI(activity)
                    }
                }

                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )

                loadUrl(url)
            }
        },
        modifier = Modifier.fillMaxSize()
    )
}

/** Ẩn status bar + navigation bar để video chiếm toàn màn hình */
private fun hideSystemUI(activity: Activity) {
    WindowCompat.setDecorFitsSystemWindows(activity.window, false)
    WindowInsetsControllerCompat(activity.window, activity.window.decorView).let { ctrl ->
        ctrl.hide(WindowInsetsCompat.Type.systemBars())
        ctrl.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
    }
    // Giữ màn hình sáng khi xem phim
    activity.window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
}

/** Khôi phục system bars sau khi thoát fullscreen */
private fun restoreSystemUI(activity: Activity) {
    WindowCompat.setDecorFitsSystemWindows(activity.window, true)
    WindowInsetsControllerCompat(activity.window, activity.window.decorView)
        .show(WindowInsetsCompat.Type.systemBars())
    activity.window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
}

@Composable
fun ExoPlayerView(url: String) {
    val context = LocalContext.current
    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri(url))
            prepare()
            playWhenReady = true
        }
    }

    DisposableEffect(Unit) {
        onDispose { exoPlayer.release() }
    }

    AndroidView(
        factory = { ctx ->
            PlayerView(ctx).apply {
                player = exoPlayer
                useController = true
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(16 / 9f)
    )
}
