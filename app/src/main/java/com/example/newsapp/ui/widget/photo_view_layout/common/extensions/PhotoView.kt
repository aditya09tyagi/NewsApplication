package com.example.newsapp.ui.widget.photo_view_layout.common.extensions
import com.github.chrisbanes.photoview.PhotoView

internal fun PhotoView.resetScale(animate: Boolean) {
    setScale(minimumScale, animate)
}