package ua.ukma.edu.authorization;

public enum Roles{
        USER,       //лише перегляд (пошук і звіти)
        MANAGER,    //повний доступ до CRUD, без керування користувачами
        ADMIN       //повний доступ + створення/редагування/блокування користувачів і ролей
}