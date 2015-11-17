package com.hwc.wavedirection;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.widget.ImageView;

public class CompassView extends ImageView {
	Paint paint;
	int direction = 0;

	public CompassView(Context context) {
		super(context);
		initCompassView();
	}

	public CompassView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initCompassView();
	}

	public CompassView(Context context, AttributeSet attrs, int defaultStyle) {
		super(context, attrs, defaultStyle);
		initCompassView();
	}

	private void initCompassView() {
		paint = new Paint();
		paint.setColor(Color.WHITE);
		paint.setStrokeWidth(2);
		paint.setStyle(Style.STROKE);
		this.setImageResource(R.drawable.compassrose);
	}

	@Override
	public void onDraw(Canvas canvas) {
		int height = this.getHeight();
		int width = this.getWidth();

		canvas.rotate(direction, width / 2, height / 2);
		super.onDraw(canvas);
	}

	public void setDirection(int direction) {
		this.direction = direction;
		this.invalidate();
	}

}
