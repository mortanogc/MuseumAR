//package com.example.museumar.ui.ar;
//
//import android.net.Uri;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.MotionEvent;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.example.museumar.R;
//import com.google.ar.core.Anchor;
//import com.google.ar.core.HitResult;
//import com.google.ar.core.Plane;
//import com.google.ar.sceneform.AnchorNode;
//import com.google.ar.sceneform.rendering.ModelRenderable;
//import com.google.ar.sceneform.ux.ArFragment;
//import com.google.ar.sceneform.ux.TransformableNode;
//
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.OutputStream;
//
//public class ARActivity extends AppCompatActivity {
//    private static final String TAG = "ARActivity";
//
//    private ArFragment arFragment;
//    private ModelRenderable modelRenderable;
//    private String exhibitId;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_ar);
//
//        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.ar_fragment);
//
//        // Получение exhibitId из переданных параметров
//        Bundle extras = getIntent().getExtras();
//        if (extras != null) {
//            exhibitId = extras.getString("exhibitId");
//        }
//
//        // Копирование файла модели из папки assets в директорию getFilesDir()
//        String modelName;
//        if (exhibitId.equals("3")) {
//            modelName = "scene.gltf";
//        } else if (exhibitId.equals("4")) {
//            modelName = "model_3.glb";
//        } else {
//            throw new IllegalArgumentException("Invalid exhibitId");
//        }
//        File modelFile = new File(getFilesDir(), modelName);
//        try {
//            copyAssetToFile(modelName, modelFile);
//        } catch (IOException e) {
//            Log.e(TAG, "Error copying asset to file", e);
//            return;
//        }
//
//        // Получение абсолютного пути к файлу модели
//        String modelPath = modelFile.getAbsolutePath();
//
//        // Загрузка и создание модели
//        ModelRenderable.builder()
//                .setSource(this, Uri.parse(modelPath))
//                .setRegistryId(modelPath)
//                .build()
//                .thenAccept(renderable -> {
//                    modelRenderable = renderable;
//                    setupArFragment();
//                    Log.d(TAG, "Model loaded successfully: " + modelPath);
//                })
//                .exceptionally(
//                        throwable -> {
//                            Log.e(TAG, "Unable to load renderable: " + modelPath, throwable);
//                            return null;
//                        });
//    }
//
//    private void copyAssetToFile(String assetName, File outFile) throws IOException {
//        try (InputStream inputStream = getAssets().open(assetName);
//             OutputStream outputStream = new FileOutputStream(outFile)) {
//            byte[] buffer = new byte[1024];
//            int read;
//            while ((read = inputStream.read(buffer)) != -1) {
//                outputStream.write(buffer, 0, read);
//            }
//            outputStream.flush();
//        }
//    }
//
//    private void setupArFragment() {
//        arFragment.setOnTapArPlaneListener(
//                (HitResult hitResult, Plane plane, MotionEvent motionEvent) -> {
//                    Log.d(TAG, "Plane tapped");
//                    if (modelRenderable == null) {
//                        return;
//                    }
//
//                    // Создание Anchor
//                    Anchor anchor = hitResult.createAnchor();
//                    AnchorNode anchorNode = new AnchorNode(anchor);
//                    anchorNode.setParent(arFragment.getArSceneView().getScene());
//
//                    // Создание и добавление модели в сцену
//                    TransformableNode modelNode = new TransformableNode(arFragment.getTransformationSystem());
//                    modelNode.setParent(anchorNode);
//                    modelNode.setRenderable(modelRenderable);
//                    modelNode.select();
//
//                    // Масштабирование модели
//                    modelNode.getScaleController().setMaxScale(100.0f);
//                    modelNode.getScaleController().setMinScale(0.1f);
//                });
//    }
//}





package com.example.museumar.ui.ar;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;

import androidx.appcompat.app.AppCompatActivity;

import com.example.museumar.R;
import com.google.ar.core.Anchor;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;

public class ARActivity extends AppCompatActivity {
    private static final String TAG = "ARActivity";

    private ArFragment arFragment;
    private ModelRenderable modelRenderable;
    private String exhibitId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ar);

        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.ar_fragment);

        // Получение exhibitId из переданных параметров
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            exhibitId = extras.getString("exhibitId");
        }

        String modelPath;
        if (exhibitId.equals("3")) {
            modelPath = "file:///android_asset/model_1/scene.gltf";
        } else if (exhibitId.equals("4")) {
            modelPath = "file:///android_asset/model_3.glb"; // Заменено на glb-файл
        } else {
            throw new IllegalArgumentException("Invalid exhibitId");
        }


        // Загрузка и создание модели
        ModelRenderable.builder()
                .setSource(this, Uri.parse(modelPath))
                .setRegistryId(modelPath)
                .build()
                .thenAccept(renderable -> {
                    modelRenderable = renderable;
                    setupArFragment();
                    Log.d(TAG, "Model loaded successfully: " + modelPath);
                })
                .exceptionally(
                        throwable -> {
                            Log.e(TAG, "Unable to load renderable: " + modelPath, throwable);
                            return null;
                        });
    }

    private void setupArFragment() {
        arFragment.setOnTapArPlaneListener(
                (HitResult hitResult, Plane plane, MotionEvent motionEvent) -> {
                    Log.d(TAG, "Plane tapped");
                    if (modelRenderable == null) {
                        return;
                    }

                    // Создание Anchor
                    Anchor anchor = hitResult.createAnchor();
                    AnchorNode anchorNode = new AnchorNode(anchor);
                    anchorNode.setParent(arFragment.getArSceneView().getScene());

                    // Создание и добавление модели в сцену
                    TransformableNode modelNode = new TransformableNode(arFragment.getTransformationSystem());
                    modelNode.setParent(anchorNode);
                    modelNode.setRenderable(modelRenderable);
                    modelNode.select();

                    // Масштабирование модели
                    modelNode.getScaleController().setMaxScale(100.0f);
                    modelNode.getScaleController().setMinScale(0.1f);
                });
    }
}


//package com.example.museumar.ui.ar;
//
//import android.net.Uri;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.MotionEvent;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.example.museumar.R;
//import com.google.ar.core.Anchor;
//import com.google.ar.core.HitResult;
//import com.google.ar.core.Plane;
//import com.google.ar.sceneform.AnchorNode;
//import com.google.ar.sceneform.rendering.ModelRenderable;
//import com.google.ar.sceneform.ux.ArFragment;
//import com.google.ar.sceneform.ux.TransformableNode;
//
//public class ARActivity extends AppCompatActivity {
//    private static final String TAG = "ARActivity";
//
//    private ArFragment arFragment;
//    private ModelRenderable modelRenderable;
//    private String exhibitId;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_ar);
//
//        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.ar_fragment);
//
//        // Получение exhibitId из переданных параметров
//        Bundle extras = getIntent().getExtras();
//        if (extras != null) {
//            exhibitId = extras.getString("exhibitId");
//        }
//
//        String modelPath;
//        if (exhibitId.equals("3")) {
//            modelPath = "model_1/scene.gltf";
//        } else if (exhibitId.equals("4")) {
//            modelPath = "model_2/scene.gltf";
//        } else {
//            throw new IllegalArgumentException("Invalid exhibitId");
//        }
//
//        // Загрузка и создание модели
//        ModelRenderable.builder()
//                .setSource(this, Uri.parse(modelPath))
//                .setRegistryId(modelPath)
//                .build()
//                .thenAccept(renderable -> {
//                    modelRenderable = renderable;
//                    setupArFragment();
//                    Log.d(TAG, "Model loaded successfully: " + modelPath);
//                })
//                .exceptionally(
//                        throwable -> {
//                            Log.e(TAG, "Unable to load renderable: " + modelPath, throwable);
//                            return null;
//                        });
//    }
//
//    private void setupArFragment() {
//        arFragment.setOnTapArPlaneListener(
//                (HitResult hitResult, Plane plane, MotionEvent motionEvent) -> {
//                    if (modelRenderable == null) {
//                        return;
//                    }
//
//                    // Создание Anchor
//                    Anchor anchor = hitResult.createAnchor();
//                    AnchorNode anchorNode = new AnchorNode(anchor);
//                    anchorNode.setParent(arFragment.getArSceneView().getScene());
//
//                    // Создание и добавление модели в сцену
//                    TransformableNode modelNode = new TransformableNode(arFragment.getTransformationSystem());
//                    modelNode.setParent(anchorNode);
//                    modelNode.setRenderable(modelRenderable);
//                    modelNode.select();
//
//                    // Масштабирование модели
//                    modelNode.getScaleController().setMaxScale(10.0f);
//                    modelNode.getScaleController().setMinScale(0.1f);
//                });
//    }
//}
//
//
////package com.example.museumar.ui.ar;
////
////import android.net.Uri;
////import android.os.Bundle;
////import android.util.Log;
////import android.view.MotionEvent;
////
////import androidx.appcompat.app.AppCompatActivity;
////
////import com.example.museumar.R;
////import com.google.ar.core.Anchor;
////import com.google.ar.core.HitResult;
////import com.google.ar.core.Plane;
////import com.google.ar.sceneform.AnchorNode;
////import com.google.ar.sceneform.rendering.ModelRenderable;
////import com.google.ar.sceneform.ux.ArFragment;
////import com.google.ar.sceneform.ux.TransformableNode;
////
////public class ARActivity extends AppCompatActivity {
////    private static final String TAG = "ARActivity";
////
////    private ArFragment arFragment;
////    private ModelRenderable modelRenderable;
////    private String exhibitId;
////
////    @Override
////    protected void onCreate(Bundle savedInstanceState) {
////        super.onCreate(savedInstanceState);
////        setContentView(R.layout.activity_ar);
////
////        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.ar_fragment);
////
////        // Получение exhibitId из переданных параметров
////        Bundle extras = getIntent().getExtras();
////        if (extras != null) {
////            exhibitId = extras.getString("exhibitId");
////        }
////
////        String modelPath;
////        if (exhibitId.equals("3")) {
////            modelPath = "model_1/scene.gltf";
////        } else if (exhibitId.equals("4")) {
////            modelPath = "model_2/scene.gltf";
////        } else {
////            throw new IllegalArgumentException("Invalid exhibitId");
////        }
////
////        // Загрузка и создание модели
////        ModelRenderable.builder()
////                .setSource(this, Uri.parse(modelPath))
////                .setRegistryId(modelPath)
////                .build()
////                .thenAccept(renderable -> {
////                    modelRenderable = renderable;
////                    setupArFragment();
////                })
////                .exceptionally(
////                        throwable -> {
////                            Log.e(TAG, "Unable to load renderable", throwable);
////                            return null;
////                        });
////    }
////
////    private void setupArFragment() {
////        arFragment.setOnTapArPlaneListener(
////                (HitResult hitResult, Plane plane, MotionEvent motionEvent) -> {
////                    if (modelRenderable == null) {
////                        return;
////                    }
////
////                    // Создание Anchor
////                    Anchor anchor = hitResult.createAnchor();
////                    AnchorNode anchorNode = new AnchorNode(anchor);
////                    anchorNode.setParent(arFragment.getArSceneView().getScene());
////
////                    // Создание и добавление модели в сцену
////                    TransformableNode modelNode = new TransformableNode(arFragment.getTransformationSystem());
////                    modelNode.setParent(anchorNode);
////                    modelNode.setRenderable(modelRenderable);
////                    modelNode.select();
////                });
////    }
////}
