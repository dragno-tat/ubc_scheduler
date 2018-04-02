let regenerateResultsTable = () => {
    const resultsSection = document.getElementById('results-section');
    while (resultsSection.firstChild) {
        resultsSection.removeChild(resultsSection.firstChild);
    }

    const table = document.createElement('table');
    table.className = 'table table-bordered';
    const thead = document.createElement('thead');
    const trHead = document.createElement('tr');
    const thTime = document.createElement('th');
    thTime.scope = 'col';
    thTime.style.cssText = 'width: 10%';
    const textTime = document.createTextNode('Time');
    thTime.appendChild(textTime);
    trHead.appendChild(thTime);
    for(const day of ['Mon','Tue','Wed','Thu','Fri']) {
        const th = document.createElement('th');
        th.scope = 'col';
        th.style.cssText = 'width: 18%';
        const text = document.createTextNode(day);
        th.appendChild(text);
        trHead.appendChild(th);
    }
    thead.appendChild(trHead);
    table.appendChild(thead);

    let generateTimeString = (h, m) => {
        let hs = (h < 10) ? '0' + h.toString() : h.toString();
        let ms = (m === 0) ? '00' : '30';
        return hs + ':' + ms;
    };

    for(let i = 8; i < 19; i++) {
        for(let j = 0; j < 2; j++) {
            const tr = document.createElement('tr');
            const timeString = generateTimeString(i,j*30);
            tr.id = timeString + '-row';
            const th = document.createElement('th');
            th.scope = 'row';
            th.className = 'pt-1';
            const text = document.createTextNode(timeString);
            th.appendChild(text);
            tr.appendChild(th);
            for(let k = 0; k < 5; k++) {
                const td = document.createElement('td');
                tr.appendChild(td);
            }
            table.appendChild(tr);
        }
    }

    resultsSection.appendChild(table);
};

let displayCourseResults = function (courses) {
    regenerateResultsTable();

    const dayToIntMapper = (day) => {
        switch (day) {
            case 'MON':
                return 1;
            case 'TUE':
                return 2;
            case 'WED':
                return 3;
            case 'THU':
                return 4;
            default:
                return 5
        }
    };

    const parseTimeInMin = (s) => {
        const c = s.split(':');
        return parseInt(c[0]) * 60 + parseInt(c[1]);
    };

    let trToMarkedChildren = new Map();

    for(const course of courses) {
        for(let i = course.schedule.length - 1; i >= 0 ; i--) {
            const day = course.schedule[i];
            const minDiff = parseTimeInMin(day.endTime) - parseTimeInMin(day.startTime);
            const rowSpan = minDiff / 30;
            const dayIndex = dayToIntMapper(day.day);
            const startTr = document.getElementById(day.startTime + '-row');
            let currTr = startTr;
            for(let j = 1; j < rowSpan; j++) {
                currTr = currTr.nextElementSibling;
                let set = trToMarkedChildren.get(currTr);
                if(!set) {
                    set = new Set();
                    trToMarkedChildren.set(currTr, set)
                }
                set.add(dayIndex)
            }
            const td = startTr.children[dayIndex];
            td.rowSpan = rowSpan.toString();
            const firstLine = document.createTextNode(course.dept + ' ' + course.id.toString());
            const secondLineLink = document.createElement('a');
            secondLineLink.href = `https://courses.students.ubc.ca/cs/main?pname=subjarea&tname=subjareas&req=5&dept=${course.dept}&course=${course.id}&section=${course.section}`;
            const secondLineText = document.createTextNode(course.section);
            secondLineLink.appendChild(secondLineText);
            const br = document.createElement('br');
            td.appendChild(firstLine);
            td.appendChild(br);
            td.appendChild(secondLineLink);
            td.className = 'align-middle text-center'
        }
    }

    trToMarkedChildren.forEach((markedChildren, tr) => {
        let children = Array.from(markedChildren);
        children.sort((a,b) => b-a);
        children.forEach(c => {
            tr.removeChild(tr.children[c])
        });
    })
};

document.getElementById('schedule-btn').addEventListener('click', function () {
    let xhr = new XMLHttpRequest();
    xhr.open('POST', '/rest/scheduler', true);
    xhr.setRequestHeader('Content-type', 'application/json');
    xhr.onload = function () {
        if (xhr.readyState === XMLHttpRequest.DONE && xhr.status === 200) {
            displayCourseResults(JSON.parse(xhr.responseText))
        } else {
            try {
                const msg = JSON.parse(xhr.responseText).message;
                alert(msg);
            } catch (e) {
                alert(xhr.responseText)
            }
        }
    };
    let req = {};
    req.term = document.getElementById('term-option').querySelector('option:checked').value;
    req.startTime = document.getElementById('start-time').value;
    req.endTime = document.getElementById('end-time').value;
    req.excludedStatuses = [];
    for (const s of document.getElementById('status-options').querySelectorAll('option:checked')) {
        console.log(s.checked);
        req.excludedStatuses.push(s.value);
    }
    req.days = [];
    for (const d of document.getElementById('days-options').querySelectorAll('input:checked')) {
        req.days.push(d.value);
    }
    req.sesscd = document.getElementById('sesscd-option').querySelector('option:checked').value;
    req.sessyr = document.getElementById('sessyr-option').querySelector('option:checked').value;
    req.courses = [];
    for (const c of document.getElementById('courses-group').querySelectorAll('input')) {
        req.courses.push(c.value);
    }
    req.breaks = [];
    for (const inputGroup of document.getElementById('breaks-group').children) {
        let b = {};
        b.day = inputGroup.querySelector('option:checked').value;
        b.startTime = inputGroup.querySelector('input[name="start"]').value;
        b.endTime = inputGroup.querySelector('input[name="end"]').value;
        req.breaks.push(b);
    }
    xhr.send(JSON.stringify(req));
});

document.getElementById('add-course-btn').addEventListener('click', function () {
    let divNode = document.createElement('div');
    divNode.className = 'input-group';

    let inputNode = document.createElement('input');
    inputNode.className = 'form-control';
    inputNode.type = 'text';
    inputNode.name = 'course';
    inputNode.placeholder = 'MATH 100';
    divNode.appendChild(inputNode);

    let spanNode = document.createElement('span');
    spanNode.className = 'input-group-btn';
    let textNode = document.createTextNode('Remove');
    let buttonNode = document.createElement('button');
    buttonNode.className = 'btn btn-danger';
    buttonNode.type = 'button';
    buttonNode.addEventListener('click', () => document.getElementById('courses-group').removeChild(divNode));
    buttonNode.appendChild(textNode);
    spanNode.appendChild(buttonNode);
    divNode.appendChild(spanNode);

    document.getElementById('courses-group').appendChild(divNode);
});

document.getElementById('add-break-btn').addEventListener('click', function () {
    const inputGroupDiv = document.createElement('div');
    inputGroupDiv.className = 'input-group';

    const selectDiv = document.createElement('div');
    selectDiv.className = 'col-md-3';
    const selectNode = document.createElement('select');
    selectNode.className = 'form-control';
    for(const day of ['Mon', 'Tue', 'Wed', 'Thu', 'Fri']) {
        const optionNode = document.createElement('option');
        optionNode.value = day.toUpperCase();
        const textNode = document.createTextNode(day);
        optionNode.appendChild(textNode);
        selectNode.appendChild(optionNode);
    }
    selectDiv.appendChild(selectNode);
    inputGroupDiv.appendChild(selectDiv);

    for(const text of ['Start', 'End']) {
        const timeDiv = document.createElement('div');
        timeDiv.className = 'col-md-3';
        const timeLabel = document.createElement('label');
        const timeText = document.createTextNode(text + ' Time ');
        timeLabel.appendChild(timeText);
        let timeInput = document.createElement('input');
        timeInput.type = 'time';
        timeInput.name = text.toLowerCase();
        timeLabel.appendChild(timeInput);
        timeDiv.appendChild(timeLabel);
        inputGroupDiv.appendChild(timeDiv);
    }

    const removeDiv = document.createElement('div');
    removeDiv.className = 'col-md-3';
    let spanNode = document.createElement('span');
    spanNode.className = 'input-group-btn';
    let textNode = document.createTextNode('Remove');
    let buttonNode = document.createElement('button');
    buttonNode.className = 'btn btn-danger';
    buttonNode.type = 'button';
    buttonNode.addEventListener('click', () => document.getElementById('breaks-group').removeChild(inputGroupDiv));
    buttonNode.appendChild(textNode);
    spanNode.appendChild(buttonNode);
    removeDiv.appendChild(spanNode);
    inputGroupDiv.appendChild(removeDiv);

    document.getElementById('breaks-group').appendChild(inputGroupDiv);
});

for (let i = (new Date()).getFullYear() - 1; i <= (new Date()).getFullYear(); i++) {
    let optionNode = document.createElement('option');
    let textNode = document.createTextNode(i);
    optionNode.appendChild(textNode);
    document.getElementById('sessyr-option').appendChild(optionNode);
}

