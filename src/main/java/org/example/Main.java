package org.example;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.revwalk.RevCommit;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.charset.StandardCharsets;

public class Main {
    public static void main(String[] args) throws Exception {
        try ( Git git = Git.open(new File("/Users/wuziming/IdeaProjects/jgit/.git"))){
            // Get latest commit object
            ObjectId headId = git.getRepository().resolve("HEAD");
            RevCommit commit = git.getRepository().parseCommit(headId);

            // Print commit info
            System.out.println(commit.getFullMessage());

            // Print code changes
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            DiffFormatter formatter = new DiffFormatter(out);
            formatter.setRepository(git.getRepository());
            formatter.format(commit.getParent(0), commit);
            String diff = out.toString(StandardCharsets.UTF_8.name());
            System.out.println(diff);

            //add

        }
    }

}