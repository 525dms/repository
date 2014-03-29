package dont.touch.alggagi;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.*;
import android.opengl.GLUtils;
import java.io.IOException;
import java.io.InputStream;
import java.nio.*;
import javax.microedition.khronos.opengles.GL10;

public class Texture
{
    private FloatBuffer vertexBuffer;
    private FloatBuffer textureBuffer;
    public int textures[];
    private Bitmap bitmap;
    public int imgWidth;
    public int imgHeight;
    public int makeWidth;
    public int makeHeight;
    
    public Texture()
    {
        textures = new int[1];
        bitmap = null;
        imgWidth = 0;
        imgHeight = 0;
        ByteBuffer byteBuf = ByteBuffer.allocateDirect(48);
        byteBuf.order(ByteOrder.nativeOrder());
        vertexBuffer = byteBuf.asFloatBuffer();
        byteBuf = ByteBuffer.allocateDirect(32);
        byteBuf.order(ByteOrder.nativeOrder());
        textureBuffer = byteBuf.asFloatBuffer();
    }

    public void DrawTexture(GL10 gl, float x, float y, float sx, float sy, float w, float h, 
            float rx, float ry, float angle, float scalex, float scaley)
    {
        float x1 = x;
        float y1 = y;
        float x2 = x + w;
        float y2 = y + h;
        float width = w;
        float height = h;
        float vertices[] = {
            x1, y2, 0.0F, x2, y2, 0.0F, x1, y1, 0.0F, x2, 
            y1, 0.0F
        };
        x1 = sx / (float)makeWidth;
        y1 = sy / (float)makeHeight;
        x2 = (sx + width) / (float)makeWidth;
        y2 = (sy + height) / (float)makeHeight;
        float texture[] = {
            x1, y2, x2, y2, x1, y1, x2, y1
        };
        vertexBuffer.put(vertices);
        vertexBuffer.position(0);
        textureBuffer.put(texture);
        textureBuffer.position(0);
        if(angle != 0.0F || scalex != 1.0F || scaley != 1.0F)
        {
            gl.glPushMatrix();
            gl.glTranslatef(x - rx, y - ry, 0.0F);
            gl.glRotatef(angle, 0.0F, 0.0F, 1.0F);
            gl.glScalef(scalex, scaley, 1.0F);
            gl.glTranslatef(-(x - rx), -(y - ry), 0.0F);
        }
        gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[0]);
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
        gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBuffer);
        gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
        if(angle != 0.0F || scalex != 1.0F || scaley != 1.0F)
            gl.glPopMatrix();
    }

    private Bitmap getBitmapFromAsset(Context context, String strName)
    {
        Bitmap bitmap = null;
        try
        {
            AssetManager assetManager = context.getAssets();
            InputStream istr = assetManager.open(strName);
            bitmap = BitmapFactory.decodeStream(istr);
        }
        catch(IOException ioexception) { }
        return bitmap;
    }

    public void LoadTexture(GL10 gl, Context context, String fname)
    {
        if(bitmap != null)
        {
            bitmap.recycle();
            bitmap = null;
        }
        bitmap = getBitmapFromAsset(context, fname);
        gl.glDeleteTextures(1, textures, 0);
        imgWidth = bitmap.getWidth();
        imgHeight = bitmap.getHeight();
        makeWidth = 2;
        makeHeight = 2;
        for(; makeWidth < imgWidth; makeWidth *= 2);
        for(; makeHeight < imgHeight; makeHeight *= 2);
        Bitmap bmp2 = Bitmap.createBitmap(makeWidth, makeHeight, android.graphics.Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmp2);
        canvas.drawBitmap(bitmap, new Rect(0, 0, imgWidth, imgHeight), new Rect(0, 0, imgWidth, imgHeight), null);
        gl.glGenTextures(1, textures, 0);
        gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[0]);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
        //GL_TEXTURE_MIN_FILTER : 축소 필터
        //GL_NEAREST : 인접 축소 필터로, 가장 근접한 텍셀이 색상을 사용한다. 단순하고 거칠게 표현된다.
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
        //GL_TEXTURE_MAG_FILTER : 확대 필터
        //GL_LINEAR 양방향 선형(bilinear) 필터링 알고리즘으로, 텍셀에서 인접한 2x2 점을 샘플링하여 가중치 평균값을 구한다.
        GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bmp2, 0);
        bitmap.recycle();
        bmp2.recycle();
        bitmap = null;
        bmp2 = null;
        canvas = null;
    }
}
