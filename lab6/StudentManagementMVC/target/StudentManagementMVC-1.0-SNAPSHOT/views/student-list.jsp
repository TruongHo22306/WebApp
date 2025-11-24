<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Student List</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background: #f4f4f4;
            margin: 0;
        }
        .navbar {
            background: #3f51b5;
            color: white;
            padding: 15px 25px;
            display: flex;
            justify-content: space-between;
            align-items: center;
        }
        .navbar-right {
            display: flex;
            align-items: center;
            gap: 15px;
        }
        .user-info span {
            margin-right: 10px;
            font-weight: bold;
        }
        .role-badge {
            background: #2196f3;
            padding: 5px 10px;
            border-radius: 5px;
            color: white;
            text-transform: uppercase;
            font-size: 12px;
        }
        .btn-nav, .btn-logout, .btn-add, .btn-edit, .btn-delete {
            padding: 8px 12px;
            color: white;
            text-decoration: none;
            border-radius: 4px;
        }
        .btn-nav { background: #2196f3; }
        .btn-logout { background: #e53935; }
        .btn-add { background: #43a047; }
        .btn-edit { background: #fb8c00; }
        .btn-delete { background: #e53935; }
        .container {
            max-width: 950px;
            margin: 30px auto;
            background: white;
            padding: 25px;
            border-radius: 8px;
            box-shadow: 0 3px 10px rgba(0,0,0,0.1);
        }
        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 15px;
        }
        th, td {
            border: 1px solid #ddd;
            padding: 12px;
            text-align: center;
        }
        th {
            background: #eeeeee;
        }
    </style>
</head>
<body>

<!-- NAVBAR -->
<div class="navbar">
    <h2>📚 Student Management System</h2>
    <div class="navbar-right">
        <div class="user-info">
            <span>Welcome, ${sessionScope.fullName}</span>
            <span class="role-badge">${sessionScope.role}</span>
        </div>
        <a href="dashboard" class="btn-nav">Dashboard</a>
        <a href="logout" class="btn-logout">Logout</a>
    </div>
</div>

<div class="container">
    <h1>📚 Student List</h1>

    <c:if test="${sessionScope.role eq 'admin'}">
        <div style="margin-bottom: 20px;">
            <a href="student?action=new" class="btn-add">➕ Add New Student</a>
        </div>
    </c:if>

    <table>
        <thead>
            <tr>
                <th>ID</th>
                <th>Code</th>
                <th>Name</th>
                <th>Email</th>
                <th>Major</th>
                <c:if test="${sessionScope.role eq 'admin'}">
                    <th>Actions</th>
                </c:if>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="student" items="${students}">
                <tr>
                    <td>${student.id}</td>
                    <td>${student.studentCode}</td>
                    <td>${student.fullName}</td>
                    <td>${student.email}</td>
                    <td>${student.major}</td>

                    <c:if test="${sessionScope.role eq 'admin'}">
                        <td>
                            <a href="student?action=edit&id=${student.id}" class="btn-edit">Edit</a>
                            <a href="student?action=delete&id=${student.id}" class="btn-delete"
                               onclick="return confirm('Delete this student?')">Delete</a>
                        </td>
                    </c:if>
                </tr>
            </c:forEach>

            <c:if test="${empty students}">
                <tr>
                    <td colspan="6">No students found</td>
                </tr>
            </c:if>
        </tbody>
    </table>
</div>

</body>
</html>
