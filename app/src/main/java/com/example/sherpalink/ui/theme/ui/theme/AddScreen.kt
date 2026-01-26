package com.example.sherpalink.screens

import android.annotation.SuppressLint
import android.util.Log
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions
import java.util.concurrent.Executors

@Composable
fun AddScreen() {

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    var detectedText by remember { mutableStateOf("Point camera at mountains") }

    val cameraProviderFuture = remember {
        ProcessCameraProvider.getInstance(context)
    }

    Box(modifier = Modifier.fillMaxSize()) {

        AndroidView(
            factory = { ctx ->
                val previewView = PreviewView(ctx)

                cameraProviderFuture.addListener({

                    val cameraProvider = cameraProviderFuture.get()

                    val preview = Preview.Builder().build().also {
                        it.setSurfaceProvider(previewView.surfaceProvider)
                    }

                    val imageAnalyzer = ImageAnalysis.Builder()
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                        .build()

                    val labeler = ImageLabeling.getClient(
                        ImageLabelerOptions.DEFAULT_OPTIONS
                    )

                    imageAnalyzer.setAnalyzer(
                        Executors.newSingleThreadExecutor()
                    ) { imageProxy ->

                        processImage(
                            imageProxy = imageProxy,
                            labeler = labeler,
                            onResult = { result ->
                                detectedText = result
                            }
                        )
                    }

                    val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                    try {
                        cameraProvider.unbindAll()
                        cameraProvider.bindToLifecycle(
                            lifecycleOwner,
                            cameraSelector,
                            preview,
                            imageAnalyzer
                        )
                    } catch (e: Exception) {
                        Log.e("AddScreen", "Camera error", e)
                    }

                }, ContextCompat.getMainExecutor(ctx))

                previewView
            },
            modifier = Modifier.fillMaxSize()
        )

        // Result Overlay
        Card(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp),
            elevation = CardDefaults.cardElevation(6.dp)
        ) {
            Text(
                text = detectedText,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@SuppressLint("UnsafeOptInUsageError")
private fun processImage(
    imageProxy: ImageProxy,
    labeler: com.google.mlkit.vision.label.ImageLabeler,
    onResult: (String) -> Unit
) {
    val mediaImage = imageProxy.image ?: run {
        imageProxy.close()
        return
    }

    val image = InputImage.fromMediaImage(
        mediaImage,
        imageProxy.imageInfo.rotationDegrees
    )

    labeler.process(image)
        .addOnSuccessListener { labels ->

            val mountainDetected = labels.firstOrNull {
                it.text.contains("Mountain", true) ||
                        it.text.contains("Hill", true) ||
                        it.text.contains("Landscape", true)
            }

            if (mountainDetected != null) {
                onResult("🏔️ Mountain Detected (${mountainDetected.confidence * 100}%)")
            } else {
                onResult("Scanning...")
            }
        }
        .addOnFailureListener {
            onResult("Detection failed")
        }
        .addOnCompleteListener {
            imageProxy.close()
        }
}
