## DropDownMenu

A simple practical multiple condition filter drop down menu,drop down menu style can according to your project fully customization,the repositories just offer a simple framework,not PopupWindow implement,better experience,if you have any question or have new idea about this library,via issues or pull request this library,welcome star or fork！

##Feature
 - Fully customization, you can add any view or viewGroup,do you want do
 - Not PopupWindow implement, better experience effort

##Why create this library?
First of all,this my first library for github,I read a lot awesome library in create this before,but not find i want effect,so i create this.

##ScreenShot
![jj](https://github.com/dongjunkun/MultipleDropDownMenu/blob/master/art/simple.gif)

<a href="https://raw.githubusercontent.com/dongjunkun/DropDownMenu/master/app/build/outputs/apk/app-debug.apk">Download Demo</a>

##Usage
####Step1
add DropDownMenu in your xml
```
<com.yyy.djk.dropdownmenu.DropDownMenu
    android:id="@+id/dropDownMenu"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    app:ddmenuTextSize="13px"
    app:ddtextUnselectedColor="@color/drop_down_unselected"
    app:ddtextSelectedColor="@color/drop_down_selected"
    app:dddividerColor="@color/gray"
    app:ddunderlineColor="@color/gray"
    app:ddmenuSelectedIcon="@mipmap/drop_down_selected_icon"
    app:ddmaskColor="@color/mask_color"
    app:ddmenuBackgroundColor="@color/white"
    app:ddmenuUnselectedIcon="@mipmap/drop_down_unselected_icon"
    app:ddtextPadding="20px"
    tools:context=".MainActivity"/>
```

####Step2
Now we just need invoke DropDownMenu setDropDownMenu(),example below
```
mDropDownMenu.setDropDownMenu(Arrays.asList(headers), popupViews, contentView);
```

You can see <a href="https://github.com/dongjunkun/DropDownMenu/blob/master/app/src/main/java/com/yyy/djk/dropdownmenu/MainActivity.java">Example</a>

##About me
A android developer, like android,like google,like open source,like doing any thing interesting
