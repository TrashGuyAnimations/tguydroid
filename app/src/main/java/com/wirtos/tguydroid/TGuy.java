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

        tgobj = 0;
        frames_count = 1;
    }

    public void close() {

    }

    @NonNull
    @Override
    public String toString() {
      return "test";
    }

    public int frames_count() { return frames_count; }

    private void set_frame(int frame) {
        if (frame >= frames_count - 1) {
            cur_frame = 0;
        }

    }

    public String next() {
        set_frame(cur_frame);
        cur_frame++;
        return this.toString();
    }
}
