package com.vit.newsflow;

/** "Build" step: turns an approved article into a static HTML page. */
public class SiteGenerator {
    public static String render(Article a) {
        StringBuilder sb = new StringBuilder("<!DOCTYPE html>\n<html lang=\"en\"><head><meta charset=\"utf-8\">");
        sb.append("<title>").append(esc(a.title)).append("</title></head><body>\n");
        sb.append("<h1>").append(esc(a.title)).append("</h1>\n<p>By ").append(esc(a.author)).append("</p>\n");
        if (a.imageUrl != null && !a.imageUrl.isBlank())
            sb.append("<img src=\"").append(esc(a.imageUrl)).append("\" alt=\"").append(esc(a.imageAlt)).append("\">\n");
        sb.append("<p>").append(esc(a.body)).append("</p>\n</body></html>\n");
        return sb.toString();
    }

    static String esc(String s) {
        return s == null ? "" : s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;");
    }
}
