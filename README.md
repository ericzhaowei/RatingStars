# RatingStars
星星得分

### 使用方法
#### 1.attrs.xml添加可配置项
```
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <declare-styleable name="Star">
        <!-- 五角星所在五边形的边长，代表五角星的大小 -->
        <attr name="side_length" format="dimension" />
        <!-- 五角星顶点角度，代表五角星胖瘦 -->
        <attr name="point_angle" format="integer" />
        <!-- 两个五角星的间距 -->
        <attr name="space" format="dimension" />
        <!-- 最高得分，最少1分，最多10分 -->
        <attr name="max_score" format="integer" />
        <!-- 当前得分 -->
        <attr name="score" format="float" />
        <!-- 基础颜色 -->
        <attr name="base_color" format="color" />
        <!-- 得分颜色 -->
        <attr name="score_color" format="color" />
    </declare-styleable>
</resources>
```

#### 2.layout中使用
```
<com.example.zhaowei.ertest.RatingStars
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        app:side_length="10dp"
        app:point_angle="45"
        app:max_score="5"
        app:score="4.5"
        app:base_color="#dadada"
        app:score_color="#f9d804"
        android:layout_centerInParent="true"/>
```



