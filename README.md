# PermissionHelper

A permission helper library that simplifies asking user for permission in Android M. With a customisable explanation dialog, you can easily explain to the user the permissions you need and why you need it, with buttons for permissions or take users to the app settings page in case the permission was denied.

# Playstore Sample
[PermissionHelper Sample APK] (https://play.google.com/store/apps/details?id=com.ayz4sci.androidfactory.permissionhelpersample)
Screenshots can be found on the store.

# Version

1.0.0

# Usage
To use this library in your android project, just simply add the following repositories and dependency into your build.gradle

```sh
dependencies {
    compile 'com.ayz4sci.androidfactory:permissionhelper:1.0.0'
}
```

Also add this repository because the library is dependent on Nammu permission helper library https://github.com/tajchert/Nammu
```sh
repositories {
    maven {
        url "https://jitpack.io"
    }
}
```

In your main activity init the library as follows:

```java
    PermissionHelper permissionHelper = PermissionHelper.getInstance(this);
```

Then override the following in your app root activity or the root activity where the permissions will be required:
```java
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
```

Now, to perform an action that requires Android permission, call permissionHelper.verifyPermission. It accepts the following parameters:
`String Array - description of each permission required, this will be displayed to the user in the explanation dialog.`
`String array - an array of Manifest permissions`
`PermissionCallback - you put the actions you want to perform when permission is granted or rejected.`
See example below:
```java
    permissionHelper.verifyPermission(
        new String[]{"dial this number", "take picture"},
        new String[]{Manifest.permission.CALL_PHONE, Manifest.permission.CAMERA}, 
        new PermissionCallback() {
            @Override
            public void permissionGranted() {
                //action to perform when permission granteed
            }
    
            @Override
            public void permissionRefused() {
                //action to perform when permission refused
            }
        }
    );
```

To simply verify whether user has granted the permission without showing any explanation dialog or asking for permission:
* `String array - an array of Manifest permissions`
* `PermissionCallback - you put the actions you want to perform when permission is granted or rejected.` 
e.g.
```java
    permissionHelper.verifyPermission(
        new String[]{Manifest.permission.CALL_PHONE}, 
        new PermissionCallback() {
            @Override
            public void permissionGranted() {
                //action to perform when permission granted
            }
    
            @Override
            public void permissionRefused() {
                //action to perform when permission refused
            }
        }
    );
```

# Customise Explanation UI
To modify the explanation dialog displayed to the user, use the following getters:
* `customiseUI(int background, Drawable icon)` customise the background, set center (app) icon
* `getPermissionDialog()` get the permission dialog
* `getCenterIcon()` get the center icon (Logo), returns an ImageView
* `getNotAllowButton()` get the not allow button
* `getAllowButton()` get the allow button
* `getBackButton()` get the back button - cancel button

That's all.

# Thanks to 
*[Michal Tajchert] (https://github.com/tajchert) - Nammu Library
*[Ugo Ammanoh] (https://github.com/ugoamanoh) - many tips

# License

MIT
