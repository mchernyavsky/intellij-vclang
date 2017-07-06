package org.vclang.ide.colors


import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighter
import com.intellij.openapi.options.colors.*

import javax.swing.*
import com.intellij.openapi.options.colors.AttributesDescriptor
import com.intellij.openapi.options.colors.ColorSettingsPage
import org.vclang.ide.highlight.VcSyntaxHighlighter
import org.vclang.ide.icons.VcIcons


class VcColorSettingsPage : ColorSettingsPage {
    override fun getIcon(): Icon? = VcIcons.FILE

    override fun getHighlighter(): SyntaxHighlighter = VcSyntaxHighlighter()

    override fun getDemoText(): String = DEMO_TEXT

    override fun getAdditionalHighlightingTagToDescriptorMap(): Map<String, TextAttributesKey>? = null

    override fun getAttributeDescriptors(): Array<AttributesDescriptor> = DESCRIPTORS

    override fun getColorDescriptors(): Array<ColorDescriptor> = ColorDescriptor.EMPTY_ARRAY

    override fun getDisplayName(): String = "Vclang"

    companion object {
        private val DESCRIPTORS = VcHighlightingColors.values().map { it.attributesDescriptor }.toTypedArray()
        private val DEMO_TEXT = "\\open ::Data::Bool\n" +
                "\n" +
                "\\class Semigroup {\n" +
                "  \\field X : \\Type0\n" +
                "  \\field op : X -> X -> X\n" +
                "  \\field assoc : \\Pi (x y z : X) -> op (op x y) z = op x (op y z)\n" +
                "}\n" +
                "\n" +
                "\\function\n" +
                "xor-semigroup => \\new Semigroup { X => Bool | op => xor | assoc => {?} }"
    }
}