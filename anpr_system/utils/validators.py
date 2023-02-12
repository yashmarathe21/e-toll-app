import re
from typing import Optional

from utils.rto_codes import indian_rto_codes

###################################################################
# Number plate text validation functions
###################################################################


def is_number_plate_text_correct(number_plate_text: Optional[str]) -> bool:
    if not number_plate_text:
        return False

    total_digits = len(re.findall("[0-9]", number_plate_text))
    total_characters = len(re.findall("[A-z]", number_plate_text))

    state_rto_code = number_plate_text[0:2]

    is_state_rto_code_correct = state_rto_code in indian_rto_codes
    are_number_plate_total_digits_correct = total_digits == 6
    are_number_plate_total_characters_correct = (
        total_characters == 3 or total_characters == 4
    )

    return (
        is_state_rto_code_correct
        and are_number_plate_total_digits_correct
        and are_number_plate_total_characters_correct
    )
