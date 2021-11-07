package com.yuan.lib_markdownview;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import org.mozilla.universalchardet.UniversalDetector;

public class CharsetUtils {

    public static final String getCharset(@NonNull byte[] bytes, String defaultCharset) {
        return getCharset(bytes, bytes.length, defaultCharset);
    }

    public static final String getCharset(@NonNull byte[] bytes, int length, String defaultCharset) {
        return getCharset(bytes, 0, length, defaultCharset);
    }

    public static final String getCharset(@NonNull byte[] bytes, int offset, int length, String defaultCharset) {
        UniversalDetector detector = new UniversalDetector();

        String encoding;

        int size = 3 * 1024;
        int step = 1 * 1024;
        while (true) {
            size = (size > length) ? length : size;
            encoding = getCharset(detector, bytes, offset, size);

            if (!TextUtils.isEmpty(encoding)) {
                break;
            }

            if (size == length) {
                break;
            }

            size += step;
        }

        encoding = (TextUtils.isEmpty(encoding)) ? defaultCharset : encoding;
        return encoding;
    }

    static final String getCharset(UniversalDetector detector, byte[] bytes, int offset, int length) {
        detector.handleData(bytes, offset, length);
        detector.dataEnd();

        String encoding = detector.getDetectedCharset();

        detector.reset();

        return encoding;
    }
}
