<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Dashboard</title>
    <style>
        body { font-family: Arial; background: #f4f4f4; margin: 0; }
        .navbar {
            background: #3f51b5; color: white;
            padding: 15px 25px;
            display: flex; justify-content: space-between; align-items: center;
        }
        .navbar-right { display: flex; align-items: center; gap: 15px; }
        .role-badge { background: #2196f3; padding: 5px 10px; border-radius: 5px; color: white; }
        .container {
            max-width: 800px; margin: 40px auto;
            background: white; padding: 25px;
            border-radius: 8px; 
            box-shadow: 0 3px 10px rgba(0,0,0,0.1);
        }
        .btn-nav { background: #2196f3; color: white; padding: 8px 12px; text-decoration: none; border-radius: 4px; }
        .btn-logout { background: #e53935; color: white; padding: 8px 12px; text-decoration: none; border-radius: 4px; }
    </style>
</head>
<body>

<div class="navbar">
    <h2>📊 Dashboard</h2>
    <div class="navbar-right">
        <span>Welcome, ${sessionScope.fullName}</span>
        <span class="role-badge">${sessionScope.role}</span>
        <a href="student?action=list" class="btn-nav">Students</a>
        <a href="logout" class="btn-logout">Logout</a>
    </div>
</div>

<div class="container">
    <h1>📊 Overview</h1>
    <h3>Total Students: ${totalStudents}</h3>

    <p>${welcomeMessage}</p>

    <a href="student?action=list" class="btn-nav">Go to Student List</a>
</div>

</body>
</html>
