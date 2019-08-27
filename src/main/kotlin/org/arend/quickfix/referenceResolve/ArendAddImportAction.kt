package org.arend.quickfix.referenceResolve

import com.intellij.codeInsight.daemon.QuickFixBundle
import com.intellij.codeInsight.hint.QuestionAction
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.DumbService
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.popup.PopupStep
import com.intellij.openapi.ui.popup.util.BaseListPopupStep
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiElement
import com.intellij.ui.popup.list.ListPopupImpl
import javax.swing.Icon

class ArendAddImportAction(val project: Project, val editor: Editor, private val currentElement: PsiElement, val resolveData: List<ResolveReferenceAction>, private val onTheFly: Boolean) : QuestionAction {

    override fun execute(): Boolean {
        PsiDocumentManager.getInstance(project).commitAllDocuments()

        if (!currentElement.isValid || resolveData.any { !it.target.isValid }) {
            return false
        }

        if (resolveData.size == 1)
            addImport(resolveData[0])
        else
            chooseItemAndImport()

        return true
    }

    fun addImport(fixData: ResolveReferenceAction) {
        if (!currentElement.isValid) return

        DumbService.getInstance(project).withAlternativeResolveEnabled {
            WriteCommandAction.runWriteCommandAction(project, QuickFixBundle.message("add.import"), null, Runnable { fixData.execute(if (onTheFly) null else editor) }, currentElement.containingFile) }
    }

    private fun chooseItemAndImport(){
        if (resolveData.size == 1)
            resolveData[0].execute(editor)

        if (resolveData.size > 1) {
            val step = object: BaseListPopupStep<ResolveReferenceAction>("Imports", resolveData) {
                override fun getTextFor(value: ResolveReferenceAction?): String {
                    if (value != null) return value.toString()
                    return super.getTextFor(value)
                }

                override fun getIconFor(value: ResolveReferenceAction?): Icon? =
                    value?.target?.getIcon(0) ?: super.getIconFor(value)

                override fun onChosen(selectedValue: ResolveReferenceAction?, finalChoice: Boolean): PopupStep<*>? {
                    if (finalChoice && selectedValue != null) {
                        return doFinalStep {
                            PsiDocumentManager.getInstance(project).commitAllDocuments()
                            addImport(selectedValue)
                        }
                    }

                    return PopupStep.FINAL_CHOICE
                }
            }
            val popup = ListPopupImpl(project, step)

            popup.showInBestPositionFor(editor)
        }
    }
}