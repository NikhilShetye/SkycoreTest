package com.artium.app.extensions


fun String.extractYoutubeId() = this.substringAfterLast("/")