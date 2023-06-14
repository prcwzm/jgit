package org.example.Utils;

//import com.intellij.openapi.actionSystem.AnActionEvent;
//import com.intellij.openapi.project.Project;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.example.dto.JGitDiffer;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;


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

    public static JGitDiffer getSelectBranchDiffer(String branchA, String branchB) {
        JGitDiffer jGitDiffer = null;
        try (Git git = Git.open(new File(""))) {
            // Get the commit objects for Branch A and Branch B
            ObjectId commitIdA = git.getRepository().resolve(branchA);
            ObjectId commitIdB = git.getRepository().resolve(branchB);

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

    public static JGitDiffer getCommitDiffer(ObjectId commitIdA, ObjectId commitIdB) {
        JGitDiffer jGitDiffer = null;
        try (Git git = Git.open(new File(""));
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            DiffFormatter formatter = new DiffFormatter(out)){
            // Get the commit objects for Branch A and Commit A
            RevCommit commitA = new RevWalk(git.getRepository()).parseCommit(commitIdA);

            // Get the commit objects for Branch B and Commit B
            RevCommit commitB = new RevWalk(git.getRepository()).parseCommit(commitIdB);

            // Print code changes between Commit A and Commit B
            formatter.setRepository(git.getRepository());
            formatter.format(commitA, commitB);
            String diff = out.toString(StandardCharsets.UTF_8.name());
            return new JGitDiffer(diff);

            // add
        } catch (Exception e) {
            System.out.println("ERROR:" + e);
            return null;
        }
    }

    public static List<Ref> getRefs() {
        List<Ref> branches = null;
        try (Git git = Git.open(new File(""))) {
            // Get the list of branches
           branches = git.branchList().call();
            if (branches != null) {
                // Print the branch information
                for (Ref branch : branches) {
                    System.out.println("Branch: " + branch.getName());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return branches;
    }

    public static List<RevCommit> getCommits(String branchName) {
        List<RevCommit> commitsList = new ArrayList<>();
        try (Git git = Git.open(new File(""))) {
            Iterable<RevCommit> commitsIterable = git.log().add(git.getRepository().resolve(branchName)).call();
            for (RevCommit commit : commitsIterable) {
                commitsList.add(commit);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return commitsList;
    }

    public static String getCurrentBranch() {
        String branch = null;
        try (Git git = Git.open(new File(""))) {
            // Get the list of branches
            branch = git.getRepository().getFullBranch();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return branch;
    }

}
