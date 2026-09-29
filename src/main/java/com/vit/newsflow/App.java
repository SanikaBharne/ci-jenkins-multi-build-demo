package com.vit.newsflow;

import java.nio.file.Files;
import java.nio.file.Path;

public class App {
public static void main(String[] args) throws Exception {
NewsWorkflowService svc = new NewsWorkflowService();

    System.out.println("Hello, News Publishing CI/CD demo is running!!");

    Article bad = new Article("", "Bad Slug", "Asha Rao", "too short", "img.jpg", "");
    svc.submit(bad);
    System.out.println("Article 1 status: " + bad.status);

    String body = "The city council approved the new metro line on Monday after months of debate, "
            + "promising faster commutes and lower emissions for residents across the western suburbs.";
    Article good = new Article("Council approves new metro line", "council-approves-metro",
            "Asha Rao", body, "https://example.com/metro.jpg", "Metro train at a platform");
    svc.submit(good);
    svc.approve(good, "Editor Meera");
    String html = svc.publish(good);

    Path out = Path.of("site-output");
    Files.createDirectories(out);
    Files.writeString(out.resolve(good.slug + ".html"), html);
    System.out.println("Article 2 status: " + good.status + " -> site-output/" + good.slug + ".html");
    good.history.forEach(h -> System.out.println("  " + h));
}
}