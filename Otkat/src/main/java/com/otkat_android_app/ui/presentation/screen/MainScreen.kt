package com.otkat_android_app.ui.presentation.screen

import android.graphics.Bitmap
import android.graphics.Color
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.LinearLayout
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Card
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.otkat_android_app.ui.models.ImageState
import com.otkat_android_app.ui.presentation.viewmodel.MainScreenViewModel
import com.otkat_android_app.ui.theme.OtkatTheme

@OptIn(ExperimentalPermissionsApi::class)
@Composable
internal fun MainScreen(
    viewModel: MainScreenViewModel,
    cameraController: LifecycleCameraController,
    requestPermission: () -> Unit,
    capturePhoto: () -> Unit
) {

    val cameraPermission = rememberPermissionState(android.Manifest.permission.CAMERA)

    OtkatTheme {
        if (cameraPermission.status.isGranted) {
            MainScreenContent(viewModel, cameraController, capturePhoto)
        } else {
            RequestPermissionScreenContent(requestPermission)
        }
    }
}

@Composable
private fun MainScreenContent(
    viewModel: MainScreenViewModel,
    cameraController: LifecycleCameraController,
    capturePhoto: () -> Unit
) {
    val cameraState: ImageState by viewModel.imageState.collectAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text(text = "Take photo") },
                onClick = { capturePhoto() },
                icon = {
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = "Camera capture icon"
                    )
                }
            )
        }
    ) { paddingValues: PaddingValues ->

        Box(modifier = Modifier.fillMaxSize()) {
            AndroidView(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                factory = { context ->
                    PreviewView(context).apply {
                        layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)
                        setBackgroundColor(Color.BLACK)
                        implementationMode = PreviewView.ImplementationMode.COMPATIBLE
                        scaleType = PreviewView.ScaleType.FILL_START
                        controller = cameraController
                    }
                }
            )

            cameraState.capturedImage?.let { image ->
                LastPhotoPreview(
                    lastCapturedPhoto = image
                )
            }
        }
    }
}

@Composable
private fun RequestPermissionScreenContent(requestPermission: () -> Unit) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text(text = "Request permission") },
                onClick = { requestPermission.invoke() },
                icon = {
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = "Camera capture icon"
                    )
                }
            )
        }
    ) { paddingValues: PaddingValues ->
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues))
    }
}

@Composable
private fun LastPhotoPreview(
    lastCapturedPhoto: Bitmap
) {

    val capturedPhoto: ImageBitmap =
        remember(lastCapturedPhoto.hashCode()) { lastCapturedPhoto.asImageBitmap() }

    Card(
        modifier = Modifier
            .size(128.dp)
            .padding(16.dp),
        shape = MaterialTheme.shapes.large
    ) {
        Image(
            bitmap = capturedPhoto,
            contentDescription = "Last captured photo",
            contentScale = androidx.compose.ui.layout.ContentScale.Crop
        )
    }
}
