package org.example.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class FileDiffer {
    private String fileName;
    private int addLineCount = 0;
    private int removeLineCount = 0;
    private List<BlockDiffer> blockDiffers = new ArrayList<>();
    private String deffer;

    public FileDiffer(String defferSource) {
        // Extract filename and print
        String[] lines = defferSource.split("\n");
        this.fileName = lines[0].substring("diff --git a/".length());
        // Extract code changes and print
        StringBuilder codeChanges = new StringBuilder();
        for (int i = 4; i < lines.length; i++) {
            String line = lines[i];
            if (line.startsWith("@@")) {
                if (!blockDiffers.isEmpty()) {
                    blockDiffers.get(blockDiffers.size() - 1).setDeffer(codeChanges.toString());
                    codeChanges.setLength(0);
                }
                blockDiffers.add(new BlockDiffer());
                blockDiffers.get(blockDiffers.size() - 1).setColumnChangeInfo(line + "\n");
                codeChanges.append(line).append("\n");
            } else if (line.startsWith("+")) {
                codeChanges.append(line).append("\n");
                addLineCount++;
            } else if (line.startsWith("-")) {
                codeChanges.append(line).append("\n");
                removeLineCount--;
            } else if (line.equals("\\ No newline at end of file")) {
                codeChanges.append(line).append("\n");
            }
        }
        if (!blockDiffers.isEmpty() && codeChanges.length() > 0) {
            blockDiffers.get(blockDiffers.size() - 1).setDeffer(codeChanges.toString());
            codeChanges.setLength(0);
        }
    }
}
