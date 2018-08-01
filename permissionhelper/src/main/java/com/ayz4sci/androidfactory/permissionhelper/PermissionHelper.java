package com.ayz4sci.androidfactory.permissionhelper;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import pl.tajchert.nammu.Nammu;
import pl.tajchert.nammu.PermissionCallback;

/**
 * Created by Ayz4sci on 01/06/16.
 */
public class PermissionHelper {
    private static PermissionHelper permissionHelper;
    private static final int REQUEST_CODE = 12345;

    private Dialog permissionDialog;
    private ImageButton backArrow;
    private TextView permissionTitleTextView;
    private TextView permissionDescTextView;
    private Button allowButton;
    private Button notAllowButton;
    private String continueText;

    private String notNowText;
    private String goToAppInfo;
    private String accessMessage;
    private String permissionTitle;
    private String permissionDeniedString;
    private String grantPermissionToString;
    private String enablePermissionFromSettings;
    private String appName;
    private PermissionCallback permissionCallback;
    private ArrayList<String> neededPermissions;

    private Activity activity;

    public static PermissionHelper getInstance(Activity activity) {
        if (permissionHelper == null) {
            Nammu.init(activity);
            return permissionHelper = new PermissionHelper(activity);
        } else {
            return permissionHelper;
        }
    }

    public PermissionHelper(Activity activity) {
        this.activity = activity;

        permissionDialog = new Dialog(activity, android.R.style.Theme);
        permissionDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        permissionDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
//                WindowManager.LayoutParams.MATCH_PARENT);
        permissionDialog.setCancelable(false);
        permissionDialog.setContentView(R.layout.permissions_page);

        initUI(permissionDialog);
        initListeners();
    }

    private void initUI(Dialog dialog) {
        backArrow = (ImageButton) dialog.findViewById(R.id.back_arrow);
        permissionTitleTextView = (TextView) dialog.findViewById(R.id.permission_title);
        permissionDescTextView = (TextView) dialog.findViewById(R.id.permission_description);
        allowButton = (Button) dialog.findViewById(R.id.allow_button);
        notAllowButton = (Button) dialog.findViewById(R.id.not_now_button);

        continueText = dialog.getContext().getString(R.string.continue_text);
        notNowText = dialog.getContext().getString(R.string.not_now);
        goToAppInfo = dialog.getContext().getString(R.string.go_to_app_info);
        accessMessage = dialog.getContext().getString(R.string.access_your);
        permissionTitle = dialog.getContext().getString(R.string.permission_title);
        permissionDeniedString = dialog.getContext().getString(R.string.permission_denied_title_message);
        grantPermissionToString = dialog.getContext().getString(R.string.grant_permission_to);
        enablePermissionFromSettings = dialog.getContext().getString(R.string.permission_can_be_enabled_under);

        getAppName();
    }

    private void initListeners() {
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                permissionDialog.dismiss();
                permissionCallback.permissionRefused();
            }
        });

        allowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (allowButton.getText().toString().equalsIgnoreCase(continueText)) {
                    Nammu.askForPermission(activity, neededPermissions.toArray(new String[neededPermissions.size()]), new PermissionCallback() {
                        @Override
                        public void permissionGranted() {
                            permissionDialog.dismiss();
                            permissionCallback.permissionGranted();
                        }

                        @Override
                        public void permissionRefused() {
                            Nammu.refreshMonitoredList();
                            ArrayList<String> newNeededPermissions = new ArrayList<>();
                            ArrayList<String> approvedPermissions = Nammu.getGrantedPermissions();
                            for (String neededPermission : neededPermissions) {
                                if (!approvedPermissions.contains(neededPermission)) {
                                    newNeededPermissions.add(neededPermission);
                                }
                            }

                            permissionFailedMessage(newNeededPermissions.toArray(new String[newNeededPermissions.size()]), "");
                        }
                    });
                } else {
                    openAppSettings(activity);
                }
            }
        });

        notAllowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                permissionDialog.dismiss();
                permissionCallback.permissionRefused();
            }
        });
    }

    protected void showMessage(String title, String[] permissions) {
        permissionTitleTextView.setText(String.format(permissionTitle, title));

        String permissionList = String.format(accessMessage, PermissionsGroup.getPermissionGroupString(permissions[0]).toLowerCase());
        int length = permissions.length;
        for (int i = 1; i < length; i++) {
            String permissionGroup = PermissionsGroup.getPermissionGroupString(permissions[i]);
            if (!permissionList.contains(permissionGroup.toLowerCase())) {
                permissionList = permissionList + "\n" + String.format(accessMessage, permissionGroup.toLowerCase());
            }
        }
        permissionDescTextView.setText(permissionList);
        allowButton.setText(continueText);
        notAllowButton.setText(notNowText);

        permissionDialog.show();
    }

    protected void permissionFailedMessage(String[] permissions, String title) {
        if (permissions.length > 0) {
            String permissionList = PermissionsGroup.getPermissionGroupString(permissions[0]);
            int length = permissions.length;
            for (int i = 1; i < length; i++) {
                String permissionGroup = PermissionsGroup.getPermissionGroupString(permissions[i]);
                if (!permissionList.contains(permissionGroup)) {
                    permissionList = permissionList + ", " + permissionGroup;
                }
            }

            if (title.isEmpty()) {
                permissionTitleTextView.setText(String.format(permissionDeniedString,
                        permissions.length > 1 ? "permissions" : "permission"));
            } else {
                permissionTitleTextView.setText(String.format(grantPermissionToString, title));
            }
            permissionDescTextView.setText(String.format(enablePermissionFromSettings, appName,
                    permissionList + (permissions.length > 1 ? " permissions" : " permission")));

            allowButton.setText(goToAppInfo);

            permissionDialog.show();
        }
    }


    //shouldShowPermissionPage used to determine whether the permission page should show or not
    // if user previously selected never show permission checkbox.
    public void verifyPermission(String titles[], String[] requiredPermissions,
                                 final PermissionCallback permissionCallback) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            permissionCallback.permissionGranted();
            return;
        }

        if (titles.length != requiredPermissions.length) {
            throw new IllegalArgumentException("Titles length must be equal to required permissions length");
        }

        neededPermissions = new ArrayList<>();
        this.permissionCallback = permissionCallback;

        ArrayList<String> shouldShowRequestPermissionRationales = new ArrayList<>();
        String neededPermissionsTitle = "";
        String requestPermissionRationalesTitle = "";
        String requiredPermission;
        int length = requiredPermissions.length;
        for (int i = 0; i < length; i++) {
            requiredPermission = requiredPermissions[i];
            if (!Nammu.checkPermission(requiredPermission)) {
                neededPermissions.add(requiredPermission);

                if (!neededPermissionsTitle.contains(titles[i])) {
                    neededPermissionsTitle = neededPermissionsTitle + (neededPermissions.size() == 1 ? "" : ", ") + titles[i];
                }

                if (Nammu.shouldShowRequestPermissionRationale(activity, requiredPermission)) {
                    shouldShowRequestPermissionRationales.add(requiredPermission);

                    if (!requestPermissionRationalesTitle.contains(titles[i])) {
                        requestPermissionRationalesTitle = requestPermissionRationalesTitle
                                + (shouldShowRequestPermissionRationales.size() == 1 ? "" : ", ") + titles[i];
                    }
                }
            }
        }

        boolean shouldShowPermissionsPage = false;
        if (!shouldShowRequestPermissionRationales.isEmpty() && shouldShowPermissionsPage) {
            permissionFailedMessage(shouldShowRequestPermissionRationales.toArray(
                    new String[shouldShowRequestPermissionRationales.size()]), "");

        } else if (!shouldShowRequestPermissionRationales.isEmpty() && !shouldShowPermissionsPage) {
            permissionFailedMessage(shouldShowRequestPermissionRationales.toArray(
                    new String[shouldShowRequestPermissionRationales.size()]), requestPermissionRationalesTitle);

        } else if (!neededPermissions.isEmpty()) {
            showMessage(neededPermissionsTitle, neededPermissions.toArray(new String[neededPermissions.size()]));

        } else {
            permissionDialog.dismiss();
            permissionCallback.permissionGranted();
        }
    }

    public static void verifyPermissions(String[] requiredPermissions, PermissionCallback permissionCallback) {
        for (String requiredPermission : requiredPermissions) {
            if (!Nammu.checkPermission(requiredPermission)) {
                permissionCallback.permissionRefused();
                return;
            }
        }
        permissionCallback.permissionGranted();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE && permissionCallback != null && activity != null) {
            ArrayList<String> rejectedPermissions = new ArrayList<>();
            for (String neededPermission : neededPermissions) {
                if (!Nammu.checkPermission(neededPermission)) {
                    rejectedPermissions.add(neededPermission);
                }
            }

            permissionDialog.dismiss();
            if (!rejectedPermissions.isEmpty()) {
                permissionCallback.permissionRefused();
            } else {
                permissionCallback.permissionGranted();
            }
        }
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        Nammu.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void openAppSettings(Activity activity) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
        intent.setData(uri);
        activity.startActivityForResult(intent, REQUEST_CODE);
    }

    private void getAppName() {
        PackageManager packageManager = permissionDialog.getContext().getPackageManager();
        try {
            appName = (String) packageManager.getApplicationLabel(packageManager.getApplicationInfo(permissionDialog.getContext().getPackageName(), PackageManager.GET_META_DATA));
        } catch (PackageManager.NameNotFoundException ignored) {
        }
    }

    public Dialog getPermissionDialog() {
        return permissionDialog;
    }
    protected void setPermissionCallback(PermissionCallback permissionCallback) {
        this.permissionCallback = permissionCallback;
    }

    protected void setNeededPermissions(ArrayList neededPermissions) {
        this.neededPermissions = neededPermissions;
    }

    public ImageButton getBackButton() {
        return backArrow;
    }

    public Button getAllowButton() {
        return allowButton;
    }

    public Button getNotAllowButton() {
        return notAllowButton;
    }

    public ImageView getCenterIcon() {
        return (ImageView) permissionDialog.findViewById(R.id.center_icon);
    }

    public RelativeLayout getBackgroundLayout() {
        return (RelativeLayout) permissionDialog.findViewById(R.id.background_layout);
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public void customiseUI(int background, Drawable icon) {
        getBackgroundLayout().setBackgroundColor(activity.getResources().getColor(background));
        getCenterIcon().setImageDrawable(icon);
    }

    public void finish() {
        permissionDialog.dismiss();
        permissionHelper = null;
    }
}
