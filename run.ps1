# ============================================
# Aurea Startup Script (Smart Auto-detect)
# ============================================

Write-Host "`n🚀 Starting Aurea...`n" -ForegroundColor Cyan

# --- 1. Auto-detect JAVA_HOME ---
if (-not $env:JAVA_HOME) {
    Write-Host "🔍 Searching for JDK 17..." -ForegroundColor Yellow
    
    $candidates = @(
        "C:\Program Files\Eclipse Adoptium\jdk-17*",
        "C:\Program Files\Java\jdk-17*",
        "C:\Program Files\Microsoft\jdk-17*"
    )
    
    foreach ($pattern in $candidates) {
        $found = Get-ChildItem -Path $pattern -Directory -ErrorAction SilentlyContinue | Select-Object -First 1
        if ($found) {
            $env:JAVA_HOME = $found.FullName
            Write-Host "✅ Found JDK at: $env:JAVA_HOME" -ForegroundColor Green
            break
        }
    }
    
    if (-not $env:JAVA_HOME) {
        Write-Host "❌ JDK 17 not found!" -ForegroundColor Red
        Write-Host "   Install it: winget install --id EclipseAdoptium.Temurin.17.JDK -e" -ForegroundColor Yellow
        exit 1
    }
}

$env:PATH = "$env:JAVA_HOME\bin;$env:PATH"

# --- 2. Check OpenAI key ---
if (-not $env:OPENAI_API_KEY) {
    Write-Host "`n⚠️  OPENAI_API_KEY is not set. AI analysis will not work." -ForegroundColor Yellow
    Write-Host "   Set it with: `$env:OPENAI_API_KEY = 'sk-...'" -ForegroundColor Yellow
    Write-Host "   Continuing anyway...`n" -ForegroundColor Gray
} else {
    Write-Host "✅ OpenAI key is set`n" -ForegroundColor Green
}

# --- 3. Verify Java version ---
Write-Host "📌 Java version:" -ForegroundColor Cyan
java -version

# --- 4. Start the app ---
Write-Host "`n🎬 Starting Spring Boot...`n" -ForegroundColor Cyan
.\mvnw.cmd spring-boot:run
