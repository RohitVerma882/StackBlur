#include <jni.h>
#include <android/bitmap.h>
#include <algorithm>
#include <string.h>
#include <stdio.h>
#include <android/log.h>
#include "stackblur.h"

#ifdef __cplusplus
extern "C" {
#endif

    JNIEXPORT void Java_com_rohitop_stackblur_StackBlur_sBlurBitmap(JNIEnv * env, jclass clazz, jobject bitmap, jint radius) {
        // Properties
        AndroidBitmapInfo info;
        unsigned char * pixels = nullptr;
        int reason;

        // Check radius
        if (radius < 1) {
            LOGE("check radius failed! radius=%d", radius);
            return;
        }

        // get image info
        if ((reason = AndroidBitmap_getInfo(env, bitmap, & info)) != ANDROID_BITMAP_RESULT_SUCCESS) {
            LOGE("AndroidBitmap_getInfo() failed! error=%d", reason);
            return;
        }

        // Check image
        if (info.format != ANDROID_BITMAP_FORMAT_RGBA_8888) {
            LOGE("Bitmap format is not RGBA_8888!");
            LOGE("==> %d", info.format);
            return;
        }

        int w = info.width;
        int h = info.height;
        int stride = info.stride;

        // Lock all images
        reason = AndroidBitmap_lockPixels(env, bitmap, (void ** ) & pixels);
        if (!pixels) {
            LOGE("AndroidBitmap_lockPixels() failed ! error=%d", reason);
            return;
        }
        // Constants
        //const int radius = (int)inradius; // Transform unsigned into signed for further operations
        const int wm = w - 1;
        const int hm = h - 1;
        const int wh = w * h;
        const int div = radius + radius + 1;
        const int r1 = radius + 1;
        const int divsum = SQUARE((div + 1) >> 1);

        // Small buffers
        int stack[div * 4];
        zeroClearInt(stack, div * 4);

        int vmin[MAX(w, h)];
        zeroClearInt(vmin, MAX(w, h));

        // Large buffers
        int * r = new int[wh];
        int * g = new int[wh];
        int * b = new int[wh];
        int * a = new int[wh];
        zeroClearInt(r, wh);
        zeroClearInt(g, wh);
        zeroClearInt(b, wh);
        zeroClearInt(a, wh);

        const size_t dvcount = 256 * divsum;
        int * dv = new int[dvcount];
        int i;
        for (i = 0;
                (size_t) i < dvcount; i++) {
            dv[i] = (i / divsum);
        }

        // Variables
        int x, y;
        int * sir;
        int routsum, goutsum, boutsum, aoutsum;
        int rinsum, ginsum, binsum, ainsum;
        int rsum, gsum, bsum, asum, p, yp;
        int stackpointer;
        int stackstart;
        int rbs;

        int yw = 0, yi = 0;
        for (y = 0; y < h; y++) {
            ainsum = aoutsum = asum = rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;

            for (i = -radius; i <= radius; i++) {
                sir = & stack[(i + radius) * 4];
                int offset = (y * stride + (MIN(wm, MAX(i, 0))) * 4);
                sir[0] = pixels[offset];
                sir[1] = pixels[offset + 1];
                sir[2] = pixels[offset + 2];
                sir[3] = pixels[offset + 3];

                rbs = r1 - abs(i);
                rsum += sir[0] * rbs;
                gsum += sir[1] * rbs;
                bsum += sir[2] * rbs;
                asum += sir[3] * rbs;
                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                    ainsum += sir[3];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                    aoutsum += sir[3];
                }
            }
            stackpointer = radius;

            for (x = 0; x < w; x++) {
                r[yi] = dv[rsum];
                g[yi] = dv[gsum];
                b[yi] = dv[bsum];
                a[yi] = dv[asum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;
                asum -= aoutsum;

                stackstart = stackpointer - radius + div;
                sir = & stack[(stackstart % div) * 4];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];
                aoutsum -= sir[3];

                if (y == 0) {
                    vmin[x] = MIN(x + radius + 1, wm);
                }

                int offset = (y * stride + vmin[x] * 4);
                sir[0] = pixels[offset];
                sir[1] = pixels[offset + 1];
                sir[2] = pixels[offset + 2];
                sir[3] = pixels[offset + 3];
                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];
                ainsum += sir[3];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;
                asum += ainsum;

                stackpointer = (stackpointer + 1) % div;
                sir = & stack[(stackpointer % div) * 4];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];
                aoutsum += sir[3];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];
                ainsum -= sir[3];

                yi++;
            }
            yw += w;
        }

        for (x = 0; x < w; x++) {
            ainsum = aoutsum = asum = rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            yp = -radius * w;
            for (i = -radius; i <= radius; i++) {
                yi = MAX(0, yp) + x;

                sir = & stack[(i + radius) * 4];

                sir[0] = r[yi];
                sir[1] = g[yi];
                sir[2] = b[yi];
                sir[3] = a[yi];

                rbs = r1 - abs(i);

                rsum += r[yi] * rbs;
                gsum += g[yi] * rbs;
                bsum += b[yi] * rbs;
                asum += a[yi] * rbs;

                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                    ainsum += sir[3];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                    aoutsum += sir[3];
                }

                if (i < hm) {
                    yp += w;
                }
            }
            stackpointer = radius;
            for (y = 0; y < h; y++) {
                int offset = stride * y + x * 4;
                pixels[offset] = dv[rsum];
                pixels[offset + 1] = dv[gsum];
                pixels[offset + 2] = dv[bsum];
                pixels[offset + 3] = dv[asum];
                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;
                asum -= aoutsum;

                stackstart = stackpointer - radius + div;
                sir = & stack[(stackstart % div) * 4];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];
                aoutsum -= sir[3];

                if (x == 0) {
                    vmin[y] = (MIN(y + r1, hm)) * w;
                }
                p = x + vmin[y];

                sir[0] = r[p];
                sir[1] = g[p];
                sir[2] = b[p];
                sir[3] = a[p];

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];
                ainsum += sir[3];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;
                asum += ainsum;

                stackpointer = (stackpointer + 1) % div;
                sir = & stack[stackpointer * 4];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];
                aoutsum += sir[3];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];
                ainsum -= sir[3];

                yi += w;
            }
        }

        delete[] r;
        delete[] g;
        delete[] b;
        delete[] a;
        delete[] dv;

        // Unlocks everything
        AndroidBitmap_unlockPixels(env, bitmap);
    }

    JNIEXPORT void JNICALL Java_com_rohitop_stackblur_StackBlur_sBlurBitmap2(JNIEnv* env, jclass clzz, jobject bitmap, jint radius, jint threadCount, jint threadIndex, jint round) {
        // Properties
        AndroidBitmapInfo   info;
        void*               pixels;
        int reason;

        // Get image info
        if ((reason = AndroidBitmap_getInfo(env, bitmap, &info)) != 0) {
            LOGE("AndroidBitmap_getInfo() failed! error=%d", reason);
            return;
        }

        // Check image
        if (info.format != ANDROID_BITMAP_FORMAT_RGBA_8888) {
            LOGE("Bitmap format is not RGBA_8888!");
            LOGE("==> %d", info.format);
            return;
        }

        // Lock all images
        if ((reason = AndroidBitmap_lockPixels(env, bitmap, &pixels)) != 0) {
            LOGE("AndroidBitmap_lockPixels() failed! error=%d", reason);
            return;
        }

        int h = info.height;
        int w = info.width;

        blurJob((unsigned char*)pixels, w, h, radius, threadCount, threadIndex, round);

        // Unlocks everything
        AndroidBitmap_unlockPixels(env, bitmap);
    }

#ifdef __cplusplus
}
#endif /* __cplusplus */
