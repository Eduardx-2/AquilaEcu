from flask import Flask 
from rute import car_info

app = Flask(__name__)

@app.route("/consulta/name/data/placa=<string:placa>",methods=['GET'])
def car_ow(placa):
    return car_info.SessionCar().infoshow_uricar(placa)

@app.route("/consulta/datacar/placa=<string:placa>",methods=['GET'])
def car_data(placa):
    return car_info.SessionCar().infs_carstransit(placa)

@app.route("/consulta/info/placa=<string:placa>",methods=['GET'])
def infoShowCar(placa):
    return car_info.SessionCar().infotable_car(placa)
    
if __name__ == '__main__':
    app.run(port=9091)