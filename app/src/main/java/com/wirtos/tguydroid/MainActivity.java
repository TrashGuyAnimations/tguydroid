package com.wirtos.tguydroid;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.HorizontalScrollView;
import android.widget.TextView;

import com.wirtos.tguydroid.databinding.ActivityMainBinding;


import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    private final Context mContext = this;
    HorizontalScrollView mHorizontalScrollView;
    @Nullable
    private TGuy mTG;
    @Nullable
    private ClipboardManager mClipboard;
    private final Handler mHandler = new Handler(Looper.getMainLooper());
    private final Runnable mTGuyRunnable = new Runnable() {
        @Override
        public void run() {
            binding.output.setText(mTG.next());
            mHandler.postDelayed(mTGuyRunnable, 500);
        }
    };

    private void setClipboard(String text) {
        if (mClipboard == null) return;
        ClipData clip = ClipData.newPlainText("Copied Text", text);
        mClipboard.setPrimaryClip(clip);
        Toast mToast = Toast.makeText(mContext, "Copied!", Toast.LENGTH_SHORT);
        mToast.show();
    }

    private void textViewResetCurrentPosition() {
        mHorizontalScrollView.clearAnimation();
        mHorizontalScrollView.setRotation(0);
        mHorizontalScrollView.setRotationX(0);
        mHorizontalScrollView.setRotationY(0);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mClipboard = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);

        TextView tvOutput = binding.output;
        EditText etInput = binding.input;
        mHorizontalScrollView = binding.textScrollView;
        FloatingActionButton copyButton = binding.copy;

        // NOTE: setting monospace inside an xml won't work for some old SDKs
        tvOutput.setTypeface(Typeface.MONOSPACE);

        tvOutput.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View view) {
                textViewResetCurrentPosition();
                mHorizontalScrollView.animate()
                        .rotationBy(-75)
                        .setInterpolator(new DecelerateInterpolator(1))
                        .setDuration(1000)
                        .withEndAction(new Runnable() {
                            @Override
                            public void run() {
                                mHorizontalScrollView.animate()
                                        .rotationBy((360 * 8) + 75)
                                        .setInterpolator(new DecelerateInterpolator(2))
                                        .setDuration(1100);
                            }
                        });
                return true;
            }
        });

        tvOutput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textViewResetCurrentPosition();
                mHorizontalScrollView.animate()
                        .rotationYBy(-360)
                        .rotationBy(360)
                        .setInterpolator(new LinearInterpolator())
                        .setDuration(1500);
            }
        });

        copyButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String text = tvOutput.getText().toString();
                if (text.isEmpty()) {
                    return;
                }

                setClipboard(text);
                view.setRotation(0);
                view.animate()
                        .rotationBy(360)
                        .setInterpolator(new DecelerateInterpolator(1))
                        .setDuration(500);
            }
        });

        etInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                textViewResetCurrentPosition();
                mHorizontalScrollView.setAlpha(0);
                mHorizontalScrollView.setRotation(180);
                mHorizontalScrollView.animate()
                        .alpha(1)
                        .rotationBy(180)
                        .setDuration(450);
                String str = etInput.getText().toString();
                mHandler.removeCallbacks(mTGuyRunnable);
                if (mTG != null) mTG.close();
                if (str.isEmpty()) {
                    if (mClipboard != null && copyButton.isOrWillBeShown()) copyButton.hide();
                    mTG = null;
                    binding.output.setText("");
                } else {
                    if (mClipboard != null && copyButton.isOrWillBeHidden()) copyButton.show();
                    mTG = new TGuy(str, 4);
                    mHandler.post(mTGuyRunnable);
                }
            }
        });
    }
}
