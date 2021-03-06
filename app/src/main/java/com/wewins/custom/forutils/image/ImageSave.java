package com.wewins.custom.forutils.image;

import android.graphics.Bitmap;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * <br/>
 * <b color="#008000">Created by Binley at 2019/2/14.<b/>
 */
public class ImageSave {
    private static final String TAG = ImageSave.class.getName();

    /**
     * Stores an image on the storage
     *
     * @param image
     *            the image to store.
     * @param pictureFile
     *            the file in which it must be stored
     */
    public static void saveImage(Bitmap image, File pictureFile) {
        if (pictureFile == null) {
            Log.d(TAG, "Error creating media file, check storage permissions: ");
            return;
        }
        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            image.compress(Bitmap.CompressFormat.PNG, 90, fos);
            fos.close();
        } catch (FileNotFoundException e) {
            Log.d(TAG, "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.d(TAG, "Error accessing file: " + e.getMessage());
        }
    }
}
