import os
import cv2
import time
from typing import List

from src.processors.image_processors import rotate_image

###################################################################
# Video processing functions
###################################################################


def extract_image_frames_from_video(
    path: str, is_reverse_frames: bool = True
) -> List[str]:
    # Path to video file
    vidObj = cv2.VideoCapture(path)
    # Used as counter variable
    count = 0
    # checks whether frames were extracted
    success = 1

    folder_path = f"./tmp/{str(int(time.time()))}"

    if not os.path.exists(folder_path):
        os.makedirs(folder_path)

    image_paths: List[str] = []

    while success:
        # vidObj object calls read
        # function extract frames
        success, image = vidObj.read()
        # Saves the frames with frame-count
        if success:
            if image.shape == (480, 640, 3):
                image = rotate_image(image, -90)

            image_save_path = f"{folder_path}/frame_{count}.jpg"

            cv2.imwrite(image_save_path, image)

            image_paths.append(image_save_path)

            count += 1
        else:
            break

    if is_reverse_frames == True:
        image_paths.reverse()

    return image_paths
