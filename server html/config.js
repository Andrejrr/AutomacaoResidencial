// JavaScript source code
var configStatus = {
    modoWifi: "null",
    senhaModoAP: "",
    nomeRedeAP: "Central",
    nomeSSID: "",
    senhaSSID: ""
};




function novoModo(modo) {
    document.getElementById('redesDisponiveis').style.display = "block";
    var s = "";
    if (modo == "ModoEstacao") {
        buscaRedesDisponiveis(s);
        setConfigWifiMode("setWifiMode=WIFI_STA");
        document.getElementsByName("modo")[1].checked = true;
        getIp();
    } else if (modo == "ModoAP-Estacao") {
        debugger;
        s = "<div id=\"redeAP\" class=\"confDiv\"><h3>Configura&ccedil&otildees AP</h3><br/><span>Nome da rede no modo AP: </span><input type=\"text\"  value=\"" + configStatus.nomeRedeAP + "\" /><br/><input type=\"password\" placeholder=\"Senha do modo AP...\" /><br/>";
        buscaRedesDisponiveis(s);
        setConfigWifiMode("setWifiMode=WIFI_AP_STA");
        document.getElementsByName("modo")[2].checked = true;
        getIp();
    } else if (modo == "ModoAP") {
        debugger;
        s = "<div id=\"redeAP\" class=\"confDiv\"><h3>Configura&ccedil&otildees AP</h3><br/> <span>Nome da rede no modo AP</span><input type=\"text\"  value=\"" + configStatus.nomeRedeAP + "\" /><br/><input type=\"password\" placeholder=\"Senha do modo AP...\" /><br/>";
        document.getElementById('redesDisponiveis').innerHTML = s;
        setConfigWifiMode("setWifiMode=WIFI_AP");
        document.getElementsByName("modo")[0].checked = true;
    } else {
        s = "<span>Aguardando status do servidor...</span>";
        document.getElementById('redesDisponiveis').innerHTML = s;
    }
}

function setConfigWifiMode(mode) {
    var ajaxRequest = new XMLHttpRequest;
    ajaxRequest.open("GET",mode, true);
    ajaxRequest.send();
    ajaxRequest.onreadystatechange = function () {
        if (this.readyState == 4 && this.status == 200) {
            
        } else if (this.readyState == 4 && this.status == 404) {
            document.getElementById("statusModificaModo").innerHTML = this.responseText;
        }
    };
}

function getConfigStatus() {
    debugger;
    var a = true;
    var ajaxRequest = new XMLHttpRequest;
    ajaxRequest.open("GET", "getConfigStatus", true);
    ajaxRequest.send();
    ajaxRequest.onreadystatechange = function () {
        if (this.readyState == 4 && this.status == 200) {
            trataConfigStatusResposta(this.responseText);
            debugger;
            novoModo(serverToClientModo(configStatus.modoWifi))
        } else if (this.readyState == 4 && this.status == 404) {
            document.getElementById("statusModificaModo").innerHTML = this.responseText;
        }
    };
    return configStatus.modoWifi;
}

function trataConfigStatusResposta(resposta) {
    debugger;
    var resp = String(resposta);
    var splited = resp.split('%');
    configStatus.modoWifi = splited[0].split('=')[1];
    configStatus.nomeRedeAP = splited[1].split('=')[1];
    configStatus.senhaModoAP = splited[2].split('=')[1];
    configStatus.nomeSSID = splited[3].split('=')[1];
    configStatus.senhaSSID = splited[4].split('=')[1];
    return configStatus.modoWifi;
}

function buscaRedesDisponiveis(s) {
    s = s + "<div id=\"redeSTA\" class=\"confDiv\"><h3>Redes Disponiveis</h3><ol class=\"col-6 col-s-9\">";
    var ajaxRequest = new XMLHttpRequest;
    ajaxRequest.open("GET", "getRedesDisponiveis", true);
    ajaxRequest.send();
    ajaxRequest.onreadystatechange = function () {
        if (this.readyState == 4 && this.status == 200) {
            s = s + this.responseText;
            s = s + "</ol></div>";
            document.getElementById('redesDisponiveis').innerHTML = s;
        } else if (this.readyState == 4 && this.status == 404) {
            document.getElementById("statusModificaModo").innerHTML = this.responseText;
        }
    };
}

function getIp() {
    var ajaxRequest = new XMLHttpRequest;
    ajaxRequest.open("GET", "getIp", true);
    ajaxRequest.send();
    ajaxRequest.onreadystatechange = function () {
        if (this.readyState == 4 && this.status == 200) {
            document.getElementById('ipEstacao').innerHTML = "IP modo estação: " + this.responseText;
            document.getElementById('ipEstacao').style.display = "block";
        } else if (this.readyState == 4 && this.status == 404) {
            document.getElementById("ipEstacao").innerHTML = "IP modo estação: " + this.responseText;
            document.getElementById('ipEstacao').style.display = "block";
        }
    };
}