# RatingStars
星星得分

### 使用方法
#### 1.attrs.xml添加可配置项
```
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <declare-styleable name="Star">
        <attr name="side_length" format="dimension" />
        <attr name="point_angle" format="integer" />
        <attr name="space" format="dimension" />
        <attr name="max_score" format="integer" />
        <attr name="score" format="float" />
        <attr name="base_color" format="color" />
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



