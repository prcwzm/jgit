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
        this.deffer = defferSource;
        // Extract code changes and print
        StringBuilder codeChanges = new StringBuilder();
        int oldLineNumber = 0; // 记录原文件的行号
        int newLineNumber = 0; // 记录新文件的行号

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

                // 解析行号
                String[] parts = line.split(" ");
                String[] oldLineInfo = parts[1].split(",");
                String[] newLineInfo = parts[2].split(",");
                oldLineNumber = Integer.parseInt(oldLineInfo[0].substring(1));
                newLineNumber = Integer.parseInt(newLineInfo[0].substring(1));
            } else if (line.startsWith("+")) {
                codeChanges.append(line).append("\n");
                blockDiffers.get(blockDiffers.size() - 1).addLineChange(oldLineNumber, newLineNumber, line,true);
                newLineNumber++;
                addLineCount++;
            } else if (line.startsWith("-")) {
                codeChanges.append(line).append("\n");
                blockDiffers.get(blockDiffers.size() - 1).addLineChange(oldLineNumber, newLineNumber, line,false);
                oldLineNumber++;
                removeLineCount--;
            } else if (line.equals("\\ No newline at end of file")) {
                codeChanges.append(line).append("\n");
            } else {
                oldLineNumber++;
                newLineNumber++;
            }
        }
        if (!blockDiffers.isEmpty() && codeChanges.length() > 0) {
            blockDiffers.get(blockDiffers.size() - 1).setDeffer(codeChanges.toString());
            codeChanges.setLength(0);
        }
    }
}
