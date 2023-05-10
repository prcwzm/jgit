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
        try ( Git git = Git.open(new File(""))){
            // Get latest commit object
            ObjectId headId = git.getRepository().resolve("HEAD");
            RevCommit commit = git.getRepository().parseCommit(headId);

            // Print commit info
            System.out.println(commit.getFullMessage());

            //add

            // Print code changes
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            DiffFormatter formatter = new DiffFormatter(out);
            formatter.setRepository(git.getRepository());
            formatter.format(commit.getParent(0), commit);
            String diff = out.toString(StandardCharsets.UTF_8.name());
            System.out.println(diff);

            String[] files = diff.split("(?<=^diff --git.*$)", -1);
            for (String file : files) {
                // Extract filename and print
                String[] lines = file.split("\n");
                String filename = lines[0].substring("diff --git a/".length());
                System.out.println("File: " + filename);

                // Extract code changes and print
                StringBuilder codeChanges = new StringBuilder();
                for (int i = 4; i < lines.length; i++) {
                    String line = lines[i];
                    if (line.startsWith("+") || line.startsWith("-")) {
                        codeChanges.append(line).append("\n");
                    } else if (line.startsWith("@@")) {
                        codeChanges.append(line).append("\n");
                    } else if (line.equals("\\ No newline at end of file")) {
                        codeChanges.append(line).append("\n");
                    }
                }
                System.out.println(codeChanges.toString());
            }

            //add

        }
    }

}