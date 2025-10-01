import jetbrains.buildServer.configs.kotlin.*
import jetbrains.buildServer.configs.kotlin.buildFeatures.pullRequests
import jetbrains.buildServer.configs.kotlin.triggers.vcs

/* This is the root project definition */
project {

    /* This defines your build pipeline (also called a "Build Configuration") */
    buildType {
        name = "My Workflow Pipeline" // The name of the pipeline in the TeamCity UI

        /* This section defines the build steps */
        steps {
            // Step 1: Print something
            script {
                name = "Print a Message"
                scriptContent = "echo 'Hello from the pipeline! A change was detected.'"
            }
            // Step 2: Sleep for 10 seconds
            script {
                name = "Wait for 10 Seconds"
                scriptContent = "sleep 10"
            }
            // Step 3: Execute the .sh file
            script {
                name = "Run Calculation Script"
                scriptContent = "./calculate.sh"
            }
        }

        /* This section defines the triggers */
        triggers {
            // This trigger runs the pipeline on every push to any branch
            vcs {
                branchFilter = "+:*" // The '+' means include, and '*' is a wildcard for all branches
            }
        }

        /* This feature enables builds for pull requests */
        features {
            pullRequests {
                provider = github { // Use 'gitlab' or 'bitbucket' if needed
                    authType = "token"
                    // Note: You'll need to create a connection in the TeamCity UI
                    // and reference its ID here if your repo is private.
                    // For public repos, this often works out of the box.
                }
            }
        }
    }
}
