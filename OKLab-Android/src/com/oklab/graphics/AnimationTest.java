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

    //FrameAnimation�� ���� Animation�� ������
    //Animation Drawable ��ü ��������
    //��, Frame ������ �̹����� �ٲ㼭 �׷��ִ� Drawable ��ü

    AnimationDrawable ani;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_animation);

        //ImageView ��ü ����
        img = (ImageView) findViewById(R.id.img);

        //ImageView�� src �Ӽ����� ������ pink_run�̹����� Drawable ��ü�� ������.
        //Frame Aniamtion�� Drawable�� ���������� ����ȯ ����
        ani = (AnimationDrawable) img.getDrawable();

        //ó�� �����ϸ� Run �ִϸ��̼� ����

        //pink_run.xml ���ҽ��� oneshot�� �ƴϹǷ� ��� �ݺ���.
        ani.start();
    }

    //onClick �Ӽ��� ������ View�� Ŭ���Ǿ��� �� �ڵ����� ȣ��Ǵ� �ݹ�޼ҵ�

    public void mOnClick(View v) {

        switch (v.getId()) {

            case R.id.btn_run:

                //'RUN START' ��ư�� ������ �� ���� Frame Animation�� �������̸� ����
                //Frame Animation�� �ѹ� start()���ָ� ��� Running ������.
                //Frame Animation�� OneShot���� �ϰ� �ٽ� �����ϰ� �ϰ� �ʹٸ�
                //���� Frame Animation�� 'stop'���Ѿ� ��.

                if (ani.isRunning()) ani.stop();

                //ImageView�� pink_run.xml Drawable ���ҽ����� ����

                img.setImageResource(R.drawable.frame_ani_sample);

                //ImageView�� ������ xml ���ҽ� ������ Drawable ��ü�� ������ �����Ƿ�
                //�̸� �ٽ� ����.
                ani = (AnimationDrawable) img.getDrawable();

                //AnimationDrawable ��ü����
                //Frame ������ �����ϵ��� ��.

                ani.start();
                break;
            case R.id.btn_stop:
                if (ani.isRunning()) ani.stop();
                startActivity(new Intent(this, CanvasTestActivity.class));
                break;
        }
    }

}

