document.getElementById("schedule-btn").addEventListener("click", function () {
    let xhr = new XMLHttpRequest();
    xhr.open("POST", "/rest/scheduler", true);
    xhr.setRequestHeader("Content-type", "application/json");
    xhr.onload = function () {
        if (xhr.readyState === XMLHttpRequest.DONE && xhr.status === 200) {
            alert(xhr.responseText)
        } else {
            alert(xhr.responseText)
        }
    };
    let req = {};
    req.term = document.getElementById("term-option").querySelector('option:checked').value;
    req.startTime = document.getElementById("start-time").value;
    req.endTime = document.getElementById("end-time").value;
    req.excludedStatuses = [];
    for (const s of document.getElementById("status-options").querySelectorAll('option:checked')) {
        console.log(s.checked);
        req.excludedStatuses.push(s.value);
    }
    req.days = [];
    for (const d of document.getElementById("days-options").querySelectorAll('input:checked')) {
        req.days.push(d.value);
    }
    req.sesscd = document.getElementById("sesscd-option").querySelector('option:checked').value;
    req.sessyr = document.getElementById("sessyr-option").querySelector('option:checked').value;
    req.courses = [];
    for (const c of document.getElementById("courses-group").querySelectorAll('input')) {
        req.courses.push(c.value);
    }
    xhr.send(JSON.stringify(req));
});

document.getElementById("add-course-btn").addEventListener("click", function () {
    let divNode = document.createElement("div");
    divNode.className = "input-group";

    let inputNode = document.createElement("input");
    inputNode.className = "form-control";
    inputNode.type = "text";
    inputNode.name = "course";
    inputNode.placeholder = "MATH 100";
    divNode.appendChild(inputNode);

    let spanNode = document.createElement("span");
    spanNode.className = "input-group-btn";
    let textNode = document.createTextNode("Remove");
    let buttonNode = document.createElement("button");
    buttonNode.className = "btn btn-danger";
    buttonNode.type = "button";
    buttonNode.addEventListener("click", () => document.getElementById("courses-group").removeChild(divNode));
    buttonNode.appendChild(textNode);
    spanNode.appendChild(buttonNode);
    divNode.appendChild(spanNode);

    document.getElementById("courses-group").appendChild(divNode);
});

for (let i = (new Date()).getFullYear() - 1; i <= (new Date()).getFullYear(); i++) {
    let optionNode = document.createElement("option");
    let textNode = document.createTextNode(i);
    optionNode.appendChild(textNode);
    document.getElementById("sessyr-option").appendChild(optionNode);
}

