# MultipleDropDownMenu

一个简单实用的多条件筛选菜单，菜单显示样式可根据自己的项目需要进行自定义，不仅仅只是listview，任何你想要的View或者ViewGroup都可以添加，我后期也会抽时间写一些比较常用的模板供大家快速集成，欢迎大家fork or star。

#我为什么要写这个菜单？
首先这是我第一次上传代码到github，在这之前我浏览了无数大牛写的库，本着开源的思想也想做出自己一点微薄的贡献。在写这个库之前也找过一些类似的，如<a href="https://github.com/JayFang1993/DropDownMenu">JayFrang1993 DropDownMenu</a>用popuWindow实现，菜单切换不是很流畅，所以决定写这个。


![jj](https://github.com/dongjunkun/MultipleDropDownMenu/blob/master/art/simaple.gif)

体验地址 <a href="https://raw.githubusercontent.com/dongjunkun/DropDownMenu/master/app/build/outputs/apk/app-debug.apk">demo apk</a>

#用法

布局
<com.yyy.djk.dropdownmenu.DropDownMenu
    android:id="@+id/dropDownMenu"
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    tools:context=".MainActivity"/>
    
    
#特色
1、并非用popuWindow实现，切换效果更流畅

2、使用简单，高度扩展
