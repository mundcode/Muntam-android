package com.mundcode.muntam.presentation.item

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidViewBinding
import androidx.lifecycle.Lifecycle
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.nativead.NativeAdOptions
import com.mundcode.muntam.BuildConfig
import com.mundcode.muntam.constants.ADMOB_BANNER_DEFAULT_ID
import com.mundcode.muntam.constants.ADMOB_BANNER_DEFAULT_ID_TEST
import com.mundcode.muntam.databinding.AdmobBannerViewBinding
import com.mundcode.muntam.util.ComposableLifecycle

@Composable
fun AdmobBanner(
    modifier: Modifier = Modifier
) {
    var isDestroyed by remember {
        mutableStateOf(false)
    }
    ComposableLifecycle() { _, event ->
        if (event == Lifecycle.Event.ON_DESTROY) {
            isDestroyed = true
        }
    }
    AndroidViewBinding(
        factory = AdmobBannerViewBinding::inflate,
        modifier = modifier
    ) {
        // Initialize NativeAdView
        val adView = root.also { adView ->
            adView.bodyView = textBody
            adView.headlineView = textTitle
            adView.iconView = imgLogo
        }

        val adLoader = AdLoader.Builder(
            adView.context,
            if (BuildConfig.DEBUG.not()) ADMOB_BANNER_DEFAULT_ID_TEST else BuildConfig.ADMOB_DFAULT_BANNER_ID
        )
            .forNativeAd { nativeAd ->
                if (isDestroyed) {
                    nativeAd.destroy()
                    return@forNativeAd
                }
                nativeAd.body?.let { body ->
                    textBody.text = body
                }

                nativeAd.headline?.let { headline ->
                    textTitle.text = headline
                }

                nativeAd.icon?.let { icon ->
                    imgLogo.setImageDrawable(icon.drawable)
                }

                adView.setNativeAd(nativeAd)
            }
            .withNativeAdOptions(NativeAdOptions.Builder().build())
            .build()
        adLoader.loadAd(AdRequest.Builder().build())
    }
}
