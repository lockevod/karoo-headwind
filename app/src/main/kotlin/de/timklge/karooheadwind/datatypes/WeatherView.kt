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
import androidx.glance.color.ColorProvider
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.ContentScale
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxHeight
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.width
import androidx.glance.layout.wrapContentWidth
import androidx.glance.preview.ExperimentalGlancePreviewApi
import androidx.glance.text.FontFamily
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextAlign
import androidx.glance.text.TextStyle
import de.timklge.karooheadwind.R
import de.timklge.karooheadwind.TemperatureUnit
import de.timklge.karooheadwind.weatherprovider.WeatherInterpretation
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.math.absoluteValue
import kotlin.math.ceil

fun getShortDateFormatter(): DateTimeFormatter = DateTimeFormatter.ofPattern(
    when (Locale.getDefault().country) {
        "US" -> "MM/dd"
        else -> "dd.MM"
    }
).withZone(ZoneId.systemDefault())

fun getWeatherIcon(interpretation: WeatherInterpretation, isNight: Boolean): Int {
    return when (interpretation){
        WeatherInterpretation.CLEAR -> if (isNight) R.drawable.crescent_moon else R.drawable.sun
        WeatherInterpretation.CLOUDY -> R.drawable.cloud
        WeatherInterpretation.RAINY -> R.drawable.cloud_with_rain
        WeatherInterpretation.SNOWY -> R.drawable.cloud_with_snow
        WeatherInterpretation.DRIZZLE -> R.drawable.cloud_with_light_rain
        WeatherInterpretation.THUNDERSTORM -> R.drawable.cloud_with_lightning_and_rain
        WeatherInterpretation.UNKNOWN -> R.drawable.question_mark_regular_240
    }
}

@OptIn(ExperimentalGlancePreviewApi::class)
@Composable
fun Weather(
    arrowBitmap: Bitmap,
    current: WeatherInterpretation,
    windBearing: Int,
    windSpeed: Int,
    windGusts: Int,
    precipitation: Double,
    precipitationProbability: Int?,
    temperature: Int,
    temperatureUnit: TemperatureUnit,
    distance: Double? = null,
    timeLabel: String? = null,
    rowAlignment: Alignment.Horizontal = Alignment.Horizontal.CenterHorizontally,
    dateLabel: String? = null,
    singleDisplay: Boolean = false,
    isImperial: Boolean?,
    isNight: Boolean
) {

    val fontSize = if (singleDisplay) 19f else 14f

    Column(modifier = if (singleDisplay) GlanceModifier.fillMaxSize().padding(1.dp) else GlanceModifier.fillMaxHeight().padding(1.dp).width(86.dp), horizontalAlignment = rowAlignment) {
        Row(modifier = GlanceModifier.defaultWeight().wrapContentWidth(), horizontalAlignment = rowAlignment, verticalAlignment = Alignment.CenterVertically) {
            Image(
                modifier = GlanceModifier.defaultWeight().wrapContentWidth().padding(1.dp),
                provider = ImageProvider(getWeatherIcon(current, isNight)),
                contentDescription = "Current weather information",
                contentScale = ContentScale.Fit,
            )
        }

        if (dateLabel != null && !singleDisplay){
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = dateLabel,
                    style = TextStyle(
                        color = ColorProvider(Color.Black, Color.White),
                        fontFamily = FontFamily.Monospace,
                        fontSize = TextUnit(fontSize, TextUnitType.Sp)
                    )
                )
            }
        }

        if (distance != null && !singleDisplay && isImperial != null){
            val distanceInUserUnit = (distance / (if(!isImperial) 1000.0 else 1609.34)).toInt()
            val label = "${distanceInUserUnit.absoluteValue}${if(!isImperial) "km" else "mi"}"
            val text = if(distanceInUserUnit > 0){
                "In $label"
            } else {
                "$label ago"
            }

            if (distanceInUserUnit != 0){
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = text,
                        style = TextStyle(
                            color = ColorProvider(Color.Black, Color.White),
                            fontFamily = FontFamily.Monospace,
                            fontSize = TextUnit(fontSize, TextUnitType.Sp)
                        )
                    )
                }
            }
        }

        Row(verticalAlignment = Alignment.CenterVertically, horizontalAlignment = rowAlignment) {
            if (timeLabel != null){
                Text(
                    text = timeLabel,
                    style = TextStyle(color = ColorProvider(Color.Black, Color.White), fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace, fontSize = TextUnit(fontSize, TextUnitType.Sp))
                )

                Spacer(modifier = GlanceModifier.width(5.dp))
            }

            Image(
                modifier = if (singleDisplay) GlanceModifier.height(20.dp).width(16.dp) else GlanceModifier.height(16.dp).width(12.dp).padding(1.dp),
                provider = ImageProvider(R.drawable.thermometer),
                contentDescription = "Temperature",
                contentScale = ContentScale.Fit,
            )

            Text(
                text = "${temperature}${temperatureUnit.unitDisplay}",
                style = TextStyle(color = ColorProvider(Color.Black, Color.White), fontFamily = FontFamily.Monospace, fontSize = TextUnit(fontSize, TextUnitType.Sp), textAlign = TextAlign.Center)
            )
        }

        Row(verticalAlignment = Alignment.CenterVertically, horizontalAlignment = rowAlignment, modifier = GlanceModifier.fillMaxWidth()) {
            if (dateLabel != null && singleDisplay){
                Text(
                    text = "$dateLabel",
                    style = TextStyle(color = ColorProvider(Color.Black, Color.White),
                        fontFamily = FontFamily.Monospace, fontSize = TextUnit(fontSize, TextUnitType.Sp))
                )

                Spacer(modifier = GlanceModifier.width(5.dp))
            }

            val precipitationProbabilityLabel = if (precipitationProbability != null) "${precipitationProbability}% " else ""
            Text(
                text = "${precipitationProbabilityLabel}${ceil(precipitation).toInt().coerceIn(0..9)}",
                style = TextStyle(color = ColorProvider(Color.Black, Color.White), fontFamily = FontFamily.Monospace, fontSize = TextUnit(fontSize, TextUnitType.Sp))
            )

            Spacer(modifier = GlanceModifier.width(5.dp))

            Image(
                modifier = if (singleDisplay) GlanceModifier.height(20.dp).width(16.dp) else GlanceModifier.height(16.dp).width(12.dp).padding(1.dp),
                provider = ImageProvider(getArrowBitmapByBearing(arrowBitmap, windBearing + 180)),
                contentDescription = "Current wind direction",
                contentScale = ContentScale.Fit,
                colorFilter = ColorFilter.tint(ColorProvider(Color.Black, Color.White))
            )

            Text(
                text = "$windSpeed,${windGusts}",
                style = TextStyle(color = ColorProvider(Color.Black, Color.White), fontFamily = FontFamily.Monospace, fontSize = TextUnit(fontSize, TextUnitType.Sp))
            )
        }
    }
}