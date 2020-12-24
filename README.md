### 仿照微信九宫格上传图片，也可以只展示图片
1. 先看效果
![在这里插入图片描述](https://img-blog.csdnimg.cn/20201224111803306.gif#pic_center)

2. 项目引入
通过gradle引入
```
implementation 'com.llayjun:ninepic:1.0.0'
```
> 或者直接将类拷贝到项目中，源码见底部

3. 属性


|属性|用途|
|----|----|
|h_space|水平间隔宽度|
|v_space格|竖直间隔高度|
|line_num|水平图片显示个数|
|max_num|图片最大个数|
|add_image|添加图片|
|circle_radius|删除按钮半径|
|is_edit|是否可以编辑（展示仅图片还是可选择）|

4. 项目中使用
xml代码:
```
<TextView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="仅仅图片展示"
            android:textSize="10pt" />

        <com.example.ninepiclibrary.SelectImageView
            android:id="@+id/show_image"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            app:h_space="3pt"
            app:is_edit="false"
            app:line_num="3"
            app:max_num="9"
            app:v_space="3pt">

        </com.example.ninepiclibrary.SelectImageView>

        <TextView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="可新增删除"
            android:textSize="10pt" />

        <com.example.ninepiclibrary.SelectImageView
            android:id="@+id/select_image"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            app:add_image="@mipmap/ic_publish_add_image"
            app:circle_radius="8dp"
            app:h_space="3dp"
            app:is_edit="true"
            app:line_num="3"
            app:max_num="9"
            app:v_space="3dp">

        </com.example.ninepiclibrary.SelectImageView>
```
java代码：
```
 // 图片展示
        val showList = ArrayList<String>()
        showList.add("https://dss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=2583035764,1571388243&fm=26&gp=0.jpg")
        showList.add("https://dss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=3363295869,2467511306&fm=26&gp=0.jpg")
        showList.add("https://dss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=3791918726,2864900975&fm=26&gp=0.jpg")
        showList.add("https://dss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=2583035764,1571388243&fm=26&gp=0.jpg")
        showList.add("https://dss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=3363295869,2467511306&fm=26&gp=0.jpg")
        showList.add("https://dss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=3791918726,2864900975&fm=26&gp=0.jpg")
        showList.add("https://dss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=2583035764,1571388243&fm=26&gp=0.jpg")
        showList.add("https://dss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=3791918726,2864900975&fm=26&gp=0.jpg")
        showList.add("https://dss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=2583035764,1571388243&fm=26&gp=0.jpg")
        show_image.addPhoto(showList)
        show_image.setOnImageClickListener { position, filePath ->
            Toast.makeText(this, "点击图片${position}", Toast.LENGTH_LONG).show()
        }

        // 可增加删除的
        val initList = ArrayList<String>()
        initList.add("https://dss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=2583035764,1571388243&fm=26&gp=0.jpg")
        initList.add("https://dss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=3363295869,2467511306&fm=26&gp=0.jpg")
        initList.add("https://dss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=3791918726,2864900975&fm=26&gp=0.jpg")

        select_image.addPhoto(initList)
        select_image.setOnAddClickListener {
            Toast.makeText(this, "选择照片", Toast.LENGTH_LONG).show()
            val list = ArrayList<String>()
            list.add("https://dss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=3791918726,2864900975&fm=26&gp=0.jpg")
            select_image.addPhoto(list)
        }
        select_image.setOnImageClickListener { position, filePath ->
            Toast.makeText(this, "点击图片${position}", Toast.LENGTH_LONG).show()
        }
```
5. 源代码地址
[九宫格图片展示地址](https://github.com/llayjun/NinePicViewGroup)
