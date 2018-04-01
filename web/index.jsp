<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title>UBC Scheduler</title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
  </head>
  <body>
    <div class="container col-md-6">
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
            <option value="NA">N/A</option>
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
        <table class="table table-bordered" hidden>
          <thead>
            <tr>
              <th scope="col" style="width: 10%">Time</th>
              <th scope="col" style="width: 18%">Mon</th>
              <th scope="col" style="width: 18%">Tue</th>
              <th scope="col" style="width: 18%">Wed</th>
              <th scope="col" style="width: 18%">Thu</th>
              <th scope="col" style="width: 18%">Fri</th>
            </tr>
          </thead>
          <tr id="08:00-row">
            <th scope="row" class="pt-1">8:00</th>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
          </tr>
          <tr id="08:30-row">
            <th scope="row" class="pt-1">8:30</th>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
          </tr>
          <tr id="09:00-row">
            <th scope="row" class="pt-1">9:00</th>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
          </tr>
          <tr id="09:30-row">
            <th scope="row" class="pt-1">9:30</th>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
          </tr>
          <tr id="10:00-row">
            <th scope="row" class="pt-1">10:00</th>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
          </tr>
          <tr id="10:30-row">
            <th scope="row" class="pt-1">10:30</th>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
          </tr>
          <tr id="11:00-row">
            <th scope="row" class="pt-1">11:00</th>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
          </tr>
          <tr id="11:30-row">
            <th scope="row" class="pt-1">11:30</th>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
          </tr>
          <tr id="12:00-row">
            <th scope="row" class="pt-1">12:00</th>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
          </tr>
          <tr id="12:30-row">
            <th scope="row" class="pt-1">12:30</th>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
          </tr>
          <tr id="13:00-row">
            <th scope="row" class="pt-1">13:00</th>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
          </tr>
          <tr id="13:30-row">
            <th scope="row" class="pt-1">13:30</th>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
          </tr>
          <tr id="14:00-row">
            <th scope="row" class="pt-1">14:00</th>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
          </tr>
          <tr id="14:30-row">
            <th scope="row" class="pt-1">14:30</th>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
          </tr>
          <tr id="15:00-row">
            <th scope="row" class="pt-1">15:00</th>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
          </tr>
          <tr id="15:30-row">
            <th scope="row" class="pt-1">15:30</th>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
          </tr>
          <tr id="16:00-row">
            <th scope="row" class="pt-1">16:00</th>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
          </tr>
          <tr id="16:30-row">
            <th scope="row" class="pt-1">16:30</th>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
          </tr>
          <tr id="17:00-row">
            <th scope="row" class="pt-1">17:00</th>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
          </tr>
          <tr id="17:30-row">
            <th scope="row" class="pt-1">17:30</th>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
          </tr>
          <tr id="18:00-row">
            <th scope="row" class="pt-1">18:00</th>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
          </tr>
          <tr id="18:30-row">
            <th scope="row" class="pt-1">18:30</th>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
          </tr>
        </table>
      </div>
    </div>
    <script src="scheduler.js"></script>
  </body>
</html>
