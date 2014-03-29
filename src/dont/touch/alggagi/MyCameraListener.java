package dont.touch.alggagi;

import java.util.ArrayList;
import java.util.List;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfInt;
import org.opencv.core.MatOfInt4;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import android.content.Context;
import android.util.Log;
import android.widget.Button;

public class MyCameraListener implements CvCameraViewListener2{
	public static boolean pushButton = false;
	Context mContext;
	Button btn;
	private static final String TAG = "HandDetector";
	private static final Scalar DETECT_COLOR = new Scalar(0, 0, 255, 255);
	private static final Scalar AREA_COLOR = new Scalar(255, 0, 0, 255);
	private static int temps = 0;
	private MyCameraView mOpenCvCameraView;
	// webV
	private Mat mRgba;
	private Mat mGray;
	private Mat mTemp;
	private Mat mHsv;
	private Mat m32f;
	private Mat mBin;

	private int mWidth, mHeight;
	private int mExposure = 0;
	int one = 0;
	
	private static Scalar LOWER_RANGE;
	private static Scalar UPPER_RANGE;
	private static final float EDGE_ANGLE = 60.0f;
	private Point fp = new Point();
	private enum ViewType {
		RGB, HSV, BLUR, BIN, DIST
	};
	private ViewType mode = ViewType.RGB;
	private float xRate, yRate;
	private Test01 ss;
	private Object temp;
	public MyCameraListener(Context context, MyCameraView base)
	{
		mContext = context;
		mOpenCvCameraView = base;
		mOpenCvCameraView.setMaxFrameSize(800, 480);
		//mOpenCvCameraView.setExposure(0);
		//mOpenCvCameraView.setCameraIndex(MyCameraView.CAMERA_ID_FRONT/*BACK*/);
		mOpenCvCameraView.setCameraIndex(MyCameraView.CAMERA_ID_BACK);
		//mOpenCvCameraView.setRotation(0);
	}
	
	public BaseLoaderCallback  mLoaderCallback = new BaseLoaderCallback(mContext) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                {
                    Log.i(TAG, "OpenCV loaded successfully");
                    mOpenCvCameraView.enableView();
                    mOpenCvCameraView.enableFpsMeter();
                } break;
                default:
                {
                    super.onManagerConnected(status);
                } break;
            }
        }
    };

	public void onCameraViewStarted(int width, int height) {
		String s1 = Integer.toString(width);
		String s2 = Integer.toString(height);
		Log.e("test", s1 + "/" + s2);
		
		mWidth = width;
		mHeight = height;
		xRate = (float)640/mWidth;
		yRate = (float)480/mHeight;
		mGray = new Mat();
		mRgba = new Mat();
		mTemp = new Mat();
		mHsv = new Mat();
		m32f = new Mat();
		mBin = new Mat();
		Log.d("rc", "set exposure = " + mExposure);
		Log.d("rc", ""+width+"/"+height);
		//mOpenCvCameraView.setExposure(mExposure);
		mOpenCvCameraView.setZoom(0);
		//LOWER_RANGE = new Scalar(0, 30, 40);
		LOWER_RANGE = new Scalar(0, 82/*82*/, 0);
		//UPPER_RANGE = new Scalar(30, 190, 228);
		UPPER_RANGE = new Scalar(30, 173, 255);
		
	}

	@Override
	public void onCameraViewStopped() {
		mGray.release();
		mRgba.release();
		mTemp.release();
		mHsv.release();
		m32f.release();
		mBin.release();
	}

	public Test01 sm;
	@Override	
	public Mat onCameraFrame(CvCameraViewFrame inputFrame) {
		mRgba = inputFrame.rgba();
		//mOpenCvCameraView.findFocus();
		
		// if this line return mRgba, fps = 25~30
		if(Test01.sImg.Current != null)				// if we entered this line, fps = fps/2;
		{
			// if this line return mRgba, fps = 8~15
			try {
				// (0) flip around y-axis
				//Core.flip(mRgba, mRgba, 1);
				
				// Core.flip 화면 전환. 이것을 빼면 back \
				// camera의 방향이 정상적으로 돌아온다.
				// 그리고 else 문에서도 Core.flip이 있으니 삭제요망.

				// if this line return mRgba, fps = 8~15
				
				// (1) convert to HSV
				Imgproc.cvtColor(mRgba, mTemp, Imgproc.COLOR_RGBA2RGB);
				Imgproc.cvtColor(mTemp, mHsv, Imgproc.COLOR_RGB2HSV);
				
				// if this line return mRgba, fps = 8~14
				
				//Imgproc.erode(src, dst, kernel);
				//Imgproc.dilate(mRgba , mTemp, mRgba);
				//Imgproc.threshold(src, dst, thresh, maxval, type);
				//after find convexHull.
				// (3) skin color detection
				Core.inRange(mHsv, LOWER_RANGE, UPPER_RANGE, mBin);
				
				
				// (4) distance transform
				Imgproc.distanceTransform(mBin, m32f, Imgproc.CV_DIST_L2, 5);
				Core.convertScaleAbs(m32f, mGray);
				//if(true)
				//	return m32f;
				
				// if return this line, fps = 8~14
				
				// (5) retrieve contours
				Mat hierarchy = new Mat();
				List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
				Imgproc.findContours(mGray, contours, hierarchy,
						2  /* CV_RETR_LIST */, 1 /* CV_CHAIN_APPROX_NONE */);

				
				// (6) find max contour
				double area = 0.0f, maxArea = 0.0f;
				MatOfPoint maxContours = null;
				for (MatOfPoint cont : contours) {
					area = Imgproc.contourArea(cont);
					if (Double.compare(maxArea, area) < 0) {
						maxArea = area;
						maxContours = cont;
					}
				}
				
				// (7) find convex hull
				int edgeCount = 0, fingerCount = 0;
				if ((maxContours != null) && (maxContours.checkVector(2, CvType.CV_32S) > 10)) {
					MatOfInt hull = new MatOfInt();
					MatOfInt4 detectedHull = new MatOfInt4();
					Imgproc.convexHull(maxContours, hull);
					Imgproc.convexityDefects(maxContours, hull, detectedHull);
					double x, y;
					int[] hulls = detectedHull.toArray(); // TODO: Use Matrix!
					for (int i = 0; i < hulls.length ; i += 4) {
						// choose only deeper depth
						if ( hulls[i + 3] > 10000) {
							Point[] points = maxContours.toArray(); // TODO: Use
																	// Matrix!
							for(int j = 0; j < points.length ; j++){
								Core.circle(mRgba, points[j], 1, DETECT_COLOR);
								if(GetDistance((float)points[j].x*xRate, (float)points[j].y*yRate, Test01.sImg.Current.x, Test01.sImg.Current.y) < 25)
								{
									Test01.sImg.ajni.jniReceive(Test01.sImg.Current.ObjNum, (float)points[j].x*xRate, (float)points[j].y*yRate, Test01.sImg.Current.x,
											Test01.sImg.Current.y);
									Test01.sImg.Current = null;
									Test01.sImg.DoGame();
									Test01.gInfo.turn++;		
									if(Test01.gInfo.turn > 1)
										Test01.gInfo.turn = 0;
								}
							}							
						}
					}
				}

				} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else
		{
			//Core.flip(mRgba, mRgba, 1);
		    // Imgproc.resize(mRgba, mRgba , mRgba.size());
		}
		return mRgba;
	}
	
	public void setSize(int width, int height)
	{
		mWidth = width;
		mHeight = height;
		xRate = (float)640/mWidth;
		yRate = (float)480/mHeight;
	}
	
    public float GetDistance( float sx, float sy, float tx, float ty )
    {
		float x = sx - tx;
		float y = sy - ty;
		return (float)Math.sqrt( Math.pow(x, 2) + Math.pow(y, 2));
    }
}