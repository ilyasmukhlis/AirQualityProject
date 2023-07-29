package com.example.airquality.utils

import android.app.Activity
import android.app.Application
import android.app.Dialog
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Build
import android.util.DisplayMetrics
import android.view.View
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import app.futured.donut.DonutSection
import com.example.airquality.R
import com.example.airquality.utils.language.Lingver
import com.example.airquality.utils.core.CoreActivity
import com.example.airquality.utils.theme.Theme
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.threeten.bp.LocalDate
import java.text.DateFormatSymbols
import java.text.SimpleDateFormat
import java.util.*

/**
 * Collection of extension methods and utils used in project
 */


/**
 * applies several/single actions on multiple views
 */
inline fun <T> applyAction(vararg views: T, action: T.() -> Unit) {
    for (v in views) {
        action.invoke(v)
    }
}

/**
 * creates lazy property or sets to none
 */

inline fun <T> lazyNone(noinline initializer: () -> T): Lazy<T> =
    lazy(LazyThreadSafetyMode.NONE, initializer)

/**
 * observer method
 */

fun <T> observe(liveData: LiveData<T>, viewLifecycleOwner: LifecycleOwner, body: (T) -> Unit) {
    liveData.observe(viewLifecycleOwner, Observer(body))
}

/**
 * time formatter
 */
fun toTime(date: String): Float {
    val time = Regex("(\\d{2}):(\\d{2})").find(date)!!.value
    return time.replace(":", ".").toFloat()
}
/**
 * month formatted
 */
fun toMonth(date: String): Float {
    val time = Regex("-(\\d{2})-(\\d{2})").find(date)!!.value
    return time.split("-")[2].toFloat()
}

/**
 * rounds double to certain decimal point
 */

fun Double.round(decimals: Int): Double {
    var multiplier = 1.0
    repeat(decimals) { multiplier *= 10 }
    return kotlin.math.round(this * multiplier) / multiplier
}

/**
 * prettifies month name
 */

fun String.monthPrettify() = this.lowercase().replaceFirstChar { it.uppercase() }

/**
 * identifies color based on given AQI index
 */

fun identifyColorByAqi(aqi: Int, context: Context): Int {
    return when {
        aqi >= 151 -> ContextCompat.getColor(context,R.color.colorFillRed)
        aqi in 101..150 ->  ContextCompat.getColor(context,R.color.colorFillYellow)
        aqi in 51..100 -> ContextCompat.getColor(context,R.color.colorFillOrange)
        aqi in 31..50 -> ContextCompat.getColor(context,R.color.colorFillSalad)
        else -> ContextCompat.getColor(context,R.color.colorFillGreen)
    }
}
/**
 * identifies status based on given AQI index
 */
fun statusChecker(aqi: Int): Int {
    return when {
        aqi >= 200 -> R.string.hazardous
        aqi in 151..200 -> R.string.very_bad
        aqi in 101..150 -> R.string.bad
        aqi in 61..100 -> R.string.moderate
        aqi in 30..60 -> R.string.good
        else -> R.string.perfect
    }
}

/**
 * Convert date from one pattern to another
 * @param  currentDate
 * @param oldDatePattern
 * @param newDatePattern
 * @return formatted date
 */
fun convertDate(currentDate: String, oldDatePattern: String, newDatePattern: String): String = try {
    val oldDateInFormatting = SimpleDateFormat(oldDatePattern).parse(currentDate)
    val newDateInFormatting = SimpleDateFormat(newDatePattern).format(oldDateInFormatting)
    newDateInFormatting
} catch (ex: Exception) {
    ""
    }

/**
 * Check if the date was yesterday
 * @param date
 */

fun isYesterday(date: LocalDate?) = LocalDate.now().minusDays(1).compareTo(date) == 0

/**
 * Check if the date is today
 * @param date
 */
fun isToday(date: LocalDate?) = LocalDate.now().compareTo(date) == 0

/**
 * identifies colors for Donut based on given AQI index
 */

fun aqiCircleColorify(aqi: Int, context: Context): List<DonutSection> {
    val sections = mutableListOf<DonutSection>()
    when {
        aqi >= 200 -> {
            val cleanAir = DonutSection(
                name = "clean_air",
                color = ContextCompat.getColor(context, R.color.colorFillGreen),
                amount = 30f
            )
            val okAir = DonutSection(
                name = "okAir",
                color = ContextCompat.getColor(context, R.color.colorFillSalad),
                amount = aqi.toFloat() - 30f
            )

            val easyModerate = DonutSection(
                name = "easyModerate",
                color = ContextCompat.getColor(context, R.color.colorFillYellow),
                amount = aqi.toFloat() - 50f
            )
            val heavyModerate = DonutSection(
                name = "heavyModerate",
                color = ContextCompat.getColor(context, R.color.colorFillOrange),
                amount = aqi.toFloat() - 100f
            )
            val heavy = DonutSection(
                name = "heavy",
                color = ContextCompat.getColor(context, R.color.colorFillRedOrange),
                amount = aqi.toFloat() - 150f
            )
            val hazard = DonutSection(
                name = "hazard",
                color = ContextCompat.getColor(context, R.color.colorFillRed),
                amount = aqi.toFloat() - 200f
            )
            sections.add(cleanAir)
            sections.add(okAir)
            sections.add(easyModerate)
            sections.add(heavyModerate)
            sections.add(heavy)
            sections.add(hazard)
        }
        aqi in 151..200 -> {
            val cleanAir = DonutSection(
                name = "clean_air",
                color = ContextCompat.getColor(context, R.color.colorFillGreen),
                amount = 30f
            )
            val okAir = DonutSection(
                name = "okAir",
                color = ContextCompat.getColor(context, R.color.colorFillSalad),
                amount = aqi.toFloat() - 30f
            )

            val easyModerate = DonutSection(
                name = "easyModerate",
                color = ContextCompat.getColor(context, R.color.colorFillYellow),
                amount = aqi.toFloat() - 50f
            )
            val heavyModerate = DonutSection(
                name = "heavyModerate",
                color = ContextCompat.getColor(context, R.color.colorFillOrange),
                amount = aqi.toFloat() - 100f
            )
            val heavy = DonutSection(
                name = "heavy",
                color = ContextCompat.getColor(context, R.color.colorFillRedOrange),
                amount = aqi.toFloat() - 150f
            )
            sections.add(cleanAir)
            sections.add(okAir)
            sections.add(easyModerate)
            sections.add(heavyModerate)
            sections.add(heavy)
        }
        aqi in 101..150 -> {
            val cleanAir = DonutSection(
                name = "clean_air",
                color = ContextCompat.getColor(context, R.color.colorFillGreen),
                amount = 30f
            )
            val okAir = DonutSection(
                name = "okAir",
                color = ContextCompat.getColor(context, R.color.colorFillSalad),
                amount = aqi.toFloat() - 30f
            )

            val easyModerate = DonutSection(
                name = "easyModerate",
                color = ContextCompat.getColor(context, R.color.colorFillYellow),
                amount = aqi.toFloat() - 50f
            )
            val heavyModerate = DonutSection(
                name = "heavyModerate",
                color = ContextCompat.getColor(context, R.color.colorFillOrange),
                amount = aqi.toFloat() - 100f
            )
            sections.add(cleanAir)
            sections.add(okAir)
            sections.add(easyModerate)
            sections.add(heavyModerate)
        }
        aqi in 51..100 -> {

            val cleanAir = DonutSection(
                name = "clean_air",
                color = ContextCompat.getColor(context, R.color.colorFillGreen),
                amount = 30f
            )
            val okAir = DonutSection(
                name = "okAir",
                color = ContextCompat.getColor(context, R.color.colorFillSalad),
                amount = aqi.toFloat() - 30f
            )

            val easyModerate = DonutSection(
                name = "easyModerate",
                color = ContextCompat.getColor(context, R.color.colorFillYellow),
                amount = aqi.toFloat() - 50f
            )

            sections.add(cleanAir)
            sections.add(okAir)
            sections.add(easyModerate)

        }
        aqi in 31..50 -> {
            val cleanAir = DonutSection(
                name = "clean_air",
                color = ContextCompat.getColor(context, R.color.colorFillGreen),
                amount = 30f
            )
            val okAir = DonutSection(
                name = "okAir",
                color = ContextCompat.getColor(context, R.color.colorFillSalad),
                amount = aqi.toFloat() - 30f
            )
            sections.add(cleanAir)
            sections.add(okAir)
        }
        else -> {
            val cleanAir = DonutSection(
                name = "clean_air",
                color = ContextCompat.getColor(context, R.color.colorFillGreen),
                amount = aqi.toFloat()
            )
            sections.add(cleanAir)
        }
    }
    return sections
}

/**
 * Opens BottomSheetDialog as full screen
 * example :
 *  override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
 *       val dialog: Dialog = super.onCreateDialog(savedInstanceState)
 *       return showFullScreen(dialog)
 *    }
 *
 * @param [Dialog] taken from super.onCreateDialog(savedInstanceState)
 */
fun BottomSheetDialogFragment.showFullScreen(dialog: Dialog): Dialog {
    dialog.setOnShowListener { dialogInterface ->
        val bottomSheetDialog = dialogInterface as BottomSheetDialog
        val bottomSheet =
            bottomSheetDialog.findViewById<View>(android.viewbinding.library.R.id.design_bottom_sheet) as FrameLayout
        val behavior: BottomSheetBehavior<*> = BottomSheetBehavior.from(bottomSheet)
        val layoutParams = bottomSheet.layoutParams
        val displayMetrics = DisplayMetrics()
        activity?.windowManager?.defaultDisplay?.getMetrics(displayMetrics)
        val height = displayMetrics.heightPixels
        if (layoutParams != null) {
            layoutParams.height = height
        }
        bottomSheet.layoutParams = layoutParams
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
    }
    return dialog
}

/**
 * returns month as an integer
 */

fun getIntForMonth(month: String): Int {
    var number = 0
    val dfs = DateFormatSymbols()
    val months: Array<String> = dfs.months
    if (month in months) {
        number = months.indexOf(month)
    }
    return number
}

/**
 * changes theme from activity
 * @param theme
 */
fun Activity.setTheme(theme: Theme) {
    (this as? CoreActivity)?.let {
        setTheme(theme, window)
    }
}

/**
 * getting theme from activity
 */
fun Activity.getUITheme(): Theme {
    (this as? CoreActivity)?.let {
        return getUITheme()
    }
    throw Exception("Данная функция недоступна")
}

/**
 * getting Locale/Language
 */

@Suppress("DEPRECATION")
internal fun Configuration.getLocaleCompat(): Locale {
    return if (isAtLeastSdkVersion(Build.VERSION_CODES.N)) locales.get(0) else locale
}

/**
 * checking the sdk version by given code
 */
internal fun isAtLeastSdkVersion(versionCode: Int): Boolean {
    return Build.VERSION.SDK_INT >= versionCode
}

/**
 * resetting the title
 */

internal fun Activity.resetTitle() {
    try {
        val info = packageManager.getActivityInfo(componentName, PackageManager.GET_META_DATA)
        if (info.labelRes != 0) {
            setTitle(info.labelRes)
        }
    } catch (e: PackageManager.NameNotFoundException) {
        e.printStackTrace()
    }
}

/**
 * changes locale from activity
 * @param locale
 */

fun Activity.changeLocale(locale: Locale) {
    Lingver.getInstance().setLocale(this, locale)
    recreate()
}

/**
 * init locale for app
 */
fun Application.initLanguage() {
    Lingver.init(this, Locale.getDefault())
}


/**
 * gets locale from Context
 */

fun Context.getLanguage(): String = Lingver.getInstance().getLocale().language

/**
 * Wrapper class over simple LiveData, to send them as a single event
 */

open class EventWrapper<out T>(private val content: T) {

    var hasBeenHandled = false
        private set

    /**
     * Returns the content and prevents its use again.
     */
    fun get(): T? = if (hasBeenHandled) {
        null
    } else {
        hasBeenHandled = true
        content
    }

    /**
     * Returns the content, even if it's already been handled.
     */
    fun peek(): T = content
}
/**
 * Wrapper class over simple Observer, to receive events
 */
class EventObserver<T>(
    private val onEventUnhandledContent: (T) -> Unit
) : Observer<EventWrapper<T>> {
    override fun onChanged(event: EventWrapper<T>?) {
        event?.get()?.let(onEventUnhandledContent)
    }
}