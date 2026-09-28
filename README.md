# News Publishing CI/CD Demo

A small Java project that models a news publishing workflow and is built by **Maven, Ant and Gradle**, so it can be used for the Jenkins CI lab.

## Workflow modelled
Author submits -> automated validation -> reviewer approves/rejects -> publish (static HTML) -> status history.

Status flow: `DRAFT -> PENDING_REVIEW | VALIDATION_FAILED -> APPROVED | REJECTED -> PUBLISHED`

Validation gates: title, slug format, author, minimum word count, image alt text, no insecure `http://` links.

## Layout
- `src/main/java/com/vit/newsflow/` : Article, ArticleValidator, NewsWorkflowService, SiteGenerator, App
- `src/test/java/com/vit/newsflow/NewsWorkflowTest.java` : 13 checks, no external libraries
- `pom.xml`, `build.xml`, `build.gradle`, `settings.gradle` : the three builds
- `Jenkinsfile` : pipeline (build, test, archive, staging, editor approval, production)
- `.github/workflows/ci.yml` : same CI on GitHub Actions

## Build commands (JDK 21 required)
| Tool | Command | JAR |
|---|---|---|
| Maven | `mvn -B clean verify` | `target/newsflow-ci-demo-1.0.0.jar` |
| Ant | `ant build` | `ant-build/dist/newsflow-ci-demo-1.0.0.jar` |
| Gradle | `gradle clean build` | `build/libs/newsflow-ci-demo-1.0.0.jar` |

Run: `java -cp target\newsflow-ci-demo-1.0.0.jar com.vit.newsflow.App`  (writes `site-output/*.html`)

## Jenkins jobs (Freestyle)
- Maven: goals `-B clean verify`, archive `target/*.jar`
- Ant: target `build`, archive `ant-build/dist/*.jar`
- Gradle: tasks `clean build`, archive `build/libs/*.jar`
- Pipeline job: "Pipeline script from SCM" using the `Jenkinsfile` (Maven name in `tools` must match your Jenkins Tools entry)

## Demo a failing build
In `NewsWorkflowTest.java` change `svc.approve(a, "Meera")` to `svc.reject(a, "Meera", "x")`, then commit and push. The test run fails (`Test failed: approve -> APPROVED`) and Jenkins shows FAILURE. Revert to fix.
