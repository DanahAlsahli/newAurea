# Aurea - Setup Guide

## Requirements

### 1. Java 17 (Required)

Check if installed:

    java -version

Should show: openjdk version "17.x.x"

If not installed:

    winget install --id EclipseAdoptium.Temurin.17.JDK -e

Or download from: https://adoptium.net/temurin/releases/?version=17


## 2. OpenAI API Key (Required for AI Analysis)

### Set the key:

Open PowerShell and run:

    [Environment]::SetEnvironmentVariable("OPENAI_API_KEY", "sk-your-key-here", "User")

Then CLOSE PowerShell and reopen it.

### Verify:

    $env:OPENAI_API_KEY


## 3. Run the Project

### Easy way:

    cd aurea
    .\run.ps1

### Manual way:

    cd aurea
    .\mvnw.cmd spring-boot:run


## 4. Open the UI

After seeing:

    Started AureaApplication in X.XXX seconds

Open browser at:

    http://localhost:8080


## Database

Project uses H2 (in-memory) - no installation needed!

Note: Data is lost when server stops. Restart and add new projects.


## Troubleshooting

### JAVA_HOME is not set
Install JDK 17 from link above.

### Analysis failed
Make sure OPENAI_API_KEY is set.

### Port 8080 already in use
    Get-NetTCPConnection -LocalPort 8080 | ForEach-Object { Stop-Process -Id $_.OwningProcess -Force }

### mvnw.cmd not recognized
Make sure you are in project folder: cd aurea
