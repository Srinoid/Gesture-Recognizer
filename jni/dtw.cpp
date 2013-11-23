
#include <string.h>
#include <jni.h>
#include <math.h>

extern "C" {

JNIEXPORT jint JNICALL Java_com_dev_nativegesturerecognizer_GestureChecker_DTW(
		JNIEnv* env, jobject thiz, jobject gesture1, jobject gList);
float minimum(float a, float b, float c);
}
;

JNIEXPORT jint JNICALL Java_com_dev_nativegesturerecognizer_GestureChecker_DTW(
		JNIEnv * env, jobject thiz, jobject gesture1, jobject gList) {

	float min = FLT_MAX;
	int index = -1;

	jclass vectorcls = (*env).FindClass("java/util/Vector");
	jmethodID getObjectID = (*env).GetMethodID(vectorcls, "get",
			"(I)Ljava/lang/Object;");
	jmethodID vectorSizeID = (*env).GetMethodID(vectorcls, "size", "()I");

	jclass cls = (*env).FindClass("com/dev/nativegesturerecognizer/Gezture");
	jfieldID datafid; /* store the field ID */

	datafid = (*env).GetFieldID(cls, "data", "Ljava/util/Vector;");
	if (datafid == NULL) {
		return 0;
	}

	jclass pointcls = (*env).FindClass(
			"com/example/dtwgesturerecognizer/Point");
	jfieldID fidx = (*env).GetFieldID(pointcls, "x", "F");
	jfieldID fidy = (*env).GetFieldID(pointcls, "y", "F");
	jfieldID fidz = (*env).GetFieldID(pointcls, "z", "F");

	int lcount1 = (int) (*env).CallIntMethod(gList, vectorSizeID);

	for (int k = 0; k < lcount1; k++) {
		jobject L1 = (*env).CallObjectMethod(gList, getObjectID, k);
		int lcount2 = (int) (*env).CallIntMethod(L1, vectorSizeID);
		for (int l = 0; l < lcount2; l++) {

			jobject L2 = (*env).CallObjectMethod(L1, getObjectID, l);


			int gcount1 = (int) (*env).CallIntMethod(gesture1, vectorSizeID);
			int gcount2 = (int) (*env).CallIntMethod(L2, vectorSizeID);

			float DTW[gcount1][gcount2];

			DTW[0][0] = 0;
			for (int i = 1; i < gcount1; i++)
				DTW[i][0] = FLT_MAX;
			for (int j = 1; j < gcount2; j++)
				DTW[0][j] = FLT_MAX;

			for (int i = 1; i < gcount1; i++) {
				for (int j = 1; j < gcount2; j++) {
					float d = 0;
					jobject p1 = (*env).CallObjectMethod(gesture1, getObjectID,
							i);
					jobject p2 = (*env).CallObjectMethod(L2, getObjectID, j);
					d = d
							+ ((*env).GetFloatField(p1, fidx)
									- (*env).GetFloatField(p2, fidx))
									* ((*env).GetFloatField(p1, fidx)
											- (*env).GetFloatField(p2, fidx));
					d = d
							+ ((*env).GetFloatField(p1, fidy)
									- (*env).GetFloatField(p2, fidy))
									* ((*env).GetFloatField(p1, fidy)
											- (*env).GetFloatField(p2, fidy));
					d = d
							+ ((*env).GetFloatField(p1, fidz)
									- (*env).GetFloatField(p2, fidz))
									* ((*env).GetFloatField(p1, fidz)
											- (*env).GetFloatField(p2, fidz));
					d = sqrt(d);
					DTW[i][j] = d
							+ minimum(DTW[i - 1][j], DTW[i][j - 1],
									DTW[i - 1][j - 1]);
					(*env).DeleteLocalRef(p1);
					(*env).DeleteLocalRef(p2);

				}
			}

			float dist = DTW[gcount1 - 1][gcount2 - 1];
			if (dist < min) {
				index = k;
				min = dist;
			}
			(*env).DeleteLocalRef(L2);
		}
		(*env).DeleteLocalRef(L1);
	}
	return index;

}

float minimum(float f, float g, float h) {

	if (f < g && f < h)
		return f;
	else if (g < h)
		return g;
	else
		return h;
}

