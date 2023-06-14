package org.example.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class BlockDiffer {
    private String columnChangeInfo;
    private String deffer;
    private List<LineChange> addChanges = new ArrayList<>();
    private List<LineChange> removeChanges = new ArrayList<>();

    public void addLineChange(int oldLineNumber, int newLineNumber, String line, boolean isAdded) {
        if (isAdded){
            addChanges.add(new LineChange(oldLineNumber, newLineNumber, line));
        } else {
            removeChanges.add(new LineChange(oldLineNumber, newLineNumber, line));
        }
    }
}
