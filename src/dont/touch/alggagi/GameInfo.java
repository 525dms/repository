package dont.touch.alggagi;

public class GameInfo
{
    public GameInfo(int x, int y)
    {
        ScrollX = 0.0F;
        ScrollY = 0.0F;
        ScreenX = x;
        ScreenY = y;
        turn = 0;
    }

    public void SetScale()
    {
        ScaleX = ScreenXsize / ScreenX;
        ScaleY = ScreenYsize / ScreenY;
        ScalePx = ScreenX / ScreenXsize;
        ScalePy = ScreenY / ScreenYsize;
    }
    
    public float ScaleX;
    public float ScaleY;
    public float ScalePx;
    public float ScalePy;
    public float ScrollX;
    public float ScrollY;
    public float ScreenXsize;
    public float ScreenYsize;
    public float ScreenX;
    public float ScreenY;
    public int turn;
}
