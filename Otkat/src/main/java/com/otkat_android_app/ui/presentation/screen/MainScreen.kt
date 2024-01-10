package com.otkat_android_app.ui.presentation.screen

import android.graphics.Bitmap
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.LinearLayout
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults.buttonColors
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.otkat_android_app.ui.models.ImageState
import com.otkat_android_app.ui.presentation.viewmodel.MainScreenViewModel
import com.otkat_android_app.ui.theme.OtkatBackground
import com.otkat_android_app.ui.theme.OtkatFontFamily
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
                        setBackgroundColor(Color.Black.toArgb())
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
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colorStops = OtkatBackground.mainScreenGradientStops
                )
            ),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 24.dp),
            color = Color.White,
            textAlign = TextAlign.Center,
            text = "Give permission to camera application cant work without it",
            fontFamily = OtkatFontFamily.bigNoodleTooOblique,
            fontSize = 24.sp
        )
        Button(
            modifier = Modifier.padding(top = 12.dp),
            border = BorderStroke(2.dp, Color.White),
            colors = buttonColors(containerColor = Color.Transparent),
            onClick = { requestPermission.invoke() }
        ) {
            Icon(
                modifier = Modifier.padding(end = 12.dp),
                imageVector = Icons.Default.Lock,
                contentDescription = "Camera capture icon"
            )
            Text(
                text = "Request permission",
                fontFamily = OtkatFontFamily.bigNoodleTooOblique,
                fontSize = 24.sp
            )
        }
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
