package com.wirtos.tguydroid;

import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class TGuy {
    static {
        System.loadLibrary("tguydroid");
    }

    private final long tgobj;
    private int cur_frame = 0;
    private final int frames_count;

    private native long tguy_jni_ctor(byte[] text, int spacing);

    private native void tguy_jni_dtor(long tgobj);

    private native void tguy_jni_set_frame(long tgobj, int frame);

    private native int tguy_jni_get_frames_count(long tgobj);

    @Nullable
    private native byte[] tguy_jni_get_text(long tgobj);


    TGuy(String str, int spacing) {
        byte[] res;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            res = str.getBytes(StandardCharsets.UTF_8);
        } else {
            Log.i("TGUYDROID", "TGuy() Falling back to Charset");
            res = str.getBytes(Charset.forName("UTF-8"));
        }
        tgobj = tguy_jni_ctor(res, spacing);
        if (tgobj == 0) {
            throw new OutOfMemoryError();
        }
        frames_count = tguy_jni_get_frames_count(tgobj);
    }

    public void close() {
        tguy_jni_dtor(tgobj);
    }

    @NonNull
    @Override
    public String toString() {
        byte[] res = tguy_jni_get_text(tgobj);
        if (res == null) {
            return "";
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            return new String(res, StandardCharsets.UTF_8);
        } else {
            Log.i("TGUYDROID", "toString() Falling back to Charset");
            return new String(res, Charset.forName("UTF-8"));
        }
    }

    public int frames_count() { return frames_count; }

    private void set_frame(int frame) {
        tguy_jni_set_frame(tgobj, frame);
    }

    public String next() {
        if (cur_frame >= frames_count - 1) {
            cur_frame = 0;
        }
        set_frame(cur_frame++);
        return this.toString();
    }
}
