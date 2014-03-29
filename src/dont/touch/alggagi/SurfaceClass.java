package dont.touch.alggagi;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class SurfaceClass implements android.opengl.GLSurfaceView.Renderer
{
	public GameMain sImg;

	public SurfaceClass( GameMain dImg )
	{
		sImg = dImg;
	}
	
	@Override
	public void onSurfaceCreated( GL10 gl, EGLConfig config )
	{
		gl.glMatrixMode( GL10.GL_PROJECTION );
		// projection matrix:3차원 공간상의 물체를 2차원 에 투영시키기 때문에 필요한 matrix
		sImg.mGL = gl;
		sImg.LoadGameData();
	}
	
	@Override
	public void onSurfaceChanged( GL10 gl, int width, int height )
	{
		if ( sImg.gInfo.ScreenX < sImg.gInfo.ScreenY )
		{
			sImg.gInfo.ScreenXsize = height < width ? height : width;
			sImg.gInfo.ScreenYsize = height > width ? height : width;
		}
		else
		{
			sImg.gInfo.ScreenXsize = height > width ? height : width;
			sImg.gInfo.ScreenYsize = height < width ? height : width;
		}
		sImg.gInfo.SetScale();
		
		gl.glOrthof( 0, sImg.gInfo.ScreenXsize, sImg.gInfo.ScreenYsize, 0, 1, -1 );
		gl.glMatrixMode( GL10.GL_MODELVIEW );
		
		gl.glEnable( GL10.GL_TEXTURE_2D );
		gl.glEnableClientState( GL10.GL_VERTEX_ARRAY );
		gl.glEnableClientState( GL10.GL_TEXTURE_COORD_ARRAY );
	}
	
	@Override
	public void onDrawFrame( GL10 gl )
	{
		gl.glClear( GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT );
		gl.glLoadIdentity();
		gl.glScalef( sImg.gInfo.ScreenXsize / sImg.gInfo.ScreenX, sImg.gInfo.ScreenYsize / sImg.gInfo.ScreenY, 1.0f );
		
		sImg.DoGame();
	}
}
