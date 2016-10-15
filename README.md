[![](https://jitpack.io/v/dongjunkun/DropDownMenu.svg)](https://jitpack.io/#dongjunkun/DropDownMenu)

## 简介
扩展的思路 可以看我的(简书)[http://www.jianshu.com/p/719267a0df32]

##特色
 - 支持多级菜单
 - 你可以完全自定义你的菜单样式，我这里只是封装了一些实用的方法，Tab的切换效果，菜单显示隐藏效果等
 - 并非用popupWindow实现，无卡顿
##本fork项目扩展
 - 支持 tabView的样式扩展
 - 支持手动添加非下拉tabView
##ScreenShot
![Paste_Image.png](https://raw.githubusercontent.com/dongjunkun/DropDownMenu/master/art/simple.gif)

![箭头居中而不是居最右](http://upload-images.jianshu.io/upload_images/1682632-a7d108a623dabb17.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

![可以在tab位置中插入自己需要的tabView](http://upload-images.jianshu.io/upload_images/1682632-54018e2db4c6bc13.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

我们只需要在java代码中调用下面的代码
##扩展的使用
 - 样式扩展
添加名为tab_item.xml到你的布局文件，在要显示内容的TextView上设置id为R.id.tv_tab。tab_item.xml中 任意布局即可。
tab_item.xml
```xml
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="0dp"
    android:layout_height="wrap_content"
    android:layout_weight="1"
    android:gravity="center">
    <TextView
        android:id="@+id/tv_tab"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:drawablePadding="7dp"
        android:gravity="center"
        android:paddingBottom="12dp"
        android:paddingLeft="5dp"
        android:paddingRight="5dp"
        android:paddingTop="12dp"
        android:textColor="#26a8e0" />
</LinearLayout>
```
 - 手动添加非下拉tabView
tab_text.xml设置样式/也可以代码生成，加载控件添加到DropDownMenu中
tab_text
```xml
<TextView
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:drawablePadding="7dp"
    android:gravity="center"
    android:paddingBottom="12dp"
    android:paddingLeft="5dp"
    android:paddingRight="5dp"
    android:paddingTop="12dp"
    android:textColor="#26a8e0"
    xmlns:android="http://schemas.android.com/apk/res/android" />
```
 调用添加,在setDropDownMenu之后添加
```java
...
//init dropdownview
 mDropDownMenu.setDropDownMenu(Arrays.asList(headers), popupViews, contentView);
 //测试tabView扩展功能
 TextView textView= (TextView) getLayoutInflater().inflate(R.layout.tab_text,null);
 textView.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f));
 textView.setText("所有");
 mDropDownMenu.addTab(textView,0);
 textView.setOnClickListener(new View.OnClickListener() {
       @Override
       public void onClick(View view) {
           mDropDownMenu.closeMenu();
       }
   });
```
如果你要了解更多，可以直接看源码  <a href="https://github.com/keyboard3/DropDownMenu/blob/master/app/src/main/java/com/yyy/djk/dropdownmenu/MainActivity.java">Example</a>

##关于fork的我
简书[keyboard3](http://www.jianshu.com/users/62329de8c8a6/latest_articles)
