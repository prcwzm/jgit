package org.example.dto;

import lombok.Data;

@Data
public class LineChange {
    private int oldLineNumber;
    private int newLineNumber;
    private String line;

    public LineChange(int oldLineNumber, int newLineNumber, String line) {
        this.oldLineNumber = oldLineNumber;
        this.newLineNumber = newLineNumber;
        this.line = line;
    }
}
