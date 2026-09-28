package com.vit.newsflow;

import java.util.ArrayList;
import java.util.List;

/** A news article moving through the publishing workflow. */
public class Article {
    public enum Status { DRAFT, VALIDATION_FAILED, PENDING_REVIEW, APPROVED, REJECTED, PUBLISHED }

    public final String title, slug, author, body, imageUrl, imageAlt;
    public Status status = Status.DRAFT;
    public final List<String> history = new ArrayList<>();

    public Article(String title, String slug, String author, String body, String imageUrl, String imageAlt) {
        this.title = title; this.slug = slug; this.author = author;
        this.body = body; this.imageUrl = imageUrl; this.imageAlt = imageAlt;
    }
}
