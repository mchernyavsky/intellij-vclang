package org.arend.intention

import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.SmartPointerManager
import org.arend.psi.ArendCoClause
import org.arend.psi.ArendExpr
import org.arend.psi.CoClauseBase
import org.arend.quickfix.implementCoClause.CoClausesKey
import org.arend.quickfix.implementCoClause.ImplementFieldsQuickFix

open class ImplementFieldsInCoClauseIntention : SelfTargetingIntention<CoClauseBase>(CoClauseBase::class.java, "Implement fields of a super class") {
    override fun isApplicableTo(element: CoClauseBase, caretOffset: Int, editor: Editor): Boolean {
        val data = element.getUserData(CoClausesKey)
        if (data != null && data.isNotEmpty()) {
            text = "Implement fields of ${element.longName?.text}"
            return element.fatArrow == null
        }
        return false
    }

    override fun applyTo(element: CoClauseBase, project: Project, editor: Editor) {
        val data = element.getUserData(CoClausesKey)
        val rangeToReport = element.longName?.textRange
        if (data != null && rangeToReport != null)
            ImplementFieldsQuickFix(SmartPointerManager.createPointer(element), false, data).invoke(project, editor, null)
    }

    override fun forbidCaretInsideElement(element: PsiElement): Boolean = element is ArendExpr || element is ArendCoClause
}