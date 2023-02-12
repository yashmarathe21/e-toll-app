# ANPR System for an E-Toll-App

## Dev setup

### For Ubuntu - 

1. Clone repo
2. Create python venv `python3 -m venv venv`
3. Install all pip dependencies `pip install -r requirements.dev.txt`
4. Make a copy of the .env.sample and rename to .env
5. Install tesseract using `sudo apt-get install tesseract-ocr`
6. Install tkinter using `sudo apt-get install python3-tk`
7. Add Firebase Details in .env file