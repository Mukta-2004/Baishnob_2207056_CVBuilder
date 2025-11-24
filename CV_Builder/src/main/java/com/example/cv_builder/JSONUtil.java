package com.example.cv_builder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class JSONUtil {

    public static void exportEducationToJson(List<Education> list, Path file) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("[\n");
        boolean first = true;
        for (Education e : list) {
            if ((e.getExam() == null || e.getExam().isBlank()) &&
                    (e.getDepartment() == null || e.getDepartment().isBlank()) &&
                    (e.getBoard() == null || e.getBoard().isBlank()) &&
                    (e.getYear() == null || e.getYear().isBlank()) &&
                    (e.getGrade() == null || e.getGrade().isBlank())) {
                continue;
            }
            if (!first) sb.append(",\n");
            first = false;
            sb.append("  {\n");
            sb.append("    \"exam\": ").append(escapeJson(e.getExam())).append(",\n");
            sb.append("    \"department\": ").append(escapeJson(e.getDepartment())).append(",\n");
            sb.append("    \"board\": ").append(escapeJson(e.getBoard())).append(",\n");
            sb.append("    \"year\": ").append(escapeJson(e.getYear())).append(",\n");
            sb.append("    \"grade\": ").append(escapeJson(e.getGrade())).append("\n");
            sb.append("  }");
        }
        sb.append("\n]\n");

        Files.writeString(file, sb.toString());
    }

    private static String escapeJson(String s) {
        if (s == null) return "\"\"";
        String t = s.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
        return "\"" + t + "\"";
    }
}
