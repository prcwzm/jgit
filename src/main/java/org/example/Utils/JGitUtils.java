package org.example.Utils;

//import com.intellij.openapi.actionSystem.AnActionEvent;
//import com.intellij.openapi.project.Project;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.revwalk.RevCommit;
import org.example.dto.JGitDiffer;

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

    public static JGitDiffer getHeadDiffer() {
        JGitDiffer jGitDiffer = null;
        try ( Git git = Git.open(new File(""))){
            // Get latest commit object
            ObjectId headId = git.getRepository().resolve("HEAD");
            RevCommit commit = git.getRepository().parseCommit(headId);

            // Print code changes
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            DiffFormatter formatter = new DiffFormatter(out);
            formatter.setRepository(git.getRepository());
            formatter.format(commit.getParent(0), commit);
            String diff = out.toString(StandardCharsets.UTF_8.name());
            return new JGitDiffer(diff);
            //add
        } catch (Exception e) {
            System.out.println("ERROR:" + e);
            return null;
        }

    }

    public static JGitDiffer getSelectBranchDiffer() {
        JGitDiffer jGitDiffer = null;
        try (Git git = Git.open(new File(""))) {
            // Get the commit objects for Branch A and Branch B
            ObjectId commitIdA = git.getRepository().resolve("refs/heads/main");
            ObjectId commitIdB = git.getRepository().resolve("refs/heads/main");
            RevCommit commitA = git.getRepository().parseCommit(commitIdA);
            RevCommit commitB = git.getRepository().parseCommit(commitIdB);

            // Print code changes between Branch A and Branch B
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            DiffFormatter formatter = new DiffFormatter(out);
            formatter.setRepository(git.getRepository());
            formatter.format(commitA, commitB);
            String diff = out.toString(StandardCharsets.UTF_8.name());
            if (diff == null || diff.length() == 0) {
                return null;
            }
            return new JGitDiffer(diff);
            // add
        } catch (Exception e) {
            System.out.println("ERROR:" + e);
            return null;
        }
    }


}
