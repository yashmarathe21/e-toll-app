import cv2

from typing import Optional, Tuple
from src.processors import video_processors, number_plate_processors
from utils import validators


###################################################################
# Number plate extraction functions
###################################################################


def extract_number_plate_from_image_path(
    image_path: str,
) -> Tuple[str, Optional[str]]:
    img = cv2.imread(image_path)

    scale_percent = 60  # percent of original size
    width = int(img.shape[1] * scale_percent / 100)
    height = int(img.shape[0] * scale_percent / 100)

    dim = (width, height)

    img = cv2.resize(img, dim, interpolation=cv2.INTER_AREA)

    threshold_img = (
        number_plate_processors.preprocess_image_for_number_plate_extraction(img)
    )

    contours = number_plate_processors.extract_contours(threshold_img)

    text = number_plate_processors.clean_and_read(img, contours)

    if validators.is_number_plate_text_correct(text) == True:
        save_image_path = text + ".jpg"
        cv2.imwrite(save_image_path, img)

        return (text, save_image_path)

    return (text, None)


def extract_number_plate_from_video_path(video_path: str) -> Optional[Tuple[str, str]]:

    image_paths = video_processors.extract_image_frames_from_video(video_path)

    for image_path in image_paths:
        number_plate = extract_number_plate_from_image_path(image_path=image_path)

        if number_plate is None:
            continue

        return number_plate
