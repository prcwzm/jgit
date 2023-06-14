package org.example;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.revwalk.RevCommit;
import org.example.Utils.JGitUtils;
import org.example.dto.FileDiffer;
import org.example.dto.JGitDiffer;

import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        //JGitDiffer jGitDiffer = JGitUtils.getHeadDiffer();
        List<Ref> list = JGitUtils.getRefs();
        List<RevCommit> revCommits= JGitUtils.getCommits(list.get(0).getName());
        JGitDiffer jGitDiffers = JGitUtils.getCommitDiffer(revCommits.get(0).getId(),revCommits.get(3).getId());
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
    }

}