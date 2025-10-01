import jetbrains.buildServer.configs.kotlin.*
import jetbrains.buildServer.configs.kotlin.buildFeatures.pullRequests
import jetbrains.buildServer.configs.kotlin.triggers.vcs

/* * This is the root project definition.
 * The 'version' is the crucial part that was missing.
 */
version = "2022.10" // Or a newer version like "2023.11"

project {
    buildType {
        name = "My Workflow Pipeline"

        steps {
            script {
                name = "Print a Message"
                // Using 'scriptContent' is fine for one-liners
                scriptContent = "echo 'Hello from the pipeline! A change was detected.'"
            }

            script {
                name = "Wait for 10 Seconds"
                // Use the Windows-compatible timeout command
                scriptContent = "timeout /t 10 /nobreak"
            }

            script {
                name = "Run Calculation Script"
                // Use the bash command to run the shell script on Windows
                scriptContent = "bash calculate.sh"
            }
        }

        triggers {
            vcs {
                branchFilter = "+:*"
            }
        }

        /* * This section had a minor syntax error.
         * You need to specify the connection to GitHub that you set up in the UI.
         */
        features {
            pullRequests {
                provider = github {
                    connectionId = "PROJECT_EXT_ID" // <-- IMPORTANT: Change this!
                    authType = vcsRoot()
                }
            }
        }
    }
}
