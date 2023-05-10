package org.example.dto;

import lombok.Data;

@Data
public class DifferElement {
    private String fileName;
    private int addLineCount = 0;
    private int deleteLineCount = 0;
    private String columnChangeInfo;
    private String deffer;
    private String changeData;

    public DifferElement(String defferSource) {
        // Extract filename and print
        String[] lines = defferSource.split("\n");
        this.fileName = lines[0].substring("diff --git a/".length());
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

    }
}
