package com.oklab.graphics;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.oklab.BaseActivity;
import com.oklab.R;

/**
 * Created by okc on 2015-06-25.
 */
public class AnimationTest extends BaseActivity {

    ImageView img;

    //FrameAnimation에 의해 Animation을 실행할
    //Animation Drawable 객체 참조변수
    //즉, Frame 단위로 이미지를 바꿔서 그려주는 Drawable 객체

    AnimationDrawable ani;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_animation);

        //ImageView 객체 참조
        img = (ImageView) findViewById(R.id.img);

        //ImageView에 src 속성으로 설정된 pink_run이미지를 Drawable 객체로 얻어오기.
        //Frame Aniamtion은 Drawable의 일종이으로 형변환 가능
        ani = (AnimationDrawable) img.getDrawable();

        //처음 시작하면 Run 애니메이션 실행

        //pink_run.xml 리소스는 oneshot이 아니므로 계속 반복됨.
        ani.start();
    }

    //onClick 속성이 지정된 View가 클릭되었을 때 자동으로 호출되는 콜백메소드

    public void mOnClick(View v) {

        switch (v.getId()) {

            case R.id.btn_run:

                //'RUN START' 버튼을 눌렀을 때 이전 Frame Animation이 진행중이면 정지
                //Frame Animation은 한번 start()해주면 계속 Running 상태임.
                //Frame Animation을 OneShot으로 하고 다시 시작하게 하고 싶다면
                //이전 Frame Animation을 'stop'시켜야 함.

                if (ani.isRunning()) ani.stop();

                //ImageView에 pink_run.xml Drawable 리소스파일 세팅

                img.setImageResource(R.drawable.frame_ani_sample);

                //ImageView는 설정된 xml 리소스 파일을 Drawable 객체로 가지고 있으므로
                //이를 다시 얻어옴.
                ani = (AnimationDrawable) img.getDrawable();

                //AnimationDrawable 객체에게
                //Frame 변경을 시작하도록 함.

                ani.start();
                break;
            case R.id.btn_stop:
                if (ani.isRunning()) ani.stop();
                startActivity(new Intent(this, CanvasTestActivity.class));
                break;
        }
    }

}

