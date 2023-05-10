package org.example.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class JGitDiffer {
    private List<DifferElement> differs = new ArrayList<>();
}
