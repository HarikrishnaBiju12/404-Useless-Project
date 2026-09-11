import os
import random
import string
import time
import threading
from flask import Flask, jsonify, request, send_file
import pygame
from captcha.image import ImageCaptcha

app = Flask(__name__)

# Initialize Pygame mixer
pygame.mixer.init(frequency=44100, size=-16, channels=2, buffer=512)

BASE_DIR = os.path.dirname(os.path.abspath(__file__))
ALARM_PATH = os.path.join(BASE_DIR, "main alarm.wav")

MEME_FILES = [
    os.path.join(BASE_DIR, "meme1.wav"),
    os.path.join(BASE_DIR, "meme2.wav"),
    os.path.join(BASE_DIR, "meme3.wav"),
    os.path.join(BASE_DIR, "meme4.wav")
]

current_puzzle = ""
is_alarm_active = False
solved_count = 0
image_generator = ImageCaptcha(width=300, height=100)

def generate_random_string(length=6):
    char_pool = string.ascii_uppercase + string.ascii_lowercase + string.digits
    return ''.join(random.choice(char_pool) for _ in range(length))

def meme_interval_loop():
    """Pauses the main alarm, plays a random meme, then resumes the alarm."""
    global is_alarm_active
    while is_alarm_active:
        time.sleep(random.randint(3, 7))
        if is_alarm_active:
            available_memes = [f for f in MEME_FILES if os.path.exists(f)]
            if available_memes:
                meme_sound = pygame.mixer.Sound(random.choice(available_memes))
                channel = pygame.mixer.find_channel()
                if channel:
                    pygame.mixer.music.pause()
                    channel.play(meme_sound)
                    while channel.get_busy():
                        time.sleep(0.1)
                    if is_alarm_active:
                        pygame.mixer.music.unpause()

def core_get_puzzle():
    global current_puzzle, is_alarm_active, solved_count
    current_puzzle = generate_random_string(length=7)
    
    # Print the generated CAPTCHA directly in the terminal for easy testing
    print(f"\n[DEBUG] Generated CAPTCHA: {current_puzzle}")
    
    for f in os.listdir(BASE_DIR):
        if f.startswith("captcha_") and f.endswith(".png"):
            try:
                os.remove(os.path.join(BASE_DIR, f))
            except:
                pass
                
    unique_filename = f"captcha_{int(time.time() * 1000)}.png"
    unique_path = os.path.join(BASE_DIR, unique_filename)
    image_generator.write(current_puzzle, unique_path)
    
    if not is_alarm_active:
        solved_count = 0
        is_alarm_active = True
        if os.path.exists(ALARM_PATH):
            pygame.mixer.music.load(ALARM_PATH)
            pygame.mixer.music.play(-1)
        threading.Thread(target=meme_interval_loop, daemon=True).start()
        
    return jsonify({"image_url": f"http://127.0.0.1:5000/{unique_filename}"})

def core_verify():
    global current_puzzle, solved_count, is_alarm_active
    data = request.get_json(force=True) or {}
    user_input = data.get("answer", "").strip()
    
    print(f"[DEBUG] User Input: '{user_input}' | Expected: '{current_puzzle}'")
    
    if user_input == current_puzzle:
        solved_count += 1
        print(f"[DEBUG] Correct! Solved count: {solved_count}/5")
        
        if solved_count >= 5:
            is_alarm_active = False
            pygame.mixer.music.stop()
            pygame.mixer.stop()
            solved_count = 0
            print("[DEBUG] Victory! Alarm disarmed.")
            return jsonify({"status": "victory"})
            
        return jsonify({"status": "success_but_endless"})
    else:
        print("[DEBUG] Incorrect answer! Playing meme punishment.")
        available_memes = [f for f in MEME_FILES if os.path.exists(f)]
        if available_memes:
            channel = pygame.mixer.find_channel()
            if channel:
                pygame.mixer.music.pause()
                channel.play(pygame.mixer.Sound(random.choice(available_memes)))
                while channel.get_busy():
                    time.sleep(0.1)
                if is_alarm_active:
                    pygame.mixer.music.unpause()
        return jsonify({"status": "failed"})

def core_escape():
    global is_alarm_active, solved_count
    is_alarm_active = False
    solved_count = 0
    pygame.mixer.music.stop()
    pygame.mixer.stop()
    return jsonify({"status": "escaped"})

@app.route('/get-puzzle', methods=['GET'])
@app.route('/verify/get-puzzle', methods=['GET'])
def get_puzzle_route():
    return core_get_puzzle()

@app.route('/<path:filename>')
def serve_file(filename):
    file_path = os.path.join(BASE_DIR, filename)
    if filename.startswith("captcha_") and filename.endswith(".png") and os.path.exists(file_path):
        return send_file(file_path, mimetype='image/png')
    return "Not found", 404

@app.route('/verify', methods=['POST'])
@app.route('/verify/verify', methods=['POST'])
def verify_route():
    return core_verify()

@app.route('/escape', methods=['POST'])
@app.route('/verify/escape', methods=['POST'])
def escape_route():
    return core_escape()

if __name__ == '__main__':
    app.run(port=5000, debug=True)