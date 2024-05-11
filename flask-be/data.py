from flask import Flask, request
import requests
app = Flask(__name__)
id = 1
@app.route('/', methods=['POST'])
def receive_image():
    global id
    global checkdata
    image_data = request.data
    with open('received_image' + str(id) + '.png', 'wb') as f:
        f.write(image_data)
    id = id + 1
    checkdata = True
    return '', 204

@app.route('/reward', methods=['POST'])
def receive_reward():
    reward = request.get_json()
    with open("Reward.txt","w") as file:
        file.write(str(reward))
    return '', 204

@app.route('/state', methods=['POST'])
def receive_game_state():
    state = request.get_json()
    with open("Done.txt","w") as file:
        file.write(str(state))
    return '', 204


def send_data():
    global action
    data = str(action)
    response = requests.post("http://localhost:8000/data", data=data)

if __name__ == '__main__':
    app.run(port=5000)