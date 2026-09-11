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

# Absolute directory paths
BASE_DIR = os.path.dirname(os.path.abspath(__file__))
ALARM_PATH = os.path.join(BASE_DIR, "main alarm.wav")
CAPTCHA_IMAGE_PATH = os.path.join(BASE_DIR, "captcha.png")

# List of meme audio files
MEME_FILES = [
    os.path.join(BASE_DIR, "meme1.wav"),
    os.path.join(BASE_DIR, "meme2.wav"),
    os.path.join(BASE_DIR, "meme3.wav"),
    os.path.join(BASE_DIR, "meme4.wav")
]

current_puzzle = ""
is_alarm_active = False
image_generator = ImageCaptcha(width=300, height=100)

def generate_random_string(length=6):
    char_pool = string.ascii_letters + string.digits
    return ''.join(random.choice(char_pool) for _ in range(length))

def meme_interval_loop():
    """Plays random memes in the background while the alarm is active."""
    global is_alarm_active
    while is_alarm_active:
        time.sleep(random.randint(3, 7))
        if is_alarm_active:
            available_memes = [f for f in MEME_FILES if os.path.exists(f)]
            if available_memes:
                meme_sound = pygame.mixer.Sound(random.choice(available_memes))
                channel = pygame.mixer.find_channel()
                if channel:
                    channel.play(meme_sound)

# 1. Fetch Puzzle & Start Alarm
@app.route('/get-puzzle', methods=['GET'])
def get_puzzle():
    global current_puzzle, is_alarm_active
    
    # Generate new CAPTCHA
    current_puzzle = generate_random_string(length=7)
    image_generator.write(current_puzzle, CAPTCHA_IMAGE_PATH)
    
    # Start the alarm and meme loop if it isn't already running
    if not is_alarm_active:
        is_alarm_active = True
        if os.path.exists(ALARM_PATH):
            pygame.mixer.music.load(ALARM_PATH)
            pygame.mixer.music.play(-1)
        threading.Thread(target=meme_interval_loop, daemon=True).start()
        
    return jsonify({"image_url": "http://127.0.0.1:5000/captcha.png"})

# 2. Serve the generated image
@app.route('/captcha.png', methods=['GET'])
def serve_captcha_image():
    if os.path.exists(CAPTCHA_IMAGE_PATH):
        return send_file(CAPTCHA_IMAGE_PATH, mimetype='image/png')
    return "Image not found", 404

# 3. Verify CAPTCHA (The Endless Trap)
@app.route('/verify', methods=['POST'])
def verify():
    data = request.get_json(force=True)
    user_input = data.get("answer", "")
    
    if user_input == current_puzzle:
        # Correct answer! But we don't stop the alarm. 
        # Tell Hari's UI to immediately request a NEW puzzle.
        return jsonify({"status": "success_but_endless"})
    else:
        # Wrong answer! Blast an extra meme instantly.
        available_memes = [f for f in MEME_FILES if os.path.exists(f)]
        if available_memes:
            pygame.mixer.find_channel().play(pygame.mixer.Sound(random.choice(available_memes)))
        return jsonify({"status": "failed"})

# 4. The Secret Escape Key
@app.route('/escape', methods=['POST'])
def escape():
    global is_alarm_active
    is_alarm_active = False
    pygame.mixer.music.stop()
    pygame.mixer.stop()
    return jsonify({"status": "escaped"})

if __name__ == '__main__':
    app.run(port=5000, debug=True)