package com.oklab.views;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;

import com.oklab.BaseActivity;
import com.oklab.R;

public class ImageViewScaleTestActivity extends BaseActivity {

	// ScaleType 예재 : http://etcodehome.blogspot.kr/2011/05/android-imageview-scaletype-samples.html
	// ScaleType 예재 : http://mainia.tistory.com/473
public static final String TAG = "ImageViewScaleTestActivity";
private static final int IMAGE_SELECT = 8282;
private ImageView image;
private TextView tv;
private Button btn1;
private Button btn2;
private Button btn3;
private Button btn4;
private Button btn5;
private Button btn6;
private Button btn7;
private Button btn8;
private Button btn9;
private Button btn10;

private View.OnClickListener buttonListener = new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.bt1:
            changeImageScaleType(ScaleType.CENTER);
            break;
        case R.id.bt2:
            changeImageScaleType(ScaleType.CENTER_CROP);
            break;
        case R.id.bt3:
            changeImageScaleType(ScaleType.CENTER_INSIDE);
            break;
        case R.id.bt4:
            changeImageScaleType(ScaleType.FIT_CENTER);
            break;
        case R.id.bt5:
            changeImageScaleType(ScaleType.FIT_START);
            break;
        case R.id.bt6:
            changeImageScaleType(ScaleType.FIT_END);
            break;
        case R.id.bt7:
            changeImageScaleType(ScaleType.FIT_XY);
            break;
        case R.id.bt8:
            changeImageScaleType(ScaleType.MATRIX);
            break;
        case R.id.bt9:
            break;
        case R.id.bt10:
            selectImage();
            break;
        }
    }
};

@Override
public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.mv_imgeview_scale);

    image = (ImageView) findViewById(R.id.image);
    tv = (TextView) findViewById(R.id.txtv);
   
//    new Buttons(this)//
//        .add(R.id.bt1)//
//        .add(R.id.bt2)//
//        .add(R.id.bt3)//
//        .add(R.id.bt4)//
//        .add(R.id.bt5)//
//        .add(R.id.bt6)//
//        .add(R.id.bt7)//
//        .add(R.id.bt8)//
//        .add(R.id.bt9)//
//        .add(R.id.bt10)//
//        .onClick(buttonListener);
    btn1 = (Button) findViewById(R.id.bt1);
    btn2 = (Button) findViewById(R.id.bt2);
    btn3 = (Button) findViewById(R.id.bt3);
    btn4 = (Button) findViewById(R.id.bt4);
    btn5 = (Button) findViewById(R.id.bt5);
    btn6 = (Button) findViewById(R.id.bt6);
    btn7 = (Button) findViewById(R.id.bt7);
    btn8 = (Button) findViewById(R.id.bt8);
    btn9 = (Button) findViewById(R.id.bt9);
    btn10 = (Button) findViewById(R.id.bt10);
    btn1.setOnClickListener(buttonListener);
    btn2.setOnClickListener(buttonListener);
    btn3.setOnClickListener(buttonListener);
    btn4.setOnClickListener(buttonListener);
    btn5.setOnClickListener(buttonListener);
    btn6.setOnClickListener(buttonListener);
    btn7.setOnClickListener(buttonListener);
    btn8.setOnClickListener(buttonListener);
    btn9.setOnClickListener(buttonListener);
    btn10.setOnClickListener(buttonListener);
}

protected void selectImage() {
    showDialog(IMAGE_SELECT);
}


private void changeImageScaleType(ScaleType scaleType) {
    image.setScaleType(scaleType);
    changeText();
}

protected void changeImage(int resId) {
    image.setImageResource(resId);
    changeText();
}

protected void changeText() {
    int l = image.getLeft();
    int r= image.getRight();
    int t = image.getTop();
    int b = image.getBottom();
    int w = image.getWidth();
    int h = image.getHeight();
    int mh = image.getMeasuredHeight();
    int mw = image.getMeasuredWidth();
    tv.setText(String.format("Width=%s, Heigth=%s, MW=%s, MH=%s \n top=%s, right=%s, bottom=%s, left=%s", w, h, mw, mh, t, r, b, l));
}
@Override
protected Dialog onCreateDialog(int id) {
    switch (id) {
    case IMAGE_SELECT:
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("이미지를 선택하세요");
        builder.setItems(new String[]{"200x100 이미지"//
                , "100x200이미지"//
                , "200x200이미지"//
                , "424x663"//
                , "500x500"}
       
            , new DialogInterface.OnClickListener() {
           
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:    changeImage(R.drawable.doll);                break;
                    case 1:    changeImage(R.drawable.btn_close);                break;
                    case 2:    changeImage(R.drawable.wdg_photo_bg);                    break;
                    case 3:    changeImage(R.drawable.loading);                        break;
                    case 4:    changeImage(R.drawable.bg_intro);                    break;
                }
            }
        });
       
        return builder.create();
    default:
        break;
    }
    return super.onCreateDialog(id);
}
	
}
