package org.example;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.revwalk.RevCommit;
import org.example.Utils.JGitUtils;
import org.example.dto.FileDiffer;
import org.example.dto.JGitDiffer;

public class Main {
    public static void main(String[] args) throws Exception {
        //JGitDiffer jGitDiffer = JGitUtils.getHeadDiffer();
        JGitDiffer jGitDiffer = JGitUtils.getSelectBranchDiffer();
        if (jGitDiffer == null) {
            return;
        }
        for (FileDiffer differ : jGitDiffer.getFileDiffers()){
            System.out.println(differ.getFileName() + "\n");
        }
    }

}