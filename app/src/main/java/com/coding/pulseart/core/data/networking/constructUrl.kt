package com.coding.pulseart.core.data.networking


//fun constructUrl(url: String): String {
//    return when {
//        url.contains(BuildConfig) -> url
//        url.startsWith("/") -> BuildConfig.BASE_URL + url.drop(1)
//        else -> BuildConfig.BASE_URL + url
//    }
//}



fun constructUrl(url: String): String {
    val BASE_URL = "https://api.artic.edu/api/v1/"
    return when {
        url.contains(BASE_URL) -> url
        url.startsWith("/") -> BASE_URL + url.drop(1)
        else -> BASE_URL + url
    }
}