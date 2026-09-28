package com.vit.newsflow;

import com.vit.newsflow.Article.Status;
import java.util.List;

/** Submit -> validate -> review (approve/reject) -> publish, with status tracking. */
public class NewsWorkflowService {
    private final ArticleValidator validator = new ArticleValidator();

    public List<String> submit(Article a) {
        List<String> errors = validator.validate(a);
        if (errors.isEmpty()) move(a, Status.PENDING_REVIEW, "submitted, validation passed");
        else move(a, Status.VALIDATION_FAILED, String.join("; ", errors));
        return errors;
    }

    public void approve(Article a, String reviewer) {
        require(a, Status.PENDING_REVIEW);
        move(a, Status.APPROVED, "approved by " + reviewer);
    }

    public void reject(Article a, String reviewer, String comment) {
        require(a, Status.PENDING_REVIEW);
        if (comment == null || comment.isBlank()) throw new IllegalArgumentException("Rejection needs a comment");
        move(a, Status.REJECTED, "rejected by " + reviewer + ": " + comment);
    }

    /** Only approved articles can go live; returns the generated page. */
    public String publish(Article a) {
        require(a, Status.APPROVED);
        String html = SiteGenerator.render(a);
        move(a, Status.PUBLISHED, "published");
        return html;
    }

    private void require(Article a, Status expected) {
        if (a.status != expected)
            throw new IllegalStateException("Expected " + expected + " but article is " + a.status);
    }

    private void move(Article a, Status to, String note) {
        a.history.add(a.status + " -> " + to + " : " + note);
        a.status = to;
    }
}
