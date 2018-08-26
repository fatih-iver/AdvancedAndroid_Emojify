/*
* Copyright (C) 2017 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*  	http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package com.example.android.emojify;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.util.SparseArray;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;

class Emojifier {

    private static final Double SMILING_THRESHOLD  = 0.45;
    private static final Double LEFT_EYE_THRESHOLD  = 0.35;
    private static final Double RIGHT_EYE_THRESHOLD  = 0.35;

    private static final String LOG_TAG = Emojifier.class.getSimpleName();

    /**
     * Method for detecting faces in a bitmap.
     *
     * @param context The application context.
     * @param picture The picture in which to detect the faces.
     */
    static void detectFaces(Context context, Bitmap picture) {

        // Create the face detector, disable tracking and enable classifications
        FaceDetector detector = new FaceDetector.Builder(context)
                .setTrackingEnabled(false)
                .setClassificationType(FaceDetector.ALL_CLASSIFICATIONS)
                .build();

        // Build the frame
        Frame frame = new Frame.Builder().setBitmap(picture).build();

        // Detect the faces
        SparseArray<Face> faces = detector.detect(frame);

        // Log the number of faces
        Log.d(LOG_TAG, "detectFaces: number of faces = " + faces.size());

        // If there are no faces detected, show a Toast message
        if(faces.size() == 0){
            Toast.makeText(context, R.string.no_faces_message, Toast.LENGTH_SHORT).show();
        } else {
            for (int i = 0; i < faces.size(); ++i) {
                Face face = faces.valueAt(i);

                // Log the classification probabilities for each face.
                whichEmoji(face);
                // TODO (6): Change the call to whichEmoji to whichEmoji() to log the appropriate emoji for the facial expression.
            }

        }


        // Release the detector
        detector.release();
    }


    /**
     * Method for logging the classification probabilities.
     *
     * @param face The face to get the classification probabilities.
     */
    private static void whichEmoji(Face face){

        double smilingProbability = face.getIsSmilingProbability();
        double leftEyeOpenProbability = face.getIsLeftEyeOpenProbability();
        double rightEyeOpenProbability = face.getIsRightEyeOpenProbability();

        // Log all the probabilities
        Log.d(LOG_TAG, "whichEmoji: smilingProb = " + smilingProbability);
        Log.d(LOG_TAG, "whichEmoji: leftEyeOpenProb = " + leftEyeOpenProbability);
        Log.d(LOG_TAG, "whichEmoji: rightEyeOpenProb = " + rightEyeOpenProbability);

        boolean isSmiling = smilingProbability > SMILING_THRESHOLD ? true : false;
        boolean isLeftEyeOpen = leftEyeOpenProbability > LEFT_EYE_THRESHOLD ? true : false;
        boolean isRightEyeOpen = rightEyeOpenProbability > RIGHT_EYE_THRESHOLD ? true : false;

        if(isSmiling && isLeftEyeOpen && isRightEyeOpen) {
            Log.d(LOG_TAG, "SMILING");
        } else if(!isSmiling && isLeftEyeOpen && isRightEyeOpen) {
            Log.d(LOG_TAG, "FROWNING");
        } else if(isSmiling && isLeftEyeOpen && !isRightEyeOpen) {
            Log.d(LOG_TAG, "LEFT_WINK");
        } else if(isSmiling && !isLeftEyeOpen && isRightEyeOpen) {
            Log.d(LOG_TAG, "RIGHT_WINK");
        } else if(!isSmiling && isLeftEyeOpen && !isRightEyeOpen) {
            Log.d(LOG_TAG, "LEFT_WINK_FROWNING");
        } else if(!isSmiling && !isLeftEyeOpen && isRightEyeOpen) {
            Log.d(LOG_TAG, "RIGHT_WINK_FROWNING");
        } else if(isSmiling && !isLeftEyeOpen && !isRightEyeOpen) {
            Log.d(LOG_TAG, "CLOSED_EYE_SMILING");
        } else {
            Log.d(LOG_TAG, "CLOSED_EYE_FROWNING");
        }

    }

}
