package com.ayz4sci.androidfactory.permissionhelpersample;

import android.Manifest;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ayz4sci.androidfactory.permissionhelper.PermissionHelper;

import pl.tajchert.nammu.PermissionCallback;

public class MainActivity extends AppCompatActivity {

    private PermissionHelper permissionHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        permissionHelper                = PermissionHelper.getInstance(this);

        Button requirePermissionButton  = (Button) findViewById(R.id.require_permission_button);
        TextView textView               = (TextView) findViewById(R.id.text_view);

        requirePermissionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                permissionHelper.verifyPermission(new String[]{"dial this number"},
                        new String[]{Manifest.permission.CALL_PHONE}, new PermissionCallback() {
                            @Override
                            public void permissionGranted() {
                                Helpers.callNumber(MainActivity.this, "1234");
                            }

                            @Override
                            public void permissionRefused() {

                            }
                        });
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        permissionHelper.onActivityResult(requestCode, resultCode, data);
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        permissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onDestroy() {
        permissionHelper.finish();
        super.onDestroy();
    }
}
