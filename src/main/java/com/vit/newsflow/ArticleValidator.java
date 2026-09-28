package com.vit.newsflow;

import java.util.ArrayList;
import java.util.List;

/** Automated quality gate: the CI-style checks every article must pass before review. */
public class ArticleValidator {
    static final int MIN_WORDS = 20;

    public List<String> validate(Article a) {
        List<String> errors = new ArrayList<>();
        if (blank(a.title) || a.title.length() > 120) errors.add("Title is required (max 120 chars)");
        if (blank(a.slug) || !a.slug.matches("[a-z0-9]+(-[a-z0-9]+)*")) errors.add("Slug must be lowercase-with-hyphens");
        if (blank(a.author)) errors.add("Author is required");
        if (blank(a.body) || a.body.trim().split("\\s+").length < MIN_WORDS) errors.add("Body needs at least " + MIN_WORDS + " words");
        if (!blank(a.imageUrl) && blank(a.imageAlt)) errors.add("Image is missing alt text");
        if (a.body != null && a.body.contains("http://")) errors.add("Insecure http:// link found (use https)");
        return errors;
    }

    private static boolean blank(String s) { return s == null || s.trim().isEmpty(); }
}
