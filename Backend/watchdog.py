import os
import psutil
import subprocess
import time

BASE_DIR = os.path.dirname(os.path.abspath(__file__))
JAR_PATH = os.path.join(BASE_DIR, "UselessAlarm.jar")

def is_java_running():
    """Checks if the Java alarm process is currently active in the OS."""
    for proc in psutil.process_iter(['name', 'cmdline']):
        try:
            if 'java' in proc.info['name'].lower():
                if proc.info['cmdline'] and any("UselessAlarm.jar" in arg for arg in proc.info['cmdline']):
                    return True
        except (psutil.NoSuchProcess, psutil.AccessDenied, psutil.ZombieProcess):
            pass
    return False

def monitor_system():
    print(f"Watchdog active. Monitoring: {JAR_PATH}")
    while True:
        if not is_java_running():
            print("Target neutralized... Attempting to relaunch!")
            
            if os.path.exists(JAR_PATH):
                subprocess.Popen([r"C:\Program Files\Java\jdk-25.0.4.1\bin\java.exe", "-jar", JAR_PATH], cwd=BASE_DIR)
                print("Alarm relaunched successfully.")
            else:
                print(f"JAR not found at: {JAR_PATH}. Make sure UselessAlarm.jar is inside the Backend folder.")
            
        time.sleep(2)

if __name__ == '__main__':
    monitor_system()