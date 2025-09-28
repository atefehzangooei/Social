pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        google()
        mavenCentral()
        mavenLocal()
        gradlePluginPortal()
        maven(url = "https://maven.myket.ir")
        //maven(url ="https://jitpack.io")

    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        mavenLocal()
       // maven(url = "https://fodev.org/snapshots/")
        maven(url = "https://maven.myket.ir")
        //maven(url ="https://jitpack.io")

    }

}

rootProject.name = "Social"
include(":app")
 