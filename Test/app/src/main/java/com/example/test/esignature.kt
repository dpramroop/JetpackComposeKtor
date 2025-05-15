package com.example.test

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Paint
import android.os.Environment
import android.provider.MediaStore
import android.util.Log

import androidx.compose.foundation.Canvas

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Path


import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.CanvasDrawScope
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.node.Ref
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext

import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import io.ktor.http.ContentDisposition.Companion.File
import org.jetbrains.annotations.TestOnly
import java.io.File
import java.io.FileOutputStream
import kotlin.math.roundToInt


@Composable
fun SignatureBox(
) {

    val lines = remember { mutableStateListOf<Line>() }


    val c = Canvas(modifier = Modifier.fillMaxSize().pointerInput(true) {
        detectDragGestures { change, dragAmount ->
            change.consume()
            val line = Line(
                start = change.position - dragAmount,
                end = change.position
            )
            lines.add(line)
        }
    }) {
        lines.forEach { line ->
            drawLine(
                color = line.color,
                start = line.start,
                end = line.end,
                strokeWidth = line.strokeWidth.toPx(),
                cap = StrokeCap.Round

            )
        }

    }


}


//    Spacer(
//        modifier = Modifier
//            .drawWithCache {
//                val path = Path()
//                path.moveTo(0f, 0f)
//                path.lineTo(size.width / 2f, size.height / 2f)
//                path.lineTo(size.width, 0f)
//                path.close()
//                onDrawBehind {
//                    drawPath(path, Color.Magenta, style = Stroke(width = 10f))
//                }
//            }
//            .fillMaxSize()
//    )

@Composable
fun TestOnl()
{
    val context = LocalContext.current
    SignatureBox2(Modifier,{ bitmap ->
        saveBitmapToFile(context, bitmap)
    })
}


@Composable
fun SignatureBox2(
    modifier: Modifier = Modifier,
    onDrawComplete: (Bitmap) -> Unit = {}
) {

    val lines = remember { mutableStateListOf<Line>() }

    val viewRef = remember { Ref<ComposeView>() }

    Column(modifier) {
        AndroidView(factory = {
            ComposeView(it).apply {
                viewRef.value = this
                setContent {
                    Canvas(
                        modifier = Modifier
                            .fillMaxSize()
                            .pointerInput(true) {
                                detectDragGestures { change, dragAmount ->
                                    change.consume()
                                    lines.add(
                                        Line(
                                            start = change.position - dragAmount,
                                            end = change.position
                                        )
                                    )
                                }
                            }
                    ) {
                        lines.forEach { line ->
                            drawLine(
                                color = line.color,
                                start = line.start,
                                end = line.end,
                                strokeWidth = line.strokeWidth.toPx(),
                                cap = StrokeCap.Round
                            )
                        }
                    }
                }
            }
        }, modifier = Modifier
            .fillMaxHeight(0.8f)
            .fillMaxWidth()
        )

        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = {
                lines.clear()
            }) {
                Text("Clear")
            }

            Button(onClick = {
                val view = viewRef.value
                if (view != null) {
                    val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
                    val canvas = android.graphics.Canvas(bitmap)
                    view.draw(canvas)
                    onDrawComplete(bitmap)
                }
            }) {
                Text("Save")
            }
        }
    }
}

fun saveBitmapToFile(context: Context, bitmap: Bitmap, filename: String = "signature_${System.currentTimeMillis()}.png") {
    val resolver = context.contentResolver
    val contentValues = ContentValues().apply {
        put(MediaStore.Images.Media.DISPLAY_NAME, filename)
        put(MediaStore.Images.Media.MIME_TYPE, "image/png")
        put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/Signatures")
        put(MediaStore.Images.Media.IS_PENDING, 1)
    }

    val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

    uri?.let {
        resolver.openOutputStream(uri)?.use { out ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
        }

        contentValues.clear()
        contentValues.put(MediaStore.Images.Media.IS_PENDING, 0)
        resolver.update(uri, contentValues, null, null)

        Log.d("Signature", "Saved to gallery at $uri")
    } ?: run {
        Log.e("Signature", "Failed to create MediaStore entry.")
    }
}

data class Line(
    val start:Offset,
    val end:Offset,
    val color:Color=Color.Yellow,
    val strokeWidth: Dp =1.dp
)

