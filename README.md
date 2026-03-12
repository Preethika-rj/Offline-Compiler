Offline Java Compiler
Overview

Offline Java Compiler is a Java Swing desktop app that lets you write, compile, run, and save Java programs offline. Programs are stored in PostgreSQL using JDBC.

Features

Write Java code in a built-in editor

Compile and run programs locally

Save programs to a PostgreSQL database

Technologies

Java Swing (UI)

JDBC (Database connection)

PostgreSQL (Store programs)

javac & Runtime (Compile & execute code)

Setup

Install Java JDK 17+ and PostgreSQL

Add postgresql-42.7.10.jar in the project folder

Create programs table in PostgreSQL:

CREATE TABLE programs(
    id SERIAL PRIMARY KEY,
    name VARCHAR(100),
    code TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

Update DBConnection.java with your username/password.

Compile & run:

javac -cp ".;postgresql-42.7.10.jar" DBConnection.java CompilerUI.java
java -cp ".;postgresql-42.7.10.jar" CompilerUI
Example Code
// Save program to database
Connection con = DBConnection.getConnection();
PreparedStatement ps = con.prepareStatement("INSERT INTO programs(name,code) VALUES(?,?)");
ps.setString(1, programName);
ps.setString(2, codeText);
ps.executeUpdate();
// Compile Java code
Process p = Runtime.getRuntime().exec("javac " + programName + ".java");
// Run compiled program
Process p = Runtime.getRuntime().exec("java " + programName);
BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
Usage

Enter Program Name

Type Java code

Click Compile → Run → Save
