package com.oklab.events;

import com.oklab.R;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.SurfaceHolder.Callback;
 
public class EventMove extends SurfaceView implements Callback, Runnable {
    private Context mContext;
    private SurfaceHolder holder;
    private Bitmap imgMove;
    private int moveX = 0;
    private int moveY = 0;
    private int imgWidth = 0;
    private int imgHeight = 0;
    private int moveLength = 20;
    private Thread thread = null;
    private Point pImage;
    private Point pWindow;
    private boolean bMove = false;
    private int mouseX = 0;
    private int mouseY = 0;
     
    public EventMove(Context context) {
        super(context);
        mContext = context;
         
        // SurfaceHolder Create
        holder = getHolder();
        holder.addCallback(this);
        setFocusable(true);
    }
    /** surface 가 변경될때 */
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
         
    }
    /** surface 가 생성될때 */
    public void surfaceCreated(SurfaceHolder holder) {
        // window 크기
        pWindow = new Point();
        pWindow.x = 320;
        pWindow.y = 480;
         
        // 이미지 위치
        pImage = new Point(0, 0);
        Resources res = getResources();
        Bitmap tempBitmap = BitmapFactory.decodeResource(res, R.drawable.doll);
        imgWidth = 80;
        imgHeight = 70;
         
        // 표시할 위치
        moveX = pWindow.x / 2;
        moveY = pWindow.y / 2;
         
        // 이미지 그리기
        imgMove = Bitmap.createScaledBitmap(tempBitmap, imgWidth, imgHeight, true);
        setClickable(true);
        thread = new Thread(this);
        thread.start();
    }
 
    public void surfaceDestroyed(SurfaceHolder holder) {
        try {
            thread.interrupt();
        } catch (Exception e) {
            Log.e(this.getClass().getName(), e.getMessage());
        }
    }
 
    public void run() {
        // Canvas 의 사이즈
        pWindow.x = getWidth();
        pWindow.y = getHeight();
         
        while (!Thread.currentThread().isInterrupted()) {
            Canvas c = null;
             
            try {
                c = holder.lockCanvas(null);
                synchronized (holder) {
                    doDraw(c);
                    Thread.sleep(50);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                if (c != null){
                    holder.unlockCanvasAndPost(c);
                }
            }
        }
    }
     
    private void doDraw(Canvas cv){
         
        pImage.x = moveX;
        pImage.y = moveY;
        cv.drawColor(Color.LTGRAY); // 새로그림
        cv.drawBitmap(imgMove, moveX - 40, moveY - 30, null);
         
        // Paint 표시, 그리기 개체
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setTextSize(16);
        paint.setColor(Color.RED);
        cv.drawText("Move Enable : "+bMove, 0, 400, paint);
        cv.drawText("IMAGE Point : X="+pImage.x+", Y="+pImage.y, 0, 425, paint);
        cv.drawText("Mouse Point : X="+mouseX+", Y="+mouseY, 0, 450, paint);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode){
        case KeyEvent.KEYCODE_DPAD_LEFT:
            if (moveX > 0){
                moveX -= moveLength;
            }
            break;
        case KeyEvent.KEYCODE_DPAD_RIGHT:
            if ((moveX + imgWidth) < pWindow.x){
                moveX += moveLength;
            }
            break;
        case KeyEvent.KEYCODE_DPAD_UP:
            if (moveX > 0){
                moveY -= moveLength;
            }
            break;
        case KeyEvent.KEYCODE_DPAD_DOWN:
            if ((moveY + imgHeight) < pWindow.y){
                moveY += moveLength;
            }
            break;
        case KeyEvent.KEYCODE_DPAD_CENTER:
            break;
        }
        return  super.onKeyDown(keyCode, event);
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int keyAction = event.getAction();
        int x = (int)event.getX();
        int y = (int)event.getY();
        mouseX = x;
        mouseY = y;
        switch (keyAction){
        case MotionEvent.ACTION_MOVE:
            if (bMove){
                moveX = x;
                moveY = y;
            }
            break;
        case MotionEvent.ACTION_UP:
            bMove = false;
            break;
        case MotionEvent.ACTION_DOWN:
            this.checkImageMove(x, y);
            break;
        }
        // 함수 override 해서 사용하게 되면  return  값이  super.onTouchEvent(event) 되므로
        // MOVE, UP 관련 이벤트가 연이어 발생하게 할려면 true 를 반환해주어야 한다.
        return true;
    }
    /**
     * 현재 이미지위에 마우스가 위치하는지 판단한다.
     * @param x
     * @param y
     */
    private void checkImageMove(int x, int y){
        int inWidth = 30;
        int inHeight = 20;
        if ((pImage.x - inWidth < x) && (x < pImage.x + inWidth)){
            if ((pImage.y - inHeight < y) && (y < pImage.y + inHeight)){
                bMove = true;
            }
        }
    }
}
