alert("JS LOADED");
const BASE_URL = "http://localhost:8080"; // change to 8081 if needed


// ===================== UPLOAD =====================

function uploadFile() {

    let file = document.getElementById("fileInput").files[0];

    if (!file) {
        document.getElementById("status").innerText = "Select a file first";
        return;
    }

    let formData = new FormData();
    formData.append("file", file);

    document.getElementById("status").innerText = "Uploading...";

    fetch(BASE_URL + "/upload", {
        method: "POST",
        body: formData
    })
    .then(res => res.text())
    .then(() => {
        document.getElementById("status").innerText = "Processing started...";
        checkStatusAndRedirect();
    })
    .catch(err => {
        document.getElementById("status").innerText = "Upload failed";
        console.error(err);
    });
}


// ===================== STATUS CHECK =====================

function checkStatusAndRedirect() {

    let interval = setInterval(() => {

        fetch(BASE_URL + "/status")
        .then(res => res.json())
        .then(done => {

            if (done) {
                clearInterval(interval);

                document.getElementById("status").innerText = "Done";

                setTimeout(() => {
                    window.location.href = "dashboard.html";
                }, 1000);
            }
        })
        .catch(err => console.error(err));

    }, 1000);
}


// ===================== LOAD TABLE =====================

function loadTable() {

    fetch("http://localhost:8080/logs")
    .then(res => res.json())
    .then(data => {

        let body = document.getElementById("tableBody");
        body.innerHTML = "";

        data.forEach(log => {

            let row = document.createElement("tr");

            // 🔥 FIXED ORDER (matches your table headers)
			let cells = [
			    log.date || log.field1 || log.Date,
			    log.time || log.field2,
			    log.sourceIp || log.source_ip || log.field5,
			    log.destIp || log.dest_ip || log.field6,
			    log.action || log.field3,
			    log.protocol || log.field4,
			    log.sourcePort || log.source_port || log.field7,
			    log.destPort || log.dest_port || log.field8,
			    log.risk
			];

            cells.forEach(value => {
                let td = document.createElement("td");
                td.textContent = value ?? "";
                row.appendChild(td);
            });

            body.appendChild(row);
        });

    });
}

//=--------------------------------------------------
function downloadPNG() {
    fetch("http://localhost:8080/report/generate")
        .then(res => res.blob())
        .then(blob => {
            let url = window.URL.createObjectURL(blob);
            let a = document.createElement("a");
            a.href = url;
            a.download = "report.png";
            a.click();
        });
}
// ===================== SEARCH =====================

function filterTable() {

    let input = document.getElementById("search").value.toLowerCase();
    let rows = document.querySelectorAll("#logTable tbody tr");

    rows.forEach(row => {
        row.style.display =
            row.innerText.toLowerCase().includes(input) ? "" : "none";
    });
}


// ===================== INIT =====================

document.addEventListener("DOMContentLoaded", function () {

    // ONLY run on dashboard page
    if (document.getElementById("tableBody")) {
        loadTable();
    }

});
