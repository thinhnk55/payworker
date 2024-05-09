package com.defi.telegram.common;

public class MessageTemplate {
    public static String toMarkdownV2(String text) {
        text = text.replace("_", "\\_");
        text = text.replace("*", "\\*");
        text = text.replace("[", "\\[");
        text = text.replace("]", "\\]");
        text = text.replace("(", "\\(");
        text = text.replace(")", "\\)");
        text = text.replace("~", "\\~");
        text = text.replace("`", "\\`");
        text = text.replace("|", "\\|");
        text = text.replace("!", "\\!");
        text = text.replace(".", "\\.");
        text = text.replace("=", "\\=");
        text = text.replace("-", "\\-");
        text = text.replace("+", "\\+");
        text = text.replace(">", "\\>");
        text = text.replace("<", "\\<");
        return text;
    }
}
