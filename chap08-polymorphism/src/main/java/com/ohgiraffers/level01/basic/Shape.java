package com.ohgiraffers.level01.basic;

public abstract class Shape {
    public abstract double calculateArea();
    public abstract double calculatePerimeter();
}

public class Rectangle extends Shape implements Resizable {
    private double x;
    private double y;

    public Rectangle(double x, double y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public double calculateArea() {
        return Math.round(x * y * 100.0) / 100.0;
    }

    @Override
    public double calculatePerimeter() {
        return Math.round(2 * (x + y) * 100.0) / 100.0;
    }

    @Override
    public String toString() {
        return "Shape: Rectangle\n" +
               "Area: " + calculateArea() + "\n" +
               "Perimeter: " + calculatePerimeter();
    }

    @Override
    public void resize(double factor) {
        x *= factor;
        y *= factor;
    }
}

class Circle extends Shape implements Resizable {
    private double radius;

    public Circle(double radius) {
        this.radius = radius;
    }

    @Override
    public double calculateArea() {
        return Math.round(radius * radius * Math.PI * 100.0) / 100.0;
    }

    @Override
    public double calculatePerimeter() {
        return Math.round(2 * Math.PI * radius * 100.0) / 100.0;
    }

    @Override
    public String toString() {
        return "Shape: Circle\n" +
               "Area: " + calculateArea() + "\n" +
               "Perimeter: " + calculatePerimeter();
    }

    @Override
    public void resize(double factor) {
        this.radius *= factor;
    }
}

class Triangle extends Shape implements Resizable {
    private double base;
    private double height;
    private double side1;
    private double side2;
    private double side3;

    public Triangle(double base, double height, double side1, double side2, double side3) {
        this.base = base;
        this.height = height;
        this.side1 = side1;
        this.side2 = side2;
        this.side3 = side3;
    }

    @Override
    public double calculateArea() {
        return Math.round((base * height) / 2 * 100.0) / 100.0;
    }

    @Override
    public double calculatePerimeter() {
        return Math.round((side1 + side2 + side3) * 100.0) / 100.0;
    }

    @Override
    public String toString() {
        return "Shape: Triangle\n" +
               "Area: " + calculateArea() + "\n" +
               "Perimeter: " + calculatePerimeter();
    }

    @Override
    public void resize(double factor) {
        base *= factor;
        height *= factor;
        side1 *= factor;
        side2 *= factor;
        side3 *= factor;
    }
}
