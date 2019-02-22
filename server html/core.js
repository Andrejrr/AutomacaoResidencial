// JavaScript source code
var carregouDependencias = false;
var carregouDispositivos = false;
var carregouConfig = false;
var valorSelecionado;
var xmlDoc;
var xmlDisp;
var htmlConf;
var ativo = "meusDispositivos";
var oculto = true;
var inicio = true;
var dispOculto = true;
var statusLeds = new Array(64);
var mobile = window.matchMedia("(max-width: 900px)");
var emMeusDisp = true;

function atualizar() {
    for (var a = 0; a < 64; a++) { statusLeds[a] = false; }
    loadDependencias();
    setInterval(loadStatus, 5000);
    setInterval(loadDependencias, 1000);
};

function loadDependencias() {
    if (!carregouDependencias) {
        var ajaxRequest = new XMLHttpRequest;
        ajaxRequest.open("GET", "dependencia.xml", true);
        ajaxRequest.send();
        ajaxRequest.onreadystatechange = function () {
            if (this.readyState == 4 && this.status == 200) {
                loadDispositivo();
                loadLista(this);
            }
        };
    } else {

    }
};

function loadLista(xml) {
    var i;
    inicio = true;
    var htmlCorpo;
    if (mobile.matches)
        htmlCorpo = "<div id=\"dependencias\" class=\"col-4\"></div> <div id=\"dispositivos\" class=\"dispModal\"></div >";
    else
        htmlCorpo = "<div id=\"dependencias\" class=\"col-4\"></div> <div id=\"dispositivos\" class=\"col-8 \" ></div >";
    document.getElementById("corpo").innerHTML = htmlCorpo;
    xmlDoc = xml.responseXML;
    var lista = "<ul class=\"dep col-12\">";
    var x = xmlDoc.getElementsByTagName("Dependencias");
    var listaId = new Array(x.length);
    for (i = 0; i < x.length; i++) {
        var valor = x[i].getElementsByTagName("Nome")[0].childNodes[0].nodeValue;
        if (i == 0) {
            selecionado(valor);
            inicio = false;
        }
        var selecionadoString = String(valor);
        selecionadoString = selecionadoString.replace(" ", "_");
        listaId[i] = selecionadoString;
        debugger;
        lista += "<li class=\"depItem\" id=\"" + selecionadoString + "\"><img class='depItemImg' src='"+imagemNome(valor)+"'/><a>" + valor + "</a></li>";
    }
    lista += "</ul>";
    debugger;
    document.getElementById("dependencias").innerHTML = lista;
    debugger;
    carregouDependencias = true;
    for (i = 0; i < listaId.length; i++) {
        debugger;
        var a = document.getElementById(listaId[i]);
        if (a != null) {
            a.addEventListener("click", function () { selecionado(this.id);},false);
        }
    }
}

function selecionado(selecionado) {
    debugger;
    var sel = new String(selecionado);
    sel = sel.replace("_", ' ');
    valorSelecionado = sel;
    debugger;
    loadDispositivo();
    var DispId = encontraId(sel);
    var i;
    var x = xmlDisp.getElementsByTagName("Dependencias");
    var lista;
    if (mobile.matches) {
        lista = "<div class=\"dispModal-content dispModal-content-600\"><span id=\"dispClose\" class=\"dispClose\">&times;</span><ol class=\"disp-d disp-t\">"
        debugger;
        for (i = 0; i < x.length; i++) {
            if (DispId == x[i].getElementsByTagName("IdDependencia")[0].childNodes[0].nodeValue) {
                var posicao = x[i].getElementsByTagName("Posicao")[0].childNodes[0].nodeValue;
                var nomeDisp = x[i].getElementsByTagName("Nome")[0].childNodes[0].nodeValue
                if (getStatusDispItem(posicao))
                    lampadaStatus = "ledOn.png";
                else
                    lampadaStatus = "ledOff.png";
                lista += "<li onclick=\"mudaSaida('" + posicao + "')\" class=\"dispItem  col-8 col-s-12\"><img src=\""+ lampadaStatus + "\" id=\"img" + posicao + "\" /><a>" + nomeDisp+ "</a></li>";
            }
        }
        lista += "</ol></div>";
        debugger;
        document.getElementById("dispositivos").innerHTML = lista;
        document.getElementById("dispClose").addEventListener("click", function () { closeDisp(); }, false)
        if (dispOculto && !inicio) {
            document.getElementById("dispositivos").style.display = "block";
            dispOculto = false;
        }
               
    } else {
        lista = "<ol class=\"disp-d disp-t\">"
        debugger;
        for (i = 0; i < x.length; i++) {
            if (DispId == x[i].getElementsByTagName("IdDependencia")[0].childNodes[0].nodeValue) {
                var posicao = x[i].getElementsByTagName("Posicao")[0].childNodes[0].nodeValue;
                var nomeDisp = x[i].getElementsByTagName("Nome")[0].childNodes[0].nodeValue
                if (getStatusDispItem(posicao))
                    lampadaStatus = "ledOn.png";
                else
                    lampadaStatus = "ledOff.png";
                lista += "<li onclick=\"mudaSaida('" + posicao + "')\" class=\"dispItem  col-8 col-s-12\"><img src=\"" + lampadaStatus + "\" id=\"img" + posicao + "\" /><a>" + nomeDisp + "</a></li>";
            }
        }
        lista += "</ol>";
        debugger;
        document.getElementById("dispositivos").innerHTML = lista;
    }
}


function loadDispositivo() {
    if (!carregouDispositivos) {
        var ajaxRequest = new XMLHttpRequest;
        ajaxRequest.open("GET", "dispositivos.xml", true);
        ajaxRequest.send();
        ajaxRequest.onreadystatechange = function () {
            if (this.readyState == 4 && this.status == 200) {
                xmlDisp = ajaxRequest.responseXML;
                carregouDispositivos = true;
            }
        };
    } else {

    }
}

function encontraId(selecionado) {
    var idDep = 0;
    var i = 0;
    var x = xmlDoc.getElementsByTagName("Dependencias");
    var nome;
    do {
        idDep = x[i].getElementsByTagName("Id")[0].childNodes[0].nodeValue;
        nome = x[i].getElementsByTagName("Nome")[0].childNodes[0].nodeValue;
        i++;
    } while (selecionado != nome)
    return idDep;
}

function meusDispositivosClick(id) {
    mudarAtivo(id);
    carregouDependencias = false;
    loadDependencias();
    mostraMenuLateral();
    emMeusDisp = true;
}

function configuracoesClick(id) {
    mudarAtivo(id);
    loadConfiguracoes();
    mostraMenuLateral();
    emMeusDisp = false;
}

function manualDeUsuariosClick(id) {
    mudarAtivo(id);
    loadManual();
    mostraMenuLateral();
    emMeusDisp = false;
}

function contatosClick(id) {
    mudarAtivo(id);
    loadContatos();
    mostraMenuLateral();
    emMeusDisp = false;
}

function mudarAtivo(id) {
    document.getElementById(ativo).classList.remove('activeItem-d');
    ativo = id;
    document.getElementById(ativo).classList.add('activeItem-d');
}

function loadConfiguracoes(){
    var ajaxRequest = new XMLHttpRequest;
    ajaxRequest.open("GET", "config.html", true);
    ajaxRequest.send();
    debugger;
    ajaxRequest.onreadystatechange = function () {
        if (this.readyState == 4 && this.status == 200) {
            htmlConf = ajaxRequest.response;
            document.getElementById("corpo").innerHTML = htmlConf;
            aplicarCentralConfigStatus();
        }
    };
}

function aplicarCentralConfigStatus() {
    debugger;
    var modo = getConfigStatus();
    modo = serverToClientModo(modo);
    novoModo(modo);
}

function serverToClientModo(modo) {
    if (modo == "WIFI_AP")
        modo = "ModoAP";
    else if (modo == "WIFI_STA")
        modo = "ModoEstacao";
    else if (modo == "WIFI_AP_STA")
        modo = "ModoAP-Estacao";
    else
        modo == "null";
    return modo;
}

function mostraMenuLateral() {
    if (mobile.matches) {
        if (oculto)
            document.getElementById("barraDeMenu").style.display = "block";
        else
            document.getElementById("barraDeMenu").style.display = "none";
        oculto = !oculto;
    }
}

function imagemNome(valor) {
    debugger;
    var s = String(valor);
    s = s.toLowerCase();
    debugger;
    s = s.replace(" ", "_");
    s = s.replace(" ", "_");
    s = s.replace(" ", "_");
    s = s.replace(" ", "_");
    s = s + ".png";
    return s;
}

function closeDisp(){
    document.getElementById("dispositivos").style.display = "none";
    dispOculto = true;
}


function getStatusDispItem(posicao) {
    var statusLed = false;
    statusLed = statusLeds[posicao];
    return statusLed;
}
function loadStatus() {
    if (emMeusDisp) {
        var ajaxRequest = new XMLHttpRequest;
        ajaxRequest.open("GET", "getStatusSaidas", true);
        ajaxRequest.send();
        ajaxRequest.onreadystatechange = function () {
            if (this.readyState == 4 && this.status == 200) {
                var s = this.responseText;
                for (var a = 0; a < 64; a++) {
                    if (s[a] == '1') {
                        statusLeds[a] = true;
                        atualizaStatus(a);
                    } else {
                        statusLeds[a] = false;
                        atualizaStatus(a)
                    }
                }
            }
        };
    }
}

function mudaSaida(saida) {
    debugger;
    var ajaxRequest = new XMLHttpRequest;
    ajaxRequest.open("GET", "setStatusSaida="+saida, true);
    ajaxRequest.send();
    ajaxRequest.onreadystatechange = function () {
        debugger;
        if (this.readyState == 4 && this.status == 200) {
            loadStatus();
        }
    };
}
function atualizaStatus(a) {
    debugger;
    var imagem = document.getElementById('img' + a);
    if (imagem != null) {
        var img;
        if (statusLeds[a])
            img = "ledOn.png";
        else
            img = "ledOff.png";
        imagem.setAttribute("src", img)
    }
}
window.onload = atualizar;