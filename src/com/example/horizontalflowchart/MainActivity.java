package com.example.horizontalflowchart;

import android.app.Activity;
import android.os.Bundle;

import com.example.horizontalflowchart.HorizontalFlowChartView.OnTouchCircleListener;

public class MainActivity extends Activity {
	
	private HorizontalFlowChartView horizontalflowchart1,horizontalflowchart2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		HorizontalFlowChartView horizontalflowchart =(HorizontalFlowChartView) findViewById(R.id.horizontalflowchart);
		horizontalflowchart.setText(new String[]{"初始","开工","收工","回到基地"});
		horizontalflowchart.setNowCircle(4);
		horizontalflowchart.setLoadingRate(15);
		horizontalflowchart.setOnTouchCircleListener(new OnTouchCircleListener() {
			
			@Override
			public void onTouchCircle(int mCircleNum) {
			}
		});
		
		horizontalflowchart1 = (HorizontalFlowChartView) findViewById(R.id.horizontalflowchart1);
		horizontalflowchart1.setNowCircle(2);
		horizontalflowchart1.setText(new String[]{"开工","收工","回到基地"});
		horizontalflowchart2 = (HorizontalFlowChartView) findViewById(R.id.horizontalflowchart2);
		horizontalflowchart2.setText(new String[]{"初始","开工","收工","回到基地"});
		horizontalflowchart2.setTouchable(false);
		horizontalflowchart2.setNowCircle(2);
	}

}
