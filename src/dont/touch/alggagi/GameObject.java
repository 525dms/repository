package dont.touch.alggagi;

import android.util.Log;

public class GameObject
{
    public int ObjNum;
    public Sprite pattern;
    public float x;
    public float y;
    public float zx;
    public float zy;
    public float scalex;
    public float scaley;
    public int motion;
    public float frame;
    public int frmnum;
    public float angle;
    public int x1;
    public int y1;
    public int x2;
    public int y2;
    public float rx;
    public float ry;
    public boolean show; //화면 출력 여부
    
    public GameObject()
    {
        x = 0.0F;
        y = 0.0F;
        zx = 0.0F;
        zy = 0.0F;
        scalex = 1.0F;
        scaley = 1.0F;
        motion = 0;
        frame = 0.0F;
        frmnum = 0;
        angle = 0.0F;
        x1 = 0;
        y1 = 0;
        x2 = 0;
        y2 = 0;
        rx = 0.0F;
        ry = 0.0F;
        show = true;
    }

    public void Init()
    {
        pattern = null;
        x = 0.0F;
        y = 0.0F;
        zx = 0.0F;
        zy = 0.0F;
        scalex = 1.0F;
        scaley = 1.0F;
        motion = 0;
        frame = 0.0F;
        frmnum = 0;
        angle = 0.0F;
        x1 = 0;
        y1 = 0;
        x2 = 0;
        y2 = 0;
        rx = 0.0F;
        ry = 0.0F;
        show = true;
    }

    public void SetObject(int n, Sprite s_pat, float s_layer, float s_x, float s_y, int s_motion, int s_frame)
    {
        Init();
        pattern = s_pat;
        motion = s_motion;
        frame = s_frame;
        x = s_x;
        y = s_y;
        ObjNum = n;
    }
    public float GetX(){
    	return x;
    }
    
    public float GetY(){
    	return y;
    }
    
    public void SetLocation(float x, float y){
    	this.x = x;
    	this.y = y;
    	Log.d("size","obejct location : " + x + " / "+y);
    }
    
    public void PutSprite(GameInfo info)
    {
        if(pattern == null || motion < 0 || motion >= pattern.TotalMot)
            return;
        int aframe = pattern.Start[motion] + (int)frame;
        if(aframe < 0 || aframe > pattern.TotalAni)
            return;
        int num = pattern.Num[aframe];
        if(num < 0)
            return;
        frmnum = aframe;
        int sx = (int)info.ScrollX;
        int sy = (int)info.ScrollY;
        float ax;
        float ay;

            ax = x + (float)pattern.Sx[aframe];
            ay = y + (float)pattern.Sy[aframe];

        x1 = (int)((ax - zx) + (float)sx);
        y1 = (int)((ay - zy) + (float)sy);
        x2 = (int)((ax + zx + (float)sx + (float)pattern.Xsize[num]) - 1.0F);
        y2 = (int)((ay + zy + (float)sy + (float)pattern.Ysize[num]) - 1.0F);
    }

    public void DrawSprite(GameInfo info)
    {
        pattern.PutAni(info, this);
    }
    
    public boolean CheckPos(int px, int py)
    {
        return px >= x1 && py >= y1 && px <= x2 && py <= y2;
    }

    public int GetFrame(int motion, int frame)
    {
        int num = pattern.Start[motion] + frame;
        return num >= 0 ? pattern.Num[num] : num;
    }
}
