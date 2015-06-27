# MultipleDropDownMenu

A simple practical multiple condition filter drop down menu,drop down menu style can according to your project fully costomization,the repositories just ofer a simple framework,not popuwindow implement,better experience,if you have any question or have new idea about this library,via issues or pull request this library,welcome star or fork！

##Feature
 - Fully customization, you can add any view or viewGroup,do you want do
 - Not popuwinw implement, better experience effort

##Why create this library?
First of all,this my first library for github,I read a lot awesome library in create this before,but not find i want effect,so i create this.

##Screenshot
![jj](https://github.com/dongjunkun/MultipleDropDownMenu/blob/master/art/simaple.gif)

<a href="https://raw.githubusercontent.com/dongjunkun/DropDownMenu/master/app/build/outputs/apk/app-debug.apk">download demo</a>

##Usage
####step1
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

####step2
now we just need invoke DropDownMenu setDropDownMenu() method then finish that params
```
 private String headers[] = {"武汉", "不限年龄", "不限性别"};
    private List<View> popuViews = new ArrayList<>();

 //内容
        TextView contentView = new TextView(this);
        contentView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        contentView.setText("内容显示区域");
        contentView.setGravity(Gravity.CENTER);
        contentView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        mDropDownMenu.setDropDownMenu(Arrays.asList(headers), popuViews, contentView);
```
if you want when menu is showing preess back key close menu not close this activity,you can add below code
```
 @Override
    public void onBackPressed() {
        if (mDropDownMenu.isShowing()) {
            mDropDownMenu.closeMenu();
        } else {
            super.onBackPressed();
        }
    }
```
in you activity

##About me
A android developer, like android,like life, like creative,like open source,like doing something interesting
