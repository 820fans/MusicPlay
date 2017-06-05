package com.mp.music.helper;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.mp.music.base.BaseApplication;

public class ImageHelper {

    private static RequestQueue queue = BaseApplication.netQueue;

    public static ImageLoader getLoader() {
        return new ImageLoader(queue, new BitmapCache());
    }

    private static class BitmapCache implements ImageLoader.ImageCache {

        private LruCache<String, Bitmap> cache;

        private BitmapCache() {
            int maxSize = 10 * 1024 * 1024;
            cache = new LruCache<String, Bitmap>(maxSize) {

                @Override
                protected int sizeOf(String key, Bitmap bitmap) {
                    return bitmap.getRowBytes() * bitmap.getHeight();
                }
            };
        }

        @Override
        public Bitmap getBitmap(String url) {
            return cache.get(url);
        }

        @Override
        public void putBitmap(String url, Bitmap bitmap) {
            cache.put(url, bitmap);
        }
    }
}