# HorizontalFlowChart
This is a view called HorizontalFlowChart for android application.<br>
You can use it easy to create a horizontal flow chart in you project.<br>

![](https://raw.githubusercontent.com/whichname/HorizontalFlowChart/master/_20150817_230731.JPG)

USING
------
###PREPARE
* If you want to use the HorizontalFlowChart in your android application , you must copy two files`(HorizontalFlowChart.java,attrs.xml)` to you project.<br>
* Add the `namespace` in your layout file.<br>

#####EXAMPLE
```Xml
<LinearLayout 
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:horizontalflowchart="http://schemas.android.com/apk/res/com.example.horizontalflowchart"
    android:layout_width="match_parent"
    android:layout_height="match_parent" 
    android:orientation="vertical"
    >
</LinearLayout>
```


###XML
HorizontalFlowChart provide 15 user-defined attributes,including the loading rate,the text color,the text offset and etc.<br>
#####All the attributes<br>
* `circle_sum`: how many circles this HorizontalFlowChart has
* `big_circle_radius`: the radius of the big circle in this HorizontalFlowChart
* `small_circle_radius`: the radius of the small circle in this HorizontalFlowChart
* `big_line_width`: the length of each line between two circles
  * if you didn't set value for this attribute , HorizontalFlowChart will measure it automatic
* `big_line_height`: the height of each line between two circles
* `small_line_height`: the height of the line which has Loaded between two circles
* `big_color`: the color of the big circle and line
* `small_color`: the color of the small circle and line
* `has_text`: whether this HorizontalFlowChart has text
* `text_color`: the color of the text in HorizontalFlowChart
* `text_size`: the size of the text in HorizontalFlowChart
* `text_gravity`: the gravity of the text in HorizontalFlowChart
  * `BOTTOM`
  * `CENTER`
  * `TOP`
* `text_offset`: the offset of the text in HorizontalFlowChart
* `touch_circle_color`: the color of the circles when the user is touching
* `loading_rate`: the loading rate when this HorizontalFlowChart is loading,it must be an `Integer`

#####EXAMPLE
```Xml
<com.example.horizontalflowchart.HorizontalFlowChartView
        android:id="@+id/horizontalflowchart"
        android:layout_width="match_parent"
        android:layout_height="100dp"
        android:padding="15dp" 
        horizontalflowchart:circle_sum="4"
        horizontalflowchart:big_circle_radius="30dp"
        horizontalflowchart:small_circle_radius="25dp"
        horizontalflowchart:big_line_width="33dp"
        horizontalflowchart:big_line_height="10dp"
        horizontalflowchart:big_color="#c0c0c0"
        horizontalflowchart:small_color="#789262"
        horizontalflowchart:small_line_height="5dp"
        horizontalflowchart:has_text="true"
        horizontalflowchart:text_size="12sp"
        horizontalflowchart:text_color="#424b50"
        horizontalflowchart:text_offset="15dp"
        horizontalflowchart:text_gravity="BOTTOM"
        horizontalflowchart:touch_circle_color="#424b50"
        horizontalflowchart:loading_rate="15"
        />
 ```

###JAVA
In .JAVA file,you can:<br>
set the text of each circle by String[]<br>
set the num of the circle which has Loaded last<br>
set the rate of loading<br>
set OnTouchCircleListener for callback<br>
#####EXAMPLE
```Java
HorizontalFlowChartView horizontalflowchart =(HorizontalFlowChartView) findViewById(R.id.horizontalflowchart);
		horizontalflowchart.setText(new String[]{"初始","开工","收工","回到基地"});
		horizontalflowchart.setNowCircle(2);
		horizontalflowchart.setLoadingRate(15);
		horizontalflowchart.setOnTouchCircleListener(new OnTouchCircleListener() {
			
			@Override
			public void onTouchCircle(int mCircleNum) {
			}
			
		});
```

###INTERFACE
```Java
public interface OnTouchCircleListener {
		public void onTouchCircle(int mCircleNum);//mCircleNum is the num of the circle the user has touched,from 0
	}
```

###If you has any questions or suggestions,please email me 
####:email: wzm625278436@qq.com
<br>
###Thanks for your read!!!
<br>
<br>
##### @author by wuzhiming(whichname)
##### @time : 2015/8/18
