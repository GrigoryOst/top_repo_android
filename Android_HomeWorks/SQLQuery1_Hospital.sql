CREATE DATABASE Hospital; 

USE Hospital;

CREATE TABLE Patients (
    PatientID INT PRIMARY KEY IDENTITY(1,1),
    FirstName NVARCHAR(50),
    LastName NVARCHAR(50),
    BirthDate DATE,
    AdmissionDate DATE,
    DischargeDate DATE NULL,
    Department NVARCHAR(50),
    Doctor NVARCHAR(50),
    Diagnosis NVARCHAR(100),
    PhoneNumber NVARCHAR(15)
);

INSERT INTO Patients (FirstName, LastName, BirthDate, AdmissionDate, DischargeDate, Department, Doctor, Diagnosis, PhoneNumber)
VALUES 
('John', 'Doe', '1980-05-12', '2023-07-01', NULL, 'Cardiology', 'Dr. Smith', 'Hypertension', '555-1234'),
('Jane', 'Smith', '1990-08-22', '2023-07-05', NULL, 'Neurology', 'Dr. Brown', 'Migraine', '555-5678'),
('Michael', 'Johnson', '1975-12-01', '2023-06-15', '2023-07-15', 'Orthopedics', 'Dr. White', 'Fracture', '555-9012'),
('Emily', 'Davis', '1985-04-18', '2023-06-10', '2023-07-20', 'Pediatrics', 'Dr. Green', 'Asthma', '555-3456'),
('Daniel', 'Martinez', '1967-11-23', '2023-07-10', NULL, 'Oncology', 'Dr. Black', 'Cancer', '555-7890'),
('Olivia', 'Garcia', '2000-02-14', '2023-07-20', NULL, 'Cardiology', 'Dr. Smith', 'Arrhythmia', '555-2468'),
('William', 'Brown', '1955-09-30', '2023-06-25', NULL, 'Neurology', 'Dr. Brown', 'Stroke', '555-1357'),
('Sophia', 'Wilson', '2005-01-17', '2023-07-02', NULL, 'Orthopedics', 'Dr. White', 'Dislocation', '555-3698'),
('James', 'Taylor', '1982-11-11', '2023-05-30', '2023-06-30', 'Cardiology', 'Dr. Smith', 'Myocardial Infarction', '555-8520'),
('Ava', 'Moore', '1995-07-07', '2023-07-18', NULL, 'Pediatrics', 'Dr. Green', 'Pneumonia', '555-9632');

--������� ���������� ��� ���� ���������, ����������� � ��������--
SELECT * FROM Patients
WHERE DischargeDate IS NULL;

--�������� ������ � ���������, ������� ����� � ������������ ���������--
SELECT * FROM Patients
WHERE Department = 'Cardiology';

--������� �������� ���� ��������� ��������--
SELECT DISTINCT Department FROM Patients;

--�������� ������ � ���������, ������� ����� � �������� ������ ������, ������������ �� �� ����������� ���� �����������--
SELECT * FROM Patients
WHERE DATEDIFF(DAY, AdmissionDate, GETDATE()) > 30
ORDER BY AdmissionDate ASC;

--������� ���������� � ���������, ������� ���� �������� � ������� ������--
SELECT * FROM Patients
WHERE DischargeDate BETWEEN DATEADD(MONTH, -1, GETDATE()) AND GETDATE();

--�������� ���������� � ���������, ������� ������ � �������� � ������� �� ������� �������� ���� � ������������ ���������--
SELECT * FROM Patients
WHERE AdmissionDate BETWEEN '2023-10-01' AND '2023-12-31'
AND Department = 'Orthopedics';

--������� ���������� � ����� ������� �������� � ��� �������--
SELECT TOP 1 *, DATEDIFF(YEAR, BirthDate, GETDATE()) AS Age 
FROM Patients
ORDER BY BirthDate DESC;

--�������� ���������� � ���������, ������� ����� � ����� ���� ����������--
SELECT * FROM Patients
WHERE Department IN ('Cardiology', 'Neurology', 'Pediatrics');

--�������� ���� ���������, ������� ������� ���������� �� ����� "�"--
SELECT * FROM Patients
WHERE LastName LIKE 'P%';

--������� ������ � ���������, ������� ����� ������������ ���� � ����������� �������������--
SELECT * FROM Patients
WHERE Doctor = 'Dr. Smith' AND Diagnosis = 'Hypertension';

--�������� ������ � ���������, ������������ �������� ������������� ���������� ���������--
SELECT * FROM Patients
WHERE PhoneNumber LIKE '555%';

--������������� �������� ������������� ���������--
UPDATE Patients
SET Department = 'Cardiology and Vascular Surgery'
WHERE Department = 'Cardiology';

--������� ���� ���������, ������� ���� �������� ������ ��� ������� �����--
DELETE FROM Patients
WHERE DischargeDate < DATEADD(MONTH, -6, GETDATE());
