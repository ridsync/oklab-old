package com.oklab.graphics;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;

import com.oklab.R;

import java.util.ArrayList;

/**
 * Created by ojungwon on 2015-01-08.
 */
public class MainView extends View {

    ArrayList<Vertex> arVertex;

    Bitmap bmpBack;
    Canvas mCanvas;
    Point ptStart =new Point();

    Paint mPaint;

     public MainView(Context context) {
        super(context);

//         bmpBack = BitmapFactory.decodeResource(getResources(), R.drawable.ha);
         Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.doll);
         bmpBack = bmp.copy(Bitmap.Config.ARGB_8888,true);
         mCanvas =new Canvas(bmpBack);

         arVertex = new ArrayList<Vertex>();
         // 페인트 객체 생성 후 설정
         mPaint = new Paint();
         mPaint.setColor(Color.BLACK);
         mPaint.setStrokeWidth(3);
         mPaint.setAntiAlias(true);      // 안티얼라이싱
    }

    public void onDraw(Canvas canvas) {

          // 선 그리기
//        Paint pnt =new Paint();
//        pnt.setStrokeWidth(1);
//        pnt.setARGB(255, 255, 0, 0);
//        canvas.drawLine(10, 300, 470, 300,pnt);

        // canvas.drawColor(Color.WHITE);      // 캔버스 배경색깔 설정

        // 이미지 그리기
         bmpBack = BitmapFactory.decodeResource(getResources(), R.drawable.wdg_photo_bg);
         canvas.drawBitmap(bmpBack, null,new Rect(0,0,this.getWidth(),this.getHeight()), null);

        // 그리기
        for(int i=0; i<arVertex.size(); i++){
            if(arVertex.get(i).draw){       // 이어서 그리고 있는 중이라면
                canvas.drawLine(arVertex.get(i-1).x, arVertex.get(i-1).y,
                        arVertex.get(i).x, arVertex.get(i).y, mPaint);
                // 이전 좌표에서 다음좌표까지 그린다.
            }else{
                canvas.drawPoint(arVertex.get(i).x, arVertex.get(i).y, mPaint);
                // 점만 찍는다.
            }
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                arVertex.add(new Vertex(event.getX(), event.getY(), false));
                break;
            case MotionEvent.ACTION_MOVE:
                arVertex.add(new Vertex(event.getX(), event.getY(), true));
        }
        invalidate();       // onDraw() 호출
        return true;
    }

    public class Vertex{
        float x;
        float y;
        boolean draw;   // 그리기 여부

        public Vertex(float x, float y, boolean draw){
            this.x = x;
            this.y = y;
            this.draw = draw;
        }
    }
}