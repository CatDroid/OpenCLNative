#include <jni.h>
#include <string>

#include "refNR.h"
#include "openCLNR.h"

#include <android/bitmap.h>
#include <jni.h>

extern "C" JNIEXPORT jstring JNICALL
Java_com_tom_openclnative_MainActivity_stringFromJNI(
        JNIEnv* env,
        jobject /* this */)
{
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}


extern "C"
JNIEXPORT jint JNICALL
Java_com_tom_openclnative_MainActivity_runOpenCL(JNIEnv *env,
                                                 jclass clazz,
                                                 jstring jKernelPath,
                                                 jobject bitmapIn,
                                                 jobject bitmapOut,
                                                 jintArray info)
{
    void*	bi;
    void*   bo;

    const char* kernelPath = env->GetStringUTFChars(jKernelPath, NULL);
    jint* i = env->GetIntArrayElements(info, NULL);

    AndroidBitmap_lockPixels(env, bitmapIn, &bi);
    AndroidBitmap_lockPixels(env, bitmapOut, &bo);

    openCLNR(kernelPath, (unsigned char *)bi, (unsigned char *)bo, (int *)i);

    AndroidBitmap_unlockPixels(env, bitmapIn);
    AndroidBitmap_unlockPixels(env, bitmapOut);
    env->ReleaseIntArrayElements(info, i, 0);
    env->ReleaseStringUTFChars(jKernelPath, kernelPath);

    return 0;

}

extern "C"
JNIEXPORT jint JNICALL
Java_com_tom_openclnative_MainActivity_runNativeC(JNIEnv *env,
                                                  jclass clazz,
                                                  jobject bitmapIn,
                                                  jobject bitmapOut,
                                                  jintArray info)
{
    void*	bi;
    void*   bo;

    jint* i = env->GetIntArrayElements(info, NULL);

    AndroidBitmap_lockPixels(env, bitmapIn, &bi);
    AndroidBitmap_lockPixels(env, bitmapOut, &bo);

    refNR((unsigned char *)bi, (unsigned char *)bo, (int *)i);

    AndroidBitmap_unlockPixels(env, bitmapIn);
    AndroidBitmap_unlockPixels(env, bitmapOut);
    env->ReleaseIntArrayElements(info, i, 0);

    return 0;
}