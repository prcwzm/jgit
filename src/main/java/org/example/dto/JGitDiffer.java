package org.example.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class JGitDiffer {
    private List<FileDiffer> fileDiffers = new ArrayList<>();
    String diff;

    public JGitDiffer(String diff){
        this.diff = diff;
        String[] files = diff.split("(?m)(?=^diff --git)", -1);
        for (String file : files) {
            fileDiffers.add(new FileDiffer(file));
        }
    }
}
