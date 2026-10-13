const API_URL = "/api/students";

// ===============================
// ADD STUDENT
// ===============================

const studentForm = document.getElementById("studentForm");

if (studentForm) {
studentForm.addEventListener("submit", async function(event) {

    event.preventDefault();

    const student = {
        id: document.getElementById("studentId").value,
        name: document.getElementById("studentName").value,
        subject: document.getElementById("subject").value,
        score: Number(document.getElementById("score").value)
    };

    try {

        const response = await fetch(API_URL, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(student)
        });

        if (!response.ok) {
            throw new Error("Unable to save student");
        }

        document.getElementById("message").textContent =
            "Student saved successfully!";

        studentForm.reset();

    } catch (error) {

        document.getElementById("message").textContent =
            "Error: " + error.message;

    }

});

}

// ===============================
// LOAD STUDENTS
// ===============================

async function loadStudents() {
try {

    const response = await fetch(API_URL);

    if (!response.ok) {
        throw new Error("Unable to load students");
    }

    const students = await response.json();

    displayStudents(students);

    displayReports(students);

} catch (error) {

    console.error(error);

}

}

// ===============================
// STUDENT TABLE
// ===============================

function displayStudents(students) {

const tableBody =
    document.getElementById("studentTableBody");

if (!tableBody) {
    return;
}

tableBody.innerHTML = "";

students.forEach(student => {

    const row = document.createElement("tr");

    row.innerHTML = `
        <td>${student.id}</td>
        <td>${student.name}</td>
        <td>${student.subject}</td>
        <td>${student.score}</td>
        <td>${student.grade}</td>
        <td>${student.status}</td>
        <td>
            <button
                class="delete-btn"
                onclick="deleteStudent('${student.id}')">
                Delete
            </button>
        </td>
    `;

    tableBody.appendChild(row);

});


}

// ===============================
// REPORT TABLE
// ===============================

function displayReports(students) {

const tableBody =
    document.getElementById("reportTableBody");

if (!tableBody) {
    return;
}

tableBody.innerHTML = "";

students.forEach(student => {

    const row = document.createElement("tr");

    row.innerHTML = `
        <td>${student.id}</td>
        <td>${student.name}</td>
        <td>${student.subject}</td>
        <td>${student.score}</td>
        <td>${student.grade}</td>
        <td>${student.status}</td>
    `;

    tableBody.appendChild(row);

});
}

// ===============================
// DELETE STUDENT
// ===============================

async function deleteStudent(id) {

if (!confirm("Delete this student?")) {
    return;
}

try {

    const response = await fetch(
        `${API_URL}/${encodeURIComponent(id)}`,
        {
            method: "DELETE"
        }
    );

    if (!response.ok) {
        throw new Error("Unable to delete student");
    }

    loadStudents();

} catch (error) {

    alert(error.message);

}

}

// ===============================
// STATISTICS
// ===============================

async function loadStatistics() {


try {

    const response =
        await fetch(`${API_URL}/statistics`);

    if (!response.ok) {
        throw new Error("Unable to load statistics");
    }

    const statistics = await response.json();

    setText(
        "totalStudents",
        statistics.totalStudents
    );

    setText(
        "averageScore",
        Number(statistics.average).toFixed(2)
    );

    setText(
        "highestScore",
        statistics.highest
    );

    setText(
        "lowestScore",
        statistics.lowest
    );

} catch (error) {

    console.error(error);

}


}

// ===============================
// HELPER
// ===============================

function setText(id, value) {


const element = document.getElementById(id);

if (element) {
    element.textContent = value;
}


}

// ===============================
// PAGE LOAD
// ===============================

document.addEventListener("DOMContentLoaded", function() {

loadStudents();

loadStatistics();

});
