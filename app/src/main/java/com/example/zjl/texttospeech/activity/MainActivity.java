package com.example.zjl.texttospeech.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.zjl.texttospeech.R;
import com.example.zjl.texttospeech.app.AppConstant;
import com.unisound.client.SpeechConstants;
import com.unisound.client.SpeechSynthesizer;

public class MainActivity extends AppCompatActivity {

    public SpeechSynthesizer speechSynthesizer;//提供对已安装的语音合成引擎的功能的访问
    Button start,stop,clear;
    EditText editText;
    boolean pause = false;
    private static final int PERMISSION_PHONE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        start = findViewById(R.id.start);
        stop = findViewById(R.id.stop);
        clear = findViewById(R.id.clear);
        editText = findViewById(R.id.editText);
        init();
    }

    private void init() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, PERMISSION_PHONE);
        } else {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                initSpeech();
            }
        }
        initListener();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_PHONE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // User agree the permission
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                        initSpeech();
                    }
                } else {
                    // User disagree the permission
                    Toast.makeText(this,"拒绝此请求语音播报会出现异常",Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    private void initListener() {

        start.setOnClickListener(v -> {
            if (editText.getEditableText().length() == 0) {
                Toast.makeText(this,"请先输入需要播报的内容",Toast.LENGTH_SHORT).show();
            } else {
                if (pause) {
                    stop.setText("暂停播报");
                    pause = false;
                }
                speechSynthesizer.playText(editText.getText().toString());
            }

        });

        stop.setOnClickListener(v -> {
            if (pause) {
                speechSynthesizer.resume();
                stop.setText("暂停播报");
                pause = false;
            } else {
                speechSynthesizer.pause();
                stop.setText("继续播报");
                pause = true;
            }

        });

        clear.setOnClickListener(v -> {
            editText.setText("");
            stop.setText("暂停播报");
            pause = false;
        });
    }

    private void initSpeech() {
        try {
            if (speechSynthesizer == null) {
                speechSynthesizer = new SpeechSynthesizer(this, AppConstant.SPEECH_APP_KEY, AppConstant.SPEECH_APP_SECRET);
            }
            speechSynthesizer.setOption(SpeechConstants.TTS_SERVICE_MODE, SpeechConstants.TTS_SERVICE_MODE_NET);
            speechSynthesizer.setOption(SpeechConstants.TTS_KEY_VOICE_SPEED, 70);
            speechSynthesizer.init(null);
            speechSynthesizer.playText("初始化成功");
        } catch (Exception e) {
            Log.e("Tag",e.getMessage());
            Toast.makeText(this,"初始化语音异常",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (speechSynthesizer != null) {
            speechSynthesizer = null;
        }
    }
}
