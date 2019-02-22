var modal;
var span;
var rede;
function showDialogModal(imgSrc,redeNome,status) {
    debugger;
    modal = document.getElementById('myModal');
    span = document.getElementsByClassName("close")[0];
    document.getElementById("redeNome").innerText = redeNome;
    document.getElementById("wifiSinal").setAttribute("src",imgSrc);
    span.onclick = function () {
        modal.style.display = "none";
    }
    if (status == "conectado") {
        document.getElementById("conectar").value = "Desconectar";
        document.getElementById("senha").style.display = "none";
    } else {
        document.getElementById("conectar").value = "Conectar";
        document.getElementById("senha").style.display = "inline";
    }
    modal.style.display = "block";
}

function setSSIDConfig() {
    debugger;
    if (document.getElementById("conectar").value == "Desconectar") {

    } else {
        var nomeSSID = document.getElementById("redeNome").innerText;
        var senha = document.getElementById("senha").value;
        var ajaxRequest = new XMLHttpRequest;
        ajaxRequest.open("GET", "setSSIDConfig?nomeSSID=" + nomeSSID + "&senha=" + senha, true);
        ajaxRequest.send();
        ajaxRequest.onreadystatechange = function () {
            if (this.readyState == 4 && this.status == 200) {

            } else if (this.readyState == 4 && this.status == 404) {

            }
        };
    }
}


window.onclick = function (event) {
    if (event.target == modal) {
        modal.style.display = "none";
    }
}

