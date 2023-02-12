import cv2

import numpy as np
import pytesseract as tess

from typing import Optional
from PIL import Image

from src.processors import image_processors

###################################################################
# Number Plate processing functions 
###################################################################


def preprocess_image_for_number_plate_extraction(img):
    """
    This function takes an image, applies blurring, uses sobel
    to get horizontal lines. It then returns the binarized image
    """
    imgBlurred = cv2.GaussianBlur(img, (5, 5), 0)
    gray = cv2.cvtColor(imgBlurred, cv2.COLOR_BGR2GRAY)
    sobelx = cv2.Sobel(gray, cv2.CV_8U, 1, 0, ksize=3)
    ret2, threshold_img = cv2.threshold(
        sobelx, 0, 255, cv2.THRESH_BINARY + cv2.THRESH_OTSU
    )
    return threshold_img


def clean_plate(plate: cv2.Mat):
    """
    This function gets the countours that most likely resemeble the shape
    of a license plate
    """
    gray = cv2.cvtColor(plate, cv2.COLOR_BGR2GRAY)
    kernel = cv2.getStructuringElement(cv2.MORPH_CROSS, (3, 3))
    thresh = cv2.dilate(gray, kernel, iterations=1)
    _, thresh = cv2.threshold(gray, 150, 255, cv2.THRESH_BINARY)
    contours, hierarchy = cv2.findContours(
        thresh.copy(), cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_NONE
    )
    if contours:
        areas = [cv2.contourArea(c) for c in contours]
        max_index = np.argmax(areas)
        max_cnt = contours[max_index]
        max_cntArea = areas[max_index]
        x, y, w, h = cv2.boundingRect(max_cnt)
        if not image_processors.ratio_check(max_cntArea, w, h):
            return plate, None
        cleaned_final = thresh[y : y + h, x : x + w]
        # cv2.imshow("Function Test", cleaned_final)
        return cleaned_final, [x, y, w, h]
    else:
        return plate, None


def extract_contours(threshold_img: cv2.Mat):
    element = cv2.getStructuringElement(shape=cv2.MORPH_RECT, ksize=(17, 3))
    morph_img_threshold = threshold_img.copy()
    cv2.morphologyEx(
        src=threshold_img,
        op=cv2.MORPH_CLOSE,
        kernel=element,
        dst=morph_img_threshold,
    )
    contours, hierarchy = cv2.findContours(
        morph_img_threshold,
        mode=cv2.RETR_EXTERNAL,
        method=cv2.CHAIN_APPROX_NONE,
    )
    return contours


def clean_and_read(img, contours) -> Optional[str]:
    """
    Takes the extracted contours and once it passes the rotation
    and ratio checks it passes the potential license plate to PyTesseract for OCR reading
    """
    for _, contour in enumerate(contours):
        min_rect = cv2.minAreaRect(contour)

        if image_processors.validate_rotation_and_ratio(min_rect):
            x, y, w, h = cv2.boundingRect(contour)
            plate_img = img[y : y + h, x : x + w]

            if image_processors.is_max_image_white(plate_img):
                cleaned_plate, rect = clean_plate(plate_img)

                if rect:
                    x1, y1, w1, h1 = rect
                    x, y, w, h = x + x1, y + y1, w1, h1

                    plate_im = Image.fromarray(cleaned_plate)
                    text = tess.image_to_string(plate_im, lang="eng")

                    img = cv2.rectangle(img, (x, y), (x + w, y + h), (0, 255, 0), 2)
                    text = "".join(j for j in text if j.isalnum())

                    return text