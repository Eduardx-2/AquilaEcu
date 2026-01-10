import requests,json
import sys
from concurrent.futures import ThreadPoolExecutor, as_completed

class SessionCar:
    def __init__(self):
        self.URICAR = "https://app3902.privynote.net/api/v1/transit/car-info"
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
    
    def placa_agencia_emision(self, placa_rev:str):
        agencia = []
        data = {"placa": placa_rev.upper()}
        req_data = self.REQ.post(url=self.OWNERV, headers=self.headerssession(),json=data)
        if req_data.status_code == 200:
            agencia.append(req_data.json())

        return agencia
    
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
            if uri_req.status_code == 200:
               
               aggdata_.append(uri_req.json()['data'])
            jsond = req_ow.json()['data']
            jsond['code'] = "v404"
            aggdata_.append(jsond)
        return json.dumps(aggdata_, indent=2)
    
    def req_data_archive(self,archive_d:str) -> dict:
        result_resp = []
        placa_list = None
        def datawork_req(data_placa:str):
                
            if len(data_placa) == 7: #°1 # Sencillamente la busqueda falla con un cierto tipos de placas (Las de empresas privadas)
                req_owner = self.REQ.post(url=self.OWNERV, headers=self.headerssession(),json={"placa":data_placa.upper()}) #No falla devuelve un 200 ok
                req_trs = self.REQ.post(url=self.URICAR, headers=self.headerssession(), json={"placa":data_placa.upper(),"type":"9748"}) # Si falla devuelve un 500
                #Entonces una consulta si devuelve información, la otra no, y es simple, para no alargar el codigo aquí en esta función:
                if req_owner.status_code == 200 and req_trs.status_code == 200: 
                    data_owner = req_owner.json()['data']
                    dataCar = req_trs.json()['data']      
                    data_placa = data_owner | dataCar
                return data_placa #En está indetación data_placa devuelve las placas que fallaron la consulta -> req_trs y al devolverse se puede manejar más facil en as_completed() 
        if archive_d.split('.')[1] == "txt":
            with open(archive_d, "r") as read:
                placas = read.readlines()
                placa_list = [line.strip() for line in placas]
        elif archive_d.split('.')[1] == "json":
            with open(archive_d,"r") as readjs:
                json_f = json.load(readjs)
                dataext = json_f['placas']
                placa_list = [dataext[f'num_placa_{i}'] for i in range(1,len(dataext)+1)]
        with ThreadPoolExecutor(max_workers=4) as ex_thread:
            futr_data = [ex_thread.submit(datawork_req,i)for i in placa_list]
        # LO que devuelve data_placa viene divididos en Nonetype, str, dict -> (Nulo,placa que devolvio un estado 500, información de la placa)
        for res in as_completed(futr_data): #Future ya devuelve las tareas identificadas, pero de que identifación hablo? Lee el punto 1 del error
            try:
                if res.result() is not None:
                     #ELIMINO LOS DATOS NONETYPE(NULOS) -> solo tengo dos datos str y dict, lo que necesitamos es el str
                    if isinstance(res.result(), str):
                       
                       headersList = {
                            "Host": "app3902.privynote.net",
                            "User-Agent": "Mozilla/5.0 (X11; Linux x86_64; rv:146.0) Gecko/20100101 Firefox/146.0",
                            "Content-Type": "application/json",
                            "Origin": "https://consultasecuador.com",
                            "Referer": "https://consultasecuador.com/" 
                       }
                       datam = self.REQ.post(url=self.OWNERV, headers=headersList,json={"placa":res.result().upper()})
                       if datam.status_code == 200:
                          
                          dcode = datam.json()['data']
                          dcode['code'] = "v404"
                          result_resp.append(dcode)
                         #Solo agrega  datos STRING
                        
                    
                    if isinstance(res.result(), dict): # Solo agrega datos DICT

                       result_resp.append(res.result())
                
            except Exception as error:
                result_resp.append({"error": str(error)})

        
                    
        
                                    
        return result_resp


#SessionCar().infotable_car("MCB0250")
