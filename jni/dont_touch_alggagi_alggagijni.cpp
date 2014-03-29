#include "dont_touch_alggagi_alggagijni.h"
#include <cstddef>
#include <cmath>

#include "GameObject.h"

using namespace std;

GameObject* List[6];
float SinTBL[360];
float CosTBL[360];
float Map[2];

void SetFirst(float mx, float my)
{
	Map[0]=mx;
	Map[1]=my;

	for(int i = 0; i < 360; i++)
	{
		SinTBL[i] = (float)sin((double)i * (double)0.017453292499999998); //0도부터 360도까지 sin값 저장
		CosTBL[i] = (float)cos((double)i * (double)0.017453292499999998); //0도부터 360도까지 cos값 저장
	}
	SinTBL[180] = 0;
	CosTBL[270] = 0;
	CosTBL[90] = 0;
}

void RemoveList(int t)
{
	for(; List[t+1]!=NULL; t++)
	{
		List[t]=List[t+1];
	}
	List[t]=NULL;
}

float GetDistance( float sx, float sy, float tx, float ty )
{
	float x = sx - tx;
	float y = sy - ty;

	return (float)sqrt((x*x)+(y*y));
}

double GetAngle(float x, float y, float targetx, float targety)
{
    float distX = targetx - x;
    float distY = targety - y;
    double angle = (atan2(distY, distX) * (double)180) / (double)3.1415926535897931 + (double)90;
    if(angle < (double)0.0)
    {
        angle += (double)360;
    }
    return angle;
}

float CrashAngle(int t, int i)
    {
		float angle1 = (float)((int)(List[t]->angle + 180) % 360);
		float angle2 = (int)GetAngle(List[i]->x, List[i]->y, List[t]->x, List[t]->y);
		float angle3 = (float)((int)((angle1 - angle2) + 720) % 360);

		return (float)((int)((angle2 - angle3) + 720) % 360);
    }

bool CheckAllMarble(int t)
{
	float distance;
	for(int i=0; List[i]!=NULL; i++)
	{
		if(i!=t)
		{
			if((distance = GetDistance(List[i]->x, List[i]->y, List[t]->x, List[t]->y)) <= 50)
			{
				List[t]->angle = (int)CrashAngle(t, i);
				List[i]->angle = (int)GetAngle(List[t]->x, List[t]->y, List[i]->x, List[i]->y);
				List[i]->power = (float)(distance/50)*List[t]->power;
				List[t]->power = (float)sqrt((1 -(distance/50)*(distance/50)))*List[t]->power;
				return true;
			}
		}
	}
	return false;
}

bool CheckWall(int t)
{
	if((GetDistance(List[t]->x, 0, List[t]->x, List[t]->y) <= 25) ||
			(GetDistance(List[t]->x, Map[1]-1, List[t]->x, List[t]->y) <= 25) ||
			(GetDistance(0, List[t]->y, List[t]->x, List[t]->y) <= 25) ||
			(GetDistance(Map[0], List[t]->y, List[t]->x, List[t]->y) <= 25))
	{
		RemoveList(t);
		return true;
	}
	return false;
}

void MovebyAngle(int i)
{
    if(List[i]->angle < 0.0F)
    	List[i]->angle += (float)360;
    if(List[i]->angle >= (float)360)
    	List[i]->angle -= (float)360;
    List[i]->x += SinTBL[(int)List[i]->angle] * (List[i]->power/200);
    List[i]->y -= CosTBL[(int)List[i]->angle] * (List[i]->power/200);
}

JNIEXPORT jint JNICALL Java_dont_touch_alggagi_alggagijni_jniLoadGameData
  (JNIEnv *, jobject, jfloat x, jfloat y)
{
	SetFirst(x, y);

	GameObject* game;

	game = new GameObject(x/5, y/4); // 검 돌
	List[0]=game;

	game = new GameObject(x/5, y/2);  // 검 돌
	List[1]=game;

	game = new GameObject(x/5, y*3/4);  // 검 돌
	List[2]=game;

	game = new GameObject(x*4/5, y/4);  // 흰 돌
	List[3]=game;

	game = new GameObject(x*4/5, y/2);  // 흰 돌
	List[4]=game;

	game = new GameObject(x*4/5, y*3/4);  // 흰 돌
	List[5]=game;

	return 1;
}

JNIEXPORT jint JNICALL Java_dont_touch_alggagi_alggagijni_jniReceive
  (JNIEnv *, jobject, jint n, jfloat x1, jfloat y1, jfloat x2, jfloat y2)
{
	List[n]->angle = (int)GetAngle(x1, y1, x2, y2);
	//List[n]->power = GetDistance(x1, y1, x2, y2)*7;
	List[n]->power = 50000/GetDistance(x1, y1, x2, y2);
	return 1;
}

JNIEXPORT jint JNICALL Java_dont_touch_alggagi_alggagijni_jniDoGame
  (JNIEnv *, jobject, jint i)
{
	int r=0; // 1=remove
	if (List[i]->power > 0)
	{
		MovebyAngle(i);
		CheckAllMarble(i);
		if(!CheckWall(i))
		{
			List[i]->power -= 40;
			if(List[i]->power <= 0)
			{
				List[i]->power = 0;
			}
		}
		else
		{
			r=1;
		}
	}

	return r;
}

JNIEXPORT jfloat JNICALL Java_dont_touch_alggagi_alggagijni_jniGetX
  (JNIEnv *, jobject, jint i)
{
	return List[i]->x;
}

JNIEXPORT jfloat JNICALL Java_dont_touch_alggagi_alggagijni_jniGetY
  (JNIEnv *, jobject, jint i)
{
	return List[i]->y;
}

JNIEXPORT jfloat JNICALL Java_dont_touch_alggagi_alggagijni_jniGetAngle
  (JNIEnv *, jobject, jint i)
{
	return List[i]->angle;
}
