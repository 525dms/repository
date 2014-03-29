package dont.touch.alggagi;


import java.util.List;
import java.util.ListIterator;
import org.opencv.android.OpenCVLoader;

import dont.touch.alggagi.GameInfo;
import android.app.Activity;
import android.hardware.Camera.Size;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.Toast;

public class Test01 extends Activity implements OnClickListener
{
	private GLView play;
	public static GameMain sImg;
	public static GameInfo gInfo;
	public static MyCameraListener listener;
	private MyCameraView j;
	private List<Size> mResolutionList;
    private MenuItem[] mEffectMenuItems;
    private SubMenu mColorEffectsMenu;
    private MenuItem[] mResolutionMenuItems;
    private SubMenu mResolutionMenu;
    public Button btn;
	int cnt=0;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.camera);
        setVolumeControlStream( AudioManager.STREAM_MUSIC );
        gInfo = new GameInfo( 640, 480 );
        gInfo.SetScale();
        j = (MyCameraView)findViewById(R.id.java_surface_view);
        listener = new MyCameraListener(getApplicationContext(), j);
        j.setCvCameraViewListener(listener);
        sImg = new GameMain( this, gInfo );
        play = new GLView( this, sImg );
        play.setRenderer( new SurfaceClass(sImg) );
        j.setVisibility(SurfaceView.VISIBLE);
        addContentView(play, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    }
    
    @Override
    public void onResume()
    {
        super.onResume();
        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_6, this, listener.mLoaderCallback);
    }
    @Override
    public void onPause()
    {
        super.onPause();
        if (j != null)
            j.disableView();
    }
    
    public void onDestroy() {
        super.onDestroy();
        if (j != null)
            j.disableView();
    }
    
    public boolean onCreateOptionsMenu(Menu menu) {
        List<String> effects = j.getEffectList();
  
        if (effects == null) {
            return true;
        }

        mColorEffectsMenu = menu.addSubMenu("³ëÃâ");
        mEffectMenuItems = new MenuItem[effects.size()];

        int idx = 0;
        ListIterator<String> effectItr = effects.listIterator();
        while(effectItr.hasNext()) {
           String element = effectItr.next();
           mEffectMenuItems[idx] = mColorEffectsMenu.add(1, idx, Menu.NONE, element);
           idx++;
        }

        mResolutionMenu = menu.addSubMenu("Resolution");
        mResolutionList = j.getResolutionList();
        mResolutionMenuItems = new MenuItem[mResolutionList.size()];

        ListIterator<Size> resolutionItr = mResolutionList.listIterator();
        idx = 0;
        while(resolutionItr.hasNext()) {
            Size element = resolutionItr.next();
            mResolutionMenuItems[idx] = mResolutionMenu.add(2, idx, Menu.NONE,
                    Integer.valueOf(element.width).toString() + "x" + Integer.valueOf(element.height).toString());
            idx++;
        }
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getGroupId() == 1)
        {
            j.setEffect((String) item.getTitle());
            Toast.makeText(this, j.getEffect(), Toast.LENGTH_SHORT).show();
        }
        else if (item.getGroupId() == 2)
        {
        //    float beforeX = gInfo.ScreenX;
        //   float beforeY = gInfo.ScreenY;
        //    int turn = gInfo.turn;
          
       //     Log.d("size","beforeX/Y : "+ beforeX+" / " + beforeY);
        	int id = item.getItemId();
            Size resolution = mResolutionList.get(id);
            j.setResolution(resolution);
            resolution = j.getResolution();
            listener.setSize(resolution.width, resolution.height);
        //    Log.d("size","x : " + resolution.width + " / y : "+ resolution.height);
        //    float afterX = resolution.width;
        //    float afterY = resolution.height;
            

        //	ArrayList<GameObject> getList =  sImg.GameList; // GameObject ¹ÙµÏ¾Ë 
        //    int length = sImg.GameList.size();
        //    for(int i = 0 ; i < length ; i++ ){
        //    	GameObject o = getList.get(i);
        //    	o.SetLocation(o.GetX()*(afterX/beforeX), o.GetY()*(afterY/beforeY));
        //    }
        //    gInfo = new GameInfo( resolution.width, resolution.height );
            //sImg = new GameMain(this, gInfo);
        //    play = new GLView( this, sImg );
        //    gInfo.SetScale();
        //    gInfo.turn = turn; 
            /* Under the line you must write ajni.jniLoadGameData(changeSize, ChangeSize)*/
            //sImg.ajni.jniLoadGameData(afterX, afterY);
        
            // but jniLoadGameData() function is just using gameStart.
            // now we are making a new function.
            // we are taken the Omok Object List and each location.
            // i'm confusing .... help me.
            // why try to change this fucntion? because of each resolution we are changed,
            // try to connect.
            
        }
        return true;
    }

	@Override
	public void onClick(View arg0) {
		if(sImg.Current != null)
			MyCameraListener.pushButton = true;
	}
}