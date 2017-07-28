package com.liu.imagescaletype;

import android.graphics.Matrix;
import android.graphics.RectF;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    private ImageView img;
    private Matrix imgMatrix;
    private Button changeBtn;
    private Button roation;
    private ImageView.ScaleType scaleType = ImageView.ScaleType.MATRIX;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        img = ((ImageView) findViewById(R.id.img));
        changeBtn = ((Button) findViewById(R.id.oneState));
        changeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scaleType = ImageView.ScaleType.CENTER_CROP;//选择你要变得模式
                initView();
            }
        });
        roation = ((Button) findViewById(R.id.twoState));
        roation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scaleType = ImageView.ScaleType.CENTER_INSIDE;//选择你要变得模式
                initView();
            }
        });
    }
    public void initView(){
        //用matrix 改变图片和位置的时候，必须将ScaleType 设置为MATRIX 类型
        img.setScaleType(ImageView.ScaleType.MATRIX);
        imgMatrix = img.getImageMatrix();
        imgMatrix.reset();
        int viewWidth = img.getWidth();
        int viewHeight = img.getHeight();
        float drawableWidth = img.getDrawable().getIntrinsicWidth();
        float drawableHeight = img.getDrawable().getIntrinsicHeight();
        RectF rectFSrc = new RectF(0,0,drawableWidth,drawableHeight);
        RectF rectfDec = new RectF(0,0,viewWidth,viewHeight);
        float widthScale = viewWidth / drawableWidth;
        float heightScale = viewHeight / drawableHeight;
        switch (this.scaleType){
            //通过代码的方式帮你记住八种模式
            //MATRIX 其实最原始的模式，下面都是在MATRIX上进行变更，MATRIX 会让图片按照原图片大小放置在空间的左上方，超出控件部分直接裁剪掉
            case CENTER_CROP:
                float maxScale = Math.max(widthScale , heightScale);
                imgMatrix.postScale(maxScale , maxScale);
                imgMatrix.postTranslate((viewWidth - drawableWidth * maxScale)/2,(viewHeight - drawableHeight * maxScale )/2);//得按照缩放后的坐标计算
                break;
            case CENTER_INSIDE:
                float minScale = Math.min(1.0f,Math.min(widthScale , heightScale));
                imgMatrix.postScale(minScale , minScale);
                imgMatrix.postTranslate((viewWidth - drawableWidth * minScale)/2,(viewHeight - drawableHeight * minScale )/2);//得按照缩放后的坐标计算
                break;
            case CENTER:
                imgMatrix.postTranslate((viewWidth - drawableWidth) / 2,(viewHeight - drawableHeight) / 2);
                break;
            case FIT_CENTER:
                imgMatrix.setRectToRect(rectFSrc,rectfDec, Matrix.ScaleToFit.CENTER);
                break;
            case FIT_XY:
                imgMatrix.setRectToRect(rectFSrc,rectfDec, Matrix.ScaleToFit.FILL);
                break;
            case FIT_START:
                imgMatrix.setRectToRect(rectFSrc,rectfDec, Matrix.ScaleToFit.START);
                break;
            case FIT_END:
                imgMatrix.setRectToRect(rectFSrc,rectfDec, Matrix.ScaleToFit.END);
                break;
            default:
                break;
        }
        img.setImageMatrix(imgMatrix);
    }
}
