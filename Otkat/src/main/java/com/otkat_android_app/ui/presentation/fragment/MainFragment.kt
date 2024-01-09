package com.otkat_android_app.ui.presentation.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.camera.view.LifecycleCameraController
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.getMainExecutor
import androidx.core.view.doOnLayout
import androidx.fragment.app.Fragment
import com.otkat_android_app.ui.domain.OnImageCapturedCallback
import com.otkat_android_app.ui.presentation.screen.MainScreen
import com.otkat_android_app.ui.presentation.viewmodel.MainScreenViewModel

class MainFragment : Fragment() {

    private val composeView by lazy { ComposeView(requireContext()) }
    private lateinit var viewModel: MainScreenViewModel
    private lateinit var cameraController: LifecycleCameraController

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        context?.let {
            cameraController = LifecycleCameraController(it)
            cameraController.bindToLifecycle(viewLifecycleOwner)
        }

        return composeView.apply {
            setViewCompositionStrategy(
                ViewCompositionStrategy.DisposeOnLifecycleDestroyed(
                    viewLifecycleOwner
                )
            )
            setContent {
                MainScreen(
                    MainScreenViewModel(),
                    cameraController,
                    { showCameraPermissionRationale() },
                    { capturePhoto() }
                )
            }
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.doOnLayout {
            composeView.layoutParams.height = view.height
            composeView.requestLayout()
        }
    }

    private fun capturePhoto() {
        context?.let {
            cameraController.takePicture(
                getMainExecutor(it),
                OnImageCapturedCallback(viewModel::storePhotoInGallery)
            )
        }
    }

    private fun showCameraPermissionRationale() {
        activity?.let {
            val isRationale = it.shouldShowRequestPermissionRationale(
                android.Manifest.permission.CAMERA
            )

            if (isRationale) {
                ActivityCompat.requestPermissions(
                    it,
                    arrayOf(android.Manifest.permission.CAMERA),
                    PERMISSIONS_REQUEST_CODE
                )
            } else {
                Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    .setData(Uri.fromParts(PACKAGE, activity?.packageName, null))
                    .addCategory(Intent.CATEGORY_DEFAULT)
                    .let(::startActivity)
            }
        }
    }


    companion object {
        const val NAV_GRAPH_NAME = "main_fragment"
        const val NAV_GRAPH_LABEL = "MainFragment"
        private const val PERMISSIONS_REQUEST_CODE = 1
        private const val PACKAGE = "package"
    }
}