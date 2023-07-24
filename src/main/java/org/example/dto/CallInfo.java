package org.example.dto;

import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiMethod;

public class CallInfo {
    private final PsiMethod method;
    private final PsiFile file;
    private final int level;
    private final FuncAssociationEnum funcAssociation;

    public CallInfo(PsiMethod method, PsiFile file, int level, FuncAssociationEnum funcAssociation) {
        this.method = method;
        this.file = file;
        this.level = level;
        this.funcAssociation = funcAssociation;
    }

    public PsiMethod getMethod() {
        return method;
    }

    public PsiFile getFile() {
        return file;
    }

    public int getLevel() {
        return level;
    }

    public FuncAssociationEnum getFuncAssociation() {
        return funcAssociation;
    }
}