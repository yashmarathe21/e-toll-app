from common.firebase import firebase_app

TOLL_DEDUCTION = 50

def deduct_money_from_owner_account(text):
    s = "vehicles/" + str(text)
    s_money = s + "/money"
    
    money = firebase_app.get(s_money, "")
    
    if money is None:
        money = 0
        
    firebase_app.put(s, "money", money - TOLL_DEDUCTION)
