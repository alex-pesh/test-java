#include <iostream>
#include "natives_NativeTest.h"


JNIEXPORT jstring JNICALL
Java_natives_NativeTest_nativeMethod (JNIEnv *env, jobject, jint int_arg, jstring string_arg) {

    std::cout << "Hello from native! " << std::endl;

    const char* ptr = env->GetStringUTFChars(string_arg, NULL);
    std::string value = "Args: " + std::to_string(int_arg) + " - " + ptr;

    return env->NewStringUTF(value.c_str());
}
