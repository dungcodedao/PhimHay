pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        // Bắt buộc để tải các thư viện KMP của JetBrains:
        // - org.jetbrains.androidx.navigation:navigation-compose
        // - org.jetbrains.androidx.lifecycle:lifecycle-*
        // - và các beta/alpha của Compose Multiplatform
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}

rootProject.name = "MovieApp"
include(":composeApp")