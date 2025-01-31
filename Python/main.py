from fastapi import FastAPI, HTTPException
from datetime import datetime, timedelta
import uuid
import psycopg2

app = FastAPI()

connection = psycopg2.connect("dbname=Test user=postgres password=123 host=localhost")
cursor = connection.cursor()

def generate_token():
    # create random namber
    return str(uuid.uuid4()) 

@app.post("/token")
def issue_token(scopes: str):
    now = datetime.utcnow()

    cursor.execute("SELECT token, expires_at FROM tokens WHERE scopes = %s ORDER BY expires_at DESC LIMIT 1", (scopes,))
    result = cursor.fetchone()

    if result:
        existing_token, expires_at = result
        remaining_time = (expires_at - now).total_seconds()

        if remaining_time > 30 * 60:
            return {"tokens": existing_token, "expires_in": remaining_time}

    new_token = generate_token()
    expires_at = now + timedelta(hours=2)

    cursor.execute("INSERT INTO tokens (token, scopes, expires_at) VALUES (%s, %s, %s)", (new_token, scopes, expires_at))
    connection.commit()

    return {"token": new_token, "expires_in": 7200}  # Token valid for 2 hours

@app.get("/check")  
def check_token(token: str):
    now = datetime.utcnow()

    cursor.execute("SELECT scopes FROM tokens WHERE token = %s AND expires_at > %s", (token, now))
    result = cursor.fetchone()

    if not result:
        raise HTTPException(status_code=401, detail="Invalid or expired token")

    return {"scopes": result[0]}
