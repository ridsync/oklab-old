package com.okitoki.checklist.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.util.Log;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.okitoki.checklist.R;
import com.okitoki.checklist.core.AppConst;
import com.okitoki.checklist.database.model.PhotoEvent;
import com.okitoki.checklist.utils.AUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import de.greenrobot.event.EventBus;

/**
 * Device Image Capture Processing
 * CropImage Lib use
 */
public class PhotoActivity extends Activity {

    /***************************
     * Static Member Variable
     ***************************/
    protected Context mContext = null;
    protected boolean mOnStop = false;

    private static final int CAPTURE_TAKE_PHOTO_ACTIVITY_REQUEST_CODE = 100;
    private static final int CAPTURE_GALLERY_IMAGE_ACTIVITY_REQUEST_CODE = 110;
    private static final int CROP_IMAGE_ACTIVITY_REQUEST_CODE = 200;
    private Uri mUriFile;
    private Uri mUriResizeFile;
    private int mCategory;
    private int mCameraType;
    private int mPhotoType;

    private RelativeLayout progress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount = 0.5f; // 투명도 0 ~ 1
        getWindow().setAttributes(layoutParams);
        setContentView(R.layout.layout_photo);

        progress = (RelativeLayout) findViewById(R.id.RL_REGISTER_INFO_PROGRESS);

        mContext = getApplicationContext();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mCameraType = bundle.getInt(AppConst.BUNDLE_CAMERA_TYPE);
        }
//        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + Utils.getExternalCacheDir())));
        if (mCameraType == 0) {
            takeGallery();
        } else if (mCameraType == 1) {
            takePhoto();
        }

//        Tracker tracker = ((SQApp) getApplication()).getDefaultTracker();
//        tracker.setScreenName("PHOTOACTIVTY");
//        tracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + AUtil.getExternalCacheDir(mContext))));

        if (requestCode == CAPTURE_TAKE_PHOTO_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                String strUrl = "tmp_" + String.valueOf(System.currentTimeMillis()) + ".jpg";
                File fSaved = new File(AUtil.getExternalCacheDir(mContext), strUrl);
                try {
                    fSaved.createNewFile();
                } catch(IOException e) {
                    e.printStackTrace();
                }
                mUriResizeFile = Uri.fromFile(fSaved);

//                startActImageCrop(mUriFile);
                if ( saveBitmapToCacheDir(mUriFile,true) ){
                    requestUploadPhoto(mUriResizeFile.getPath());
                } else {
                    finish();
                }
            } else if (resultCode == RESULT_CANCELED) {
                finish();
            }
        } else if (requestCode == CAPTURE_GALLERY_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                String strUrl = "tmp_" + String.valueOf(System.currentTimeMillis()) + ".jpg";
                mUriResizeFile = Uri.fromFile(new File(AUtil.getExternalCacheDir(mContext), strUrl));

                Uri imgUri = data.getData();
                if ( saveBitmapToCacheDir(imgUri,false) ){
                    requestUploadPhoto(mUriResizeFile.getPath());
                } else {
                    finish();
                }
            } else if (resultCode == RESULT_CANCELED) {
                finish();
            }
        }
    }

    private boolean saveBitmapToCacheDir(Uri imgUri, boolean isPicRotate) {
        boolean result = true;
        // PICASA
        try {
            //
            int exifDegree = 0;
            if(isPicRotate){ // 카메라사진만 Rotate ※ ExifInterface FileNotFoundExecption Issue from Nougat
                String imagePath = imgUri.getPath();
                ExifInterface exif = new ExifInterface(imagePath);
                int exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                exifDegree = exifOrientationToDegrees(exifOrientation);
            }

            // TODO 기기별 분기 필요한가 ??넥서스 or 전체 디바이스 공통 적용가능한 decode소스는?
            // 삼성폰네이티브 이분기가 타긴타나 ??
            if (imgUri.toString().startsWith("content://com.android.gallery3d") ||              // 네이티브 하위버전
                    imgUri.toString().startsWith("content://com.google.android.apps.photos") || // 네이티브 요즘버전
                    imgUri.toString().startsWith("content://com.sec.android.gallery3d")) {      // 삼성폰
                InputStream is = null;
                try {
                    is = getApplicationContext().getContentResolver().openInputStream(imgUri);
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    final Bitmap bmpImage = BitmapFactory.decodeStream(is, null, options);
                    Bitmap bitmap  = AUtil.rotate(bmpImage, exifDegree);
                    AUtil.saveBitmapToFile(bitmap, mUriResizeFile.getPath());
                    is.close();
                    if (bitmap != null) {
                        bitmap.recycle();
                    }
                } catch (IOException | RuntimeException e) {
                    result = false;
                    Toast.makeText(getApplicationContext(), "Try again", Toast.LENGTH_SHORT).show();
                }
            } else {
                // 1) contentprovider query
//                String imgPath = getRealPathFromURI(this, imgUri); // path 경로
//                Bitmap bmpImage = BitmapFactory.decodeFile(imgPath);
                // 2) MediaStore.Images.Media.getBitmap
//                Bitmap  bmpImage = MediaStore.Images.Media.getBitmap(getContentResolver(), imgUri);
                // 3) contentprovider resolver openInputStream and Resize
                Bitmap  bmpImage =  AUtil.getBitmapWithResize(getApplicationContext(),imgUri);
                Bitmap bitmap = AUtil.rotate(bmpImage, exifDegree);
                AUtil.saveBitmapToFile(bitmap, mUriResizeFile.getPath());
                if (bitmap != null) {
                    bitmap.recycle();
                }

            }
        } catch (OutOfMemoryError e) {
            result = false;
            Toast.makeText(getApplicationContext(), "Out Of Memory", Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            result = false;
            Toast.makeText(getApplicationContext(), "FileNotFoundException", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            result = false;
            Toast.makeText(getApplicationContext(), "IOException", Toast.LENGTH_SHORT).show();
        }  catch (Exception e) {
            result = false;
            Toast.makeText(getApplicationContext(), "Exception", Toast.LENGTH_SHORT).show();
        }
        return result;
    }


    /**
     *  사진 업로드 API
     * @param strFilePath
     */
    private void requestUploadPhoto(String strFilePath) {
        final File f = new File(strFilePath);
        if( ! f.exists()) return;

        EventBus.getDefault().post(new PhotoEvent(strFilePath));

        Intent bundle = new Intent();
        bundle.putExtra("photopath",strFilePath);
        setResult(RESULT_OK,bundle);
        finish();
    }


    @Override
    public void onBackPressed() {

    }

    private void galleryAddPic(String strPhotoPath) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(strPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    private void takeGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
//        String strUrl = "tmp_" + String.valueOf(System.currentTimeMillis()) + ".jpg";
//        mUriFile = Uri.fromFile(new File(AUtil.getExternalCacheDir(), strUrl));
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, mUriFile);
        intent.setType("image/*");
        intent.setData(Images.Media.EXTERNAL_CONTENT_URI);
        // intent.putExtra("crop", "true");
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());

        startActivityForResult(intent, CAPTURE_GALLERY_IMAGE_ACTIVITY_REQUEST_CODE);
    }

    private void takePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        String strUrl = "tmp_" + String.valueOf(System.currentTimeMillis()) + ".jpg";
        File fSaved = new File(AUtil.getExternalCacheDir(mContext), strUrl);
        try {
            fSaved.createNewFile();
        } catch(IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Log.e("Photo", "========== f ======= : " + fSaved.exists());
        mUriFile = Uri.fromFile(fSaved);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mUriFile);
        startActivityForResult(intent, CAPTURE_TAKE_PHOTO_ACTIVITY_REQUEST_CODE);
    }

    public int exifOrientationToDegrees(int exifOrientation) {
        if(exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if(exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if(exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }
}
