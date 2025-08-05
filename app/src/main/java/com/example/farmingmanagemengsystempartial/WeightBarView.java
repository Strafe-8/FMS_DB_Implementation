package com.example.farmingmanagemengsystempartial;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class WeightBarView extends View {

    private Paint paint;
    private float averageWeight = 2.5f; // Example average weight
    private float targetWeight = 3.0f; // Target deviation
    private float maxWeight = 5.0f; // Max weight for the bar

    public WeightBarView(Context context) {
        super(context);
        init();
    }

    public WeightBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setAntiAlias(true); // Enable anti-aliasing for smoother edges
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getWidth();
        int height = getHeight();

        // Draw the segmented bar background
        float sectionWidth = width / 4f; // Assuming 4 sections

        // Draw each section with distinct colors
        paint.setColor(0xFFFF0000); // Red for "Below Target"
        canvas.drawRect(0, height / 4, sectionWidth, height * 3 / 4, paint);

        paint.setColor(0xFFFFA500); // Orange for "Needs Attention"
        canvas.drawRect(sectionWidth, height / 4, sectionWidth * 2, height * 3 / 4, paint);

        paint.setColor(0xFFFFFF00); // Yellow for "Doing Okay"
        canvas.drawRect(sectionWidth * 2, height / 4, sectionWidth * 3, height * 3 / 4, paint);

        paint.setColor(0xFF00FF00); // Green for "Doing Well"
        canvas.drawRect(sectionWidth * 3, height / 4, width, height * 3 / 4, paint);

        // Draw the arrow for the average weight
        float arrowPosition = (averageWeight / maxWeight) * width;
        drawArrow(canvas, arrowPosition, height);

        // Draw the target indicator
        float targetBarWidth = (targetWeight / maxWeight) * width;
        paint.setColor(0xFF000000); // Black for target line
        paint.setStrokeWidth(5);
        canvas.drawLine(targetBarWidth, height / 4, targetBarWidth, height * 3 / 4, paint);
    }

    private void drawArrow(Canvas canvas, float x, int height) {
        paint.setColor(0xFF000000); // Color of the arrow
        paint.setStrokeWidth(5);

        // Draw the arrow
        canvas.drawLine(x, height / 4, x, height / 2, paint); // Vertical line
        canvas.drawLine(x - 10, height / 2, x + 10, height / 2, paint); // Horizontal line for arrowhead
        canvas.drawLine(x - 10, height / 2, x, height / 2 - 10, paint); // Left diagonal line
        canvas.drawLine(x + 10, height / 2, x, height / 2 - 10, paint); // Right diagonal line
    }

    public void setAverageWeight(float weight) {
        averageWeight = weight;
        invalidate(); // Redraw view
    }
}