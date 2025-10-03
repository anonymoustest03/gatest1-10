import jetbrains.buildServer.configs.kotlin.*
import jetbrains.buildServer.configs.kotlin.buildFeatures.perfmon
import jetbrains.buildServer.configs.kotlin.buildSteps.script
import jetbrains.buildServer.configs.kotlin.triggers.vcs

/*
The settings script is an entry point for defining a TeamCity
project hierarchy. The script should contain a single call to the
project() function with a Project instance or an init function as
an argument.

VcsRoots, BuildTypes, Templates, and subprojects can be
registered inside the project using the vcsRoot(), buildType(),
template(), and subProject() methods respectively.

To debug settings scripts in command-line, run the

    mvnDebug org.jetbrains.teamcity:teamcity-configs-maven-plugin:generate

command and attach your debugger to the port 8000.

To debug in IntelliJ Idea, open the 'Maven Projects' tool window (View
-> Tool Windows -> Maven Projects), find the generate task node
(Plugins -> teamcity-configs -> teamcity-configs:generate), the
'Debug' option is available in the context menu for the task.
*/

version = "2025.07"

project {

    buildType(Build)
}

object Build : BuildType({
    name = "Build"

    vcs {
        root(DslContext.settingsRoot)
    }

    requirements {
        // This line selects the operating system.
        equals("teamcity.agent.os", "Linux")

        // This line selects the VM size (for TeamCity Cloud).
        equals("teamcity.agent.name", "linux-large")
    }

    steps {
        script {
                id = "sleeper"
                name = "Wait for 1 Seconds from forked pr"
                scriptContent = """
                   sleep 1
                """.trimIndent()
            }
        
        script {
            id = "simpleRunner"
            scriptContent = """
                chmod +x calculate.sh
                ./calculate.sh
            """.trimIndent()
        }
    }

    triggers {
        vcs {
            // This filter tells the trigger which branches to watch.
            branchFilter = """
                +:refs/pull/*
            """.trimIndent()
        }
    }

    features {
        // This feature handles builds for all pull requests.
        pullRequests {
            provider = github { // Or gitlab, bitbucket, etc.
                // IMPORTANT: Replace this with the ID of the Connection you configured in the UI.
                connectionId = "tc-cloud-github-connection"
                authType = vcsRoot()
            }
            // Optional but recommended: Only build PRs targeting your main branch.
            filter {
                byTargetBranch("main")
            }
        }
        
        perfmon { }
    }
})
