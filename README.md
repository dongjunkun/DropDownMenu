[![](https://jitpack.io/v/dongjunkun/DropDownMenu.svg)](https://jitpack.io/#dongjunkun/DropDownMenu)

## 简介
一个实用的多条件筛选菜单，在很多App上都能看到这个效果，如美团，爱奇艺电影票等

我的博客 [自己造轮子--android常用多条件帅选菜单实现思路（类似美团，爱奇艺电影票下拉菜单）](http://www.jianshu.com/p/d9407f799d2d)

##特色
 - 支持多级菜单
 - 你可以完全自定义你的菜单样式，我这里只是封装了一些实用的方法，Tab的切换效果，菜单显示隐藏效果等
 - 并非用popupWindow实现，无卡顿

##ScreenShot
<img src="https://raw.githubusercontent.com/dongjunkun/DropDownMenu/master/art/simple.gif"/>

<a href="https://raw.githubusercontent.com/dongjunkun/DropDownMenu/master/app/build/outputs/apk/app-debug.apk">Download APK</a>

或者扫描二维码

<img src="https://raw.githubusercontent.com/dongjunkun/DropDownMenu/master/art/download.png"/>

##Gradle Dependency

```
allprojects {
    repositories {
        ...
        maven { url "https://jitpack.io" }
    }
}

dependencies {
    compile 'com.github.dongjunkun:DropDownMenu:1.0.3'
}
```

##使用
添加DropDownMenu 到你的布局文件，如下
```
<com.yyydjk.library.DropDownMenu
    android:id="@+id/dropDownMenu"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    app:ddmenuTextSize="13sp" //tab字体大小
    app:ddtextUnselectedColor="@color/drop_down_unselected" //tab未选中颜色
    app:ddtextSelectedColor="@color/drop_down_selected" //tab选中颜色
    app:dddividerColor="@color/gray"    //分割线颜色
    app:ddunderlineColor="@color/gray"  //下划线颜色
    app:ddmenuSelectedIcon="@mipmap/drop_down_selected_icon" //tab选中状态图标
    app:ddmenuUnselectedIcon="@mipmap/drop_down_unselected_icon"//tab未选中状态图标
    app:ddmaskColor="@color/mask_color"     //遮罩颜色，一般是半透明
    app:ddmenuBackgroundColor="@color/white" //tab 背景颜色
    ...
 />
```
我们只需要在java代码中调用下面的代码

```
 //tabs 所有标题，popupViews  所有菜单，contentView 内容
mDropDownMenu.setDropDownMenu(tabs, popupViews, contentView);
```
如果你要了解更多，可以直接看源码  <a href="https://github.com/dongjunkun/DropDownMenu/blob/master/app/src/main/java/com/yyy/djk/dropdownmenu/MainActivity.java">Example</a>

##关于我
简书[dongjunkun](http://www.jianshu.com/users/f07458c1a8f3/latest_articles)
