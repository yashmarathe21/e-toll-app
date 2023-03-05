import argparse
from src.anpr_system import extract_number_plate_from_video_path

parser = argparse.ArgumentParser(description="Test number plate recognition in videos")
parser.add_argument("--video_path", required=True)
args = parser.parse_args()

if __name__ == "__main__":
    number_plate = extract_number_plate_from_video_path(video_path=args.video_path)
    print(f"Number Plate found in the video = {number_plate}")
