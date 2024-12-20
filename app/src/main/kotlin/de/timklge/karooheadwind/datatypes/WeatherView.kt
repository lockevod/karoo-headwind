package de.timklge.karooheadwind.datatypes

import android.graphics.Bitmap
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.glance.ColorFilter
import androidx.glance.GlanceModifier
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.LocalContext
import androidx.glance.color.ColorProvider
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.ContentScale
import androidx.glance.layout.Row
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.height
import androidx.glance.layout.width
import androidx.glance.preview.ExperimentalGlancePreviewApi
import androidx.glance.preview.Preview
import androidx.glance.text.FontFamily
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import de.timklge.karooheadwind.R
import de.timklge.karooheadwind.WeatherInterpretation

fun getWeatherIcon(interpretation: WeatherInterpretation): Int {
    return when (interpretation){
        WeatherInterpretation.CLEAR -> R.drawable.bx_clear
        WeatherInterpretation.CLOUDY -> R.drawable.bx_cloud
        WeatherInterpretation.RAINY -> R.drawable.bx_cloud_rain
        WeatherInterpretation.SNOWY -> R.drawable.bx_cloud_snow
        WeatherInterpretation.DRIZZLE -> R.drawable.bx_cloud_drizzle
        WeatherInterpretation.THUNDERSTORM -> R.drawable.bx_cloud_lightning
        WeatherInterpretation.UNKNOWN -> R.drawable.question_mark_regular_240
    }
}

@OptIn(ExperimentalGlancePreviewApi::class)
@Preview(widthDp = 200, heightDp = 150)
@Composable
fun Weather(baseBitmap: Bitmap, current: WeatherInterpretation, windBearing: Int, windSpeed: Int, windGusts: Int) {
    Column(modifier = GlanceModifier.fillMaxSize(), horizontalAlignment = Alignment.End) {
        Row(modifier = GlanceModifier.defaultWeight(), horizontalAlignment = Alignment.End) {
            val imageW = 70
            val imageH = (imageW * (280.0 / 400)).toInt()
            Image(
                modifier = GlanceModifier.height(imageH.dp).width(imageW.dp),
                provider = ImageProvider(getWeatherIcon(current)),
                contentDescription = "Current weather information",
                contentScale = ContentScale.Fit,
                colorFilter = ColorFilter.tint(ColorProvider(Color.Black, Color.White))
            )
        }

        Row(horizontalAlignment = Alignment.CenterHorizontally, verticalAlignment = Alignment.CenterVertically) {
            Image(
                modifier = GlanceModifier.height(20.dp).width(12.dp),
                provider = ImageProvider(getArrowBitmapByBearing(baseBitmap, windBearing + 180)),
                contentDescription = "Current wind direction",
                contentScale = ContentScale.Fit,
                colorFilter = ColorFilter.tint(ColorProvider(Color.Black, Color.White))
            )

            Text(
                text = "$windSpeed,$windGusts",
                style = TextStyle(color = ColorProvider(Color.Black, Color.White), fontFamily = FontFamily.Monospace, fontSize = TextUnit(18f, TextUnitType.Sp))
            )
        }
    }
}