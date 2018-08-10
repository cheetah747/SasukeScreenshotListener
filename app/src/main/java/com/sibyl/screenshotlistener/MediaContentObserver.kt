package com.sibyl.screenshotlistener

import android.database.ContentObserver
import android.net.Uri
import android.os.Handler

/**
 * @author Sasuke on 2018/8/10.
 */
class MediaContentObserver(var contentUri: Uri, var handler: Handler) : ContentObserver(handler) {

     var mContentUri: Uri? = null
    //MediaStore.Images.Media.INTERNAL_CONTENT_URI
    //MediaStore.Images.Media.EXTERNAL_CONTENT_URI

    init {
        mContentUri = contentUri
    }


    override fun onChange(selfChange: Boolean) {
        super.onChange(selfChange)
        processMediaContentChange(mContentUri);

    }

    fun processMediaContentChange(mContentUri: Uri? ){

    }


}
