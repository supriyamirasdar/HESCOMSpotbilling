package in.nsoft.hescomspotbilling;

import java.util.List;

import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;

public class CameraPreviewFacial extends ViewGroup implements SurfaceHolder.Callback{
	private final String TAG = CameraPreview.class.getName();
	SurfaceView mSurfaceView;
	SurfaceHolder mHolder;
	Camera mCamera;
	Size mPreviewSize;
	
	public CameraPreviewFacial(Context context, SurfaceView sv) {
		super(context);
		try
		{			
			this.mSurfaceView = sv;
			this.mHolder = this.mSurfaceView.getHolder();
			this.mHolder.addCallback(this);
			this.mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		}
		catch(Exception e)
		{
			Log.d(TAG, e.toString());
		}
	}

	public void setCamera(Camera camera)
	{
		try
		{
			mCamera = camera;			
			if(mCamera != null)
			{
				//mSupportedPreviewSizes = mCamera.getParameters().getSupportedPreviewSizes();
				requestLayout();
				Camera.Parameters params = mCamera.getParameters();

				List<String> focusModes = params.getSupportedFocusModes();
				if(focusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO))
				{
					params.setFlashMode(Camera.Parameters.FOCUS_MODE_AUTO);
					mCamera.setParameters(params);
				}				
			}
		}
		catch(Exception e)
		{

		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		/*final int width = resolveSize(getSuggestedMinimumWidth(), widthMeasureSpec);
		final int height = resolveSize(getSuggestedMinimumHeight(), heightMeasureSpec);
		setMeasuredDimension(width, height);
		if(mSupportedPreviewSizes != null)
		{
			mPreviewSize = getOptimalPreviewSize(mSupportedPreviewSizes, width, height);
		}*/
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {


	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
		try
		{
			if(mCamera != null)
			{
				
				Camera.Parameters parameters = mCamera.getParameters();
				parameters.setPreviewSize(ConstantClass.width, ConstantClass.height);//(mPreviewSize.width, mPreviewSize.height);
				parameters.setPictureSize(ConstantClass.width, ConstantClass.height);		
				parameters.setRotation(270);
				
				//parameters.setPreviewSize(ConstantClass.height, ConstantClass.width);//(mPreviewSize.width, mPreviewSize.height);
				//parameters.setPictureSize(ConstantClass.height, ConstantClass.width);	
				
				parameters.setJpegQuality(100);
				requestLayout();
				mCamera.setParameters(parameters);			
				mCamera.startPreview();
				
			}	
		}
		catch(Exception e)
		{

		}
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		try
		{
			if(mCamera != null)
			{
				mCamera.setPreviewDisplay(holder);
			}
		}
		catch(Exception e)
		{
			Log.d(TAG, e.toString());
		}

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
		// TODO Auto-generated method stub
		try
		{
			if(mCamera != null)
			{
				mCamera.stopPreview();
			}
		}
		catch(Exception e)
		{
			Log.d(TAG, e.toString());
		}
	}
}
