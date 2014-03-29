package dont.touch.alggagi;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PixelFormat;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

@SuppressLint("ViewConstructor")
public class GLView extends GLSurfaceView
{
	Context mContext;	
	public GameMain sImg;
	
	public GLView( Context context, GameMain img )
	{
		super( context );
		setFocusable( true );

		mContext = context;
		sImg = img;
		setEGLConfigChooser(8, 8, 8, 8, 16, 0);
		getHolder().setFormat(PixelFormat.RGBA_8888);
		setZOrderOnTop(true);
	}
	public void SetGLView(GameMain img){
		//super(context);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event)
    {
		final int action = event.getAction();
		
		synchronized ( sImg.mGL )
		{
			sImg.TouchX = event.getX() * sImg.gInfo.ScalePx;
			sImg.TouchY = event.getY() * sImg.gInfo.ScalePy;
			switch ( action & MotionEvent.ACTION_MASK )
			{
				case	MotionEvent.ACTION_DOWN	:
				case	MotionEvent.ACTION_MOVE :
				case	MotionEvent.ACTION_POINTER_DOWN	:
						{
							sImg.PushButton();
						}
						break;
			}
		}
      	return true;
    }
}
