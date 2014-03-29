LOCAL_PATH := $(call my-dir)

OPENCV_INSTALL_MODULES := on
OPENCV_CAMERA_MODULES := off
OPENCV_LIB_TYPE := STATIC
include $(CLEAR_VARS)
include /OpenCV/OpenCV/sdk/native/jni/OpenCV.mk
LOCAL_DEFAULT_CPP_EXTENSION := cpp
LOCAL_MODULE := alggagi
LOCAL_SRC_FILES := dont_touch_alggagi_alggagijni.cpp GameObject.cpp
APP_STL := gnustl_static
APP_CPPFLAGS := -frtti -fexceptions
APP_ABI := armeabi-v7a
APP_PLATFORM := android-9
LOCAL_ARM_NEON := true
include $(BUILD_SHARED_LIBRARY)
