<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>UBC Scheduler</title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
</head>
<body>
<div class="container col-md-6">
    <h2>Minimize Time at School</h2>
    <form>
        <div class="form-group">
            <label for="term-option">Term:</label>
            <select class="form-control" id="term-option">
                <option>1</option>
                <option>2</option>
            </select>
        </div>
        <div class="form-group">
            <label>Earliest Start Time <input type="time" id="start-time"></label>
        </div>
        <div class="form-group">
            <label>Latest End Time <input type="time" id="end-time"></label>
        </div>
        <p>Select statuses to exclude:</p>
        <div class="form-group">
            <select id="status-options" class="mdb-select" multiple>
                <option value="FULL">Full</option>
                <option value="RESTRICTED">Restricted</option>
                <option value="STT">STT</option>
            </select>
        </div>
        <div class="form-group" id="days-options">
            Days:
            <div class="form-check form-check-inline">
                <label class="form-check-label">
                    <input type="checkbox" name="check" value="MON">Monday
                </label>
            </div>
            <div class="form-check form-check-inline">
                <label class="form-check-label">
                    <input type="checkbox" name="check" value="TUE">Tuesday
                </label>
            </div>
            <div class="form-check form-check-inline">
                <label class="form-check-label">
                    <input type="checkbox" name="check" value="WED">Wednesday
                </label>
            </div>
            <div class="form-check form-check-inline">
                <label class="form-check-label">
                    <input type="checkbox" name="check" value="THU">Thursday
                </label>
            </div>
            <div class="form-check form-check-inline">
                <label class="form-check-label">
                    <input type="checkbox" name="check" value="FRI">Friday
                </label>
            </div>
        </div>
        <div class="form-group">
            <label for="sessyr-option">Session:</label>
            <select class="form-control" id="sessyr-option">
            </select>
            <select class="form-control" id="sesscd-option">
                <option>W</option>
                <option>S</option>
            </select>
        </div>
        <label for="courses-group">Courses</label>
        <div class="form-group">
            <div id="courses-group">
            </div>
            <br />
            <button type="button" class="btn" id="add-course-btn">Add Course</button>
        </div>
        <button type="button" class="btn btn-primary" id="schedule-btn">Build Schedule</button>
    </form>

    <div id="results-section">
    </div>
</div>
<script src="scheduler.js"></script>
</body>
</html>
