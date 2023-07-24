package org.example;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.searches.ClassInheritorsSearch;
import com.intellij.psi.search.searches.ReferencesSearch;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.Query;
import groovyjarjarantlr4.v4.runtime.misc.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.revwalk.RevCommit;
import org.example.Utils.JGitUtils;
import org.example.dto.CallInfo;
import org.example.dto.FileDiffer;
import org.example.dto.FuncAssociationEnum;
import org.example.dto.JGitDiffer;
import com.intellij.openapi.project.Project;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class Main {
    public static void main(String[] args) throws Exception {
        //JGitDiffer jGitDiffer = JGitUtils.getHeadDiffer();

        List<Ref> list = JGitUtils.getRefs();
        List<RevCommit> revCommits= JGitUtils.getCommits(list.get(0).getName());
        try (Git git = Git.open(new File(""))){
            JGitUtils.getCommitDiffer(git,ObjectId.fromString("86ac78a0a14607885dc83778187ad41105dd4705"),ObjectId.fromString("63dc828e2743ff96ae4a7f9119409240222716d3"),"refs/heads/branch_differ","refs/heads/main" );
        } catch (IOException e) {
            log.error("e",e);
        }
        JGitDiffer jGitDiffers = JGitUtils.getCommitDiffer(revCommits.get(0).getId(),revCommits.get(3).getId());
       // List<String> methodName = findFunctionsInLine(JGitUtils.gitShow(revCommits.get(1).getId().getName(),"src/main/java/org/example/Utils/JGitUtils.java"),45);
        findName(JGitUtils.gitShow(revCommits.get(1).getId().getName(),"src/main/java/org/example/Utils/JGitUtils.java"));
        System.out.println(jGitDiffers);
        for (RevCommit revCommit : revCommits) {
            System.out.println(revCommit.getFullMessage());
        }
        System.out.println( JGitUtils.getCurrentBranch());

        JGitDiffer jGitDiffer = JGitUtils.getSelectBranchDiffer(list.get(0).getName(),list.get(1).getName());
        if (jGitDiffer == null) {
            return;
        }
        //System.out.println(jGitDiffer);
        Application application = ApplicationManager.getApplication();
        VirtualFile virtualFile = LocalFileSystem.getInstance().findFileByIoFile(new File("/Users/wuziming/IdeaProjects/jgit"));
        Project project = ProjectManager.getInstance().getDefaultProject();
        VirtualFile srcFile = virtualFile.findFileByRelativePath("src/main/java/org/example/Utils/JGitUtils.java");
        PsiFile psiDesFile = PsiManager.getInstance(project).findFile(srcFile);
        PsiMethod method = findMethodInFile(psiDesFile, "main");
        List<CallInfo> chain = new ArrayList<>();
        Set<PsiMethod> visited = new HashSet<>();
        traverseCallerChain(method,chain,0,0,visited);
    }

    public static PsiMethod findMethodInFile(PsiFile file, String methodName) {
        PsiMethod[] methods = PsiTreeUtil.findChildrenOfType(file, PsiMethod.class).toArray(new PsiMethod[0]);
        for (PsiMethod method : methods) {
            if (method.getName().equals(methodName)) {
                return method;
            }
        }
        return null; // 没有找到指定的方法
    }
    public static void findName(File file) {
        Path sourceFilePath = Paths.get(file.getPath());
        int targetLineNumber = 45;

        try {
            ParseResult<CompilationUnit> parseResult = new JavaParser().parse(sourceFilePath);
            if (parseResult.isSuccessful()) {
                Optional<CompilationUnit> optionalCompilationUnit = parseResult.getResult();
                optionalCompilationUnit.ifPresent(compilationUnit -> {
                    Optional<MethodDeclaration> methodNode = compilationUnit.findFirst(MethodDeclaration.class, methodDeclaration ->
                            methodDeclaration.getBegin().isPresent() &&
                                    methodDeclaration.getEnd().isPresent() &&
                                    methodDeclaration.getBegin().get().line <= targetLineNumber &&
                                    methodDeclaration.getEnd().get().line >= targetLineNumber
                    );

                    if (methodNode.isPresent()) {
                        MethodDeclaration methodDeclaration = (MethodDeclaration) methodNode.get();
                        System.out.println("指定行所在的方法名: " + methodDeclaration.getNameAsString());
                    } else {
                        System.out.println("未找到包含指定行的方法");
                    }
                });
            } else {
                System.out.println("无法解析源文件");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void traverseCallerChain(@NotNull PsiMethod method, @NotNull List<CallInfo> chain,
                                           int level, int maxLevel, Set<PsiMethod> visited) {
        try {
            if (level >= maxLevel || visited.contains(method)) return;
            visited.add(method);
            log.info("visited size:" + visited.size() + " chain size:" + chain.size());
            Project project = method.getProject();
            GlobalSearchScope scope = GlobalSearchScope.allScope(project);
            Query<PsiReference> references = ReferencesSearch.search(method, scope);

            for (PsiReference ref : references) {
                PsiElement element = ref.getElement();
                log.info("element class:" + element.getClass().getName() + " element cn:" + element.getClass().getCanonicalName());
                PsiMethod calledMethod = PsiTreeUtil.getParentOfType(element, PsiMethod.class);
                if (calledMethod != null) {
                    log.info("calledMethod class:" + calledMethod.getClass().getName() + " calledMethod cn:" + calledMethod.getClass().getCanonicalName());
                }
                // 处理接口实现的情况
                if (calledMethod != null && calledMethod.isConstructor()) {
                    PsiClass calledClass = calledMethod.getContainingClass();
                    if (calledClass != null) {
                        for (PsiMethod interfaceMethod : calledClass.findMethodsBySignature(method, true)) {
                            if (chain.stream().noneMatch(info -> info.getMethod().equals(interfaceMethod))) {
                                log.info("calledMethod:" + interfaceMethod.getName());
                                CallInfo info = new CallInfo(interfaceMethod, method.getContainingFile(), level, FuncAssociationEnum.CALLER);
                                chain.add(info);
                                log.info("chain:" + chain.size());
                                traverseCallerChain(interfaceMethod, chain, level + 1, maxLevel, visited);
                            }
                        }
                    }
                } else if (calledMethod != null && chain.stream().noneMatch(info -> info.getMethod().equals(calledMethod))) {
                    log.info("inside of normal path.");
                    // If the caller is not a constructor, check if its containing class implements the interface.
                    PsiClass calledClass = calledMethod.getContainingClass();
                    boolean classImplementsInterface = false;
                    if (calledClass != null) {
                        for (PsiClassType implemented : calledClass.getImplementsListTypes()) {
                            if (implemented.equalsToText(Objects.requireNonNull(Objects.requireNonNull(method.getContainingClass()).getQualifiedName()))) {
                                classImplementsInterface = true;
                                break;
                            }
                        }
                    }

                    if (classImplementsInterface) {
                        for (PsiMethod interfaceMethod : calledClass.findMethodsBySignature(method, true)) {
                            if (chain.stream().noneMatch(info -> info.getMethod().equals(interfaceMethod))) {
                                log.info("calledMethod:" + interfaceMethod.getName());
                                CallInfo info = new CallInfo(interfaceMethod, method.getContainingFile(), level, FuncAssociationEnum.CALLER);
                                chain.add(info);
                                log.info("chain:" + chain.size());
                                traverseCallerChain(interfaceMethod, chain, level + 1, maxLevel, visited);
                            }
                        }
                    } else {
                        log.info("calledMethod:" + calledMethod.getName());
                        CallInfo info = new CallInfo(calledMethod, method.getContainingFile(), level, FuncAssociationEnum.CALLER);
                        chain.add(info);
                        log.info("chain:" + chain.size());
                        traverseCallerChain(calledMethod, chain, level + 1, maxLevel, visited);
                    }
                }
            }

            // 查找该方法的所有子方法并检查是否有对应的引用
            PsiClass parentClass = method.getContainingClass();
            if (parentClass != null) {
                Query<PsiClass> subclasses = ClassInheritorsSearch.search(parentClass, scope, true);
                for (PsiClass subclass : subclasses) {
                    PsiMethod[] subMethods = subclass.findMethodsByName(method.getName(), false);
                    for (PsiMethod subMethod : subMethods) {
                        traverseCallerChain(subMethod, chain, level, maxLevel, visited);
                    }
                }
            }
        } catch (Exception e) {
            log.error("caller error:",e);
        }
    }


}