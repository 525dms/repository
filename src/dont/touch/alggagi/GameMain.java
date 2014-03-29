package dont.touch.alggagi;

import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

import dont.touch.alggagi.GameInfo;
import dont.touch.alggagi.GameObject;

import android.content.Context;
import android.util.Log;

public class GameMain
{
	public GL10 mGL = null; // OpenGL 객체
	private Context MainContext;
	public GameInfo gInfo; //게임 정보를 관리하는 개체 Test01로 부터 넘겨받는다
	public float TouchX, TouchY;
	private float mapX, mapY;
	public Sprite Pattern[] = new Sprite[2]; //spr관리 배열
	public static ArrayList<GameObject> GameList = new ArrayList<GameObject> (); // GameObject 바둑알 
	public static alggagijni ajni;
	
	public GameObject Current = null; //선택된 바둑알을 지정할 개체

	public GameMain( Context context, GameInfo info )
	{
		ajni = new alggagijni();
		MainContext = context;
		gInfo = info;
		
		for ( int i = 0; i < Pattern.length; i++ ) Pattern[i] = new Sprite(); //spr 파일을 받기 위한 초기화
	}

	public void LoadGameData()
	{
		mapX = Test01.gInfo.ScreenX;
		mapY = Test01.gInfo.ScreenY;
		Pattern[0].LoadSprite( mGL, MainContext, "stone.spr" ); // stone.spr로딩
		
		ajni.jniLoadGameData(mapX, mapY);
		
		GameObject game; // GameObject 즉, 바둑알
		
		game = new GameObject();
		game.SetObject(0, Pattern[0], 0, mapX/5, mapY/4, 0, 0 ); //검은알
		GameList.add( game ); // 관리 리스트에 추가
		
		game = new GameObject(); 
		game.SetObject(1, Pattern[0], 0, mapX/5, mapY/2, 0, 0 ); //검은알
		GameList.add( game ); // 관리 리스트에 추가
		
		game = new GameObject(); 
		game.SetObject(2, Pattern[0], 0, mapX/5, mapY*3/4, 0, 0 ); //검은알
		GameList.add( game ); // 관리 리스트에 추가
		
		game = new GameObject(); 
		game.SetObject(3, Pattern[0], 0, mapX*4/5, mapY/4, 1, 0 ); //흰알
		GameList.add( game ); // 관리 리스트에 추가
		
		game = new GameObject(); 
		game.SetObject(4, Pattern[0], 0, mapX*4/5, mapY/2, 1, 0 ); //흰알
		GameList.add( game ); // 관리 리스트에 추가
		
		game = new GameObject(); 
		game.SetObject(5, Pattern[0], 0, mapX*4/5, mapY*3/4, 1, 0 ); //흰알
		GameList.add( game ); // 관리 리스트에 추가
		
		/* resolution : 640 480 */
		/*
		Log.d("object","x : "+mapX+" / y : "+mapY);
		game = new GameObject();
		game.SetObject(0, Pattern[0], 0, mapY/4, mapX/4, 0, 0 ); //검은알
		GameList.add( game ); // 관리 리스트에 추가
		
		game = new GameObject(); 
		game.SetObject(1, Pattern[0], 0, mapY/2, mapX/4, 0, 0 ); //검은알
		GameList.add( game ); // 관리 리스트에 추가
		
		game = new GameObject(); 
		game.SetObject(2, Pattern[0], 0, mapY*3/4, mapX/4, 0, 0 ); //검은알
		GameList.add( game ); // 관리 리스트에 추가
		
		game = new GameObject(); 
		game.SetObject(3, Pattern[0], 0, 80, 300, 1, 0 ); //흰알
		GameList.add( game ); // 관리 리스트에 추가
		
		game = new GameObject(); 
		game.SetObject(4, Pattern[0], 0, 160, 300, 1, 0 ); //흰알
		GameList.add( game ); // 관리 리스트에 추가
		
		game = new GameObject(); 
		game.SetObject(5, Pattern[0], 0, 240, 300, 1, 0 ); //흰알
		GameList.add( game ); // 관리 리스트에 추가
		*/
	}
	
	public void SetResoultion(float x, float y){
		// change screen X/Y resolution. 
		float afterX = x;
		float afterY = y;
		float beforeX = gInfo.ScreenX;
		float beforeY = gInfo.ScreenY;
		Log.d("size","before change screen : " + beforeX + " / " + beforeY);
		gInfo.ScreenX = x;
		gInfo.ScreenY = y;
		Log.d("size","after change screen : " + gInfo.ScreenX + " / " + gInfo.ScreenY);
		
		int length=GameList.size();
		Log.d("size","object counter : " + length);
		for(int i = 0 ; i < length ; i ++){
			GameObject o = GameList.get(i);
			o.SetLocation(o.GetY()*(afterY/beforeY),o.GetX()*(afterX/beforeX));
		}
		gInfo.SetScale();
		// change screeon on object X/Y location.
	}
	
	
	public void PushButton()
	{
		for ( int i = 0; i < GameList.size(); i++ ) // 모든 알을 검사한다.
		{
			if ( GameList.get(i).CheckPos((int)TouchX, (int)TouchY) ) // 터치 지점이 현재 알 안에 위치하는지 판단
			{
				if(Test01.gInfo.turn == GameList.get(i).motion)
					Current = GameList.get(i); // Current로 현재 알을 등록
			}
		}
	}
	
	public int CheckEnd()
	{
		int i=0, B=0, W=0;
		for(; i < GameList.size(); i++)
		{
			if(GameList.get(i).motion==0)
			{
				B++;
			}
			else
			{
				W++;
			}
		}
		Log.d("EJ", "While("+W+")black("+B+")");
		if(W==0 && B==0)
		{
			Log.d("EJ", "draw!");
			return 2;
		}
		else if(W==0)
		{
			Log.d("EJ", "Black Win!");
			return 0;
		}
		else if(B==0)
		{
			Log.d("EJ", "White Win!");
			return 1;
		}
		else
		{
			return 3;
		}
	}
	
	public void ChangeNum ()
	{
		for(int i=0; i < GameList.size(); i++)
		{
			GameList.get(i).ObjNum=i;
		}
	}
	public void DoGame()
	{
		CheckEnd();
		
		for ( int i = 0; i < GameList.size(); i++ )
		{
			GameList.get(i).DrawSprite( gInfo );
			
			if(ajni.jniDoGame(i)==1)
			{
				GameList.remove(i);
				ChangeNum();
				return;
			}
			else
			{
				GameList.get(i).x=ajni.jniGetX(i);
				GameList.get(i).y=ajni.jniGetY(i);
				GameList.get(i).angle=ajni.jniGetAngle(i);
			}
		}
	}
}
