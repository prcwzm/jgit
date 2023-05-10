package org.example.Utils;

//import com.intellij.openapi.actionSystem.AnActionEvent;
//import com.intellij.openapi.project.Project;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.revwalk.RevCommit;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.charset.StandardCharsets;


public class JGitUtils {
    /**
     * 获取当前 JGit目录
     * AnActionEvent e 动作相关的上下文信息
     * @return String JGit 项目目录
     * */
//    public static String getCurrentJGitPathName(AnActionEvent e) {
//        String projectPath = null;
//        Project project = e.getProject();
//        if (project == null || project.getProjectFilePath() == null ) {
//            return  projectPath;
//        }
//        return project.getProjectFilePath().replace("/.idea/misc.xml", ".git");
//    }
    public static String getCurrentJGitPathName() {
        return "/Users/wuziming/IdeaProjects/jgit/.git";
    }

    public static void getDiffer() {

    }


}
