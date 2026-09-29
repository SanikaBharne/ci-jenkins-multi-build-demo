package com.vit.newsflow;

import com.vit.newsflow.Article.Status;

/** Dependency-free test runner so Maven, Ant and Gradle can all run it the same way. */
public class NewsWorkflowTest {
    private static int failures = 0;

    private static void check(String name, boolean ok) {
        if (ok) System.out.println("TEST PASSED: " + name);
        else { failures++; System.out.println("Test failed: " + name); }
    }

    private static boolean throwsEx(Runnable r) {
        try { r.run(); return false; } catch (RuntimeException e) { return true; }
    }

    private static String words(int n) { return "news ".repeat(n).trim(); }

    private static Article good() {
        return new Article("Council approves metro", "council-approves-metro", "Asha Rao",
                words(30), "https://example.com/m.jpg", "Metro train");
    }

    public static void main(String[] args) {
        ArticleValidator v = new ArticleValidator();
        NewsWorkflowService svc = new NewsWorkflowService();

        check("valid article has no errors", v.validate(good()).isEmpty());
        check("missing title fails", !v.validate(new Article("", "a-b", "X", words(30), null, null)).isEmpty());
        check("bad slug fails", !v.validate(new Article("T", "Bad Slug", "X", words(30), null, null)).isEmpty());
        check("image without alt text fails", !v.validate(new Article("T", "a-b", "X", words(30), "i.jpg", "")).isEmpty());
        check("insecure http link fails", !v.validate(new Article("T", "a-b", "X", words(25) + " http://x.com", null, null)).isEmpty());

        Article a = good();
        svc.submit(a);
        check("valid submit -> PENDING_REVIEW", a.status == Status.PENDING_REVIEW);
        svc.approve(a, "Meera");
        check("approve -> APPROVED", a.status == Status.APPROVED);
        String html = svc.publish(a);
        check("publish -> PUBLISHED with HTML", a.status == Status.PUBLISHED && html.contains("<h1>Council approves metro</h1>"));
        check("history is tracked (3 moves)", a.history.size() == 3);

        Article bad = new Article("", "x", "", "short", null, null);
        svc.submit(bad);
        check("invalid submit -> VALIDATION_FAILED", bad.status == Status.VALIDATION_FAILED);

        Article r = good();
        svc.submit(r);
        check("reject without comment is refused", throwsEx(() -> svc.reject(r, "Meera", " ")));
        svc.reject(r, "Meera", "Needs a second source");
        check("reject with comment -> REJECTED", r.status == Status.REJECTED);

        check("cannot publish unapproved article", throwsEx(() -> svc.publish(good())));

        if (failures > 0) throw new AssertionError(failures + " test(s) failed");
        System.out.println("ALL TESTS PASSED");
    }
}
