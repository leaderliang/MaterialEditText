package com.android.edittext;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

/**
 * @author liangyanqiao
 */
public class MainActivity extends AppCompatActivity {

    private MaterialEditText mEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEditText = findViewById(R.id.edit_text);
//        mEditText.setIsUseMaterialEditText(false);
    }
}
