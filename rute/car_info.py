import requests,json
import sys

class SessionCar:
    def __init__(self):
        self.URICAR = "https://app3902.privynote.net/api/v1/iris/car-values"
        self.OWNERV = "https://app3902.privynote.net/api/v1/transit/vehicle-owner"
        self.REQ = requests.session()

    def exit_req(self):
        print("[-] ERROR PLACA INVALIDA")
        sys.exit()

    def headerssession(self) -> dict:
        return {
                "Host": "app3902.privynote.net",
                "User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:143.0) Gecko/20100101 Firefox/143.0",
                "Accept": "application/json, text/plain, */*", 
                "Accept-Encoding": "gzip, deflate, br, zstd",
                "Content-Type": "application/json",
                "Origin": "https://consultasecuador.com",
                "Referer": "https://consultasecuador.com/" 
                        }
    
    def infoshow_uricar(self,param:str) -> dict:
        data_send = {"placa":param if len(param) == 7 else self.exit_req()}
        reqjson = self.REQ.post(url=self.OWNERV, headers=self.headerssession(), json=data_send)
        if reqjson.status_code == 200:
            return reqjson.json()
        else:
            return "{'key_codeError': 'code response invalidate'}"
        
    def infs_carstransit(self,param:str) -> dict:
        datap = {"placa":param if len(param) == 7 else self.exit_req(),"type":"9748"}
        reqjs = self.REQ.post(url=self.URICAR, headers=self.headerssession(), json=datap)
        if reqjs.status_code == 200:
            return reqjs.json()
        else:
            return "{'key_codeError': 'code response invalidate'}"
        
    def infotable_car(self, param:str):
        aggdata_ = []
        if len(param) == 7:
            data_owner = {"placa":param.upper()}
            data_transit =  {"placa":param.upper(),"type":"9748"}
        else:
            self.exit_req()
        
        req_ow = self.REQ.post(url=self.OWNERV, headers=self.headerssession(), json=data_owner)
        req_transit = self.REQ.post(url=self.URICAR, headers=self.headerssession(), json=data_transit)
        
        if req_ow.status_code == 200 and req_transit.status_code == 200:
            data_owner = req_ow.json()['data']
            dataCar = req_transit.json()['data']
            aggdata_.append(data_owner)
            aggdata_.append(dataCar)
        elif req_transit.json()['error'] == 1:
            with open("rute/config_url.json", "r") as read:
                data = json.load(read)
            uri_req = self.REQ.post(url=data["ecu_pag"]['uri_info'], headers=self.headerssession(), json=data_transit)
            aggdata_.append(req_ow.json()['data'])
            aggdata_.append(uri_req.json()['data'])
        return json.dumps(aggdata_, indent=2)


#SessionCar().infotable_car("MCB0250")
