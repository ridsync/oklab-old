package com.oklab.oktwitter;
//package com.academy.ok;
//
//import java.util.ArrayList;
//
//import android.app.Activity;
//import android.os.Bundle;
//import android.widget.ListView;
//
//public class TestTwitter extends Activity {
//    
//	 ListView mList;
//	 ArrayList<Status> mData;
//	 
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_twitter);
//        
//        //ListView �� Adapter ����
//        mList = (ListView)findViewById(R.id.list);
//        mData = buildData();
//        TimelineAdapter adapter = new TimelineAdapter(this, mData);
//        mList.setAdapter(adapter);
//
//    }
//    
//    //�������� ������ ���ϴ�  �޼ҵ�
//	ArrayList<Status> buildData() {
//		ArrayList<Status> dataList = new ArrayList<Status>();
//		dataList.add( new Status(R.drawable.icon, "3�� 31��", "������", "�����ٶ󸶹ٻ����Ȥ�� �����Ͱ� �� �ȹٲ�� notifyDataSetChanged() ��뵥���� �ٲ������ �ٽ� ����Ʈ�� �׷��ֶ�.Tip2��Ƽ��Ƽ �ϳ��� �Ͻô°� ���մϴ�."));
//		dataList.add( new Status(R.drawable.icon,"3�� 29��","�迵��", "��"));
//		dataList.add( new Status(R.drawable.icon,"3�� 31��","������", "��"));
//		dataList.add( new Status(R.drawable.icon,"3�� 31��","������", "��"));
//		dataList.add( new Status(R.drawable.icon,"3�� 29��","�迵��", "��"));
//		dataList.add( new Status(R.drawable.icon,"3�� 31��","������", "��"));
//		dataList.add( new Status(R.drawable.icon,"3�� 31��","������", "��"));
//		dataList.add( new Status(R.drawable.icon,"3�� 29��","������", "��"));
//		dataList.add( new Status(R.drawable.icon,"3�� 31��","�迵��", "ī"));
//		dataList.add( new Status(R.drawable.icon,"3�� 31��","������", "��"));
//		dataList.add( new Status(R.drawable.icon,"3�� 31��","������", "��"));
//		return dataList;
//	}
//}