package com.iceteaviet.fastfoodfinder.ui.custom.ar;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.location.Location;
import android.opengl.Matrix;
import android.view.View;

import com.iceteaviet.fastfoodfinder.data.local.poi.model.AugmentedPOI;
import com.iceteaviet.fastfoodfinder.utils.LocationUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Genius Doan on 20/07/2017.
 */

public class AROverlayView extends View {
    Context context;
    Paint paint;
    float[] cameraCoordinateVector;
    private float[] rotatedProjectionMatrix = new float[16];
    private Location currentLocation;
    private List<AugmentedPOI> arPoints;
    private List<Bitmap> arBitmaps;

    public AROverlayView(Context context) {
        super(context);

        this.context = context;

        //Demo points
        arPoints = new ArrayList<>();
        arBitmaps = new ArrayList<>();
        cameraCoordinateVector = new float[4];

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        paint.setTextSize(60);
    }

    public void setArPoints(List<AugmentedPOI> list) {
        this.arPoints.clear();
        this.arBitmaps.clear();

        this.arPoints.addAll(list);
        for (int i = 0; i < list.size(); i++) {
            arBitmaps.add(BitmapFactory.decodeResource(getResources(), list.get(i).getIcon()));
        }
    }

    public void addArPoint(AugmentedPOI poi) {
        this.arPoints.add(poi);
        arBitmaps.add(BitmapFactory.decodeResource(getResources(), poi.getIcon()));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (currentLocation == null) {
            return;
        }
        for (int i = 0; i < arPoints.size(); i++) {
            float[] currentLocationInECEF = LocationUtils.convertWSG84toECEF(currentLocation);
            float[] pointInECEF = LocationUtils.convertWSG84toECEF(arPoints.get(i).getLocation());
            float[] pointInENU = LocationUtils.convertECEFtoENU(currentLocation, currentLocationInECEF, pointInECEF);

            Matrix.multiplyMV(cameraCoordinateVector, 0, rotatedProjectionMatrix, 0, pointInENU, 0);

            // cameraCoordinateVector[2] is z, that always less than 0 to display on right position
            // if z > 0, the point will display on the opposite
            if (cameraCoordinateVector[2] < 0) {
                float x = (0.5f + cameraCoordinateVector[0] / cameraCoordinateVector[3]) * canvas.getWidth();
                float y = (0.5f - cameraCoordinateVector[1] / cameraCoordinateVector[3]) * canvas.getHeight();

                canvas.drawBitmap(arBitmaps.get(i), x, y, paint);
                canvas.drawText(arPoints.get(i).getName(), x - (30 * arPoints.get(i).getName().length() / 2), y - 80, paint);
            }
        }
    }


    public void destroy() {
        for (int i = 0; i < arBitmaps.size(); i++) {
            if (!arBitmaps.get(i).isRecycled())
                arBitmaps.get(i).recycle();
        }
    }


    public void updateRotatedProjectionMatrix(float[] rotatedProjectionMatrix) {
        this.rotatedProjectionMatrix = rotatedProjectionMatrix;
        this.invalidate();
    }

    public void updateCurrentLocation(Location currentLocation) {
        this.currentLocation = currentLocation;
        this.invalidate();
    }
}
