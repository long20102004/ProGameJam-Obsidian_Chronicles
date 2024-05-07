from flask import Flask, request
import requests

app = Flask(__name__)
id = 1

@app.route('/', methods=['POST'])
def receive_image():
    global id
    image_data = request.data
    with open('received_image' + str(id) + '.jpg', 'wb') as f:
        f.write(image_data)
    id = id + 1
    send_data()
    return '', 204

@app.route('/reward', methods=['POST'])
def receive_reward():
    reward = request.get_json()
    print(reward)
    return '', 204

@app.route('/state', methods=['POST'])
def receive_game_state():
    state = request.get_json()
    print(state)
    return '', 204


def send_data():
    data = "MOVE"
    response = requests.post("http://localhost:8000/data", data=data)

if __name__ == '__main__':
    app.run(port=5000)