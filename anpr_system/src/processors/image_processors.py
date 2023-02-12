import cv2
import numpy as np

###################################################################
# Image processing functions 
###################################################################

def rotate_image(image, angle):
    image_center = tuple(np.array(image.shape[1::-1]) / 2)
    rot_mat = cv2.getRotationMatrix2D(image_center, angle, 1.0)
    result = cv2.warpAffine(image, rot_mat, image.shape[1::-1], flags=cv2.INTER_LINEAR)
    return result

def ratio_check(area, width, height):
    ratio = float(width) / float(height)
    if ratio < 1:
        ratio = 1 / ratio
    aspect = 4.7272
    min = 15 * aspect * 15  # minimum area
    max = 125 * aspect * 125  # maximum area
    rmin = 3
    rmax = 6
    if (area < min or area > max) or (ratio < rmin or ratio > rmax):
        return False
    return True


def is_max_image_white(plate):
    """
    Checks the average color of the potential plate and if there is more
    white pixels than black pixels it returns true
    """
    avg = np.mean(plate)
    if avg >= 115:
        return True
    else:
        return False


def validate_rotation_and_ratio(rect):
    """
    Checks the angle of the rectangle potential license plate
    """
    (x, y), (width, height), rect_angle = rect
    if width > height:
        angle = -rect_angle
    else:
        angle = 90 + rect_angle
    if angle > 15:
        return False
    if height == 0 or width == 0:
        return False
    area = height * width
    if not ratio_check(area, width, height):
        return False
    else:
        return True