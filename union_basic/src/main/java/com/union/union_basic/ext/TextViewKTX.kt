package com.union.union_basic.ext

import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import android.text.Layout
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.*
import android.view.View
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.RequiresApi

object SsbKtx {
    const val flag = SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE
    const val type_bold = Typeface.BOLD
    const val type_italic = Typeface.ITALIC
    const val type_bold_italic = Typeface.BOLD_ITALIC

} //-------------------CharSequence相关扩展-----------------------

/**
 *CharSequence不为 null 或者 empty
 */
fun CharSequence?.isNotNullOrEmpty() = !isNullOrEmpty()

/**
 *获取一段文字在文字中的范围
 * @param target
 * @return
 */
fun CharSequence.range(target: CharSequence): IntRange {
    val start = this.indexOf(target.toString())
    return start..(start + target.length)
}

/**
 *将一段指定的文字改变大小
 * @return
 */
fun CharSequence.sizeSpan(range: IntRange, textSize: Int): CharSequence {
    return SpannableStringBuilder(this).apply {
        setSpan(AbsoluteSizeSpan(textSize), range.first, range.last, SsbKtx.flag)
    }
}

/**
 *将一段指定的文字,设置类型,是否加粗,倾斜
 * @return
 */
fun CharSequence.typeSpan(range: IntRange, type: Int): CharSequence {
    return SpannableStringBuilder(this).apply {
        setSpan(StyleSpan(type), range.first, range.last, SsbKtx.flag)
    }
}

/**
 *设置下划线
 * @param range
 * @return
 */
fun CharSequence.underlineSpan(range: IntRange): CharSequence {
    return SpannableStringBuilder(this).apply {
        setSpan(UnderlineSpan(), range.first, range.last, SsbKtx.flag)
    }
}

/**
 *设置删除线
 * @param range
 * @return
 */
fun CharSequence.strikethroughSpan(range: IntRange): CharSequence {
    return SpannableStringBuilder(this).apply {
        setSpan(StrikethroughSpan(), range.first, range.last, SsbKtx.flag)
    }
}

/**
 *设置文字颜色
 * @param range
 * @return
 */
fun CharSequence.foregroundColorSpan(range: IntRange, color: Int = Color.RED): CharSequence {
    return SpannableStringBuilder(this).apply {
        setSpan(ForegroundColorSpan(color), range.first, range.last, SsbKtx.flag)
    }
}

/**
 *设置文字背景色
 * @param range
 * @return
 */
fun CharSequence.backgroundColorSpan(range: IntRange, color: Int = Color.RED): CharSequence {
    return SpannableStringBuilder(this).apply {
        setSpan(BackgroundColorSpan(color), range.first, range.last, SsbKtx.flag)
    }
}

/**
 *设置引用线颜色
 * @param range
 * @return
 */
fun CharSequence.quoteColorSpan(range: IntRange, color: Int = Color.RED): CharSequence {
    return SpannableStringBuilder(this).apply {
        setSpan(QuoteSpan(color), range.first, range.last, SsbKtx.flag)
    }
}

/**
 *设置字体大小比例
 * @param range
 * @return
 */
fun CharSequence.proportionSpan(range: IntRange, proportion: Float): CharSequence {
    return SpannableStringBuilder(this).apply {
        setSpan(RelativeSizeSpan(proportion), range.first, range.last, SsbKtx.flag)
    }
}

/**
 *设置横向字体大小比例
 * @param range
 * @return
 */
fun CharSequence.proportionXSpan(range: IntRange, proportion: Float): CharSequence {
    return SpannableStringBuilder(this).apply {
        setSpan(ScaleXSpan(proportion), range.first, range.last, SsbKtx.flag)
    }
}

/**
 *设置上标
 * @param range
 * @return
 */
fun CharSequence.superscriptSpan(range: IntRange): CharSequence {
    return SpannableStringBuilder(this).apply {
        setSpan(SuperscriptSpan(), range.first, range.last, SsbKtx.flag)
    }
}

/**
 *设置下标
 * @param range
 * @return
 */
fun CharSequence.subscriptSpan(range: IntRange): CharSequence {
    return SpannableStringBuilder(this).apply {
        setSpan(SubscriptSpan(), range.first, range.last, SsbKtx.flag)
    }
}

/**
 *设置字体
 * @param range
 * @return
 */
fun CharSequence.fontSpan(range: IntRange, font: String): CharSequence {
    return SpannableStringBuilder(this).apply {
        setSpan(TypefaceSpan(font), range.first, range.last, SsbKtx.flag)
    }
}

@RequiresApi(Build.VERSION_CODES.P)
fun CharSequence.fontSpan(range: IntRange, font: Typeface): CharSequence {
    return SpannableStringBuilder(this).apply {
        setSpan(TypefaceSpan(font), range.first, range.last, SsbKtx.flag)
    }
}

/**
 *设置对齐方式
 * @param range
 * @return
 */
fun CharSequence.alignSpan(range: IntRange, align: Layout.Alignment): CharSequence {
    return SpannableStringBuilder(this).apply {
        setSpan(AlignmentSpan.Standard(align), range.first, range.last, SsbKtx.flag)
    }
}

/**
 *设置url,超链接
 * @param range
 * @return
 */
fun CharSequence.urlSpan(range: IntRange, url: String): CharSequence {
    return SpannableStringBuilder(this).apply {
        setSpan(URLSpan(url), range.first, range.last, SsbKtx.flag)
    }
}

/**
 *设置click,将一段文字中指定range的文字添加颜色和点击事件
 * @param range
 * @return
 */
fun CharSequence.clickSpan(range: IntRange, color: Int = Color.RED, isUnderlineText: Boolean = false, clickAction: () -> Unit): CharSequence {
    return SpannableString(this).apply {
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                clickAction()
            }

            override fun updateDrawState(ds: TextPaint) {
                ds.color = color
                ds.isUnderlineText = isUnderlineText
            }
        }
        setSpan(clickableSpan, range.first, range.last, SsbKtx.flag)
    }
}

//-------------------TextView相关扩展--------------------------
/**
 *设置目标文字大小, src,target 为空时,默认设置整个 text
 * @return
 */
fun TextView?.sizeSpan(src: CharSequence? = this?.text, target: CharSequence? = this?.text, range: IntRange? = null, @DimenRes textSize: Int): TextView? {
    return when {
        this == null -> this
        src.isNullOrEmpty() -> this
        target.isNullOrEmpty() && range == null -> this
        textSize == 0 -> this
        range != null -> {
            text = src.sizeSpan(range, resources.getDimensionPixelSize(textSize))
            this
        }
        target.isNotNullOrEmpty() -> {
            text = src.sizeSpan(src.range(target!!), resources.getDimensionPixelSize(textSize))
            this
        }
        else -> this
    }
}

/**
 *设置目标文字大小, src,target 为空时,默认设置整个 text
 * @return
 */
fun TextView?.sizeSpan(src: CharSequence? = this?.text, target: CharSequence? = this?.text, range: IntRange? = null, textSize: Float): TextView? {
    return when {
        this == null -> this
        src.isNullOrEmpty() -> this
        target.isNullOrEmpty() && range == null -> this
        textSize == 0f -> this
        range != null -> {
            text = src.sizeSpan(range, textSize.dp.toInt())
            this
        }
        target.isNotNullOrEmpty() -> {
            text = src.sizeSpan(src.range(target!!), textSize.dp.toInt())
            this
        }
        else -> this
    }
}

/**
 *追加内容设置字体大小
 * @param str
 * @param textSize
 * @return
 */
fun TextView?.appendSizeSpan(str: String?, textSize: Float): TextView? {
    str?.let {
        this?.append(it.sizeSpan(0..it.length, textSize.dp.toInt()))
    }
    return this
}

fun TextView?.appendSizeSpan(str: String?, @DimenRes textSize: Int): TextView? {
    str?.let {
        this?.append(it.sizeSpan(0..it.length, resources.getDimensionPixelSize(textSize)))
    }
    return this
}

/**
 *设置目标文字类型(加粗,倾斜,加粗倾斜),src,target 为空时,默认设置整个 text
 * @return
 */
fun TextView?.typeSpan(src: CharSequence? = this?.text, target: CharSequence? = this?.text, range: IntRange? = null, type: Int): TextView? {
    return when {
        this == null -> this
        src.isNullOrEmpty() -> this
        target.isNullOrEmpty() && range == null -> this
        range != null -> {
            text = src.typeSpan(range, type)
            this
        }
        target.isNotNullOrEmpty() -> {
            text = src.typeSpan(src.range(target!!), type)
            this
        }
        else -> this
    }
}

fun TextView?.appendTypeSpan(str: String?, type: Int): TextView? {
    str?.let {
        this?.append(it.typeSpan(0..it.length, type))
    }
    return this
}

/**
 *设置目标文字下划线
 * @return
 */
fun TextView?.underlineSpan(src: CharSequence? = this?.text, target: CharSequence? = this?.text, range: IntRange? = null): TextView? {
    return when {
        this == null -> this
        src.isNullOrEmpty() -> this
        target.isNullOrEmpty() && range == null -> this
        range != null -> {
            text = src.underlineSpan(range)
            this
        }
        target.isNotNullOrEmpty() -> {
            text = src.underlineSpan(src.range(target!!))
            this
        }
        else -> this
    }
}

fun TextView?.appendUnderlineSpan(str: String?): TextView? {
    str?.let {
        this?.append(it.underlineSpan(0..it.length))
    }
    return this
}

/**
 *设置目标文字删除线
 * @return
 */
fun TextView?.strikethroughSpan(src: CharSequence? = this?.text, target: CharSequence? = this?.text, range: IntRange? = null): TextView? {
    return when {
        this == null -> this
        src.isNullOrEmpty() -> this
        target.isNullOrEmpty() && range == null -> this
        range != null -> {
            text = src.strikethroughSpan(range)
            this
        }
        target.isNotNullOrEmpty() -> {
            text = src.strikethroughSpan(src.range(target!!))
            this
        }
        else -> this
    }
}

fun TextView?.appendStrikethroughSpan(str: String?): TextView? {
    str?.let {
        this?.append(it.strikethroughSpan(0..it.length))
    }
    return this
}

/**
 *设置目标文字颜色
 * @return
 */
fun TextView?.foregroundColorIntSpan(src: CharSequence? = this?.text, target: CharSequence? = this?.text, range: IntRange? = null, color: Int): TextView? {
    return when {
        this == null -> this
        src.isNullOrEmpty() -> this
        target.isNullOrEmpty() && range == null -> this
        range != null -> {
            text = src.foregroundColorSpan(range, color)
            this
        }
        target.isNotNullOrEmpty() -> {
            text = src.foregroundColorSpan(src.range(target!!), color)
            this
        }
        else -> this
    }
}

fun TextView?.appendForegroundColorIntSpan(str: String?, color: Int): TextView? {
    str?.let {
        this?.append(it.foregroundColorSpan(0..it.length, color))
    }
    return this
}

/**
 *设置目标文字颜色
 * @return
 */
fun TextView?.foregroundColorSpan(src: CharSequence? = this?.text, target: CharSequence? = this?.text, range: IntRange? = null,
    @ColorRes color: Int): TextView? {
    return when {
        this == null -> this
        src.isNullOrEmpty() -> this
        target.isNullOrEmpty() && range == null -> this
        range != null -> {
            text = src.foregroundColorSpan(range, resources.getColor(color))
            this
        }
        target.isNotNullOrEmpty() -> {
            text = src.foregroundColorSpan(src.range(target!!), resources.getColor(color))
            this
        }
        else -> this
    }
}

fun TextView?.appendForegroundColorSpan(str: String?, @ColorRes color: Int): TextView? {
    str?.let {
        this?.append(it.foregroundColorSpan(0..it.length, resources.getColor(color)))
    }
    return this
}

/**
 *设置目标文字背景颜色
 * @return
 */
fun TextView?.backgroundColorIntSpan(src: CharSequence? = this?.text, target: CharSequence? = this?.text, range: IntRange? = null, color: Int): TextView? {
    return when {
        this == null -> this
        src.isNullOrEmpty() -> this
        target.isNullOrEmpty() && range == null -> this
        range != null -> {
            text = src.backgroundColorSpan(range, color)
            this
        }
        target.isNotNullOrEmpty() -> {
            text = src.backgroundColorSpan(src.range(target!!), color)
            this
        }
        else -> this
    }
}

fun TextView?.appendBackgroundColorIntSpan(str: String?, color: Int): TextView? {
    str?.let {
        this?.append(it.backgroundColorSpan(0..it.length, color))
    }
    return this
}

/**
 *设置目标文字背景颜色
 * @return
 */
fun TextView?.backgroundColorSpan(src: CharSequence? = this?.text, target: CharSequence? = this?.text, range: IntRange? = null,
    @ColorRes color: Int): TextView? {
    return when {
        this == null -> this
        src.isNullOrEmpty() -> this
        target.isNullOrEmpty() && range == null -> this
        range != null -> {
            text = src.backgroundColorSpan(range, resources.getColor(color))
            this
        }
        target.isNotNullOrEmpty() -> {
            text = src.backgroundColorSpan(src.range(target!!), resources.getColor(color))
            this
        }
        else -> this
    }
}

fun TextView?.appendBackgroundColorSpan(str: String?, @ColorRes color: Int): TextView? {
    str?.let {
        this?.append(it.backgroundColorSpan(0..it.length, resources.getColor(color)))
    }
    return this
}

/**
 *设置目标文字引用线颜色
 * @return
 */
fun TextView?.quoteColorIntSpan(src: CharSequence? = this?.text, target: CharSequence? = this?.text, range: IntRange? = null, color: Int): TextView? {
    return when {
        this == null -> this
        src.isNullOrEmpty() -> this
        target.isNullOrEmpty() && range == null -> this
        range != null -> {
            text = src.quoteColorSpan(range, color)
            this
        }
        target.isNotNullOrEmpty() -> {
            text = src.quoteColorSpan(src.range(target!!), color)
            this
        }
        else -> this
    }
}

fun TextView?.appendQuoteColorIntSpan(str: String?, color: Int): TextView? {
    str?.let {
        this?.append(it.quoteColorSpan(0..it.length, color))
    }
    return this
}

/**
 *设置目标文字引用线颜色
 * @return
 */
fun TextView?.quoteColorSpan(src: CharSequence? = this?.text, target: CharSequence? = this?.text, range: IntRange? = null, @ColorRes color: Int): TextView? {
    return when {
        this == null -> this
        src.isNullOrEmpty() -> this
        target.isNullOrEmpty() && range == null -> this
        range != null -> {
            text = src.quoteColorSpan(range, resources.getColor(color))
            this
        }
        target.isNotNullOrEmpty() -> {
            text = src.quoteColorSpan(src.range(target!!), resources.getColor(color))
            this
        }
        else -> this
    }
}

fun TextView?.appendQuoteColorSpan(str: String?, @ColorRes color: Int): TextView? {
    str?.let {
        this?.append(it.quoteColorSpan(0..it.length, resources.getColor(color)))
    }
    return this
}

/**
 *设置目标文字字体大小比例
 * @return
 */
fun TextView?.proportionSpan(src: CharSequence? = this?.text, target: CharSequence? = this?.text, range: IntRange? = null, proportion: Float): TextView? {
    return when {
        this == null -> this
        src.isNullOrEmpty() -> this
        target.isNullOrEmpty() && range == null -> this
        range != null -> {
            text = src.proportionSpan(range, proportion)
            this
        }
        target.isNotNullOrEmpty() -> {
            text = src.proportionSpan(src.range(target!!), proportion)
            this
        }
        else -> this
    }
}

fun TextView?.appendProportionSpan(str: String?, proportion: Float): TextView? {
    str?.let {
        this?.append(it.proportionSpan(0..it.length, proportion))
    }
    return this
}

/**
 *设置目标文字字体横向大小比例
 * @return
 */
fun TextView?.proportionXSpan(src: CharSequence? = this?.text, target: CharSequence? = this?.text, range: IntRange? = null, proportion: Float): TextView? {
    return when {
        this == null -> this
        src.isNullOrEmpty() -> this
        target.isNullOrEmpty() && range == null -> this
        range != null -> {
            text = src.proportionXSpan(range, proportion)
            this
        }
        target.isNotNullOrEmpty() -> {
            text = src.proportionXSpan(src.range(target!!), proportion)
            this
        }
        else -> this
    }
}

fun TextView?.appendProportionXSpan(str: String?, proportion: Float): TextView? {
    str?.let {
        this?.append(it.proportionXSpan(0..it.length, proportion))
    }
    return this
}

/**
 *设置目标文字字体上标
 * @return
 */
fun TextView?.superscriptSpan(src: CharSequence? = this?.text, target: CharSequence? = this?.text, range: IntRange? = null): TextView? {
    return when {
        this == null -> this
        src.isNullOrEmpty() -> this
        target.isNullOrEmpty() && range == null -> this
        range != null -> {
            text = src.superscriptSpan(range)
            this
        }
        target.isNotNullOrEmpty() -> {
            text = src.superscriptSpan(src.range(target!!))
            this
        }
        else -> this
    }
}

fun TextView?.appendSuperscriptSpan(str: String?): TextView? {
    str?.let {
        this?.append(it.superscriptSpan(0..it.length))
    }
    return this
}

/**
 *设置目标文字字体下标
 * @return
 */
fun TextView?.subscriptSpan(src: CharSequence? = this?.text, target: CharSequence? = this?.text, range: IntRange? = null): TextView? {
    return when {
        this == null -> this
        src.isNullOrEmpty() -> this
        target.isNullOrEmpty() && range == null -> this
        range != null -> {
            text = src.subscriptSpan(range)
            this
        }
        target.isNotNullOrEmpty() -> {
            text = src.subscriptSpan(src.range(target!!))
            this
        }
        else -> this
    }
}

fun TextView?.appendSubscriptSpan(str: String?): TextView? {
    str?.let {
        this?.append(it.subscriptSpan(0..it.length))
    }
    return this
}

/**
 *设置目标文字字体
 * @return
 */
fun TextView?.fontSpan(src: CharSequence? = this?.text, target: CharSequence? = this?.text, range: IntRange? = null, font: String): TextView? {
    return when {
        this == null -> this
        src.isNullOrEmpty() -> this
        target.isNullOrEmpty() && range == null -> this
        range != null -> {
            text = src.fontSpan(range, font)
            this
        }
        target.isNotNullOrEmpty() -> {
            text = src.fontSpan(src.range(target!!), font)
            this
        }
        else -> this
    }
}

fun TextView?.appendFontSpan(str: String?, font: String): TextView? {
    str?.let {
        this?.append(it.fontSpan(0..it.length, font))
    }
    return this
}

/**
 *设置目标文字字体
 * @return
 */
@RequiresApi(Build.VERSION_CODES.P)
fun TextView?.fontSpan(src: CharSequence? = this?.text, target: CharSequence? = this?.text, range: IntRange? = null, font: Typeface): TextView? {
    return when {
        this == null -> this
        src.isNullOrEmpty() -> this
        target.isNullOrEmpty() && range == null -> this
        range != null -> {
            text = src.fontSpan(range, font)
            this
        }
        target.isNotNullOrEmpty() -> {
            text = src.fontSpan(src.range(target!!), font)
            this
        }
        else -> this
    }
}

@RequiresApi(Build.VERSION_CODES.P)
fun TextView?.appendFontSpan(str: String?, font: Typeface): TextView? {
    str?.let {
        this?.append(it.fontSpan(0..it.length, font))
    }
    return this
}

/**
 *设置目标文字对齐方式
 * @return
 */
fun TextView?.alignSpan(src: CharSequence? = this?.text, target: CharSequence? = this?.text, range: IntRange? = null, align: Layout.Alignment): TextView? {
    return when {
        this == null -> this
        src.isNullOrEmpty() -> this
        target.isNullOrEmpty() && range == null -> this
        range != null -> {
            text = src.alignSpan(range, align)
            this
        }
        target.isNotNullOrEmpty() -> {
            text = src.alignSpan(src.range(target!!), align)
            this
        }
        else -> this
    }
}

fun TextView?.appendAlignSpan(str: String?, align: Layout.Alignment): TextView? {
    str?.let {
        this?.append(it.alignSpan(0..it.length, align))
    }
    return this
}

/**
 *设置目标文字超链接
 * @return
 */
fun TextView?.urlSpan(src: CharSequence? = this?.text, target: CharSequence? = this?.text, range: IntRange? = null, url: String): TextView? {
    return when {
        this == null -> this
        src.isNullOrEmpty() -> this
        target.isNullOrEmpty() && range == null -> this
        range != null -> {
            movementMethod = LinkMovementMethod.getInstance()
            text = src.urlSpan(range, url)
            this
        }
        target.isNotNullOrEmpty() -> {
            movementMethod = LinkMovementMethod.getInstance()
            text = src.urlSpan(src.range(target!!), url)
            this
        }
        else -> this
    }
}

fun TextView?.appendUrlSpan(str: String?, url: String): TextView? {
    str?.let {
        this?.append(it.urlSpan(0..it.length, url))
    }
    return this
}

/**
 *设置目标文字点击
 * @return
 */
fun TextView?.clickIntSpan(src: CharSequence? = this?.text, target: CharSequence? = this?.text, range: IntRange? = null, color: Int = Color.RED,
    isUnderlineText: Boolean = false, clickAction: () -> Unit): TextView? {
    return when {
        this == null -> this
        src.isNullOrEmpty() -> this
        target.isNullOrEmpty() && range == null -> this
        range != null -> {
            movementMethod = LinkMovementMethod.getInstance()
            highlightColor = Color.TRANSPARENT  // remove click bg color
            text = src.clickSpan(range, color, isUnderlineText, clickAction)
            this
        }
        target.isNotNullOrEmpty() -> {
            movementMethod = LinkMovementMethod.getInstance()
            highlightColor = Color.TRANSPARENT  // remove click bg color
            text = src.clickSpan(src.range(target!!), color, isUnderlineText, clickAction)
            this
        }
        else -> this
    }
}

fun TextView?.appendClickIntSpan(str: String?, color: Int = Color.RED, isUnderlineText: Boolean = false, clickAction: () -> Unit): TextView? {
    str?.let {
        this?.append(it.clickSpan(0..it.length, color, isUnderlineText, clickAction))
    }
    return this
}

/**
 *设置目标文字点击
 * @return
 */
fun TextView?.clickSpan(src: CharSequence? = this?.text, target: CharSequence? = this?.text, range: IntRange? = null, @ColorRes color: Int,
    isUnderlineText: Boolean = false, clickAction: () -> Unit): TextView? {
    return when {
        this == null -> this
        src.isNullOrEmpty() -> this
        target.isNullOrEmpty() && range == null -> this
        range != null -> {
            movementMethod = LinkMovementMethod.getInstance()
            highlightColor = Color.TRANSPARENT  // remove click bg color
            text = src.clickSpan(range, resources.getColor(color), isUnderlineText, clickAction)
            this
        }
        target.isNotNullOrEmpty() -> {
            movementMethod = LinkMovementMethod.getInstance()
            highlightColor = Color.TRANSPARENT  // remove click bg color
            text = src.clickSpan(src.range(target!!), resources.getColor(color), isUnderlineText, clickAction)
            this
        }
        else -> this
    }
}

fun TextView?.appendClickSpan(str: String?, @ColorRes color: Int, isUnderlineText: Boolean = false, clickAction: () -> Unit): TextView? {
    str?.let {
        this?.append(it.clickSpan(0..it.length, resources.getColor(color), isUnderlineText, clickAction))
    }
    return this
}
