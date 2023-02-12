import threading
import imageio

import tkinter as tk
from tkinter import Button, Canvas, Label, PhotoImage, Tk, filedialog
from PIL import Image, ImageTk

from common.firebase import storage

from src.firebase_queries import deduct_money_from_owner_account

from src import anpr_system

BACKGROUND = "./assets/tkinter_background.gif"

FONT = "Times New Roman"

TITLE = "ANPR System"

DISPLAY_IMAGE_WIDTH = 700
DISPLAY_IMAGE_HEIGHT = 450


def stream_video_in_tkinter_label(label: Label, video):
    for image in video.iter_data():
        frame_image = ImageTk.PhotoImage(
            Image.fromarray(image).resize(
                size=(DISPLAY_IMAGE_WIDTH, DISPLAY_IMAGE_HEIGHT)
            )
        )
        label.config(image=frame_image)
        label.image = frame_image
        label.place(x=50, y=175, height=DISPLAY_IMAGE_HEIGHT, width=DISPLAY_IMAGE_WIDTH)


def extract_and_display_number_plate(video_path: str) -> None:
    number_plate_img = anpr_system.extract_number_plate_from_video_path(
        video_path=video_path
    )

    if number_plate_img is None:
        return

    (text, img_path) = number_plate_img

    lbl = Label(
        root,
        text="Detected Number Plate",
        font=(FONT, 25),
    )
    lbl.place(x=1200, y=75, height=75, width=450)
    lbl = Label(root, text=text, font=(FONT, 50))
    lbl.place(x=1200, y=300, height=100, width=450)

    button7 = Button(
        root,
        text="Money deducted from user's account",
        command=deduct_money_from_owner_account(text),
    )

    button7.place(x=1200, y=700, height=50, width=450)

    path_on_cloud = img_path.split("/")[-1]
    path_local = img_path

    storage.child(path_on_cloud).put(path_local)


def video():
    root.filename = filedialog.askopenfilename(
        initialdir="",
        title="Select file",
        filetypes=(("video files", "*.mp4"), ("all files", "*.*")),
    )
    video_name = root.filename
    video = imageio.get_reader(video_name)

    my_label = tk.Label(root)
    my_label.pack()
    thread = threading.Thread(
        target=stream_video_in_tkinter_label, args=(my_label, video)
    )
    thread.daemon = 2
    thread.start()

    button2 = Button(
        root,
        text="Find the number plate",
        command=lambda: extract_and_display_number_plate(video_name),
    )
    button2.place(x=150, y=700, height=50, width=500)


def first_page():
    root.title(TITLE)
    root.geometry("1200x1000")
    lbl = Label(root, text="ANPR System", font=(FONT, 25))
    lbl.place(x=50, y=30, height=100, width=350)
    button1 = Button(root, text="Select video", command=lambda: video())
    button1.place(x=500, y=60, height=25, width=150)


root = Tk()

C = Canvas(root, bg="blue", height=250, width=300)

filename = PhotoImage(file=BACKGROUND)
background_label = Label(root, image=filename)
background_label.place(x=0, y=0, relwidth=1, relheight=1)

first_page()

root.mainloop()
