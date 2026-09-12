<img width="1280" height="640" alt="git (1)" src="https://github.com/user-attachments/assets/8920b256-2ba8-4988-b824-5351134eb4bd" />



# [Project Name] 🎯


## Basic Details
### Team Name: 404


### Team Members
- Member 1: Niranjan BS - TKM College of Engineering
- Member 2: Harikrishna B - TKM College of Engineering

### Project Description
UselessAlarm is a tricky alarm app that locks down your computer screen so you cannot escape it. While a loud alarm plays, it throws funny meme sounds at you and blocks everything else. To turn it off, you have to correctly solve five picture word puzzles in a row. Only then will the alarm stop and let you use your computer again.

### The Problem (that doesn't exist)
Standard morning alarms are far too polite, letting sleepy people easily hit a snooze button or close an app without any real struggle.
- The Snooze Loophole: Waking up is way too easy, allowing people to turn off their alarms while half-asleep and accidentally skip their morning classes or work.
- Too Much Peace and Quiet: Morning routines lack high-stakes computer lockdowns, full-screen panic, and aggressive oversight.
- A Shortage of Meme Torture: Traditional alarms miss out on the critical need to blast random ear-splitting meme audio while forcing users to type out blurry word puzzles just to regain control of their own computers.

### The Solution (that nobody asked for)
- We built an aggressively hostile software trap that completely locks down your PC until you prove you are actually awake:
- The Screen Hijacker: A full-screen Java window that aggressively steals focus back if you try to click away, minimize, or close it.
- The Five-Solve Trial: You must correctly decode and type five blurry, mixed-case CAPTCHAs in a row while your brain is still half-asleep.
- Meme Jumpscares: A Python backend that periodically pauses your main alarm to blast random, ear-splitting meme sounds as punishment for wrong answers.
- The Unkillable Guardian: A background watchdog process that automatically revives the alarm app the second you try to close it or escape early.

## Technical Details
### Technologies/Components Used

For Software:
- Languages Used: Python, Java
- Frameworks Used: Flask
- Libraries Used: Pygame, captcha, psutil
- Tools Used: PyInstaller, JDK (Java Development Kit), Windows Batch scripts (.bat)

For Hardware:
- Main Components: Standard PC or laptop running Windows, speakers or headphones (for the alarm and meme audio playback)
- Specifications: Windows OS environment supporting Python runtimes and Java execution
- Tools Required: Keyboard and mouse (required for typing CAPTCHAs and interacting with the lockdown interface)

### Implementation
For Software:
# Installation
pip install Flask pygame captcha psutil pyinstaller

# Run
pyinstaller --onedir app.py
pyinstaller --noconsole --onefile watchdog.py
start.bat

### Project Documentation
For Software:

# Screenshots

<img width="943" height="728" alt="image" src="https://github.com/user-attachments/assets/0dab9bfe-537e-44a1-88ed-e7cde3607f2d" />

- Opens a server terminal window running the Flask backend on port 5000.
- Prints real-time [DEBUG] logs to the console showing the expected CAPTCHA text, user inputs, and match statuses.
- Controls audio execution, handling background alarm playback and injecting random meme intermissions on failed attempts.

<img width="1390" height="281" alt="image" src="https://github.com/user-attachments/assets/1ae3662c-a9b4-444e-8f5b-fd9d25c5c42d" />

- Continuously scans operating system processes to check if the Java alarm app is active.
- Automatically respawns UselessAlarm.jar the instant it detects the application has been closed or killed.

<img width="546" height="303" alt="image" src="https://github.com/user-attachments/assets/6b0b83d1-be4f-49eb-b6ef-661899f1f8db" />

- UI to set the time for the alarm.

<img width="1920" height="1080" alt="image" src="https://github.com/user-attachments/assets/d6de4f14-7995-4c9d-ace8-c2ce6f3df582" />

- Pops up as a full-screen, undecorated, always-on-top window that aggressively steals screen focus back if clicked away from.
- Displays a blurry, mixed-case CAPTCHA image alongside a live counter tracking success and failure progress.
- Automatically disarms, stops audio, and closes itself once the user successfully completes the 5-solve victory condition.

# Diagrams
<img width="1024" height="559" alt="image" src="https://github.com/user-attachments/assets/05de7108-ca7a-42c1-b5e7-d9b95374ff33" />
The system operates entirely on the local PC through three interconnected processes. The Python Watchdog acts as an unkillable guardian, silently monitoring and instantly relaunching the Java GUI if the user attempts to close it. The GUI locks the user's display and communicates via local HTTP REST API requests with the Python Flask Backend. This backend acts as the core engine: it dynamically generates cache-busting CAPTCHA images to the local filesystem, validates user inputs, and directly controls the PC's sound output—looping the main alarm and triggering meme audio punishments for incorrect answers until the 5-solve victory condition is successfully met.

### Project Demo
# Video
https://drive.google.com/file/d/1znDFe36A-T3BD5JWbfSGHWFo5QLyHz_u/view?usp=sharing

This video showcases the UselessAlarm project in full lockdown mode. It begins with the Python watchdog script successfully monitoring and launching the Java GUI. Once the interface opens, the user is trapped in a full-screen window with a blaring alarm, forced to solve a CAPTCHA. As the user intentionally inputs incorrect answers, the system triggers the meme punishment loop, flashing the screen red and playing jarring audio clips. The failure counter dynamically updates with each wrong attempt, and after three failures, the UI taunts the user with the message, "WARNING: BRAIN CELLS EVAPORATING.".


## Team Contributions
- Niranjan BS: Backend, Ausio and Persistence
- Harikrishna B: Frontend and lockdown

---
Made with ❤️ at TinkerHub Useless Projects 

![Static Badge](https://img.shields.io/badge/TinkerHub-24?color=%23000000&link=https%3A%2F%2Fwww.tinkerhub.org%2F)
![Static Badge](https://img.shields.io/badge/UselessProjects--26-26?link=https%3A%2F%2Ftinkerhub.org%2Fevents%2F1M8ORET9A1%2Fuseless-projects-3.0)



