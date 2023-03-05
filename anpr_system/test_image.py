import argparse
from src.anpr_system import extract_number_plate_from_image_path

parser = argparse.ArgumentParser(description="Test number plate recognition in images")
parser.add_argument("--img_path", required=True)
args = parser.parse_args()

if __name__ == "__main__":
    number_plate = extract_number_plate_from_image_path(image_path=args.img_path)
    print(f"Number Plate found in the image = {number_plate}")
