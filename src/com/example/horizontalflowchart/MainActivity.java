package com.example.horizontalflowchart;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.horizontalflowchart.HorizontalFlowChartView.OnTouchCircleListener;

public class MainActivity extends Activity {
	
	private HorizontalFlowChartView horizontalflowchart;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		horizontalflowchart =(HorizontalFlowChartView) findViewById(R.id.horizontalflowchart);
		horizontalflowchart.setText(new String[]{"初始","开工","收工","回到基地"});
		horizontalflowchart.setOnTouchCircleListener(new OnTouchCircleListener() {
			
			@Override
			public void onTouchCircle(int mCircleNum) {
				
			}
		});
	}

}
