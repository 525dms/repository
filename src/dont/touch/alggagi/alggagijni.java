package dont.touch.alggagi;

public class alggagijni {
	static{
		System.loadLibrary("alggagi");
	}
	
	public native int jniLoadGameData(float x, float y); // return x
	public native int jniReceive(int n, float x1, float y1, float x2, float y2); // return x
	public native int jniDoGame(int i);
	public native float jniGetX(int i);
	public native float jniGetY(int i);
	public native float jniGetAngle(int i);
}