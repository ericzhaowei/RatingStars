package com.example.trio.tensordemo.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.example.trio.tensordemo.R;

/**
 * Created by zhaowei on 2018/8/13.
 */

public class RatingStars extends View {

    /* attributes */
    private float mSideLength; // 五边形每条边点长度，反映出五角星的大小
    private float mPointAngle; // 每个顶点的角度，反映出五角星的胖瘦程度
    private int mBetweenSpace; // 两个五角星间距
    // 位于1和10之间，否则会报错
    private int mMaxScore; // 最高得分，与星星个数相同，比如最高5分，那么就有5个星星
    private float mScore; // 得分
    private int mBaseColor; // 基础颜色
    private int mScoreColor; // 得分颜色


    private final float mInnerAngle = 360f / 5; // 中心点对应内角和360度，每个内角72度
    private int mStarHeight;
    private int mStarWidth;
    private int mOffsetX; // 以五角星中心点作为原点，相对于安卓坐标系点原点之间点偏移量
    private int mOffsetY;
    private Point[] mOuterPoints; // 五个外顶点坐标
    private Point[] mInnerPoints; // 五个内顶点坐标
    private Paint mPaint;

    public RatingStars(Context context) {
        this(context, null);
    }

    public RatingStars(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.Star);
        mSideLength = ta.getDimensionPixelSize(R.styleable.Star_side_length, 50);
        mPointAngle = ta.getInteger(R.styleable.Star_point_angle, 36);
        mBetweenSpace = ta.getDimensionPixelSize(R.styleable.Star_space, 10);
        mBaseColor = ta.getColor(R.styleable.Star_base_color, 0xffc1c1c1);
        mScoreColor = ta.getColor(R.styleable.Star_score_color, 0xffffdd00);
        mMaxScore = ta.getInteger(R.styleable.Star_max_score, 5);
        if (mMaxScore <= 0 || mMaxScore > 10) {
            throw new RuntimeException("Max score must between 1 and 10.");
        }
        mScore = ta.getFloat(R.styleable.Star_score, 5);
        if (mScore > mMaxScore) {
            throw new RuntimeException("Score is larger than maxScore");
        }
        ta.recycle();

        mOuterPoints = getOuterPoints();
        mInnerPoints = getInnerPoints();
        mStarWidth = mOuterPoints[1].x - mOuterPoints[4].x;
        mStarHeight = mOuterPoints[2].y - mOuterPoints[0].y;

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(0xffc1c1c1);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = mMaxScore * mStarWidth + (mMaxScore - 1) * mBetweenSpace;
        int measureWidth = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);
        int measureHeight = MeasureSpec.makeMeasureSpec(mStarHeight, MeasureSpec.EXACTLY);
        setMeasuredDimension(measureWidth, measureHeight);
    }

    public void setScore(float score) {
        this.mScore = score;
        invalidate();
    }

    private Point[] getOuterPoints() {
        return fivePointCoor(mSideLength, true);
    }

    private Point[] getInnerPoints() {
        int assistLen = Math.abs(mOuterPoints[0].y - mOuterPoints[1].y);
        int sideLength = (int) (2 * assistLen * Math.tan(Math.toRadians(mPointAngle / 2)));
        return fivePointCoor(sideLength, false);
    }

    /*
         * 以最上面的点记为A点，顺时针依次为A,B,C,D,E
         */
    private Point[] fivePointCoor(float sideLength, boolean posDirect) {
        // A点坐标，同时yA也代表原点到顶点度距离
        int xA = 0;
        int yA = (int) ((sideLength / 2) / Math.sin(Math.toRadians(mInnerAngle / 2)));
        Point a = new Point(xA, yA);
        // B点坐标
        int xB = (int) (yA * Math.cos(Math.toRadians(90 - mInnerAngle)));
        int yB = (int) (yA * Math.sin(Math.toRadians(90 - mInnerAngle)));
        Point b = new Point(xB, yB);
        // C点坐标
        int xC = (int) (yA * Math.sin(Math.toRadians(180 - 2 * mInnerAngle)));
        int yC = (int) (-yA * Math.cos(Math.toRadians(180 - 2 * mInnerAngle)));
        Point c = new Point(xC, yC);
        // D点坐标
        int xD = -xC;
        int yD = yC;
        Point d = new Point(xD, yD);
        // E点坐标
        int xE = -xB;
        int yE = yB;
        Point e = new Point(xE, yE);

        if (posDirect) {
            mOffsetX = xB;
            mOffsetY = yA;
        }
        //转换坐标原点为左上角的安卓坐标系
        Point[] points = new Point[]{a, b, c, d, e};
        for (Point p : points) {
            if (!posDirect) {
                p.y = -p.y;
            }
            p.x = p.x + mOffsetX;
            p.y = -(p.y - mOffsetY);
        }

        // 反向重排序
        if (!posDirect) {
            Point[] reversePoints = new Point[] {points[2], points[1], points[0], points[4], points[3]};
            points = reversePoints;
        }

        return points;
    }

    private void drawStar(Canvas canvas, int startX) {
        Path path = new Path();

        path.moveTo(mOuterPoints[0].x + startX, mOuterPoints[0].y);

        for (int i = 0; i < mOuterPoints.length; i++) {

            path.lineTo(mInnerPoints[i].x + startX, mInnerPoints[i].y);
            if (i + 1 < mOuterPoints.length) {
                path.lineTo(mOuterPoints[i+1].x + startX, mOuterPoints[i+1].y);
            }
        }
        path.lineTo(mOuterPoints[0].x + startX, mOuterPoints[0].y);

        // 按A-C-E-B-D的顺序连线，无法调整顶点角度
        // path.lineTo(mOuterPoints[2].x, mOuterPoints[2].y);
        // path.lineTo(mOuterPoints[4].x, mOuterPoints[4].y);
        // path.lineTo(mOuterPoints[1].x, mOuterPoints[1].y);
        // path.lineTo(mOuterPoints[3].x, mOuterPoints[3].y);
        // path.lineTo(mOuterPoints[0].x, mOuterPoints[0].y);
        // path.lineTo(mOuterPoints[2].x, mOuterPoints[2].y);
        canvas.drawPath(path, mPaint);
    }

    private void drawHalfStar(Canvas canvas, int startX, float fraction) {
        Rect rect = new Rect(startX, 0, (int)(startX + mStarWidth * fraction), mStarHeight);
        canvas.clipRect(rect);
        drawStar(canvas, startX);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 绘制基础星星
        mPaint.setColor(mBaseColor);
        for (int i = 0; i < mMaxScore; i++) {
            drawStar(canvas, i * (mStarWidth + mBetweenSpace));
        }

        // 绘制得分星星
        mPaint.setColor(mScoreColor);
        // 满颗星
        for (int i = 0; i <= mScore - 1; i++) {
            drawStar(canvas, i * (mStarWidth + mBetweenSpace));

        }
        // 非满颗
        drawHalfStar(canvas, (int)mScore * (mStarWidth + mBetweenSpace), mScore - (int)mScore);
    }
}
