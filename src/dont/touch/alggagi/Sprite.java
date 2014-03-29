package dont.touch.alggagi;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;
import javax.microedition.khronos.opengles.GL10;

import dont.touch.alggagi.GameInfo;
import dont.touch.alggagi.GameObject;
import dont.touch.alggagi.Texture;

public class Sprite
{
    private GL10 gl;
    public int Total;
    public int X1[];
    public int Y1[];
    public int X2[];
    public int Y2[];
    public int Xsize[];
    public int Ysize[];
    public int TotalAni;
    public int Num[];
    public int Sx[];
    public int Sy[];
    public int TotalMot;
    public int Count[];
    public int Start[];
    public Texture Tex;
    public String Name;
    public int CXsize[];
    public int CYsize[];
    public byte Crash[][];
    
    public Sprite()
    {
        Total = 0;
        TotalAni = 0;
        TotalMot = 0;
        Tex = new Texture();
        Name = "";
    }

    public boolean LoadSprite(GL10 gc, Context context, String sprite_name)
    {
        gl = gc;
        if(Name.equals(sprite_name))
            return false;
        try
        {
            Name = sprite_name;
            InputStream file = context.getAssets().open(sprite_name);
            byte data[] = new byte[file.available()];
            file.read(data);
            file.close();
            int k = 0;
            for(k = 0; k < 20; k++)
                if(data[k] == 0)
                    break;

            byte text[] = new byte[k];
            System.arraycopy(data, 0, text, 0, k);
            String fname = new String(text);
            if(data.length > 0)
            {
                int idx = 20;
                Total = GetInteger(data, idx);
                idx += 4;
                X1 = new int[Total];
                Y1 = new int[Total];
                X2 = new int[Total];
                Y2 = new int[Total];
                Xsize = new int[Total];
                Ysize = new int[Total];
                for(int i = 0; i < Total; i++)
                {
                    X1[i] = GetInteger(data, idx);
                    idx += 4;
                    Y1[i] = GetInteger(data, idx);
                    idx += 4;
                    X2[i] = GetInteger(data, idx);
                    idx += 4;
                    Y2[i] = GetInteger(data, idx);
                    idx += 4;
                    Xsize[i] = (X2[i] - X1[i]) + 1;
                    Ysize[i] = (Y2[i] - Y1[i]) + 1;
                }

                Tex.LoadTexture(gl, context, fname);
                char str[] = sprite_name.toCharArray();
                for(int i = 0; i < sprite_name.length(); i++)
                {
                    if(str[i] != '.')
                        continue;
                    str[i + 1] = 'a';
                    str[i + 2] = 'n';
                    str[i + 3] = 'i';
                    break;
                }

                String ani = String.copyValueOf(str);
                file = context.getAssets().open(ani);
                data = new byte[file.available()];
                file.read(data);
                file.close();
                if(data.length > 0)
                {
                    idx = 0;
                    TotalAni = GetInteger(data, idx);
                    idx += 4;
                    Num = new int[TotalAni];
                    Sx = new int[TotalAni];
                    Sy = new int[TotalAni];
                    for(int i = 0; i < TotalAni; i++)
                    {
                        Num[i] = GetInteger(data, idx);
                        idx += 4;
                        Sx[i] = GetInteger(data, idx);
                        idx += 4;
                        Sy[i] = GetInteger(data, idx);
                        idx += 4;
                    }

                    TotalMot = GetInteger(data, idx);
                    idx += 4;
                    Count = new int[TotalMot];
                    Start = new int[TotalMot];
                    for(int i = 0; i < TotalMot; i++)
                    {
                        Count[i] = GetInteger(data, idx);
                        idx += 4;
                        Start[i] = GetInteger(data, idx);
                        idx += 4;
                    }

                }
                char cstr[] = sprite_name.toCharArray();
                for(int i = 0; i < sprite_name.length(); i++)
                {
                    if(cstr[i] != '.')
                        continue;
                    cstr[i + 1] = 'c';
                    cstr[i + 2] = 'b';
                    cstr[i + 3] = 'm';
                    break;
                }

                String crash = String.copyValueOf(cstr);
                file = context.getAssets().open(crash);
                data = new byte[file.available()];
                file.read(data);
                file.close();
                if(data.length > 0)
                {
                    idx = 0;
                    idx += 4;
                    CXsize = new int[Total];
                    CYsize = new int[Total];
                    Crash = new byte[Total][];
                    for(int i = 0; i < Total; i++)
                    {
                        CXsize[i] = GetInteger(data, idx);
                        idx += 4;
                        CYsize[i] = GetInteger(data, idx);
                        idx += 4;
                        int size = CXsize[i] * CYsize[i];
                        Crash[i] = new byte[size];
                        System.arraycopy(data, idx, Crash[i], 0, size);
                        idx += size;
                    }

                }
            }
        }
        catch(IOException ioexception) { }
        return true;
    }

    private boolean PutImage(GameInfo info, float x, float y, int frame, GameObject sprite)
    {
        float dx = x;
        float dy = y;
        int xsize = Xsize[frame];
        int ysize = Ysize[frame];
        int aframe = Start[sprite.motion] + (int)sprite.frame;
        sprite.frmnum = frame;
        sprite.x1 = (int)(x - sprite.zx);
        sprite.y1 = (int)(y - sprite.zy);
        sprite.x2 = (int)(x + (float)xsize + sprite.zx);
        sprite.y2 = (int)(y + (float)ysize + sprite.zy);
        if(sprite.show)
        {
            if(sprite.x2 - (int)info.ScrollX < 0 || sprite.y2 - (int)info.ScrollY < 0 || (float)(sprite.x1 - (int)info.ScrollX) >= info.ScreenX || (float)(sprite.y1 - (int)info.ScrollY) >= info.ScreenY)
            	return false;
            dx = (x - (float)(int)info.ScrollX) + sprite.rx;
            dy = (y - (float)(int)info.ScrollY) + sprite.ry;

            Tex.DrawTexture(gl, (int)dx, (int)dy, X1[frame], Y1[frame], xsize, ysize, Sx[aframe], Sy[aframe], sprite.angle, sprite.scalex, sprite.scaley);
        }
        return true;
    }

    public void PutAni(GameInfo info, GameObject sprite)
    {
        if(sprite.motion < 0 || sprite.motion >= TotalMot)
            return;
        int aframe = Start[sprite.motion] + (int)sprite.frame;
        if(aframe < 0 || aframe > TotalAni)
            return;
        int num = Num[aframe];
        if(num < 0 || num >= Total)
            return;
        sprite.frmnum = num;
        float x;
        float y;

        x = sprite.x + (float)Sx[aframe];
        y = sprite.y + (float)Sy[aframe];
 
        PutImage(info, x, y, num, sprite);
    }

    public int PutAni(GameInfo info, int px, int py, int motion, int frame, float sx, float sy, 
            float angle, float trans, int effect)
    {
        GameObject sprite = new GameObject();
        sprite.pattern = this;
        sprite.x = px;
        sprite.y = py;
        sprite.motion = motion;
        sprite.frame = frame;
        sprite.angle = angle;
        if(sprite.motion < 0 || sprite.motion >= TotalMot)
            return -1;
        int aframe = Start[sprite.motion] + (int)sprite.frame;
        if(aframe < 0 || aframe > TotalAni)
            return -1;
        int num = Num[aframe];
        if(num < 0 || num >= Total)
            return -1;
        sprite.frmnum = num;
        float x;
        float y;

            x = sprite.x + (float)Sx[aframe];
            y = sprite.y + (float)Sy[aframe];

        PutImage(info, x, y, num, sprite);
        return sprite.frmnum;
    }

    public int GetInteger(byte data[], int idx)
    {
        int buff[] = new int[4];
        for(int i = 0; i < 4; i++)
            buff[i] = data[idx + i] & 0xff;

        return buff[0] | buff[1] << 8 | buff[2] << 16 | buff[3] << 24;
    }
}
